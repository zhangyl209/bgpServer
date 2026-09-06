package com.example.routers.handlers;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.HttpUtil;
import com.example.MyHttpResponse;
import com.example.business.base.SystemConfig;
import com.example.controllers.BgpController;
import com.example.nest.NestClient;
import com.example.nest.api.Configservices;
import com.example.nest.api.Nest;
import jdk.jfr.Unsigned;
import kotlin.UInt;
import kotlin.ULong;
import org.nutz.dao.*;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.json.Json;
import org.nutz.lang.encrypt.MsgDigestInputStream;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.commons.InParamsActor;
import com.example.commons.ResponseInfo;
import com.example.functions.FuncManager;
import com.example.functions.db.ConditionCRUD;
import com.example.functions.db.STClear;
import com.example.functions.db.STCount;
import com.example.functions.db.STFetch;
import com.example.functions.db.STInsert;
import com.example.functions.db.STQuery;
import com.example.functions.db.STUpdate;
import com.example.nutz.DBTools;
import com.example.utils.Utils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Timestamp;


@CrossOrigin(maxAge = 3600)
@Component
public class STHandler {

    private final Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();

    private Dao dao = DBTools.getDao();
    private int typeLink = 0;
    private int totalCnt = 0;
    private int curCnt = 0;
    Timestamp timestamp = new Timestamp(0);

    private Mono<User> extractUserSeqIdFromJwtToken(Mono<SecurityContext> context) {
        return context.filter(c -> Objects.nonNull(c.getAuthentication())).map(s -> s.getAuthentication().getPrincipal()).cast(User.class);
    }

    public Mono<ServerResponse> getOne(ServerRequest request) {

        String table = request.pathVariable("table");
        String id = request.pathVariable("id");

        if (!StringUtils.hasText(table)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有table参数"));
        }
        if (!StringUtils.hasText(id)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有id参数"));
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(".table", table);
        map.put(".type", "getOne");
        map.put("id", id);

        Flux<Map<String, Object>> input = Flux.just(map);

        Flux<Object> output = new ConditionCRUD<STFetch>(STFetch.class).apply(input);

        final Mono<List<Object>> responseData = output.collectList();

        Mono<Object> body = responseData.map(responses -> {
            return ResponseInfo.ok(responses.size() == 1 ? responses.get(0) : null);
        });

        return ServerResponse.ok().body(body, ResponseInfo.class);
    }

    public Mono<ServerResponse> getList(ServerRequest request) {

        return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
            String username = user.getUsername();
            String userId = null;

            Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
            if (userRecord != null) {
                userId = userRecord.getString("id");
            }

            String table = request.pathVariable("table");

            if (!StringUtils.hasText(table)) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有table参数"));
            }

            Map<String, Object> map = new HashMap<String, Object>();

            map.put(".table", table);
            map.put(".type", "getList");
            Optional<String> filter = request.queryParam("filter");
            request.queryParam("filter").ifPresent(c -> map.put("filter", c));
            request.queryParam("pageNum").ifPresent(c -> map.put("pageNum", c));
            request.queryParam("pageSize").ifPresent(c -> map.put("pageSize", c));
            request.queryParam("sort").ifPresent(c -> map.put("sort", c));
            request.queryParam("order").ifPresent(c -> map.put("order", c));

            map.put(".auth", userId);

            request.queryParam("business").ifPresent(c -> {

//				map.put("containsBusiness", true);

                Condition condition = Cnd.where("business", "=", c.toString()).and("status", "=", 1);

                Record record = dao.fetch("config_business", condition);

                if (record != null && StringUtils.hasText(record.getString("inputs")) && record.getString("inputs").split(",").length == 1) {
                    //只支持一个，不支持多个input
                    map.put(".input", record.getInt("inputs"));
                }
                if (record != null && StringUtils.hasText(record.getString("funcflow"))) {
                    map.put(".funcflow", record.getInt("funcflow"));
                    map.put("containsBusiness", true);
                }

            });

            Flux<Map<String, Object>> input = Flux.just(map);
            Flux<Object> output = new ConditionCRUD<STQuery>(STQuery.class).apply(input);

            if (map.get(".funcflow") != null) {
                InParamsActor inParamsActor = InParamsActor.builder().classname(map.get(".funcflow").toString()).input(output).authUser(userId).build();

                output = new FuncManager().apply(inParamsActor);
            }

            final Mono<List<Object>> responseData = output.collectList();

            //map.get("containsBusiness") == null 的作用：如果前台传的业务只有一个，即flux中只有一个值，返回的时候也只返回一个值，不返回数组
            Mono<Object> body = responseData.map(responses -> {
                return map.get("containsBusiness") == null || map.get("pageNum") != null || map.get("pageSize") != null ? ResponseInfo.ok(responses.get(0)) : ResponseInfo.ok(responses);
            });

            return ServerResponse.ok().body(body, ResponseInfo.class);
        });

    }

    public Mono<ServerResponse> getList_noAuth(ServerRequest request) {

        String table = request.pathVariable("table");

        if (!StringUtils.hasText(table)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有table参数"));
        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(".table", table);
        map.put(".type", "getList");
        request.queryParam("filter").ifPresent(c -> map.put("filter", c));
        request.queryParam("pageNum").ifPresent(c -> map.put("pageNum", c));
        request.queryParam("pageSize").ifPresent(c -> map.put("pageSize", c));
        request.queryParam("sort").ifPresent(c -> map.put("sort", c));
        request.queryParam("order").ifPresent(c -> map.put("order", c));

        request.queryParam("business").ifPresent(c -> {

//			map.put("containsBusiness", true);

            Condition condition = Cnd.where("business", "=", c.toString()).and("status", "=", 1);

            Record record = dao.fetch("config_business", condition);

            if (record != null && StringUtils.hasText(record.getString("inputs")) && record.getString("inputs").split(",").length == 1) {
                //只支持一个，不支持多个input
                map.put(".input", record.getInt("inputs"));
            }
            if (record != null && StringUtils.hasText(record.getString("funcflow"))) {
                map.put(".funcflow", record.getInt("funcflow"));
                map.put("containsBusiness", true);
            }

        });

        Flux<Map<String, Object>> input = Flux.just(map);
        Flux<Object> output = new ConditionCRUD<STQuery>(STQuery.class).apply(input);

        if (map.get(".funcflow") != null) {
            InParamsActor inParamsActor = InParamsActor.builder().classname(map.get(".funcflow").toString()).input(output).build();

            output = new FuncManager().apply(inParamsActor);
        }

        final Mono<List<Object>> responseData = output.collectList();

        //map.get("containsBusiness") == null 的作用：如果前台传的业务只有一个，即flux中只有一个值，返回的时候也只返回一个值，不返回数组
        Mono<Object> body = responseData.map(responses -> {
            return map.get("containsBusiness") == null || map.get("pageNum") != null || map.get("pageSize") != null ? ResponseInfo.ok(responses.get(0)) : ResponseInfo.ok(responses);
        });

        return ServerResponse.ok().body(body, ResponseInfo.class);


    }

    public Mono<ServerResponse> create(ServerRequest request) {

        String table = request.pathVariable("table");

        if (!StringUtils.hasText(table)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有table参数"));
        }

        return request.bodyToMono(Record.class).flatMap(data -> {

            Record record = new Record();
            record.put(".table", table);
            record.put(".type", "create");
            record.put("data", data);

            Flux<Map<String, Object>> input = Flux.just(record);
            Flux<Object> output = new ConditionCRUD<STInsert>(STInsert.class).apply(input);

            return output.collectList();
        }).map(responses -> {
            //如果输入是一个，输出也是一个，不是数组
            List<Record> list = Utils.objToList(responses.get(0), Record.class);
            return ResponseInfo.ok(list.size() == 1 ? list.get(0) : list);
        }).flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {

        String table = request.pathVariable("table");
        String ids = request.pathVariable("ids");

        if (!StringUtils.hasText(table)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有table参数"));
        }
        if (!StringUtils.hasText(ids)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有ids参数"));
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(".table", table);
        map.put(".type", "delete");
        map.put("ids", ids);

        Flux<Map<String, Object>> input = Flux.just(map);
//		Flux<Object> output = new SingleDelete().apply(input);
        Flux<Object> output = new ConditionCRUD<STClear>(STClear.class).apply(input);

        final Mono<List<Object>> responseData = output.collectList();

        Mono<Object> body = responseData.map(responses -> {
            return ResponseInfo.ok(responses.size() == 1 ? responses.get(0) : null);
        });

        return ServerResponse.ok().body(body, ResponseInfo.class);
    }

    public Mono<ServerResponse> update(ServerRequest request) {

        String table = request.pathVariable("table");
        String ids = request.pathVariable("ids");

        if (!StringUtils.hasText(table)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有table参数"));
        }
        if (!StringUtils.hasText(ids)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有ids参数"));
        }

        return request.bodyToMono(Record.class).flatMap(data -> {

            Record record = new Record();
            record.put(".table", table);
            record.put("ids", ids);
            record.put(".type", "update");
            record.put("data", data);

            Flux<Map<String, Object>> input = Flux.just(record);
            Flux<Object> output = new ConditionCRUD<STUpdate>(STUpdate.class).apply(input);

            return output.collectList();
        }).map(responses -> {
            //如果输入是一个，输出也是一个，不是数组
            List<Number> list = Utils.objToList(responses.get(0), Number.class);
            return ResponseInfo.ok(list.size() == 1 ? list.get(0) : list);
        }).flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
    }

    public Mono<ServerResponse> count(ServerRequest request) {

        String table = request.pathVariable("table");

        if (!StringUtils.hasText(table)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有table参数"));
        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(".table", table);
        map.put(".type", "getList");
        request.queryParam("filter").ifPresent(c -> map.put("filter", c));

        request.queryParam("business").ifPresent(c -> {

            map.put("containsBusiness", true);

            Condition condition = Cnd.where("business", "=", c.toString()).and("status", "=", 1);

            Record record = dao.fetch("config_business", condition);

            if (record != null && StringUtils.hasText(record.getString("inputs")) && record.getString("inputs").split(",").length == 1) {
                //只支持一个，不支持多个input
                map.put(".input", record.getInt("inputs"));
            }
            if (record != null && StringUtils.hasText(record.getString("funcflow"))) {
                map.put(".funcflow", record.getInt("funcflow"));
            }

        });

        Flux<Map<String, Object>> input = Flux.just(map);
        Flux<Object> output = new ConditionCRUD<STCount>(STCount.class).apply(input);

        if (map.get(".funcflow") != null) {
            InParamsActor inParamsActor = InParamsActor.builder().classname(map.get(".funcflow").toString()).input(output).build();

            output = new FuncManager().apply(inParamsActor);
        }

        final Mono<List<Object>> responseData = output.collectList();

        //map.get("containsBusiness") == null 的作用：如果前台传的业务只有一个，即flux中只有一个值，返回的时候也只返回一个值，不返回数组
        Mono<Object> body = responseData.map(responses -> {
            return ResponseInfo.ok(responses.size() == 1 ? responses.get(0) : null);
        });

        return ServerResponse.ok().body(body, ResponseInfo.class);
    }

    public Mono<ServerResponse> updateAll(ServerRequest request) {

        String table = request.pathVariable("table");
        String sceneid = request.pathVariable("sceneid");  // 获取 sceneid 参数

        // 检查表名参数
        if (!StringUtils.hasText(table)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有table参数"));
        }

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 根据 sceneid 查询数据库并获取对应的 ids
        Mono<String> idsMono = fetchIdsBySceneIdFromDatabase(table, sceneid);

        return idsMono.flatMap(fetchedIds -> {
            return request.bodyToMono(Record.class).flatMap(data -> {

                Record record = new Record();
                record.put(".table", table);
                record.put(".type", "update"); // 操作类型为 update
                record.put("data", data); // 要更新的数据
                record.put("ids", fetchedIds);  // 根据查询结果的 ids 进行更新

                // 生成 Flux 输入流
                Flux<Map<String, Object>> input = Flux.just(record);
                // 执行更新操作
                Flux<Object> output = new ConditionCRUD<STUpdate>(STUpdate.class).apply(input);

                // 收集更新结果
                return output.collectList();
            }).map(responses -> {
                @SuppressWarnings("unchecked")
                //List<Map<String, Object>> responseList = (List<Map<String, Object>>) responses;
                List<Number> list = Utils.objToList(responses.get(0), Number.class);
                return ResponseInfo.ok(list.size() == 1 ? list.get(0) : list);
            }).flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
        });
    }

    // 这个方法用于从数据库获取符合 sceneid 的记录ID
    private Mono<String> fetchIdsBySceneIdFromDatabase(String table, String sceneid) {
        // 执行 SQL 查询获取所有符合 sceneid 条件的记录 ID
        Sql sql = Sqls.create("SELECT id FROM " + table + " WHERE sceneid=@sceneid");
        sql.params().set("sceneid", sceneid);  // 设置参数
        sql.setCallback(Sqls.callback.strList());  // 设置回调函数，将结果作为列表返回
        dao.execute(sql);

        // 将ID列表转换为逗号分隔的字符串
        List<String> idList = sql.getList(String.class);
        String allIds = String.join(",", idList);

        return Mono.just(allIds);
    }

    public Mono<ServerResponse> LoadScene(ServerRequest request) {

        String sceneid = request.pathVariable("sceneid");  // 获取 sceneid 参数

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 创建返回结果对象
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sceneid", sceneid);

        // 根据 sceneid 查询数据库表scene并获取对应的 场景名称 scene
        Sql sceneSql = Sqls.create("SELECT scene FROM scene WHERE id=@sceneid");
        sceneSql.params().set("sceneid", sceneid);
        sceneSql.setCallback(Sqls.callback.str());
        dao.execute(sceneSql);
        String sceneName = sceneSql.getString();
        responseData.put("sceneName", sceneName);

        // 根据 sceneid 查询数据库视图 node_view 并获取对应的 节点信息
        Sql nodeSql = Sqls.create("SELECT\n" + "  `n`.`id` AS `id`,\n" + "  `n`.`asn` AS `asn`,\n" + "  `n`.`node` AS `node`,\n" + "  `c`.`chinese_name` AS `country`,\n" + "  `c`.`code` AS `countrycode`,\n" + "  `n`.`longitude` AS `longitude`,\n" + "  `n`.`latitude` AS `latitude`,\n" + "  `n`.`initroutes` AS `initroutes`,\n" + "  `n`.`serverid` AS `serverid`,\n" + "  `s`.`mserver` AS `mserver`,\n" + "  `s`.`ip` AS `ip` \n" + "FROM\n" + "  ((\n" + "      `node` `n`\n" + "      LEFT JOIN `mserver` `s` ON ((\n" + "          `n`.`serverid` = `s`.`id` \n" + "        )))\n" + "    LEFT JOIN `country` `c` ON ((\n" + "      `n`.`country` = `c`.`id` \n" + "  )))\n" + "  WHERE n.sceneid = @sceneid");
        nodeSql.params().set("sceneid", sceneid);
        nodeSql.setCallback(Sqls.callback.maps());
        dao.execute(nodeSql);
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) (List<?>) nodeSql.getList(Map.class);
        responseData.put("nodes", nodes);

        // 根据 sceneid 查询数据库视图 link_view 并获取对应的 链路信息
        Sql linkSql = Sqls.create("SELECT id,srcid,srcasn,srcip,destid,destasn,destip FROM `link` WHERE sceneid=@sceneid");
        linkSql.params().set("sceneid", sceneid);
        linkSql.setCallback(Sqls.callback.maps());
        dao.execute(linkSql);
        List<Map<String, Object>> links = (List<Map<String, Object>>) (List<?>) linkSql.getList(Map.class);
        responseData.put("links", links);

        // 根据 sceneid 查询数据库视图 scene_server_view 并获取对应的 服务器信息
        Sql serverSql = Sqls.create("SELECT id,mserver,ip FROM `scene_server_view` WHERE sceneid=@sceneid");
        serverSql.params().set("sceneid", sceneid);
        serverSql.setCallback(Sqls.callback.maps());
        dao.execute(serverSql);

        List<Map<String, Object>> servers = (List<Map<String, Object>>) (List<?>) serverSql.getList(Map.class);
        responseData.put("servers", servers);

        // 返回响应
        return ServerResponse.ok().bodyValue(responseData);
    }

    public Mono<ServerResponse> updataTier(ServerRequest request) {
        // 获取路径参数 sceneid
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 解析请求体 JSON
        return request.bodyToMono(Map.class).flatMap(body -> {
            // 检查 JSON 数据是否包含必要的字段
            if (!body.containsKey("nodeIds") || !body.containsKey("tier")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要参数：nodeIds 或 tier"));
            }

            // 提取请求数据
            List<Integer> nodeIds = (List<Integer>) body.get("nodeIds");
            // 通过 body.get 获取并强制转换类型
            String tier = (String) body.get("tier");


            if (nodeIds == null || nodeIds.isEmpty() || !StringUtils.hasText(tier)) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("参数 nodeIds 为空或 tier 为空"));
            }

            // 构建 SQL 查询语句
            Sql updateSql = Sqls.create("UPDATE node SET Tier=@tier WHERE sceneid=@sceneid AND id IN(@nodeIds)");
            updateSql.params().set("tier", tier);           // 设置 tier 参数
            updateSql.params().set("sceneid", sceneid);     // 设置 sceneid 参数
            updateSql.params().set("nodeIds", nodeIds);     // 设置 nodeIds 参数
            dao.execute(updateSql);

            // 检查是否更新成功
            if (updateSql.getUpdateCount() > 0) {
                return ServerResponse.ok().bodyValue(ResponseInfo.ok("更新成功", null));
            } else {
                return ServerResponse.status(HttpStatus.NOT_MODIFIED).bodyValue(ResponseInfo.not("未更新任何记录"));
            }
        }).onErrorResume(e -> {
            // 错误处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：" + e.getMessage()));
        });
    }

    public Mono<ServerResponse> getNodesWithTier(ServerRequest request) {
        // 获取路径参数 sceneid
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 构建 SQL 查询语句
        Sql querySql = Sqls.create("SELECT * FROM node WHERE sceneid=@sceneid AND tier IS NOT NULL AND Tier != ''");
        querySql.params().set("sceneid", sceneid);  // 设置参数
        querySql.setCallback(Sqls.callback.maps()); // 设置回调函数，将结果作为列表返回

        // 执行 SQL 查询
        dao.execute(querySql);

        // 获取查询结果
        List<Map<String, Object>> nodesWithTier = (List<Map<String, Object>>) (List<?>) querySql.getList(Map.class);

        typeLink = 0;
        totalCnt = 0;
        curCnt = 0;

        // 返回响应
        return ServerResponse.ok().bodyValue(ResponseInfo.ok("查询成功", nodesWithTier));
    }

    public Mono<ServerResponse> insertOperation(ServerRequest request) {
        // 获取路径参数 sceneid
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 解析请求体 JSON
        return request.bodyToMono(Map.class).flatMap(body -> {
            // 检查 JSON 数据是否包含必要的字段
            if (!(body.containsKey("Attack_network") || body.containsKey("Domestic_operator") || body.containsKey("New_Nodes")  || body.containsKey("New_Links"))) {
                // 找出具体缺少哪些字段（可选，更友好）
                List<String> missingFields = new ArrayList<>();
                if (!body.containsKey("Attack_network")) missingFields.add("Attack_network");
                if (!body.containsKey("Domestic_operator")) missingFields.add("Domestic_operator");
                if (!body.containsKey("New_Nodes")) missingFields.add("New_Nodes");
                if (!body.containsKey("New_Links")) missingFields.add("New_Links");

                return ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(ResponseInfo.not("请求体缺少必要字段：" + String.join(", ", missingFields)));
            }

            // 提取请求数据
            List<String> attackNetwork = new ArrayList<String>();
            if(body.containsKey("Attack_network")) {
                attackNetwork = (List<String>) body.get("Attack_network");
            }

            List<String> domesticOperator = new ArrayList<String>();
            if(body.containsKey("Domestic_operator")) {
                domesticOperator = (List<String>) body.get("Domestic_operator");
            }

            List<String> newnodes = new ArrayList<String>();
            if(body.containsKey("New_Nodes")) {
                newnodes = (List<String>) body.get("New_Nodes");
            }

            List<String> newlinks = new ArrayList<String>();
            if(body.containsKey("New_Links")) {
                newlinks = (List<String>) body.get("New_Links");
            }

            // 构建 SQL 插入语句
            Sql insertSql = Sqls.create("INSERT INTO operation_list (operation_list, sceneid, operating_time) VALUES (@operationList, @sceneid, CURRENT_TIMESTAMP)");
            String operationList = Json.toJson(Map.of("Attack_network", attackNetwork, "Domestic_operator", domesticOperator, "New_Nodes", newnodes, "New_Links", newlinks));
            insertSql.params().set("operationList", operationList);

            insertSql.params().set("sceneid", sceneid);

            // 执行插入
            dao.execute(insertSql);

            // 返回响应
            return ServerResponse.ok().bodyValue(ResponseInfo.ok("插入成功", null));
        }).onErrorResume(e -> {
            // 错误处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：" + e.getMessage()));
        });
    }

    public Mono<ServerResponse> getOperations(ServerRequest request) {
        // 获取路径参数 sceneid
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 构建 SQL 查询语句
        //Sql querySql = Sqls.create("SELECT operation_list FROM operation_list WHERE sceneid=@sceneid");
        Sql querySql = Sqls.create("SELECT operation_list FROM operation_list WHERE sceneid=@sceneid ORDER BY operating_time DESC LIMIT 1");
        querySql.params().set("sceneid", sceneid); // 设置参数
        querySql.setCallback(Sqls.callback.maps()); // 设置回调函数，将结果作为列表返回

        // 执行查询
        dao.execute(querySql);

        // 获取查询结果
        List<Map<String, Object>> results = (List<Map<String, Object>>) (List<?>) querySql.getList(Map.class);

        // 返回响应
        return ServerResponse.ok().bodyValue(ResponseInfo.ok("查询成功", results));
    }

    public Mono<ServerResponse> getNodesWithoutASN(ServerRequest request) {

        String sceneid = request.pathVariable("sceneid");// 获取 sceneid 参数

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 创建返回结果对象
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sceneid", sceneid);


        // 根据 sceneid 查询数据库视图 node_view 并获取对应的 节点信息
        String sql = "SELECT id,asn,node,country,longitude,latitude,serverid FROM `node_view` WHERE sceneid=" + sceneid + ";";
        Sql nodeSql = Sqls.create(sql);
        nodeSql.params().set("sceneid", sceneid);
        nodeSql.setCallback(Sqls.callback.maps());
        dao.execute(nodeSql);
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) (List<?>) nodeSql.getList(Map.class);
        responseData.put("nodes", nodes);

        // 返回响应
        return ServerResponse.ok().bodyValue(responseData);
    }

    public Mono<ServerResponse> getNodesWithASN(ServerRequest request) {

        String sceneid = request.pathVariable("sceneid");
        String asn = request.pathVariable("asn");// 获取 sceneid 参数

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 创建返回结果对象
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sceneid", sceneid);


        // 根据 sceneid 查询数据库视图 node_view 并获取对应的 节点信息
        String sql = "SELECT id,asn,node,country,longitude,latitude,serverid FROM `node_view` WHERE sceneid=" + sceneid + " and asn like '%" + asn + "%';";
        Sql nodeSql = Sqls.create(sql);
        nodeSql.params().set("sceneid", sceneid);
        nodeSql.setCallback(Sqls.callback.maps());
        dao.execute(nodeSql);
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) (List<?>) nodeSql.getList(Map.class);
        responseData.put("nodes", nodes);

        // 返回响应
        return ServerResponse.ok().bodyValue(responseData);
    }

    public Mono<ServerResponse> searchNodes(ServerRequest request) {

        String sceneid = request.pathVariable("sceneid");  // 获取 sceneid 参数
        String asn = request.pathVariable("asn");// 获取 asn 参数

        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 检查是否传入了 asn
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有asn参数"));
        }

        // 创建返回结果对象
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sceneid", sceneid);


        // 根据 sceneid 查询数据库视图 node_view 并获取对应的 节点信息
        String sql = "SELECT id, asn, node, longitude, latitude FROM `node` WHERE sceneid=" + sceneid + " and status = 1 and asn like '%" + asn + "%';";
        Sql nodeSql = Sqls.create(sql);
        nodeSql.params().set("sceneid", sceneid);
        nodeSql.setCallback(Sqls.callback.maps());
        dao.execute(nodeSql);
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) (List<?>) nodeSql.getList(Map.class);
        responseData.put("nodes", nodes);

        // 返回响应
        return ServerResponse.ok().bodyValue(responseData);
    }

    public Mono<ServerResponse> interrupt(ServerRequest request) {
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        timestamp = new Timestamp(System.currentTimeMillis());

        // 解析请求体 JSON
        return request.bodyToMono(Map.class).flatMap(body -> {
            // 检查 JSON 数据是否包含必要的字段
            if (!body.containsKey("Tier1") || !body.containsKey("Operator")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Tier1 或 Operator"));
            }

            // 提取请求数据
            List<Integer> Tier1List = (List<Integer>) body.get("Tier1");
            List<Integer> OperatorList = (List<Integer>) body.get("Operator");

            // 构造链路列表
            Map<Integer, List<Integer>> srcDstMap = new HashMap<>();
            List<Record> links = dao.query("link", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
            for (Record link : links) {
                Integer srcasn = link.getInt("srcasn");
                // 添加逻辑
                if (!srcDstMap.containsKey(srcasn)) {
                    srcDstMap.put(srcasn, new ArrayList<>());
                }

                Integer destasn = link.getInt("destasn");
                if(!srcDstMap.get(srcasn).contains(destasn)) {
                    srcDstMap.get(srcasn).add(destasn);
                }
                if (!srcDstMap.containsKey(destasn)) {
                    // 如果 key 不存在，初始化一个空列表
                    srcDstMap.put(destasn, new ArrayList<>());
                }
                if(!srcDstMap.get(destasn).contains(srcasn)) {
                    srcDstMap.get(destasn).add(srcasn);
                }
            }

            JSONObject linksObj = new JSONObject();
            ArrayList<JSONObject> linksList = new ArrayList<>();


            // 获取服务器地址列表
            List<String> serverUrls = new ArrayList<>();
            serverUrls.clear();
            Map<Integer, String> asnServerMap = new HashMap<>();
            for (Integer operator : OperatorList) { // 确保OperatorList正确定义

                // 1. 获取服务器信息（根据实际DAO逻辑调整）
                Record serverRec = dao.fetch("node_view", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", operator));
                if (serverRec == null) continue;
                serverUrls.add(serverRec.getString("ip"));
                asnServerMap.put(operator, serverRec.getString("ip"));
            }

            // 修改后的异步请求处理逻辑
            List<CompletableFuture<Integer>> futures = OperatorList.stream().map(operator -> CompletableFuture.supplyAsync(() -> {
                JSONObject statusObj = new JSONObject();
                statusObj.put("status", 1);

                String serverUrl = asnServerMap.get(operator);

                // 1. 构建请求URL（修复URL拼接错误）
                String url = "http://" + serverUrl + ":11223/api/sessions/" + sessionid + "/nodes/" + operator + "/status";
                System.out.println("interrupt Url1:" + url);
                System.out.println("interrupt statusObj:" + statusObj);

                // 2. 发送POST请求并验证状态码
                try {

//									 使用能获取HTTP状态码的方法
                    MyHttpResponse response = HttpUtil.postDataWithStatusCode(url, statusObj);

                    int statusCode = response.getStatusCode();

                    if (statusCode != 200) {
                        throw new RuntimeException("服务器 " + serverUrl + " 返回非200状态码: " + statusCode);
                    }
                    // 4. 验证响应内容（可选）
//                    String rst = response.getBody();
//                    if (!StringUtils.hasText(rst)) {
//                        throw new RuntimeException("服务器 " + serverUrl + " 返回空响应");
//                    }

                    return statusCode;


                } catch (Exception e) {
                    throw new RuntimeException("请求服务器 " + serverUrl + " 失败: " + e.getMessage());
                }
            })).toList();

            // 合并所有Future
            CompletableFuture<List<Integer>> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply(v -> futures.stream().map(CompletableFuture::join) // 获取每个Future的结果
                    .collect(Collectors.toList()));

            try {
                // 阻塞等待并获取所有结果
                List<Integer> statusCodes = allFutures.get();
                // 判断所有状态码是否为200
                boolean allSuccess = statusCodes.stream().allMatch(code -> code == 200);

                if (allSuccess) {
                    System.out.println("所有中断申请请求成功！");

                    Map<Integer, String> asnMapIP = new HashMap<>();
                    Map<Integer, String> asnMapServerIp = new HashMap<>();
                    ArrayList<String> neighborObjList = new ArrayList<>();

                    for (Integer operator : OperatorList) {
                        // 1. 获取节点信息（根据实际DAO逻辑调整）
                        Record nodeRec = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", operator));
                        if (nodeRec == null) continue;
                        int innerid = nodeRec.getInt("innerid");

                        // 提前计算第三段和第四段
                        int thirdSegment = (innerid >> 8) & 0xFF;
                        int fourthSegment = innerid & 0xFF;
                        // 构造 IP 地址
                        String ipAddress = String.format("192.168.%d.%d", thirdSegment, fourthSegment);

                        asnMapIP.put(operator, ipAddress);
                    }
                    for (Integer tierAsn : Tier1List) {
                        // 1. 获取节点信息（根据实际DAO逻辑调整）
                        Record serverRec = dao.fetch("node_view", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", tierAsn));
                        if (serverRec == null) continue;
                        String serverIP = serverRec.getString("ip");
                        asnMapServerIp.put(tierAsn, serverIP);

                        for (Integer nodeASN : srcDstMap.get(tierAsn)) {
                            if(OperatorList.contains(nodeASN)) {
                                System.out.println("nodeASN：！" + nodeASN);
                                JSONObject linkObj = new JSONObject();
                                if (OperatorList.contains(nodeASN)) {
                                    linkObj.put("as1", tierAsn);
                                    linkObj.put("as2", nodeASN);
                                    Record recSrc = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", tierAsn));
                                    Record recDest = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", nodeASN));
                                    linkObj.put("as1ID", recSrc.getInt("id"));
                                    linkObj.put("as2ID", recDest.getInt("id"));
                                    Sql sceneSql =  Sqls.create("select id from link where sceneid=@sceneid and ((srcasn=@srcasn1 and destasn=@destasn1) OR (srcasn=@srcasn2 and destasn=@destasn2))");
                                    sceneSql.params().set("sceneid", sceneid);
                                    sceneSql.params().set("srcasn1", tierAsn);
                                    sceneSql.params().set("destasn1", nodeASN);
                                    sceneSql.params().set("srcasn2", nodeASN);
                                    sceneSql.params().set("destasn2", tierAsn);
                                    sceneSql.setCallback(Sqls.callback.str());
                                    dao.execute(sceneSql);
                                    String linkid = sceneSql.getString();
                                    linkObj.put("linkid", Integer.parseInt(linkid));


//                                neighborObj.put("neighbor", asnMapIP.get(nodeASN));
                                    neighborObjList.add(asnMapIP.get(nodeASN));


                                    //TODO
                                    //更新数据库 status置为2：中断后的链路
                                    int srcasn = tierAsn;
                                    int destans = nodeASN;

                                    Chain chain = Chain.make("status", 2);//status置为2：中断后的链路
                                    dao.update("link", chain, Cnd.where("sceneid", "=", sceneid).and("srcasn", "=", srcasn).and("destasn", "=", destans));
                                    dao.update("link", chain, Cnd.where("sceneid", "=", sceneid).and("srcasn", "=", destans).and("destasn", "=", srcasn));
                                }
                                if (!linkObj.isEmpty()) {
                                    linksList.add(linkObj);
                                }
                            }
                        }

                        JSONObject neighborObj = new JSONObject();
                        neighborObj.put("neighbors", neighborObjList);
                        String url = "http://" + serverIP + ":11223/api/sessions/" + sessionid + "/nodes/" + tierAsn + "/neighbor/disable";
                        System.out.println("interrupt Url2:" + url);
                        System.out.println("interrupt neighborObj:" + neighborObj);

                        HttpUtil.postData(url, neighborObj);
                        neighborObjList.clear();


//						return ServerResponse.ok().bodyValue(ResponseInfo.ok("中断成功", null));
                    }
                    linksObj.put("links", linksList);
                    linksObj.put("type", 0);
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("interrupt", linksObj);
                    return ServerResponse.ok().bodyValue(ResponseInfo.ok("中断成功", responseData));
                } else {
                    // 获取失败详情
                    List<String> errors = new ArrayList<>();
                    for (int i = 0; i < statusCodes.size(); i++) {
                        int code = statusCodes.get(i);
                        if (code != 200) {
                            errors.add("服务器 " + serverUrls.get(i) + " 返回状态码: " + code);
                        }
                    }
                    return ServerResponse.status(500).bodyValue(Map.of("error", "部分请求失败", "details", errors));
//					return ResponseEntity.status(500)
//							.body(Map.of("error", "部分请求失败", "details", errors));
                }
            } catch (InterruptedException | ExecutionException e) {
                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：", e.getMessage()));
            }

            // 创建返回结果对象
//            Map<String, Object> responseData = new HashMap<>();
//            responseData.put("links", linksList);
//            return ServerResponse.ok().bodyValue(responseData);
        }).onErrorResume(e -> {
            // 错误处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：", e.getMessage()));
        });
    }

    //撤销操作-中断
    public Mono<ServerResponse> interruptCancel(ServerRequest request) {
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");

        String nestIP = SystemConfig.getStringValue("nestIP");
        NestClient client = new NestClient(nestIP, 50051);

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 创建返回结果对象
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sceneid", sceneid);

        List<Record> linkList = dao.query("link",
                Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("runingadd", "=", 1));
        if(!linkList.isEmpty())
        {
            List<Nest.DeleteLinkRequest> delLinks = new ArrayList<Nest.DeleteLinkRequest>();
            for(Record link : linkList)
            {
                Nest.DeleteLinkRequest.Builder delLinkBuilder = Nest.DeleteLinksRequest.newBuilder().addLinksBuilder();

                Record srcNode = dao.fetch("node", Cnd.where("sceneid", "=", sceneid).and("asn", "=", link.getInt("srcasn")));
                Record destNode = dao.fetch("node", Cnd.where("sceneid", "=", sceneid).and("asn", "=", link.getInt("destasn")));
                delLinkBuilder.setNode1Id(srcNode.getInt("innerid"));
                delLinkBuilder.setNode2Id(destNode.getInt("innerid"));
                delLinkBuilder.setIface1Id(link.getInt("srceth"));
                delLinkBuilder.setIface2Id(link.getInt("desteth"));
                delLinkBuilder.setSessionId(Integer.parseInt(sessionid));
                Nest.DeleteLinkRequest delLink = delLinkBuilder.build();
                delLinks.add(delLink);
            }
            try {
                client.deleteLinks(Integer.parseInt(sessionid), delLinks);
                int deletedRows = 0;
                deletedRows = dao.clear("link", Cnd.where("sceneid", "=", sceneid).and("runingadd", "=", 1));
                System.out.println("删除了 link表中" + deletedRows + " 条记录: sceneid:" + sceneid + " runingadd:" + 1);


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // 根据 sceneid 查询数据库视图 node_view 并获取对应的 节点信息
        List<Record> nodeList = dao.query("node",
                Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("runingadd", "=", 1));
        if(!nodeList.isEmpty())
        {
            List<Integer> delNodes = new ArrayList<>();

            for(Record node : nodeList)
            {
//                delNodes.add(node.getInt("innerid"));
                try {
                    client.deleteNode(Integer.parseInt(sessionid), node.getInt("innerid"));
                    int deletedRows = 0;
                    deletedRows = dao.clear("node", Cnd.where("sceneid", "=", sceneid).and("runingadd", "=", 1));
                    System.out.println("删除了 node表中" + deletedRows + " 条记录: sceneid:" + sceneid + " runingadd:" + 1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        // 根据 sceneid 查询数据库表scene并获取对应的 场景名称 scene
        Sql sceneSql = Sqls.create("SELECT scene FROM scene WHERE id=@sceneid");
        sceneSql.params().set("sceneid", sceneid);
        sceneSql.setCallback(Sqls.callback.str());
        dao.execute(sceneSql);
        String sceneName = sceneSql.getString();
        responseData.put("sceneName", sceneName);

        // 根据 sceneid 查询数据库视图 node_view 并获取对应的 节点信息
        Sql nodeSql = Sqls.create("SELECT\n" + "  `n`.`id` AS `id`,\n" + "  `n`.`asn` AS `asn`,\n" + "  `n`.`node` AS `node`,\n" + "  `c`.`chinese_name` AS `country`,\n" + "  `c`.`code` AS `countrycode`,\n" + "  `n`.`longitude` AS `longitude`,\n" + "  `n`.`latitude` AS `latitude`,\n" + "  `n`.`initroutes` AS `initroutes`,\n" + "  `n`.`serverid` AS `serverid`,\n" + "  `s`.`mserver` AS `mserver`,\n" + "  `s`.`ip` AS `ip` \n" + "FROM\n" + "  ((\n" + "      `node` `n`\n" + "      LEFT JOIN `mserver` `s` ON ((\n" + "          `n`.`serverid` = `s`.`id` \n" + "        )))\n" + "    LEFT JOIN `country` `c` ON ((\n" + "      `n`.`country` = `c`.`id` \n" + "  )))\n" + "  WHERE n.sceneid = @sceneid");
        nodeSql.params().set("sceneid", sceneid);
        nodeSql.setCallback(Sqls.callback.maps());
        dao.execute(nodeSql);
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) (List<?>) nodeSql.getList(Map.class);
        responseData.put("nodes", nodes);

        // 根据 sceneid 查询数据库视图 link_view 并获取对应的 链路信息
        Sql linkSql = Sqls.create("SELECT id,srcid,srcasn,srcip,destid,destasn,destip FROM `link` WHERE sceneid=@sceneid");
        linkSql.params().set("sceneid", sceneid);
        linkSql.setCallback(Sqls.callback.maps());
        dao.execute(linkSql);
        List<Map<String, Object>> links = (List<Map<String, Object>>) (List<?>) linkSql.getList(Map.class);
        responseData.put("links", links);

        // 根据 sceneid 查询数据库视图 scene_server_view 并获取对应的 服务器信息
        Sql serverSql = Sqls.create("SELECT id,mserver,ip FROM `scene_server_view` WHERE sceneid=@sceneid");
        serverSql.params().set("sceneid", sceneid);
        serverSql.setCallback(Sqls.callback.maps());
        dao.execute(serverSql);

        List<Map<String, Object>> servers = (List<Map<String, Object>>) (List<?>) serverSql.getList(Map.class);
        responseData.put("servers", servers);

        // 返回响应
        return ServerResponse.ok().bodyValue(ResponseInfo.ok("撤销成功", responseData));


    }

    /**
     * 读取 ASN 文件并生成多值字典
     *
     * @param filePath 文件路径
     * @return Map<Integer, List < String>> 字典（一个 Key 对应多个 Value）
     */
    public static Map<String, List<Integer>> readAsnFileToMultiValueDict(String filePath) {
        Map<String, List<Integer>> multiValueDict = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 1. 分割每行数据（假设格式为 "key,value"）
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    System.err.println("忽略无效行: " + line);
                    continue;
                }

                String key = parts[0].trim();
                Integer value = Integer.parseInt(parts[1].trim());

                // 2. 处理多值映射
                if (!multiValueDict.containsKey(key)) {
                    multiValueDict.put(key, new ArrayList<>());
                }
                multiValueDict.get(key).add(value);
            }
        } catch (IOException e) {
            System.err.println("读取文件错误: " + e.getMessage());
        }

        return multiValueDict;
    }

    /**
     * 读取 ASN 文件并生成多值字典
     *
     * @param filePath 文件路径
     * @return Map<Integer, List < String>> 字典（一个 Key 对应多个 Value 的list）
     */
    public static Map<String, List<List<Integer>>> readAsnFileToMultiValueDict1(String filePath) {
        Map<String, List<List<Integer>>> multiValueDict = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 1. 分割每行数据（假设格式为 "key,value"）
                String[] parts = line.split(",");
//                if (parts.length != 2) {
//                    System.err.println("忽略无效行: " + line);
//                    continue;
//                }

                String key = parts[0].trim();
                List<Integer> asPathList = new ArrayList<>();
                for(int i = 1; i < parts.length; i++)
                {
                    if(parts[i].trim().length() >= 10)
                    {
                        asPathList.clear();
                        break;
                    }
                    else {
                        asPathList.add(Integer.parseInt(parts[i].trim()));
                    }
                }
//                Integer value = Integer.parseInt(parts[1].trim());

                if(!asPathList.isEmpty())
                {
                    // 2. 处理多值映射
                    if (!multiValueDict.containsKey(key)) {
                        multiValueDict.put(key, new ArrayList<>());
                    }
                    multiValueDict.get(key).add(asPathList);
                }
            }
        } catch (IOException e) {
            System.err.println("读取文件错误: " + e.getMessage());
        }

        return multiValueDict;
    }

    /**
     * 获取最大innerid值
     */
    public int findMaxInnerIdBySceneId(String sceneId) {
        // 1. 使用参数化查询避免 SQL 注入
        String sql = "SELECT MAX(innerid) AS max_inner_id FROM node " + "WHERE sceneid = @sceneid AND status = 1"; // 添加 FROM 子句
        try {
            Sql nodeSql = Sqls.create(sql);
            nodeSql.params().set("sceneid", sceneId);
            nodeSql.setCallback(Sqls.callback.maps());
            dao.execute(nodeSql);

            // 2. 获取结果集并处理空值
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) (List<?>) nodeSql.getList(Map.class);
            if (resultList == null || resultList.isEmpty()) {
                return -1; // 或抛出业务异常
            }

            // 3. 安全类型转换
            Object maxInnerId = resultList.get(0).get("max_inner_id");
            if (maxInnerId == null) {
                return -1;
            }
            return ((Number) maxInnerId).intValue();// 根据实际数据库类型调整（如 Long/Integer）

        } catch (Exception e) {
            // 4. 日志记录或抛出统一异常
            System.out.println("查询最大 InnerID 失败: " + e.getMessage());
        }
        return 0;
    }

    /**
     * 获取table表中最大id值
     */
    public int findMaxIdBySceneId(String table) {
        // 1. 使用参数化查询避免 SQL 注入
        String sql = "SELECT MAX(id) AS max_id FROM " + table + " ;"; // 添加 FROM 子句
        try {
            Sql nodeSql = Sqls.create(sql);
            nodeSql.setCallback(Sqls.callback.maps());
            dao.execute(nodeSql);

            // 2. 获取结果集并处理空值
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) (List<?>) nodeSql.getList(Map.class);
            if (resultList == null || resultList.isEmpty()) {
                return -1; // 或抛出业务异常
            }

            // 3. 安全类型转换
            Object maxInnerId = resultList.get(0).get("max_id");
            if (maxInnerId == null) {
                return -1;
            }
            return ((Number) maxInnerId).intValue();// 根据实际数据库类型调整（如 Long/Integer）

        } catch (Exception e) {
            // 4. 日志记录或抛出统一异常
            System.out.println("查询最大 ID 失败: " + e.getMessage());
        }
        return 0;
    }
    /**
     * 处理ASN文件并更新邻居列表
     */
    public void addNeighbor(String sceneid, Integer srcasn, String serverIP, String sessionid, Integer destasn) {

        JSONObject neighborObj = new JSONObject();
        neighborObj.put("asn", destasn);
        Record nodeRec = dao.fetch(
                "node",
                Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("asn", "=", destasn));

        int innerid = nodeRec.getInt("innerid");

        // 提前计算第三段和第四段
        int thirdSegment = (innerid >> 8) & 0xFF;
        int fourthSegment = innerid & 0xFF;
        // 构造 IP 地址
        String ipAddress = String.format("192.168.%d.%d", thirdSegment, fourthSegment);

        neighborObj.put("address", ipAddress);//获取最大innerid值

        ArrayList<JSONObject> neighborsList = new ArrayList<>();
        neighborsList.add(neighborObj);
        JSONObject neighborsObj = new JSONObject();
        neighborsObj.put("neighbors", neighborsList);

        String url = "http://" + serverIP + ":11223/api/sessions/" + sessionid + "/nodes/" + srcasn + "/neighbors";
        System.out.println("addNeighbor Url2:" + url);
        System.out.println("addNeighbor neighborObj:" + neighborObj);

        HttpUtil.postData(url, neighborsObj);
    }

    /**
     * 添加链路
     */
    public int addLinkWithRunning(String sceneid, Integer operator, String sessionid, Integer nodeid, Integer status)
    {
        int srcEthId = 0;
        int destEthId = 0;
        int srcId = 0;
        int destId = 0;
        int srcInnerId = 0;
        int destInnerId = 0;

        List<Record> linkList = dao.query("link",
                Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("srcasn", "=", operator).and("destasn", "=", nodeid));
        if(!linkList.isEmpty())
        {
            return -1;
        }

        List<Record> srcList1 = dao.query("link",
                Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("srcasn", "=", operator));

        for (int i = 0; i < srcList1.size(); i++) {

            Record r = srcList1.get(i);
            if(i == 0)
            {
                srcId = r.getInt("srcId");
            }
            int eth = r.getInt("srceth");
            if (eth > srcEthId) {
                srcEthId = eth;
            }
        }
        List<Record> srcList2 = dao.query("link",
                Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("destasn", "=", operator));
        for (int i = 0; i < srcList2.size(); i++) {
            Record r = srcList2.get(i);
            if(i == 0)
            {
                srcId = r.getInt("srcId");
            }
            int eth = r.getInt("desteth");
            if (eth > srcEthId) {
                srcEthId = eth;
            }
        }

        List<Record> destList1 = dao.query("link",
                Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("srcasn", "=", nodeid));
        for (int i = 0; i < destList1.size(); i++) {
            Record r = destList1.get(i);
            if(i == 0)
            {
                destId = r.getInt("destId");
            }
            int eth = r.getInt("srceth");
            if (eth > destEthId) {
                destEthId = eth;
            }
        }
        List<Record> destList2 = dao.query("link",
                Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("destasn", "=", nodeid));
        for (int i = 0; i < destList2.size(); i++) {
            Record r = destList2.get(i);
            if(i == 0)
            {
                destId = r.getInt("destId");
            }
            int eth = r.getInt("desteth");
            if (eth > destEthId) {
                destEthId = eth;
            }
        }

        int retId = 0;

        try {
            Record nodeRec = dao.fetch(
                    "node",
                    Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("asn", "=", operator));

            srcInnerId = nodeRec.getInt("innerid");
            Record nodeRec1 = dao.fetch(
                    "node",
                    Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("asn", "=", nodeid));

            destInnerId = nodeRec1.getInt("innerid");
            String nestIP = SystemConfig.getStringValue("nestIP");
            NestClient client = new NestClient(nestIP, 50051);

            int srcasn = operator;
            int destasn = nodeid;
//            int curInnerIdSrc = findMaxInnerIdBySceneId(String.valueOf(sceneid)) + 1;
            // 提前计算第三段和第四段
            int thirdSegment = (srcInnerId >> 8) & 0xFF;
            int fourthSegment = srcInnerId & 0xFF;
            // 构造 IP 地址
            String ipAddresSrc = String.format("192.168.%d.%d", thirdSegment, fourthSegment);

//            int curInnerIdDest = curInnerIdSrc + 1;
            // 提前计算第三段和第四段
            thirdSegment = (destInnerId >> 8) & 0xFF;
            fourthSegment = destInnerId & 0xFF;
            // 构造 IP 地址
            String ipAddressDest = String.format("192.168.%d.%d", thirdSegment, fourthSegment);


            String srcip = ipAddresSrc;
            String destip = ipAddressDest;
            String networksegment = "24";

            dao.insert("link", Chain.make("srcId",nodeRec.get("id")).add("srcasn", operator).add("srcip", ipAddresSrc).add("srceth", srcEthId+1)
                    .add("destId",nodeRec1.get("id")).add("destasn", nodeid).add("destip", ipAddressDest).add("desteth", destEthId+1)
                    .add("runingadd", 1).add("sceneid", sceneid).add("status", status));

            retId = dao.fetch("link", Cnd.where("sceneid", "=", sceneid).and("srcasn", "=", operator).and("destasn", "=", nodeid)).getInt("id");
            client.addLink(Integer.parseInt(sessionid), srcInnerId, destInnerId, null, null, srcEthId + 1, destEthId + 1, null);

            String cmd = "ip route change " + destip + " dev eth" + (srcEthId + 1);
            Nest.NodeCommandResponse ret = client.nodeCommand(Integer.parseInt(sessionid),  srcInnerId, cmd, false, false);
            cmd = "ip route change " + srcip + " dev eth" + (destEthId + 1);
            ret = client.nodeCommand(Integer.parseInt(sessionid),  destInnerId, cmd, false, false);

            String cmd1 = "ip route add " + destip + " dev eth" + (srcEthId + 1);
            ret = client.nodeCommand(Integer.parseInt(sessionid),  srcInnerId, cmd1, false, false);
            cmd1 = "ip route add " + srcip + " dev eth" + (destEthId + 1);
            ret = client.nodeCommand(Integer.parseInt(sessionid),  destInnerId, cmd1, false, false);

            return retId;

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retId;
    }

    /**
     * 添加节点链路
     */
    public int addNodesAndLinksWithRunning(String sceneid, Integer srcASN, String serverIP, String sessionid, Integer nodeASN) {


        int retId = 0;

        try {

            Record newNodeRec = dao.fetch(
                    "nodes",
                    Cnd.where("asn", "=", nodeASN).and("status", "<>", 0));

            Record srcNodeRec = dao.fetch(
                    "node",
                    Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("asn", "=", srcASN));

            List<Record> allNode = dao.query("node",
                    Cnd.where("status", "!=", 0).and("sceneid", "=", sceneid));
            List<Record> allLink = dao.query("link",
                    Cnd.where("status", "!=", 0).and("sceneid", "=", sceneid));

            int curInnerId = allNode.size() + allLink.size() + 1;


            String nestIP = SystemConfig.getStringValue("nestIP");
            NestClient client = new NestClient(nestIP, 50051);

            Integer asn = newNodeRec.getInt("asn");
            String node = newNodeRec.getString("node");
            Integer country = newNodeRec.getInt("country");
            Double lon = newNodeRec.getDouble("longitude");
            Double lat = newNodeRec.getDouble("latitude");
            Float lon_f = lon.floatValue();
            Float lat_f = lat.floatValue();
            Integer serverid = srcNodeRec.getInt("serverid");
            Record server = dao.fetch("mserver", Cnd.where("id", "=", serverid));
            String serverName = server.getString("mserver");
//            String serverIP = server.getString("ip");

            Nest.Node.Builder nodeBuilder = Nest.Node.newBuilder();
            if(nestIP.equals(serverIP))
                nodeBuilder.setServer("");
            else
                nodeBuilder.setServer(serverName);

            String routetype = srcNodeRec.getString("routetype");
            String routeinfobgp = srcNodeRec.getString("routeinfobgp");
            int monitor = 1;
            int status = 1;
//            int curInnerId = findMaxInnerIdBySceneId(String.valueOf(sceneid)) + 1;
            int innerid = curInnerId;
            int id = findMaxIdBySceneId("node");

            Map<String, String> configMonitor = new HashMap<String, String>();
            configMonitor.put("mode", "interrupt");
            Configservices.ConfigServiceConfig configServiceConfig = Configservices.ConfigServiceConfig.newBuilder().setNodeId(asn)
                    .putAllConfig(configMonitor).build();
            nodeBuilder
                    .addConfigServices("BgpMonitor")
                    .putConfigServiceConfigs("BgpMonitor", configServiceConfig);

            //协议配置
            Map<String, String> configBgpv4 = new HashMap<String, String>();
            Map<String, String> configOSPFv2 = new HashMap<String, String>();
            if (StringUtils.hasText(srcNodeRec.getString("routetype"))) {
                if (srcNodeRec.getString("routetype").indexOf("1") > -1 && StringUtils.hasText(srcNodeRec.getString("routeinfobgp"))) { //bgp
                    String routeinfobgp1 = srcNodeRec.getString("routeinfobgp");
                    JSONObject json = JSON.parseObject(routeinfobgp1);
                    for (String key : json.keySet()) {

                        configBgpv4.put(key, json.getString(key));
                    }


                    Configservices.ConfigServiceConfig configServiceConfigbpg = Configservices.ConfigServiceConfig.newBuilder().setNodeId(srcNodeRec.getInt("asn"))
                            .putAllConfig(configBgpv4).build();

                    nodeBuilder
                            .addConfigServices("Bgpv4")
                            .putConfigServiceConfigs("Bgpv4", configServiceConfigbpg);
                }
            }


            nodeBuilder
                    .setId(curInnerId)
                    .setName("n" + asn)
                    .setType(Nest.NodeType.Enum.DEFAULT)
                    .setGeo(Nest.Geo.newBuilder().setLon(lon_f).setLat(lat_f).setAlt(0))
                    .addConfigServices("zebra")
                    .addConfigServices("DefaultLoRoute")
                    .build();

            Nest.Node newNode = nodeBuilder.build();

            Nest.Link.Builder linkBuilder = Nest.Link.newBuilder();

            Integer srcId = srcNodeRec.getInt("id");
            Integer srcInnerId = srcNodeRec.getInt("innerid");

            // 提前计算第三段和第四段
            int thirdSegment = (srcInnerId >> 8) & 0xFF;
            int fourthSegment = srcInnerId & 0xFF;
            // 构造 IP 地址
            String ipAddresSrc = String.format("192.168.%d.%d", thirdSegment, fourthSegment);

            int curInnerIdDest = curInnerId;
            // 提前计算第三段和第四段
            thirdSegment = (curInnerIdDest >> 8) & 0xFF;
            fourthSegment = curInnerIdDest & 0xFF;
            // 构造 IP 地址
            String ipAddressDest = String.format("192.168.%d.%d", thirdSegment, fourthSegment);


            String srcip = ipAddresSrc;
            String destip = ipAddressDest;
            String networksegment = "24";

            int srcEthId = 0;
            int destEthId = 0;

            List<Record> srcList1 = dao.query("link",
                    Cnd.where("status", "!=", 0).and("sceneid", "=", sceneid).and("srcasn", "=", srcASN));
            for (int i = 0; i < srcList1.size(); i++) {
                Record r = srcList1.get(i);
                int eth = r.getInt("srceth");
                if (eth > srcEthId) {
                    srcEthId = eth;
                }
            }
            List<Record> srcList2 = dao.query("link",
                    Cnd.where("status", "!=", 0).and("sceneid", "=", sceneid).and("srcasn", "=", srcASN));
            for (int i = 0; i < srcList2.size(); i++) {
                Record r = srcList2.get(i);
                int eth = r.getInt("desteth");
                if (eth > srcEthId) {
                    srcEthId = eth;
                }
            }
            srcEthId += 1;



            linkBuilder
                    .setNode1Id(srcInnerId)
                    .setNode2Id(curInnerIdDest)
                    .setType(Nest.LinkType.Enum.WIRED)
                    .setIface1(Nest.Interface.newBuilder()
                            .setId(srcEthId)
                            .setNetId(srcEthId + 1)
                            .setName("eth" + srcEthId)
//                            .setIp4(srcip)
//                            .setIp4Mask(Integer.parseInt(networksegment))
//                                    .setMac("00:00:00:00:00:01")
                            .build())
                    .setIface2(Nest.Interface.newBuilder()
                            .setId(destEthId)
                            .setNetId(destEthId+1)
                            .setName("eth" + destEthId)
//                            .setIp4(destip)
//                            .setIp4Mask(Integer.parseInt(networksegment))
//                                    .setMac("00:00:00:00:00:02")
                            .build())
                    .build();

            Nest.Link newLink = linkBuilder.build();

            client.addNodesLinks(Integer.parseInt(sessionid), newNode, newLink);

            String cmd = "ip route change " + ipAddressDest + " dev eth" + srcEthId;
            Nest.NodeCommandResponse ret = client.nodeCommand(Integer.parseInt(sessionid),  srcInnerId, cmd, false, false);
            cmd = "ip route change " + ipAddresSrc + " dev eth" + destEthId;
            ret = client.nodeCommand(Integer.parseInt(sessionid),  innerid, cmd, false, false);

            String cmd1 = "ip route add " + ipAddressDest + " dev eth" + srcEthId;
            ret = client.nodeCommand(Integer.parseInt(sessionid),  srcInnerId, cmd1, false, false);
            cmd1 = "ip route add " + ipAddresSrc + " dev eth" + destEthId;
            ret = client.nodeCommand(Integer.parseInt(sessionid),  innerid, cmd1, false, false);
//            client.addNode(Integer.parseInt(sessionid), newNode);
//            client.addLink(Integer.parseInt(sessionid), srcASN, nodeASN, srcip, destip, srcEthId+1, 0, networksegment);

            dao.insert("node", Chain.make("asn",asn).add("node", node).add("country", country).add("longitude", lon)
                    .add("latitude",lat).add("serverid", serverid).add("routetype", routetype).add("routeinfobgp", routeinfobgp)
                    .add("monitor", monitor).add("innerid", innerid).add("runingadd", 1).add("sceneid", sceneid)
                    .add("status", status).add("runingadd", 1));
            Record srcNodeNewRec = dao.fetch(
                    "node",
                    Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("asn", "=", srcASN));
            Integer idSrc = srcNodeNewRec.getInt("id");
            Record destNodeNewRec = dao.fetch(
                    "node",
                    Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("asn", "=", nodeASN));
            Integer idDest = destNodeNewRec.getInt("id");
            dao.insert("link", Chain.make("srcId",idSrc).add("srcasn", srcASN).add("srcip", ipAddresSrc).add("srceth", srcEthId)
                    .add("destId",idDest).add("destasn", nodeASN).add("destip", ipAddressDest).add("desteth", destEthId)
                    .add("runingadd", 1).add("sceneid", sceneid).add("status", status));
            retId = dao.fetch("link", Cnd.where("sceneid", "=", sceneid).and("srcasn", "=", srcASN).and("destasn", "=", nodeASN)).getInt("id");

            return retId;

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retId;
    }


    public Mono<ServerResponse> IntraNetWorkRerouting(ServerRequest request) {
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 解析请求体 JSON
        return request.bodyToMono(Map.class).flatMap(body -> {
            // 检查 JSON 数据是否包含必要的字段
            if (!body.containsKey("Tier1") || !body.containsKey("Operator")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Tier1 或 Operator"));
            }

            // 提取请求数据
            List<Integer> Tier1List = (List<Integer>) body.get("Tier1");
            List<Integer> OperatorList = (List<Integer>) body.get("Operator");

            List<Integer> nodesList = new ArrayList<>();
            List<Record> nodes = dao.query("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
            for(Record node : nodes)
            {
                Integer asn = node.getInt("asn");
                nodesList.add(asn);
            }

            // 构造链路列表
            Map<Integer, List<Integer>> srcDstMap = new HashMap<>();
            List<Record> links = dao.query("link", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
            for (Record link : links) {
                Integer srcasn = link.getInt("srcasn");
                // 添加逻辑
                if (!srcDstMap.containsKey(srcasn)) {
                    srcDstMap.put(srcasn, new ArrayList<>());
                }

                Integer destasn = link.getInt("destasn");
                srcDstMap.get(srcasn).add(destasn);
                if (!srcDstMap.containsKey(destasn)) {
                    // 如果 key 不存在，初始化一个空列表
                    srcDstMap.put(destasn, new ArrayList<>());
                }
                srcDstMap.get(destasn).add(srcasn);
            }

            JSONObject linksObj = new JSONObject();
            JSONObject objRst = new JSONObject();
            ArrayList<JSONObject> linksList = new ArrayList<>();
            ArrayList<JSONObject> nodesListRst = new ArrayList<>();


            // 获取服务器地址列表
            List<String> serverUrls = new ArrayList<>();
            serverUrls.clear();
            Map<Integer, String> asnServerMap = new HashMap<>();
            for (Integer operator : OperatorList) { // 确保OperatorList正确定义

                // 1. 获取服务器信息（根据实际DAO逻辑调整）
                Record serverRec = dao.fetch("node_view", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", operator));
                if (serverRec == null) continue;
                serverUrls.add(serverRec.getString("ip"));
                asnServerMap.put(operator, serverRec.getString("ip"));
            }

            // 修改后的异步请求处理逻辑
            List<CompletableFuture<Integer>> futures = OperatorList.stream().map(operator -> CompletableFuture.supplyAsync(() -> {
                JSONObject statusObj = new JSONObject();
                statusObj.put("status", 0);

                String serverUrl = asnServerMap.get(operator);

                // 1. 构建请求URL（修复URL拼接错误）
                String url = "http://" + serverUrl + ":11223/api/sessions/" + sessionid + "/nodes/" + operator + "/status";
                System.out.println("IntraNetWorkRerouting Url1:" + url);
                System.out.println("IntraNetWorkRerouting statusObj:" + statusObj);

                // 2. 发送POST请求并验证状态码
                try {
//                    使用能获取HTTP状态码的方法
                    MyHttpResponse response = HttpUtil.postDataWithStatusCode(url, statusObj);

                    int statusCode = response.getStatusCode();
                    if (statusCode != 200) {
                        throw new RuntimeException("服务器 " + serverUrl + " 返回非200状态码: " + statusCode);
                    }

                    // 4. 验证响应内容（可选）
//                    String rst = response.getBody();
//                    if (!StringUtils.hasText(rst)) {
//                        throw new RuntimeException("服务器 " + serverUrl + " 返回空响应");
//                    }

                    return statusCode;
//                    return  200;


                } catch (Exception e) {
                    throw new RuntimeException("请求服务器 " + serverUrl + " 失败: " + e.getMessage());
                }
            })).toList();

            // 合并所有Future
            CompletableFuture<List<Integer>> allFutures =
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                            .thenApply(v -> futures.stream().map(CompletableFuture::join) // 获取每个Future的结果
                    .collect(Collectors.toList()));

            try {
                // 阻塞等待并获取所有结果
                List<Integer> statusCodes = allFutures.get();
                // 判断所有状态码是否为200
                boolean allSuccess = statusCodes.stream().allMatch(code -> code == 200);

                if (allSuccess) {
                    System.out.println("所有本网迂回申请请求成功！");

                    for (Integer operator : OperatorList)
                    {

                        String serverUrl = asnServerMap.get(operator);

//                        JSONObject statusObj = new JSONObject();
//                        statusObj.put("sessionid", Integer.parseInt(sessionid));
//                        statusObj.put("nodeid", operator);
//                        statusObj.put("timestamp", timestamp.getTime());

                        String url = "http://" + serverUrl + ":11223/api/sessions/" + sessionid + "/nodes/" + operator + "/prefixes/delete?timestamp=" + timestamp.getTime();
                        String msg = HttpUtil.getData(url);
//                        String msg = HttpUtil.postData(url, statusObj);
                        System.out.println("reRouting Url2:" + url);
                        System.out.println("reRouting msg:" + msg);

                        List<String> prefixList = new ArrayList<>();

                        // 1. 解析JSON
                        JSONObject objPrefix = JSON.parseObject(msg);
                        JSONArray prefixesArray = objPrefix.getJSONArray("prefixes");

                        // 2. 遍历处理每个prefix值
                        for (int i = 0; i < prefixesArray.size(); i++) {
                            long prefix = prefixesArray.getLong(i);

                            String prefixTmp = unsignedLongToIPv4AndMask(prefix);

                            prefixList.add(prefixTmp);
                        }

                        for (String s : prefixList) {
                            System.out.println("prefixList：" + s);
                        }

//                        String prefix = "223.119.0.0/16";
//                        String prefix = "1.6.64.0/22";
                        System.out.println("当前工作目录: " + System.getProperty("user.dir"));

                        String allRouteFileName = System.getProperty("user.dir") + "/routeFile/" + operator + "_prefix_path.txt";

//                        Map<String, List<Integer>> asnPrefixMap = readAsnFileToMultiValueDict(allRouteFileName);
                        Map<String, List<List<Integer>>> asnPrefixMap = readAsnFileToMultiValueDict1(allRouteFileName);
                        for(String prefix : prefixList) {
                            List<Record> nodesTMP = dao.query("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
                            for(Record node : nodesTMP)
                            {
                                Integer asn = node.getInt("asn");
                                if(!nodesList.contains(asn))
                                {
                                    nodesList.add(asn);
                                }
                            }

                            List<List<Integer>> valuesList = asnPrefixMap.get(prefix);
                                boolean isAllNodesExist = false;
                                boolean isAllLinksExist = false;
                                boolean isExist = false;
                                boolean isNeedAddNode = true;
                                Integer prefixPublisher = 0;
                                boolean isFind = false;
                                List<Integer> countAddNodes = new ArrayList<>();
                                    // 存储 targetValue -> 被删除子列表的最后一个元素
                                    Map<Integer, List<Integer>> removedMap = new HashMap<>();

                                    for (Integer tier : Tier1List) {
                                        for (List<Integer> pathList : valuesList) {
                                            if (pathList.size() == 1 && pathList.get(0).equals(tier)) {
                                                valuesList.remove(pathList);
                                            }
                                        }
                                        removedMap.put(tier, new ArrayList<>());
                                        // 先收集将被删除的子列表的最后一个元素
                                        List<Integer> lastElements = valuesList.stream()
                                                .filter(list -> list != null && !list.isEmpty() && list.contains(tier) && !(list.get(list.size() - 1).equals(tier)))
                                                .map(list -> list.get(list.size() - 1))
                                                .collect(Collectors.toList());

                                        // 然后删除符合条件的子列表
//                                        valuesList.removeIf(list -> list != null && !list.isEmpty() && list.contains(tier));

                                        // 打印或处理被删除子列表的最后一个元素
                                        System.out.println("被删除子列表的最后一个元素: " + lastElements);
                                        if (!lastElements.isEmpty())
                                            removedMap.put(tier, lastElements);
                                        else
                                            removedMap.clear();
                                    }

                                    //判断查找出的aspath链路是否都在节点的list中 nodeslist
                                    Set<Integer> set2 = new HashSet<>(nodesList); // 转换为 HashSet，查找效率 O(1)
                                    List<List<Integer>> allMatchingLists = new ArrayList<>();
                                    for (List<Integer> valueList : valuesList) {
                                        boolean allElementsPresent = set2.containsAll(valueList);
                                        if (allElementsPresent == true) {
                                            isAllNodesExist = allElementsPresent;
                                            allMatchingLists.add(valueList);
                                        }
                                    }
                                    if (!valuesList.isEmpty()) {
                                        //全部节点都存在，只需要判断链路情况
                                        if (isAllNodesExist) {
                                            for (List<Integer> valueList : allMatchingLists) {
                                                for (int i = 0; i < valueList.size() - 1; i++) {
                                                    Integer value = valueList.get(i);
                                                    Integer valueNext = valueList.get(i+1);
                                                    //只要有一条aspath的链路都存在即可
                                                    srcDstMap.clear();
                                                    List<Record> linksTMP = dao.query("link", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
                                                    for (Record link : linksTMP) {
                                                        Integer srcasn = link.getInt("srcasn");
                                                        // 添加逻辑
                                                        if (!srcDstMap.containsKey(srcasn)) {
                                                            srcDstMap.put(srcasn, new ArrayList<>());
                                                        }

                                                        Integer destasn = link.getInt("destasn");
                                                        srcDstMap.get(srcasn).add(destasn);
                                                        if (!srcDstMap.containsKey(destasn)) {
                                                            // 如果 key 不存在，初始化一个空列表
                                                            srcDstMap.put(destasn, new ArrayList<>());
                                                        }
                                                        srcDstMap.get(destasn).add(srcasn);
                                                    }
                                                    if (i == 0 && (srcDstMap.get(operator).contains(value) || srcDstMap.get(value).contains(operator))) {
                                                        System.out.println("prefix is already in links now. node is :" + value);
                                                        isExist = true;
//                                                        Sql sceneSql = Sqls.create("select id from link where sceneid=@sceneid and ((srcasn=@srcasn1 and destasn=@destasn1) OR (srcasn=@srcasn2 and destasn=@destasn2))");
//                                                        sceneSql.params().set("sceneid", sceneid);
//                                                        sceneSql.params().set("srcasn1", operator);
//                                                        sceneSql.params().set("destasn1", value);
//                                                        sceneSql.params().set("srcasn2", value);
//                                                        sceneSql.params().set("destasn2", operator);
//                                                        sceneSql.setCallback(Sqls.callback.str());
//                                                        dao.execute(sceneSql);
//                                                        String linkid = sceneSql.getString();
//
//                                                        Record recSrc = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", operator));
//                                                        Record recDest = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", value));
//
//                                                        JSONObject linkObj = new JSONObject();
//                                                        linkObj.put("as1", operator);
//                                                        linkObj.put("as1ID", recSrc.getInt("id"));
//                                                        linkObj.put("as2", value);
//                                                        linkObj.put("as2ID", recDest.getInt("id"));
//                                                        linkObj.put("linkid", Integer.parseInt(linkid));
//                                                        //                                                linkObj.put("nodeExist", true);
//                                                        linkObj.put("linkExist", true);
//                                                        linkObj.put("node", value);
//                                                        JSONObject linksObj1 = new JSONObject();
//                                                        linksObj1.put("links", linkObj);
//                                                        linksObj1.put("type", 1);
//                                                        objRst.put("IntraNet", linksObj1);
//                                                        break;
                                                    }
                                                    else if(!(srcDstMap.get(value).contains(valueNext) || srcDstMap.get(valueNext).contains(value)))
                                                    {
                                                        isExist = false;
                                                    }
                                                }
                                                if (isExist == true) {
                                                    break;
                                                }
                                            }
                                            if (isExist == false) {
                                                boolean isFinished = false;
                                                for (List<Integer> valueList : allMatchingLists) {
                                                    int srcasn = 0;
                                                    int destasn = 0;
                                                    for (int i = 0; i < valueList.size(); i++) {
                                                        if (i == 0) {
                                                            srcasn = operator;
                                                            destasn = valueList.get(i);
                                                        } else {
                                                            srcasn = valueList.get(i - 1);
                                                            destasn = valueList.get(i);
                                                        }
                                                        if (srcDstMap.containsKey(srcasn) && srcDstMap.get(srcasn).contains(destasn)) {
                                                            continue;
                                                        } else {
                                                            System.out.println("prefix is not in links now. but node is already in , only need to add link: as1:" + srcasn + ", as2:" + destasn);

                                                            String serverIp = asnServerMap.get(operator);
                                                            addNeighbor(sceneid, srcasn, serverIp, sessionid, destasn);
                                                            int linkid = addLinkWithRunning(sceneid, srcasn, sessionid, destasn, 3);

                                                            Record recSrc = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", srcasn));
                                                            Record recDest = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", destasn));

                                                            JSONObject linkObj = new JSONObject();
                                                            linkObj.put("as1", srcasn);
                                                            linkObj.put("as1ID", recSrc.getInt("id"));
                                                            linkObj.put("as2", destasn);
                                                            linkObj.put("as2ID", recDest.getInt("id"));
                                                            linkObj.put("linkid", linkid);
                                                            linkObj.put("linkExist", false);
                                                            JSONObject linksObj1 = new JSONObject();
                                                            linksObj1.put("links", linkObj);
                                                            linksObj1.put("type", 2);
                                                            objRst.put("IntraNet", linksObj1);
                                                            isFinished = true;
                                                            //                                                    break;
                                                        }
                                                    }
                                                    if (isFinished == true)
                                                        break;
                                                }
                                            }
                                        } else//需要添加节点
                                        {
                                            for (List<Integer> valueList : valuesList) {
                                                int cnt = 0;
                                                for (Integer value : valueList) {
                                                    boolean isContain = nodesList.contains(value);
                                                    if (isContain == false) {
                                                        cnt++;
                                                    }
                                                }
                                                countAddNodes.add(cnt);
                                            }
                                            int min = Collections.min(countAddNodes); // 找到最小值
                                            int minIndex = countAddNodes.indexOf(min); // 找到最小值的索引

                                            List<Integer> subList = valuesList.get(minIndex);
                                            int srcasn = 0;
                                            int destasn = 0;
                                            for (int i = 0; i < subList.size(); i++) {
                                                int curAsn = subList.get(i);
                                                if (nodesList.contains(curAsn) == false) {
                                                    if (i == 0) {
                                                        srcasn = operator;
                                                        destasn = subList.get(i);
                                                    } else {
                                                        srcasn = subList.get(i - 1);
                                                        destasn = subList.get(i);
                                                    }
                                                    System.out.println("prefix is not in links now. need to add node and link:" + destasn);
                                                    String serverIp = asnServerMap.get(srcasn);
                                                    int linkid1 = addNodesAndLinksWithRunning(sceneid, srcasn, serverIp, sessionid, curAsn);
                                                    int linkid2 = addLinkWithRunning(sceneid, curAsn, sessionid, subList.get(i + 1), 4);
                                                    addNeighbor(sceneid, srcasn, serverIp, sessionid, curAsn);
                                                    addNeighbor(sceneid, curAsn, serverIp, sessionid, srcasn);
                                                    addNeighbor(sceneid, curAsn, serverIp, sessionid, subList.get(i + 1));
                                                    addNeighbor(sceneid, subList.get(i + 1), serverIp, sessionid, curAsn);

                                                    Record recSrc = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", srcasn));
                                                    Record recDest = dao.fetch("node_view", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", destasn));
                                                    Record recDestNest = dao.fetch("node_view", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", subList.get(i + 1)));
                                                    JSONObject linkObj = new JSONObject();
                                                    linkObj.put("as1", srcasn);
                                                    linkObj.put("as1ID", recSrc.getInt("id"));
                                                    linkObj.put("as2", destasn);
                                                    linkObj.put("as2ID", recDest.getInt("id"));
                                                    linkObj.put("isExist", false);
                                                    linkObj.put("linkid", linkid1);
                                                    linksList.add(linkObj);
                                                    JSONObject linkObj1 = new JSONObject();
                                                    linkObj1.put("as1", destasn);
                                                    linkObj1.put("as1ID", recDest.getInt("id"));
                                                    linkObj1.put("as2", subList.get(i + 1));
                                                    linkObj1.put("as2ID", recDestNest.getInt("id"));
                                                    linkObj1.put("linkid", linkid2);
                                                    linkObj1.put("isExist", false);
                                                    linksList.add(linkObj1);
                                                    JSONObject nodeObj = new JSONObject();
                                                    nodeObj.put("node", destasn);
                                                    nodeObj.put("lon", recDest.getDouble("longitude"));
                                                    nodeObj.put("lat", recDest.getDouble("latitude"));
                                                    nodeObj.put("id", recDest.getInt("id"));
                                                    nodeObj.put("asn", recDest.getInt("asn"));
                                                    nodeObj.put("name", recDest.getString("node"));
                                                    nodeObj.put("country", recDest.getString("country"));
                                                    nodeObj.put("mserver", recDest.getString("mserver"));
                                                    nodeObj.put("serverip", recDest.getString("ip"));
                                                    nodeObj.put("isExist", false);
                                                    nodesListRst.add(nodeObj);
                                                    JSONObject linksObj1 = new JSONObject();
                                                    linksObj1.put("links", linksList);
                                                    linksObj1.put("nodes", nodesListRst);
                                                    linksObj1.put("type", 3);
                                                    objRst.put("IntraNet", linksObj1);
                                                }
                                            }

                                        }
                                    }

//                            System.out.println("Key: " + key);
//                            values.forEach(value -> System.out.println("  Value: " + value));
                        }



//						return ServerResponse.ok().bodyValue(ResponseInfo.ok("节点 " + tierAsn + "本网迂回成功", null));
                    }

                } else {
                    // 获取失败详情
                    List<String> errors = new ArrayList<>();
                    for (int i = 0; i < statusCodes.size(); i++) {
                        int code = statusCodes.get(i);
                        if (code != 200) {
                            errors.add("服务器 " + serverUrls.get(i) + " 返回状态码: " + code);
                        }
                    }
                    return ServerResponse.status(500).bodyValue(Map.of("error", "部分请求失败", "details", errors));
//					return ResponseEntity.status(500)
//							.body(Map.of("error", "部分请求失败", "details", errors));
                }
            } catch (InterruptedException | ExecutionException e) {
                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：" + e.getMessage()));
            }

//            Map<String, Object> responseData = new HashMap<>();
//            responseData.put("IntraNet", retList);
            return ServerResponse.ok().bodyValue(ResponseInfo.ok("本网迂回成功", objRst));
        }).onErrorResume(e -> {
            // 错误处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：" + e.getMessage()));
        });
    }


    public static <T> List<List<T>> getUniquePairs(List<T> list) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                List<T> pair = new ArrayList<>();
                pair.add(list.get(i));
                pair.add(list.get(j));
                result.add(pair);
            }
        }
        return result;
    }

    public Mono<ServerResponse> InterNetWorkRerouting(ServerRequest request) {
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        // 处理

//            List<Integer> OperatorList = (List<Integer>) body.get("Operator");
            List<Integer> OperatorList = new ArrayList<>();
            OperatorList.add(4134);
            OperatorList.add(4837);

            // 获取服务器地址列表
            Map<Integer, String> asnServerMap = new HashMap<>();
            List<Record> serverList = dao.query("node_view", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0));


            for(Record serverRec : serverList) {
                for (Integer operator : OperatorList) { // 确保OperatorList正确定义
                    // 1. 获取服务器信息（根据实际DAO逻辑调整）
                    if(serverRec.getInt("asn") == operator) {
                        asnServerMap.put(operator, serverRec.getString("ip"));
                    }
                }
            }


            List<List<Integer>> pairs = getUniquePairs(OperatorList);
            JSONObject linkObj = new JSONObject();
            JSONObject linksObj = new JSONObject();
            ArrayList<JSONObject> linksList = new ArrayList<>();

            for(List<Integer> pair: pairs)
            {

                int linkid = addLinkWithRunning(sceneid,  pair.get(0), sessionid, pair.get(1), 5);

                String serverIp = asnServerMap.get(pair.get(0));
                addNeighbor(sceneid, pair.get(0), serverIp, sessionid, pair.get(1));
                serverIp = asnServerMap.get(pair.get(1));
                addNeighbor(sceneid, pair.get(1), serverIp, sessionid, pair.get(0));
                linkObj.put("as1", pair.get(0));
                linkObj.put("as2", pair.get(1));
                Record recSrc = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", pair.get(0)));
                Record recDest = dao.fetch("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).and("asn", "=", pair.get(1)));
                linkObj.put("as1ID", recSrc.getInt("id"));
                linkObj.put("as2ID", recDest.getInt("id"));
                linkObj.put("linkid", linkid);
                linksList.add(linkObj);
            }

            System.out.println("linksList:" + linksList);
            linksObj.put("links", linksList);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("InterNet", linksObj);
            return ServerResponse.ok().bodyValue(ResponseInfo.ok("跨网迂回成功", responseData));
    }

    public static long IPv4ToUnsignedLong(String ipAddress, String mask)
    {
        // 将 IP 地址拆分为 4 个部分
        String[] strList = ipAddress.split("\\.");

        // 将字符串转换为整数
        int[] ipParts = new int[4];
        for (int i = 0; i < 4; i++) {
            ipParts[i] = Integer.parseInt(strList[i]);
        }

        // 计算无符号长整型值
        long ipULong = ((long) ipParts[0] << 24) +
                ((long) ipParts[1] << 16) +
                ((long) ipParts[2] << 8) +
                ((long) ipParts[3]) +
                (Long.parseLong(mask) << 32);


        // 输出结果
//        System.out.println("IPv4: " + ipAddress);
//        System.out.println("Unsigned Long: " + ipULong);

        return ipULong;
    }

    /**
     * 从组合的long值还原IPv4地址和前缀长度
     * @param combined 组合值，格式为 (prefixLength << 32) | ipv4Value
     * @return 字符串数组，第一个元素是IPv4地址，第二个元素是前缀长度
     */
    public static String unsignedLongToIPv4AndMask(long combined) {
        // 1. 提取前缀长度（取高32位）
        int prefixLength = (int)(combined >>> 32);

        // 验证前缀长度是否合法
        if(prefixLength < 0 || prefixLength > 32) {
            throw new IllegalArgumentException("非法前缀长度: " + prefixLength);
        }

        // 2. 提取IPv4的十进制值（取低32位）
        long ipv4Value = combined & 0xFFFFFFFFL;

        // 3. 将十进制IPv4值转换为点分十进制格式
        String ipAddress = longToIpv4(ipv4Value);

        return ipAddress + '/' + String.valueOf(prefixLength);
    }

    /**
     * 将long类型的IPv4值转换为点分十进制字符串
     * @param ipv4Value IPv4的十进制表示
     * @return 点分十进制字符串
     */
    private static String longToIpv4(long ipv4Value) {
        return String.format("%d.%d.%d.%d",
                (ipv4Value >> 24) & 0xFF,  // 第一个字节
                (ipv4Value >> 16) & 0xFF,  // 第二个字节
                (ipv4Value >> 8) & 0xFF,   // 第三个字节
                ipv4Value & 0xFF           // 第四个字节
        );
    }

    /**
     * 处理ASN文件并更新邻居列表
     */
    public int getASPathCount(String sessionid, List<String> serverList, List<Long> prefixesList, Map<String, List<Integer>> serverNodeDict, List<Integer>TierList, List<Integer>OperatorList)
    {

        // 修改后的异步请求处理逻辑
        List<CompletableFuture<Integer>> futures = serverList.stream().map(serverIP -> CompletableFuture.supplyAsync(() -> {

            List<List<Integer>> linksList = new ArrayList<>();
            ArrayList<JSONObject> reqObjList = new ArrayList<>();

            List<Integer> nodesList = serverNodeDict.get(serverIP);
            for(Integer node : nodesList)
            {
                JSONObject reqObj = new JSONObject();
                if(!(TierList.contains(node) || OperatorList.contains(node)))
                {
                    reqObj.put("nodeId", node);
                    reqObj.put("prefixes", prefixesList);
                    reqObjList.add(reqObj);
                }
            }
            JSONObject nodeObj = new JSONObject();
            nodeObj.put("nodes", reqObjList);
            // 1. 构建请求URL（修复URL拼接错误）
            String url = "http://" + serverIP + ":11223/api/sessions/" + sessionid + "/list-prefix-path";
            System.out.println("getASPath Url:" + url);
//            System.out.println("getASPath nodeObj:" + nodeObj);

            // 2. 发送POST请求并验证状态码
            try {
//									 使用能获取HTTP状态码的方法
                String rst = HttpUtil.postData(url, nodeObj);
                int cnt = 0;
                //初始路由
                if (StringUtils.hasText(rst)) {
//					"nodeRoutes"

                    JSONObject json = JSONObject.parseObject(rst);

                    String rstString = json.getString("routes");

                    JSONArray jsonArray = JSONArray.parseArray(rstString);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Integer asn = jo.getInteger("id");
                        JSONArray routes = jo.getJSONArray("routes");
                        for (int j = 0; j < routes.size(); j++) {
                            JSONObject route = routes.getJSONObject(j);
                            String prefix = route.getString("id");
                            String aspath = route.getString("asPath");
//                            System.out.println("======getASPath======\n url:" + url + "aspath:" + aspath + " asn:" + asn);
                            if(aspath != null && !aspath.equals("null"))
                            {
                                String[] asnArray = aspath.substring(1, aspath.length() - 1).split(",");

                                Integer publisher = Integer.parseInt(asnArray[asnArray.length-1]);//发布者
                                if(typeLink == 1)
                                {
                                    if(TierList.contains(publisher))
                                    {
                                        cnt += 1;
                                    }

                                }
                                else if(typeLink == 2 || typeLink == 0)
                                {
                                    if(OperatorList.contains(publisher))
                                    {
                                        cnt += 1;
                                    }

                                }


                            }
                        }
                        //TODO

//                        System.out.println("======getASPath======\n url:" + url + "linksList:" + linksList );
                    }
                }

                return  cnt;
            } catch (Exception e) {
                throw new RuntimeException("请求服务器 " + serverIP + " 失败: " + e.getMessage());
            }
        })).toList();

        // 等待所有 Future 完成，并合并结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        // 合并所有结果
        CompletableFuture<List<Integer>> combinedFuture = allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)  // 获取每个 Future 的结果
                        .collect(Collectors.toList())  // 合并成 List<List<Integer>>
        );

        // 获取最终结果（阻塞直到所有任务完成）
        List<Integer> finalResult = combinedFuture.join();


//        System.out.println("合并后的结果: " + finalResult);
        // 去重逻辑
        Set<String> seen = new HashSet<>();
        List<Integer> deduped = finalResult.stream()
                .collect(Collectors.toList());

        System.out.println("去重后: " + deduped);
        int count = 0;
        for(int i = 0; i < deduped.size(); i++)
        {
            count = count + deduped.get(i);
        }
//        if(typeLink == 1)
//            totalCnt = count;
//        else if(typeLink == 2)
//            totalCnt = totalCnt - count;

        return count;
    }

    /**
     * 劫持反劫持方法
     */
    public void attack(String sceneid, String sessionid, List<Integer> nodesList, List<Map<String, Object>> routesList)
    {

        for(int i = 0; i < routesList.size(); i++)
        {
            Map<String, Object> route = routesList.get(i);
            String prefix = (String) route.get("Prefixs"); // 获取 IP 前缀
            List<Integer> asPath = (List<Integer>) route.get("AsPath"); // 获取 AS 路径

            System.out.println("Prefix: " + prefix + ", AsPath: " + asPath);
        }

        // 修改后的异步请求处理逻辑
        List<CompletableFuture<String>> futures = nodesList.stream().map(node -> CompletableFuture.supplyAsync(() -> {


            Record serverREC = dao.fetch("node_view", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("asn", "=", node));
            if (serverREC != null) {
                JSONArray routeArray = new JSONArray();
                String serverIP = serverREC.getString("ip");
                for(int i = 0; i < routesList.size(); i++)
                {
                    Map<String, Object> route = routesList.get(i);
                    String prefix = (String) route.get("Prefixs");
                    List<Integer> asPath = (List<Integer>) route.get("AsPath");
                    JSONObject routeObj = new JSONObject();
                    routeObj.put("family", 1);
                    routeObj.put("prefix", prefix);
                    routeObj.put("nextHop", "");
                    JSONArray communityArray = new JSONArray();
//                    routeObj.put("community", communityArray);
                    JSONArray largeCommunityArray = new JSONArray();
//                    routeObj.put("largeCommunity", largeCommunityArray);
//                    routeObj.put("med", 0);
                    JSONArray asPathArray = new JSONArray();
                    for(Integer asn : asPath)
                    {
                        asPathArray.add(asn);
                    }
                    routeObj.put("asPath", asPathArray);

                    routeArray.add(routeObj);
                }
                JSONObject routesObj = new JSONObject();
                routesObj.put("routes", routeArray);

                String url = "http://" + serverIP + ":11223/api/sessions/" + sessionid + "/nodes/" + node + "/routes";
                System.out.println("attack Url:" + url);
                System.out.println("attack routesObj:" + routesObj);
                String msg = HttpUtil.postData(url, routesObj);
                System.out.println("attack msg:" + msg);
                return msg;
            }
            else {
                return "error";
            }

        })).toList();

        return;
    }

    /*
    * 通过前缀以及攻击节点，被劫持节点查询链路
     */
    public Mono<ServerResponse> searchLinkByPrefix(ServerRequest request) {
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        List<Integer> allNodes = new ArrayList<>();
        List<Integer> remainingNodes = new ArrayList<>();
        List<String> serverList = new ArrayList<>();

        List<Record> nodesRec = dao.query("node", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("sceneid", "=", sceneid));
        for (Record node : nodesRec) {
            Integer asn = node.getInt("asn");
            allNodes.add(asn);
        }

        // 解析请求体 JSON
        return request.bodyToMono(Map.class).flatMap(body -> {
            // 检查 JSON 数据是否包含必要的字段
            if (!body.containsKey("Operator")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Operator"));
            }
            if (!body.containsKey("Tier1")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Tier"));
            }
            if (!body.containsKey("Prefixs")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Prefixs"));
            }

            List<Integer> OperatorList = (List<Integer>) body.get("Operator");
            List<Integer> TierList = (List<Integer>) body.get("Tier1");

            for(Integer node : allNodes)
            {
                if(!(OperatorList.contains(node) || TierList.contains(node)))
                {
                    remainingNodes.add(node);
                }
            }

            List<String> PrefixsStrList = (List<String>) body.get("Prefixs");
            List<Long> PrefixsList = new ArrayList<>();
//            System.out.println("PrefixsStrList: " + PrefixsStrList);
            for(String prefix : PrefixsStrList)
            {
                String[] tmpStr = prefix.split("/");
                String ipv4Str = tmpStr[0];
                String maskStr = tmpStr[1];

//                System.out.println("ipv4Str: " + ipv4Str + " maskStr:" +maskStr);
                Long prefixLong = IPv4ToUnsignedLong(ipv4Str, maskStr);
//                System.out.println("Unsigned Long prefixLong: " + prefixLong);
                PrefixsList.add(prefixLong);
            }

            // 获取服务器地址列表
            Map<Integer, String> asnServerMap = new HashMap<>();
            List<Record> serverRec = dao.query("node_view", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0));
            List<Integer> nodeServer = new ArrayList<>();
            Map<String, List<Integer>> serverAsnDic = new HashMap<>();

            for(Record server: serverRec)
            {
                String serverIP = server.getString("ip");
                Integer asn = server.getInt("asn");
                serverList.add(serverIP);
                if (!serverAsnDic.containsKey(serverIP)) {
                    serverAsnDic.put(serverIP, new ArrayList<>());
                }
                serverAsnDic.get(serverIP).add(asn);

            }
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            System.out.println("(Line:" + Thread.currentThread().getStackTrace()[1].getLineNumber() + ") - " +  LocalDateTime.now().format(formatter));

            Set<String> set = new HashSet<>(serverList); // 将 List 转换为 HashSet
            List<String> uniqServerList = new ArrayList<>(set); // 将 HashSet 转换回 List

//            List<List<Integer>> rst =  getASPath(sessionid, uniqServerList, PrefixsList, serverAsnDic);
            // 修改后的异步请求处理逻辑
            List<CompletableFuture<List<List<Integer>>>> futures = uniqServerList.stream().map(serverIP -> CompletableFuture.supplyAsync(() -> {

                List<List<Integer>> linksList = new ArrayList<>();
                ArrayList<JSONObject> reqObjList = new ArrayList<>();

                List<Integer> nodesList = serverAsnDic.get(serverIP);
                for(Integer node : nodesList)
                {
                    JSONObject reqObj = new JSONObject();
                    if(!(TierList.contains(node) || OperatorList.contains(node)))
                    {
                        reqObj.put("nodeId", node);
                        reqObj.put("prefixes", PrefixsList);
                        reqObjList.add(reqObj);
                    }
                }
                JSONObject nodeObj = new JSONObject();
                nodeObj.put("nodes", reqObjList);
                // 1. 构建请求URL（修复URL拼接错误）
                String url = "http://" + serverIP + ":11223/api/sessions/" + sessionid + "/list-prefix-path";
//                String url = "http://100.146.4.41:8080/api/" + serverIP + "/sessions/" + sessionid + "/list-prefix-path";
                System.out.println("getASPath Url:" + url);
                System.out.println("getASPath nodeObj:" + nodeObj);

                // 2. 发送POST请求并验证状态码
                try {
//									 使用能获取HTTP状态码的方法
                    String rst = HttpUtil.postData(url, nodeObj);
                    System.out.println("rst:" + rst);
                    //初始路由
                    if (StringUtils.hasText(rst)) {
//					"nodeRoutes"

                        JSONObject json = JSONObject.parseObject(rst);

                        String rstString = json.getString("routes");

                        JSONArray jsonArray = JSONArray.parseArray(rstString);
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Integer asn = jo.getInteger("id");
                            JSONArray routes = jo.getJSONArray("routes");
                            for (int j = 0; j < routes.size(); j++) {
                                JSONObject route = routes.getJSONObject(j);
                                String id = route.getString("id");
                                String aspath = route.getString("asPath");
//                            System.out.println("======getASPath======\n url:" + url + "aspath:" + aspath + " asn:" + asn);
                                if(aspath != null && !aspath.equals("null"))
                                {
                                    String[] asnArray = aspath.substring(1, aspath.length() - 1).split(",");

                                    int curAsn = 0;
                                    int preAsn = asn;
                                    boolean isChangeNode = false;
                                    Integer publisher = Integer.parseInt(asnArray[asnArray.length-1]);//发布者
                                    if(typeLink == 1)
                                    {
                                        if(TierList.contains(publisher))
                                        {
                                            isChangeNode = true;
                                        }

                                    }
                                    else if(typeLink == 2)
                                    {
                                        if(OperatorList.contains(publisher))
                                        {
                                            isChangeNode = true;
                                        }

                                    }
                                    for(int l = 0; l < asnArray.length; l++)
                                    {
                                        List<Integer> prefixPathList = new ArrayList<>();
                                        curAsn = Integer.parseInt(asnArray[l].trim());

                                        if(isChangeNode || typeLink == 0)
                                        {
                                            if (curAsn < preAsn) {
                                                prefixPathList.add(curAsn);
                                                prefixPathList.add(preAsn);
                                            } else {
                                                prefixPathList.add(preAsn);
                                                prefixPathList.add(curAsn);
                                            }
                                            prefixPathList.add(asn);
                                            preAsn = curAsn;
                                            linksList.add(prefixPathList);
                                        }


                                    }
                                    isChangeNode = false;
                                }
                            }
                            //TODO

//                        System.out.println("======getASPath======\n url:" + url + "linksList:" + linksList );
                        }
                    }

//                    System.out.println("(Line:" + Thread.currentThread().getStackTrace()[1].getLineNumber() + ") - " +  LocalDateTime.now().format(formatter));

                    return  linksList;
                } catch (Exception e) {
                    throw new RuntimeException("请求服务器 " + serverIP + " 失败: " + e.getMessage());
                }
            })).toList();

            // 等待所有 Future 完成，并合并结果
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            );

            // 合并所有结果
            CompletableFuture<List<List<Integer>>> combinedFuture = allFutures.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join)  // 获取每个 Future 的结果
                            .flatMap(List::stream)         // 展平 List<List<Integer>> → List<Integer>
                            .collect(Collectors.toList())  // 合并成 List<List<Integer>>
            );

            // 获取最终结果（阻塞直到所有任务完成）
            List<List<Integer>> finalResult = combinedFuture.join();


//        System.out.println("合并后的结果: " + finalResult);
            // 去重逻辑
            Set<String> seen = new HashSet<>();
            List<List<Integer>> rst = finalResult.stream()
                    .filter(list -> seen.add(list.toString())) // 利用 Set 去重
                    .collect(Collectors.toList());

//        System.out.println("去重后: " + deduped);

            JSONObject linksObj = new JSONObject();
            ArrayList<JSONObject> linksList = new ArrayList<>();
            List<Integer> nodesList = new ArrayList<>();

            List<Record> linkRecList = dao.query("link", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0));

            for(List<Integer> link : rst)
            {
                JSONObject linkObj = new JSONObject();
                Integer srcasn = link.get(0);
                Integer destasn = link.get(1);

                for(int i = 0; i < linkRecList.size(); i++)
                {
                    Record linkRec = linkRecList.get(i);
                    Integer srcRec = linkRec.getInt("srcasn");
                    Integer destRec = linkRec.getInt("destasn");
//                    System.out.println("(Line:" + Thread.currentThread().getStackTrace()[1].getLineNumber() + ") - " +  LocalDateTime.now().format(formatter));
//                    System.out.println("srcRec:" + srcRec + " destRec:" + destRec);

//                    System.out.println("srcRec:" + srcRec + " destRec:" + destRec);
//                    System.out.println("srcasn:" + srcasn + " destasn:" + destasn);

                    if((srcRec.equals(srcasn)) && (destRec.equals(destasn)))
                    {
                        linkObj.put("srcid", srcasn);
                        linkObj.put("destid", destasn);
                        linkObj.put("linkid", linkRec.getInt("id"));
                        linksList.add(linkObj);
                        nodesList.add(link.get(2));
//                        System.out.println("srcRec:" + srcRec + " destRec:" + destRec);
//                        System.out.println("srcasn:" + srcasn + " destasn:" + destasn);
                        break;
                    }
                    else if((srcRec.equals(destasn)) && (destRec.equals(srcasn)))
                    {
                        linkObj.put("srcid", destasn);
                        linkObj.put("destid", srcasn);
                        linkObj.put("linkid", linkRec.getInt("id"));
                        linksList.add(linkObj);
                        nodesList.add(link.get(2));
//                        System.out.println("srcRec:" + srcRec + " destRec:" + destRec);
//                        System.out.println("srcasn:" + srcasn + " destasn:" + destasn);

                        break;
                    }
                }

//                Record linkRec = dao.fetch("link", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("srcasn", "=", link.get(0)).and("destasn", "=", link.get(1)));
//                if(linkRec != null)
//                {
//                    linkObj.put("srcid", link.get(0));
//                    linkObj.put("destid", link.get(1));
//                    linkObj.put("linkid", linkRec.getInt("id"));
//                    linksList.add(linkObj);
//                    nodesList.add(link.get(2));
//                }
//                else {
//                    Record linkRec2 = dao.fetch("link", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("srcasn", "=", link.get(1)).and("destasn", "=", link.get(0)));
//                    if(linkRec2 != null)
//                    {
//                        linkObj.put("srcid", link.get(1));
//                        linkObj.put("destid", link.get(0));
//                        linkObj.put("linkid", linkRec2.getInt("id"));
//                        linksList.add(linkObj);
//                        nodesList.add(link.get(2));
//                    }
//                }


            }
//            System.out.println("(Line:" + Thread.currentThread().getStackTrace()[1].getLineNumber() + ") - " +  LocalDateTime.now().format(formatter));


            Set<Integer> nodeSet = new HashSet<>(nodesList); // 将 List 转换为 HashSet
            List<Integer> uniqNodeList = new ArrayList<>(nodeSet); // 将 HashSet 转换回 List

            ArrayList<JSONObject> nodesRstList = new ArrayList<>();

            for (Record node : nodesRec) {
                Integer asn = node.getInt("asn");
                Integer nodeID = node.getInt("id");
                for(Integer nodeASN : uniqNodeList)
                {
                    if(nodeASN.equals(asn))
                    {
                        JSONObject nodeObj = new JSONObject();
                        nodeObj.put("id", nodeID);
                        nodeObj.put("asn", asn);
                        nodesRstList.add(nodeObj);
                        break;
                    }
                }
            }
            System.out.println("nodesRstList:" + nodesRstList);

            linksObj.put("links", linksList);
            linksObj.put("nodes", nodesRstList);
            linksObj.put("type", typeLink);


            return ServerResponse.ok().bodyValue(linksObj);
        }).onErrorResume(e -> {
            // 错误处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：" + e.getMessage()));
        });
    }

    public Mono<ServerResponse> hijack(ServerRequest request) {
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");
        typeLink = 1;

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        List<String> serverList = new ArrayList<>();



        // 解析请求体 JSON
        return request.bodyToMono(Map.class).flatMap(body -> {
            // 检查 JSON 数据是否包含必要的字段
            if (!body.containsKey("Operator")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Operator"));
            }
            if (!body.containsKey("Tier1")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Tier"));
            }
            if (!body.containsKey("Routes")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Routes"));
            }

            List<Integer> TierList = (List<Integer>) body.get("Tier1");
            // 处理 Routes 中的 AsPath
            List<Map<String, Object>> routesList = (List<Map<String, Object>>) body.get("Routes");
            System.out.println("body.get(\"Routes\"):" + body.get("Routes") + " routesList:" + routesList);


            attack(sceneid, sessionid, TierList, routesList);



            return ServerResponse.ok().bodyValue(ResponseInfo.ok("劫持成功"));
        }).onErrorResume(e -> {
            // 错误处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：" + e.getMessage()));
        });
    }

    public Mono<ServerResponse> resHijack(ServerRequest request) {
        // 获取路径参数 sceneid
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");
        typeLink = 2;

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        List<String> serverList = new ArrayList<>();



        // 解析请求体 JSON
        return request.bodyToMono(Map.class).flatMap(body -> {
            // 检查 JSON 数据是否包含必要的字段
            if (!body.containsKey("Operator")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Operator"));
            }
            if (!body.containsKey("Tier1")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Tier"));
            }
            if (!body.containsKey("Routes")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Routes"));
            }

            List<Integer> OperatorList = (List<Integer>) body.get("Operator");
            // 处理 Routes 中的 AsPath
            List<Map<String, Object>> routesList = (List<Map<String, Object>>) body.get("Routes");
            System.out.println("body.get(\"Routes\"):" + body.get("Routes") + " routesList:" + routesList);


            attack(sceneid, sessionid, OperatorList, routesList);



            return ServerResponse.ok().bodyValue(ResponseInfo.ok("反劫持成功"));
        }).onErrorResume(e -> {
            // 错误处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：" + e.getMessage()));
        });
    }

//    http://100.146.4.41:8080/api/100.146.4.48/sessions/99/nodes/4780/
//    his-routes?cidr=&paging={"pageNumber":1,"pageSize":100000}&sorting={"sortBy":"timestamp","sortDirection":2}&types=[1,2,3]
//http://100.146.4.41:8080/api/100.146.4.48/sessions/99/nodes/4134/
// routes?paging={%22pageNumber%22:1,%22pageSize%22:10}&sorting={%22sortBy%22:%22timestamp%22,%22sortDirection%22:2}&type=1
    public Mono<ServerResponse> searchPrefixByAsn(ServerRequest request) {
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");
        String nodeid = request.pathVariable("nodeid");

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }
        // 检查是否传入了 nodeid
        if (!StringUtils.hasText(nodeid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有nodeid参数"));
        }

        JSONArray prefixArray = new JSONArray();
        JSONObject prefixObj = new JSONObject();

        Record nodeRec = dao.fetch("node_view", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0).and("asn", "=", nodeid));


        if(nodeRec != null)
        {
            String serverIP = nodeRec.getString("ip");

            String url = "http://" + serverIP + ":11223/api/sessions/" + sessionid + "/nodes/" + nodeid +
                    "/routes?paging=%7B%22pageNumber%22%3A1%2C%22pageSize%22%3A20%7D&sorting=%7B%22sortBy%22%3A%22timestamp%22%2C%22sortDirection%22%3A2%7D&type=1";

//            String url = "http://100.146.4.41:8080/api/" + serverIP + "/sessions/" + sessionid + "/nodes/" + nodeid + "/his-routes?cidr=&paging=%7B%22pageNumber%22%3A1%2C%22pageSize%22%3A100000%7D&sorting=%7B%22sortBy%22%3A%22timestamp%22%2C%22sortDirection%22%3A2%7D&types=%5B1%2C2%2C3%5D";

            String rst = HttpUtil.getData(url);

            //初始路由
            if (StringUtils.hasText(rst))
            {
                JSONObject json = JSONObject.parseObject(rst);

                String rstString = json.getString("routes");
                if (rstString != null)
                {
                    JSONObject jsonTmp = JSONObject.parseObject(rstString);

                    String ribString = jsonTmp.getString("rib");
                    JSONArray jsonArray = JSONArray.parseArray(ribString);

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String prefix = jo.getString("prefix");
                        prefixArray.add(prefix);
                    }

                    prefixObj.put("prefix", prefixArray);
                }

            }
        }

        return ServerResponse.ok().bodyValue(prefixObj);

    }

    public Mono<ServerResponse> getCntNetWork(ServerRequest request) {
        // 获取路径参数 sceneid
        String sessionid = request.pathVariable("sessionid");
        String sceneid = request.pathVariable("sceneid");

        // 检查是否传入了 sessionid
        if (!StringUtils.hasText(sessionid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sessionid参数"));
        }
        // 检查是否传入了 sceneid
        if (!StringUtils.hasText(sceneid)) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有sceneid参数"));
        }

        List<Integer> allNodes = new ArrayList<>();
        List<Integer> remainingNodes = new ArrayList<>();
        List<String> serverList = new ArrayList<>();

        List<Record> nodesRec = dao.query("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
        for (Record node : nodesRec) {
            Integer asn = node.getInt("asn");
            allNodes.add(asn);
        }

        // 解析请求体 JSON
        return request.bodyToMono(Map.class).flatMap(body -> {
            // 检查 JSON 数据是否包含必要的字段
            if (!body.containsKey("Operator")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Operator"));
            }
            if (!body.containsKey("Tier1")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Tier"));
            }
            if (!body.containsKey("Prefixs")) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请求体缺少必要字段：Prefixs"));
            }

            List<Integer> OperatorList = (List<Integer>) body.get("Operator");
            List<Integer> TierList = (List<Integer>) body.get("Tier1");

            for(Integer node : allNodes)
            {
                if(!(OperatorList.contains(node) || TierList.contains(node)))
                {
                    remainingNodes.add(node);
                }
            }

            List<String> PrefixsStrList = (List<String>) body.get("Prefixs");
            List<Long> PrefixsList = new ArrayList<>();
            for(String prefix : PrefixsStrList)
            {
                String[] tmpStr = prefix.split("/");
                String ipv4Str = tmpStr[0];
                String maskStr = tmpStr[1];

                Long prefixLong = IPv4ToUnsignedLong(ipv4Str, maskStr);
//                System.out.println("Unsigned Long prefixLong: " + prefixLong);
                PrefixsList.add(prefixLong);
            }

            // 获取服务器地址列表
            Map<Integer, String> asnServerMap = new HashMap<>();
            List<Record> serverRec = dao.query("node_view", Cnd.where("sceneid", "=", sceneid).and("status", "<>", 0));
            List<Integer> nodeServer = new ArrayList<>();
            Map<String, List<Integer>> serverAsnDic = new HashMap<>();

            for(Record server: serverRec)
            {
                String serverIP = server.getString("ip");
                Integer asn = server.getInt("asn");
                serverList.add(serverIP);
                if (!serverAsnDic.containsKey(serverIP)) {
                    serverAsnDic.put(serverIP, new ArrayList<>());
                }
                serverAsnDic.get(serverIP).add(asn);

            }
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            System.out.println("(Line:" + Thread.currentThread().getStackTrace()[1].getLineNumber() + ") - " +  LocalDateTime.now().format(formatter));

            Set<String> set = new HashSet<>(serverList); // 将 List 转换为 HashSet
            List<String> uniqServerList = new ArrayList<>(set); // 将 HashSet 转换回 List

            Integer rstCnt =  getASPathCount(sessionid, uniqServerList, PrefixsList, serverAsnDic, TierList, OperatorList);

            JSONObject cntObj = new JSONObject();

            System.out.println("typeLink:" + typeLink);
            System.out.println("totalCnt:" + totalCnt);
            System.out.println("rstCnt:" + rstCnt);
            if(typeLink == 0)
            {
                totalCnt = rstCnt;
                cntObj.put("cnt", rstCnt);
            }
            else if(typeLink == 1)
            {
                curCnt = totalCnt - rstCnt;
                cntObj.put("cnt", curCnt);
            }
            else if(typeLink == 2)
            {
                if(rstCnt < curCnt)
                    cntObj.put("cnt", curCnt);
                else if(rstCnt > totalCnt)
                    cntObj.put("cnt", totalCnt);
                else {
                    cntObj.put("cnt", rstCnt);
                }
            }
            System.out.println("typeLink:" + typeLink);
            System.out.println("totalCnt:" + totalCnt);
            System.out.println("rstCnt:" + rstCnt);
//            cntObj.put("cnt", rstCnt);
            cntObj.put("timestamp", System.currentTimeMillis()/1000);


            return ServerResponse.ok().bodyValue(cntObj);
        }).onErrorResume(e -> {
            // 错误处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(ResponseInfo.not("服务器内部错误：" + e.getMessage()));
        });
    }



}
