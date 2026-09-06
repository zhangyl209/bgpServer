package com.example.businesses;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.nest.SimulationService;
import com.example.nest.api.Nest;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.HttpUtil;
import com.example.business.base.SystemConfig;
import com.example.commons.InParamsActor;
import com.example.commons.InParamsDb;
import com.example.nest.NestClient;
import com.example.nest.StartSessionMThread;
import com.example.nest.StopSessionMThread;
import com.example.nest.api.Configservices.ConfigServiceConfig;
import com.example.nest.api.Nest.Geo;
import com.example.nest.api.Nest.Interface;
import com.example.nest.api.Nest.Link;
import com.example.nest.api.Nest.LinkOptions;
import com.example.nest.api.Nest.LinkType;
import com.example.nest.api.Nest.Node;
import com.example.nest.api.Nest.Node.Builder;
import com.example.nest.api.Nest.NodeType;
import com.example.nutz.DBTools;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;

import com.google.common.base.CaseFormat;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.nutz.trans.Atom;
import org.nutz.trans.Trans;
import reactor.core.scheduler.Schedulers;

import java.util.Set;
import java.util.LinkedHashSet;

@Component
public class BGPBusiness {
	
	Dao dao = DBTools.getDao();
	@Autowired
	private SimulationService simulationService;
	/*
	 * 分配节点到服务器（post）
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> assignNode() {
		return flux -> flux.map(record -> {
			
			if (StringUtils.hasText(record.getString("sceneid"))) {
				
				Cnd cnd = Cnd.where("status", "!=", 0).and("sceneid", "=", record.getString("sceneid"));
				
				List<Record> servers = dao.query("scene_server_view", cnd, null, "id,mserver,ip");
				
				if (servers.size() == 0) {
					return record;
				}
				
				for (int i = 0; i < servers.size(); i++) {
					servers.get(i).set("name", servers.get(i).get("mserver").toString());
					servers.get(i).remove("mserver");
				}
				
				List<Record> links = dao.query("link", cnd);
				List<String> asns = new ArrayList<String>();
				for (int i = 0; i < links.size(); i++) {
					Record link = links.get(i);
					asns.add(link.getString("srcasn"));
					asns.add(link.getString("destasn"));
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("servers", servers);
				jsonObject.put("links", asns);
				
//				System.out.println(asns.size());
				System.out.println(jsonObject);
				String assignNode = SystemConfig.getStringValue("assignNode");
				String result = HttpUtil.postData("http://"+ assignNode, jsonObject);
				
				JSONObject resultObject = JSON.parseObject(result);
				String serversResult = resultObject.getString("servers");
				JSONArray list = JSONArray.parseArray(serversResult);
				for (int i = 0 ; i < list.size(); i++) {
					JSONObject map = (JSONObject) list.get(i);
					String serverId = map.get("id").toString();
					List<String> nodes = (List<String>) map.get("nodes");
					System.out.println(nodes.size());
					System.out.println(nodes);
					
//					//由于节点asn返回了字符串格式，需要处理，待接口修改后可注释掉
//					for(int j = 0; j < nodes.size(); j++) {
//						nodes.set(j, nodes.get(i).replace("\"", ""));
//					}
//					
					
					if (nodes != null && nodes.size() > 0) {
						dao.update("node", Chain.make("serverid", serverId), Cnd.where("asn", "in", String.join(",", nodes)).and("sceneid" ,"=", record.getString("sceneid")));
					}
					
				}
			}
			
			return record;
		});
	}

	/*
	 * 分配节点到服务器（post）
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> updateNode() {
		return flux -> flux.map(record -> {

			if (StringUtils.hasText(record.getString("sceneid"))) {

				String sceneid = record.getString("sceneid");
				String asn = record.getString("asn");
				String country = record.getString("country");
				String id = record.getString("id");
				String latitude = record.getString("latitude");
				String longitude = record.getString("longitude");
				String mlabel = record.getString("mlabel");
				String node = record.getString("node");
				String org = record.getString("org");
				String serverid = record.getString("serverid");
				int countryId = 0;

				Cnd cnd = Cnd.where("status", "!=", 0).and(id, "=", id).and("sceneid", "=", record.getString("sceneid"));

				Cnd cndCountry = Cnd.where("status", "!=", 0).and("chinese_name", "=", country);
				Record countryRec = dao.fetch("country", cndCountry);
				if (countryRec != null) {
					countryId = countryRec.getInt("id");
				} else {
					countryId = 1;
				}

				dao.update("node", Chain.make("serverid", serverid).add("org", org).add("node", node).add("mlabel", mlabel).add("longitude", longitude)
						.add("latitude", latitude).add("country", countryId), Cnd.where("asn", "=", asn).and("id", "=", id).and("sceneid" ,"=", sceneid));

			}

			return record;
		});
	}

	/*
	 * 分配节点到服务器(随机分配)
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> assignNode_old() {
		return flux -> flux.map(record -> {
			
			if (StringUtils.hasText(record.getString("sceneid"))) {
				
				List<Record> nodes = dao.query("node", Cnd.where("status", "=", 1).and("serverid", "is", null).and("sceneid", "=", record.getInt("sceneid")));
				List<Record> servers = dao.query("scene_server", Cnd.where("status", "=", 1).and("sceneid", "=", record.getInt("sceneid")));
				
				if (servers != null && servers.size() > 0 ) {
					int serverCount = servers.size();

					for (int i = 0; i < nodes.size(); i++) {
						Record node = nodes.get(i);
						int index = i % serverCount;
						Record server = servers.get(index);
						
						dao.update("node", Chain.make("serverid", server.getInt("serverid")), Cnd.where("id", "=", node.getInt("id")));
						
					}
				} 
			}
			return record;
		});
	}
	
	/*
	 * 分配链路节点IP
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> assignLinkIp() {
		return flux -> flux.map(record -> {
			
			if (StringUtils.hasText(record.getString("sceneid"))) {

				int sceneid = record.getInt("sceneid");

				// 获取当前时间
				LocalDateTime now = LocalDateTime.now();

				// 设置时间格式
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				// 格式化时间并输出
				String formattedTime = now.format(formatter);

				// 根据 sceneid 查询数据库视图 node_view 并获取对应的 节点信息
				String sql = "DELETE FROM link WHERE (ISNULL(destid) OR ISNULL(srcid)) and sceneid="+ sceneid + ";";
				Sql nodeSql = Sqls.create(sql);
				nodeSql.params().set("sceneid", sceneid);
				dao.execute(nodeSql);

				// 打印带时间的日志
				System.out.println("[" + formattedTime + "] - 开始更新所有链路ip.");
				List<Record> nodeList = dao.query(
						"node",
						Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).orderBy("asn", "asc")
				);
				List<Record> links_withoutNS = dao.query("link", Cnd.where("status", "=", 1).and("networksegment", "=", null).and("sceneid", "=", record.getInt("sceneid")));

				// 创建一个 Map 来保存 asn 和 ip 地址的关系
				Map<Integer, String> asnToIpMap = new HashMap<>();

				// 第一遍循环：计算 IP 地址并保存至 Map
				for (int i = 0; i < nodeList.size(); i++)
				{
					Record node = nodeList.get(i);
					int innerid = i + 1;

					// 提前计算第三段和第四段
					int thirdSegment = (innerid >> 8) & 0xFF;
					int fourthSegment = innerid & 0xFF;

					// 构造 IP 地址
					String ipAddress = String.format("192.168.%d.%d", thirdSegment, fourthSegment);

					// 更新 node 表
					dao.update("node", Chain.make("innerid", innerid), Cnd.where("id", "=", node.getInt("id")).and("sceneid", "=", sceneid));

					// 将 IP 地址和 ASN 的映射关系存入 Map 中
					int asn = node.getInt("asn");
					asnToIpMap.put(asn, ipAddress);
				}
				// 获取当前时间
				now = LocalDateTime.now();

				// 设置时间格式
				formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				// 格式化时间并输出
				formattedTime = now.format(formatter);

				// 打印带时间的日志
				System.out.println("[" + formattedTime + "] - 更新innerid完成.");

				// 第二遍循环：根据 link 表的 srcasn 和 destasn 从 Map 获取对应的 IP 地址并同步更新
				for (int j = 0; j < links_withoutNS.size(); j++)
				{
					Record link = links_withoutNS.get(j);
					int srcasn = link.getInt("srcasn");
					int destasn = link.getInt("destasn");

					// 从 Map 获取 srcip 和 destip
					String srcip = asnToIpMap.get(srcasn);
					String destip = asnToIpMap.get(destasn);

					// 如果 srcip 和 destip 不为 null，则更新 link 表
					if (srcip != null && destip != null) {
						// 同步更新 link 表
						dao.update("link", Chain.make("srcip", srcip).add("destip", destip), Cnd.where("srcasn", "=", srcasn).and("destasn", "=", destasn).and("sceneid", "=", sceneid).and("id", "=", link.getInt("id")));
					}
				}
				// 获取当前时间
				now = LocalDateTime.now();

				// 设置时间格式
				formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				// 格式化时间并输出
				formattedTime = now.format(formatter);

				// 打印带时间的日志
				System.out.println("[" + formattedTime + "] - 更新所有链路ip完成.");
				
			}
			return record;
		});
	}
	
	/*
	 * 复制策略
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> copyStatement() {
		return flux -> flux.map(record -> {
			List<String> toNodeIds = new ArrayList<String>();
			String[] list = record.getString("to").split(",");
			
			Record statement = dao.fetch("statement", Cnd.where("id", "=", record.getString("from")));
			statement.set(".table", "statement");
			statement.set("copyfrom", statement.getString("id"));
			
			for (int i = 0 ; i < list.length; i++) {
				if (!list[i].equals(statement.getString("nodeid"))) {
					toNodeIds.add(list[i]);
				}
			}
			
			if (toNodeIds.size() > 0) {
				List<Record> statementList = dao.query("statement", 
						Cnd.where("copyfrom", "=", statement.getString("id")).and("nodeid", "in", toNodeIds.stream().map(String::valueOf).collect(Collectors.joining(","))).and("status", "=", "1"));
				
				if (statementList == null || statementList.size() == 0) { //说明是第一次复制(只有新复制的时候)
					for (int i = 0 ; i < toNodeIds.size(); i++ ) {
						Record statementCopied = statement.clone();
						statementCopied.remove("id");
						statementCopied.set("nodeid", toNodeIds.get(i));
						
						statementList.add(statementCopied);
					}
					List<Record> statementListRet = dao.fastInsert(statementList);
					dao.update(statement, Cnd.where("id", "=", statement.getString("id")));
				} else { //包括曾经复制过和新增加的
					List<String> ids = new ArrayList<String>();
					List<String> nodeIds = new ArrayList<String>();
					for (int i = 0 ; i < statementList.size(); i++ ) {
						Record statementCopied = statementList.get(i);
						ids.add(statementCopied.getString("id"));
						nodeIds.add(statementCopied.getString("nodeid"));
					}
					dao.update("statement", 
							Chain.make("statement", statement.getString("statement")).add("direction", statement.getString("direction")).add("action", statement.getString("action")),
							Cnd.where("id", "in",  String.join(",", ids)));
					//找到新增加的
					List<String> nodeIds_new = new ArrayList<String>();
					if (toNodeIds.size() > nodeIds.size()) {
						for (int i = 0; i < toNodeIds.size(); i++ ) {
							if (nodeIds.indexOf(toNodeIds.get(i)) < 0) {
								nodeIds_new.add(toNodeIds.get(i));
							}	
						}
						
						List<Record> statementList_new = new ArrayList<Record>();
						for (int i = 0 ; i < nodeIds_new.size(); i++ ) {
							Record statementCopied = statement.clone();
							statementCopied.remove("id");
							statementCopied.set("nodeid", nodeIds_new.get(i));
							
							statementList_new.add(statementCopied);
						}
						List<Record> statementListRet = dao.fastInsert(statementList_new);
						dao.update(statement, Cnd.where("id", "=", statement.getString("id")));
					}
				}
			}
	
			return record;
		});
	}
	
	public static List reverseorRemove(List list, Object element) {
	    for (int i = list.size() - 1; i >= 0; i--) {
	        if (element.equals(list.get(i))) {
	            list.remove(i);
	        }
	    }
	    return list;
	}
	
//	/*
//	 * 统计服务器分配节点数量
//	 * */
//	@Bean
//	public Function<Flux<Record>, Flux<Record>> addNodeCount() {
//		return flux -> flux.map(record -> {
//			String sceneid = record.getString("sceneid");
//			String serverid = record.getString("serverid");
//			
//			int count = dao.count("node", Cnd.where("status", "=", "1").and("sceneid", "=", sceneid).and("serverid", "=", serverid));
//			
//			record.set("nodecount", count);
//			
//			return record;
//		});
//	}
	
	/*
	 * 统计服务器分配节点数量
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> addNodeCount() {
		return flux -> flux.map(record -> {
			String sceneid = record.getString("sceneid");
			String serverid = record.getString("serverid");
			
			List<String> nodeIds = new ArrayList<String>();
			List<Record> list = dao.query("node", Cnd.where("status", "=", "1").and("sceneid", "=", sceneid).and("serverid", "=", serverid));
			for (int i = 0 ; i < list.size(); i++) {
				Record node = list.get(i);
				nodeIds.add(node.getString("id"));
			}
			
			String nodeIdsStr = StringUtils.collectionToDelimitedString(nodeIds, ",") ;
			
			
			int linkCount = nodeIdsStr.length() > 0 ? dao.count("link", Cnd.where("status", "=", "1").and("srcid", "in", nodeIdsStr).or("destid", "in", nodeIdsStr)) : 0;
			
			record.set("nodecount", list.size());
			record.set("linkcount", linkCount);
			
			return record;
		});
	}
	
	/*
	 * 获取节点树
	 * */
	@Bean
	public Function<Flux<Record>, Flux<List<Record>>> getNodeTreeInScene() {
		return flux -> flux.map(record -> {
			SqlExpressionGroup cnd = Cnd.exps("status", "<>", 0).and("sceneid", "=", record.getString("sceneid"));
			SqlExpressionGroup cnd1 = Cnd.exps("status", "<>", 0).and("sceneid", "=", record.getString("sceneid"));
			
			Record scene = dao.fetch("scene", Cnd.where("id", "=", record.getString("sceneid")));
			String defaultLabel = scene.getString("scene");
			
			if (StringUtils.hasText(record.getString("mlabel"))) {
				if (record.getString("mlabel").equals(defaultLabel)) {
					cnd.and("mlabel", "is", null);
					cnd1.and("mlabel", "is", null);
				} else {
					cnd.and("mlabel", "=", record.getString("mlabel"));
					cnd1.and("mlabel", "=", record.getString("mlabel"));
				}
			}
			
			Condition c = Cnd.where(cnd);
			if (StringUtils.hasText(record.getString("node"))) {
				SqlExpressionGroup e1 = cnd.and("node", "like", "%" + record.getString("node") + "%");
				SqlExpressionGroup e2 = cnd1.and("asn", "like", "%" + record.getString("node") + "%");
				c = Cnd.where(e1).or(e2);
			}
			
			List<Record> list = dao.query("node_view", c);
			
			List<Record> result = new ArrayList<Record>();
			Map<String, List<Record>> map = new HashMap<String, List<Record>>();
			for (int i = 0; i < list.size(); i++) {
				Record node = list.get(i);
				String label = StringUtils.hasText(node.getString("mlabel")) ? node.getString("mlabel") : defaultLabel;
				
				List<Record> l = map.get(label) != null ? map.get(label) : new ArrayList<Record>();
				l.add(node);
				
				map.put(label, l);
			}
			
			for (String key : map.keySet()) {
				Record r = new Record();
				r.put("mlabel", key);
				r.put("children", map.get(key));
				result.add(r);
			}
			
			return result;
		});
	}
	
	/*
	 * 获取节点树——set
	 * */
	@Bean
	public Function<Flux<Record>, Flux<List<Record>>> getNodeTreeInSet() {
		return flux -> flux.map(record -> {
			
			Record mlabelset = dao.fetch("mlabelset", Cnd.where("id", "=", record.getString("setid")));
			
			List<String> mlabels = Arrays.asList(mlabelset.getString("mlabel").split(","));
			
			List<Record> list = dao.query("node", Cnd.where("status", "<>", 0).and("mlabel", "in", mlabels));

			List<Record> result = new ArrayList<Record>();
			Map<String, List<Record>> map = new HashMap<String, List<Record>>();
			for (int i = 0; i < list.size(); i++) {
				Record node = list.get(i);
				
				String label = node.getString("mlabel");
				
				List<Record> l = map.get(label) != null ? map.get(label) : new ArrayList<Record>();
				l.add(node);
				
				map.put(label, l);
			}
			
			for (String key : map.keySet()) {
				Record r = new Record();
				r.put("mlabel", key);
				r.put("children", map.get(key));
				result.add(r);
			}
			
			return result;
		});
	}
	
	/*
	 * 获取链路树
	 * */
	@Bean
	public Function<Flux<Record>, Flux<List<Record>>> getLinkTreeInScene() {
		return flux -> flux.map(record -> {
			List<Record> result = new ArrayList<Record>();
			
			Cnd cnd = Cnd.where("status", "<>", 0).and("sceneid", "=", record.getString("sceneid"));
			
			List<Record> list = dao.query("link_view", cnd);
			
			Map<String, List<Record>> map = new HashMap<String, List<Record>>();
			for (int i = 0; i < list.size(); i++) {
				Record link = list.get(i);
				
				int srccountry = StringUtils.hasText(link.getString("srccountry")) ? link.getInt("srccountry") : 0;
				int destcountry = StringUtils.hasText(link.getString("destcountry")) ? link.getInt("destcountry") : 0;
				
				String label = "国内-国际";
				if (srccountry == 64 && destcountry == 64) { //国内-国内
					label = "国内-国内";
				} else if (srccountry != 64 && destcountry != 64) {
					label = "国际-国际";
				}
				List<Record> l = map.get(label) != null ? map.get(label) : new ArrayList<Record>();
				l.add(link);
				
				map.put(label, l);
			}
			
			for (String key : map.keySet()) {
				Record r = new Record();
				r.put("mlabel", key);
				r.put("children", map.get(key));
				result.add(r);
			}
			
			return result;
		});
	}
	
	/*
	 * 开始场景仿真
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> startScene() {
		return flux -> flux.map(record -> {
			String sceneid = record.getString("sceneid");
			String nestIP = SystemConfig.getStringValue("nestIP");
//			//获取场景服务器
			List<Record> servers = dao.query("scene_server_view", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
			List<Nest.Server> nestServers = new ArrayList<>();
			Map<Integer, Record> serverDic = new HashMap<>();
			for (int i = 0; i < servers.size(); i++) {
				Record server = servers.get(i);
				if (!(server.getString("ip")).equals(nestIP)) {
					nestServers.add(Nest.Server.newBuilder().setHost(server.getString("ip"))
							.setName(server.getString("mserver")).build());
					serverDic.put(server.getInt("id"), server);
				}
			}
			Record simulation = new Record();
			simulation.set("sceneid", sceneid);
			Date start = new Date();
			Date started = start;
			simulation.set("starttime", start);
			
			//获取仿真sessionid
			int sessionid = 1;
//			NestClient client1 = new NestClient(nestIP, 50051);
//            try {
//                sessionid = client1.getSessionCount() + 1;
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            List<Record> simulationList = dao.query("simulation", Cnd.where("status", "<>", 0).desc("sessionid"));

			if (simulationList != null && simulationList.size() > 0) {
				Record simulationLastest = simulationList.get(0);

				sessionid = StringUtils.hasText(simulationLastest.getString("sessionid")) ? simulationLastest.getInt("sessionid") + 1 : 1;
			}
			simulation.set("sessionid", sessionid);
			
			//获取业务
			List<Record> businessList = dao.query("business", Cnd.where("status", "=", 1).and("sceneid", "=", sceneid));
			Map<String, Map<String, String>> businessesMap = new HashMap<String, Map<String, String>>();
			for (int i = 0; i < businessList.size(); i++ ) {
				Record business = businessList.get(i);
				Map<String, String> config = new HashMap<String, String>();
				config.put("flowId", business.getString("id"));
				config.put("srcId", business.getString("srcasn"));
				config.put("dstId", business.getString("destasn"));
				
				float rate = Float.parseFloat(business.getString("lifecycle"));
				if (business.getString("typevalue2").equals("Kbps/s")) {
					rate = rate * 1000;
				} else if (business.getString("typevalue2").equals("Mbps/s")) {
					rate = rate * 1000000;
				} else if (business.getString("typevalue2").equals("Gbps/s")) {
					rate = rate * 1000000000;
				} 
				int rateInteger = (int)rate;
				
				config.put("rate", Integer.toString(rateInteger));
				config.put("interval", business.getString("packagelength"));
				config.put("duration", business.getString("starttime"));
				
//				Map<String, String> trafficFlow = new HashMap<String, String>();
//				trafficFlow.put("nodeId", business.getString("srcasn"));
//				trafficFlow.put("config", config.toString());
				businessesMap.put(business.getString("srcasn"), config);
				businessesMap.put(business.getString("destasn"), config);
			}
			
//			String nestIP = SystemConfig.getStringValue("nestIP");
			NestClient client = new NestClient(nestIP, 50051);
			List<Node> nodes = new ArrayList<Node>();
			Record result = new Record();
//			for (int i = 0; i < servers.size(); i++) {
//				Record server = servers.get(i);

			Map<Long, Integer> mapInnerID = new HashMap<>();//创建asn与innerid对应map
			mapInnerID.clear();
				
			//节点
//			List<Record> nodeList = dao.query("node", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
			List<Record> nodeList = dao.query(
					"node",
					Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid).orderBy("asn", "asc")
			);
			for (int j = 0; j < nodeList.size(); j++) {
				Record r = nodeList.get(j);
				int innerID = j + 1;
				r.set("innerid", innerID);
				mapInnerID.put(r.getLong("asn"), innerID);
//				dao.update("node", Chain.make("innerid", innerID), Cnd.where("id", "=", r.getInt("id")));
				float lon = 10;
				try {
					lon = StringUtils.hasText(r.getString("longitude")) ? Float.parseFloat(r.getString("longitude")) : 10;
				} catch (Exception e) {
					
				}
				float lat = 10;
				try {
					lat = StringUtils.hasText(r.getString("latitude")) ? Float.parseFloat(r.getString("latitude")) : 10;
				} catch (Exception e) {
					
				}
//				float lon = StringUtils.hasText(r.getString("longitude")) ? Float.parseFloat(r.getString("longitude")) : 10;
//				float lat = StringUtils.hasText(r.getString("latitude")) ? Float.parseFloat(r.getString("latitude")) : 10;
				Builder nodeBuilder = Node.newBuilder();
				
				nodeBuilder
					.setId(r.getInt("innerid"))
	                .setName("n" + r.getString("asn"))
	                .setType(NodeType.Enum.DEFAULT)
	                .setGeo(Geo.newBuilder().setLon(lon).setLat(lat).setAlt(0))
	                .addConfigServices("zebra");
				
				if (r.getInt("serverid") > 0) {
					Record serverinfo = serverDic.get(r.getInt("serverid"));
					if (serverinfo != null && serverinfo.getString("ip") != null && !(serverinfo.getString("ip")).equals(nestIP)) {
						nodeBuilder.setServer(serverinfo.getString("mserver"));
					} 
				}
				
				//监控
				Map<String, String> configMonitor = new HashMap<String, String>();
				if (r.getInt("monitor") == 1 || r.getInt("monitor") == 2 || r.getInt("monitor") == 3) {
					if (r.getInt("monitor") == 1) {
						configMonitor.put("mode", "none");
//					nodeBuilder.addConfigServices("BgpMonitor");
					}
					if (r.getInt("monitor") == 2) {
						configMonitor.put("mode", "interrupt");
//					nodeBuilder.addConfigServices("BgpMonitor");
					}
					if (r.getInt("monitor") == 3) {
						configMonitor.put("mode", "hijack");
//					nodeBuilder.addConfigServices("BgpMonitor");
					}
					ConfigServiceConfig configServiceConfig = ConfigServiceConfig.newBuilder().setNodeId(r.getInt("asn"))
							.putAllConfig(configMonitor).build();

					nodeBuilder
							.addConfigServices("BgpMonitor")
							.putConfigServiceConfigs("BgpMonitor", configServiceConfig);
				}

				nodeBuilder.addConfigServices("DefaultLoRoute");
				
				//协议配置
				Map<String, String> configBgpv4 = new HashMap<String, String>();
				Map<String, String> configOSPFv2 = new HashMap<String, String>();
				if (StringUtils.hasText(r.getString("routetype"))) {
					if ( r.getString("routetype").indexOf("1") > -1 && StringUtils.hasText(r.getString("routeinfobgp"))) { //bgp
						String routeinfobgp = r.getString("routeinfobgp");
						JSONObject json = JSON.parseObject(routeinfobgp);
						for (String key : json.keySet()){
							
//							if (key.equals("area")) {
//								configBgpv4.put("area", json.getString(key));
//							} else if (key.equals("hello_interval")) {
//								configBgpv4.put("hello_interval", json.getString(key));
//							} else if (key.equals("area")) {
//								configBgpv4.put("area", json.getString(key));
//							} else if (key.equals("area")) {
//								configBgpv4.put("area", json.getString(key));
//							}
							
							configBgpv4.put(key, json.getString(key));
				        }

						//策略组
						Record statement = dao.fetch("statement", Cnd.where("status", "=", 1).and("nodeid", "=", r.getString("id")));
						if (statement != null) {
							JSONObject jsonObject = nodeAssignments(r, sceneid, sessionid, false);
							
							configBgpv4.put("assignments", jsonObject.get("assignments").toString());
						}
						
						ConfigServiceConfig configServiceConfig = ConfigServiceConfig.newBuilder().setNodeId(r.getInt("asn"))
								.putAllConfig(configBgpv4).build();
						
						nodeBuilder
							.addConfigServices("Bgpv4")
			                .putConfigServiceConfigs("Bgpv4", configServiceConfig);
					}
					if ( r.getString("routetype").indexOf("2") > -1 && StringUtils.hasText(r.getString("routeinfoospf"))) { //ospf
						
						String routeinfoospf = r.getString("routeinfoospf");
						JSONObject json = JSON.parseObject(routeinfoospf);
						for (String key : json.keySet()){
							configOSPFv2.put(key, json.getString(key));
				        }

						ConfigServiceConfig configServiceConfig = ConfigServiceConfig.newBuilder().setNodeId(r.getInt("asn"))
								.putAllConfig(configOSPFv2).build();
						
						nodeBuilder
							.addConfigServices("OSPFv2")
			                .putConfigServiceConfigs("OSPFv2", configServiceConfig);
						
					}
					if ( r.getString("routetype").indexOf("3") > -1 ) { //RIP
						nodeBuilder.addConfigServices("RIP");
					}
				}
				
				
				
				if (businessesMap.get(r.getString("asn")) != null) {
					
					Map<String, String> trafficFlow = businessesMap.get(r.getString("asn"));

					ConfigServiceConfig configServiceConfig = ConfigServiceConfig.newBuilder().setNodeId(r.getInt("asn"))
							.putAllConfig(trafficFlow).build();
					
					nodeBuilder
						.addConfigServices("TrafficFlow")
		                .putConfigServiceConfigs("TrafficFlow", configServiceConfig);
				}
				Node node = nodeBuilder.build();
				nodes.add(node);
			}
//			}
			//链路
			List<Record> linkList = dao.query("link", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
			List<Link> links = new ArrayList<Link>();
			Map<String, Integer> ethMap = new HashMap<String, Integer>();
			for (int j = 0; j < linkList.size(); j++) {
				Record r = linkList.get(j);
				
				int srcEthId = StringUtils.hasText(r.getString("srceth")) ? r.getInt("srceth") : ethMap.get(r.getString("srcid")) != null ? ethMap.get(r.getString("srcid")) + 1 : 0;
				int destEthId = StringUtils.hasText(r.getString("desteth")) ? r.getInt("desteth") : ethMap.get(r.getString("destid")) != null ? ethMap.get(r.getString("destid")) + 1 : 0;
				
				String networksegment = r.getString("networksegment");
				if (StringUtils.hasText(networksegment) && networksegment.indexOf("/") > 0) {
					networksegment = networksegment.substring(networksegment.indexOf("/")+1);
				} else {
					networksegment = "24";
				}
				
				//查node，然后计算距离计算延时
//				List<Record> linkNodes = dao.query("node", Cnd.where("asn", "in", r.getInt("srcasn") + "," + r.getInt("destasn")));
				List<Record> linkNodes = dao.query("node",
						Cnd.where("sceneid", "=", sceneid)
								.and(Cnd.exps("asn", "=", r.getInt("srcasn"))
										.or("asn", "=", r.getInt("destasn")))
				);
				Long delay = 0L;
				//int speed = 200000000;
				double speed = 3e8;
				if (linkNodes.size() == 2) {
					double longitude1 = linkNodes.get(0).getDouble("longitude");
					double latitude1 = linkNodes.get(0).getDouble("latitude");
					double longitude2 = linkNodes.get(1).getDouble("longitude");
					double latitude2 = linkNodes.get(1).getDouble("latitude");
					
					double distance = getDistance4(longitude1, latitude1, longitude2, latitude2, Ellipsoid.Sphere);
					
					delay =Math.round( distance / speed) ;
				}
				String isdelay = r.getString("isdelay");
				if(isdelay != null)
					delay += Long.valueOf(r.getString("delay"));
//				System.out.println("链路信息：");
//				System.out.println("srcasn:" + Long.parseLong(r.getString("srcasn")));
//				System.out.println("destasn:" + Long.parseLong(r.getString("destasn")));
//				System.out.println("mapInnerID size:" + mapInnerID.size());
//				System.out.println("链路信息!!!");
//
//				if(mapInnerID.containsKey(Long.parseLong(r.getString("srcasn"))) == true)
//				{
//					System.out.println("srcasn:" + Long.parseLong(r.getString("srcasn")));
//				}
				int node1ID = mapInnerID.get(Long.parseLong(r.getString("srcasn")));
				int node2ID = mapInnerID.get(Long.parseLong(r.getString("destasn")));

				Link link = Link.newBuilder()
		                .setNode1Id(node1ID)
		                .setNode2Id(node2ID)
		                .setType(LinkType.Enum.WIRED)
						.setOptions(LinkOptions.newBuilder()
								.setDelay(delay)
								.setBandwidth(r.getLong("bandwidth"))
								.setLoss((float) r.getDouble("loss"))
								.setJitter(r.getLong("jitter"))
								.build())
		                .setIface1(Interface.newBuilder()
		                                    .setId(srcEthId)
		                                    .setNetId(srcEthId + 1)
		                                    .setName("eth" + srcEthId)
//		                                    .setIp4(r.getString("srcip"))
//		                                    .setIp4Mask(Integer.parseInt(networksegment))
//			                                    .setMac("00:00:00:00:00:01")
		                                    .build())
		                .setIface2(Interface.newBuilder()
		                                    .setId(destEthId)
		                                    .setNetId(destEthId + 1)
		                                    .setName("eth" + destEthId)
//		                                    .setIp4(r.getString("destip"))
//		                                    .setIp4Mask(Integer.parseInt(networksegment))
//			                                    .setMac("00:00:00:00:00:02")
		                                    .build())
		                .build();
				dao.update("link", Chain.make("srceth", srcEthId).add("desteth", destEthId), Cnd.where("id", "=", r.getString("id")));
				ethMap.put(r.getString("srcid"), srcEthId);
				ethMap.put(r.getString("destid"), destEthId);
				
				links.add(link);
			}
			
//			boolean res = false;
//			try {
//				res = client.startSession(sessionid, nodes, links);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if (res) {
//				Date startedNow = new Date();
//				if (startedNow.after(started)) {
//					started = startedNow;
//				}
//				
//				result.set(nestIP, "stated");
//			} else {
//				result.set(nestIP, "error");
//			}
//				
//			simulation.set("startedtime", started);
//			
//			long timeStart = start.getTime();
//			long timeStarted = started.getTime();
//			simulation.set("startduration", timeStarted - timeStart);
//			simulation.set("running", 1);
			
			//增加半物理接入 semiphysics
			List<Record> semiphysicsList = dao.query("semiphysics", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
			for (int j = 0; j < semiphysicsList.size(); j++) {
				Record r = semiphysicsList.get(j);

				int node2ID = mapInnerID.get((r.getLong("asn")));

				Builder nodeBuilder = Node.newBuilder();
				
				int netid = (int) (System.currentTimeMillis() / 1000) ;
				
				nodeBuilder
					.setId(netid)
	                .setName(r.getString("net"))
	                .setType(NodeType.Enum.RJ45);
	                
				Node node = nodeBuilder.build();
				nodes.add(node);
				
				Link link = Link.newBuilder()
						.setNode1Id(netid)	 // 桥接网卡节点id
						.setNode2Id(node2ID) // 桥接虚拟节点id
						.setType(LinkType.Enum.WIRED)
						.setIface2(Interface.newBuilder()
								.setId(0)
								.setNetId(1)
								.setName("eth0")
								.setIp4(r.getString("ipv4")) // 桥接虚拟节点的ip
								.setIp4Mask(24)
//								.setMac("00:00:00:00:00:02")
								.build())
						.build();
				links.add(link);
			}
			
			mapInnerID.clear();
			simulationService.startSession(sessionid, nodes, links, nestServers);
//			StartSessionMThread mThread = new StartSessionMThread(sessionid, nodes, links, nestServers);
//			Thread t = new Thread(mThread);
//	        t.start();
	        
			dao.insert("simulation", Chain.from(simulation));
			
			result.set("sessionid", sessionid);
			result.set("startedtime", start);
			
			return result;
		});
	}
	
	/*
	 * 结束场景仿真
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> stopScene() {
		return flux -> flux.map(record -> {
			
			Record result = new Record();
			
			int sessionId = record.getInt("sessionid");
			
			Record simulation = dao.fetch("simulation", Cnd.where("status", "<>", 0).and("sessionid", "=", sessionId));
			
			Date stop = new Date();
			
			if (simulation == null) {
				result.put("stopedtime", stop);
				return result;
			}
			simulation.set("stoptime", stop);
			simulation.set("running", 2);
						
//			StopSessionMThread mThread = new StopSessionMThread(sessionId);
//			Thread t = new Thread(mThread);
//	        t.start();

			simulationService.stopSession(sessionId);

			int sceneId = simulation.getInt("sceneid");
			dao.update("simulation", Chain.from(simulation), Cnd.where("id", "=", simulation.getString("id")).and("sceneid", "=", sceneId).and("running", "=", 1));

			dao.update("statement", Chain.make("published", 0), Cnd.where("sceneid", "=", sceneId));

			int deletedRows = 0;
			deletedRows = dao.clear("link", Cnd.where("sceneid", "=", sceneId).and("runingadd", "=", 1));
			System.out.println("删除了 link表中" + deletedRows + " 条记录: sceneid:" + sceneId + " runingadd:" + 1);

			deletedRows = dao.clear("node", Cnd.where("sceneid", "=", sceneId).and("runingadd", "=", 1));
			System.out.println("删除了 node表中" + deletedRows + " 条记录: sceneid:" + sceneId + " runingadd:" + 1);

			result.set("sessionid", sessionId);
			result.set("stopedtime", stop);
			
			return result;
		});
	}
	
	/*
	 * 结束场景仿真
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> stopSceneByScene() {
		return flux -> flux.map(record -> {
			
			Record result = new Record();
			
			int sceneId = record.getInt("sceneid");
			
			List<Record> simulations = dao.query("simulation", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneId));
			
			Date stop = new Date();
			
			for(int i =0; i < simulations.size(); i++) {
				Record simulation = simulations.get(i);
				int sessionId = simulation.getInt("sessionid");
				if (simulation == null) {
					result.put("stopedtime", stop);
					return result;
				}
				simulation.set("stoptime", stop);
				simulation.set("running", 2);
							
//				StopSessionMThread mThread = new StopSessionMThread(sessionId);
//				Thread t = new Thread(mThread);
//		        t.start();

				simulationService.stopSession(sessionId);

				dao.update("simulation", Chain.from(simulation), Cnd.where("id", "=", simulation.getString("id")));
			}
			
			result.put("stopedtime", stop);
			return result;
		});
	}
	
//	/*
//	 * 结束场景仿真
//	 * */
//	@Bean
//	public Function<Flux<Record>, Flux<Record>> stopScene() {
//		return flux -> flux.map(record -> {
//			
//			Record result = new Record();
//			
//			String sceneid = record.getString("sceneid");
//			
//			//获取场景服务器
//			List<Record> servers = dao.query("scene_server_view", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid));
//			
//			List<Record> simulations = dao.query("simulation", Cnd.where("status", "<>", 0).and("running", "=", 1).and("sceneid", "=", sceneid));
//			
//			for (int m = 0; m < simulations.size(); m++ ) {
//				Record simulation = simulations.get(m);
//
//				Date stop = new Date();
//				Date stoped = stop;
//				simulation.set("stoptime", stop);
//				
//				int sessionid = simulation.getInt("sessionid");				
//				for (int i = 0; i < servers.size(); i++) {
//					Record server = servers.get(i);
//					NestClient client = new NestClient(server.getString("ip"), 50051);
//					
//					boolean res = false;
//					try {
//						res = client.stopSession(sessionid);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					if (res) {
//						Date stopedNow = new Date();
//						if (stopedNow.after(stoped)) {
//							stoped = stopedNow;
//						}
//						
//						result.set(server.getString("mserver"), "stoped");
//					} else {
//						result.set(server.getString("mserver"), "error");
//					}
//					
//				}
//				
//				simulation.set("stopedtime", stoped);
//				
//				long timeStop = stop.getTime();
//				long timeStoped = stoped.getTime();
//				simulation.set("stopduration", timeStoped - timeStop);
//				simulation.set("running", 0);
//				dao.update("simulation", Chain.from(simulation), Cnd.where("id", "=", simulation.getString("id")));
//				
//				result.set("sessionid", sessionid);
//				result.set("stopedtime", stoped);
//			}
//			
//			return result;
//		});
//	}
	
	/*
	 * 经纬度计算距离
	 * */
	/**
     * 方法四：（利用第三方jar包计算）
     * 计算两个经纬度之间的距离
     *
     * @param longitude1 第一点的经度
     * @param latitude1  第一点的纬度
     * @param longitude2 第二点的经度
     * @param latitude2  第二点的纬度
     * @param ellipsoid  计算方式
     * @return 返回的距离，单位m
     */
    public static double getDistance4(double longitude1, double latitude1, double longitude2, double latitude2, Ellipsoid ellipsoid) {
        // 创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GlobalCoordinates firstPoint = new GlobalCoordinates(latitude1, longitude1);
        GlobalCoordinates secondPoint = new GlobalCoordinates(latitude2, longitude2);
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, firstPoint, secondPoint);
        return geoCurve.getEllipsoidalDistance();
    }
    
    /*
	 * 批量新增节点
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> replaceNodeCountryWithCode() {
		
		return flux -> flux.map(record -> {
			if (StringUtils.hasText(record.getString("country"))) {
				Cnd cnd = Cnd.where("status", "=", 1).and("code", "=", record.getString("country"));
				Record country = dao.fetch("country", cnd);
				if (country != null) {
					record.put("country", country.getString("id"));
				} else {
					record.remove("country");
				}
			}
			return record;
		});
	}
	
	/*
	 * 批量新增节点
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> replaceLinkAsnWithId() {
		
		return flux -> flux.map(record -> {
			if (StringUtils.hasText(record.getString("srcasn")) && StringUtils.hasText(record.getString("destasn"))) {
				String[] asns = new String[] {record.getString("srcasn"), record.getString("destasn")};
				
				Cnd cnd = Cnd.where("status", "=", 1).and("asn", "in", String.join(",", asns));
				List<Record> records = dao.query("nodes", cnd); //从基础nodes里查找
				
				//插入node表
				if (records != null) {
					for (int i = 0; i < records.size(); i++) {
						Record r = records.get(i);
						//往node表插入数据
						try {
							r.remove("id");
							r.put("sceneid", record.getString("sceneid"));
							r.put("runingadd", record.getString("runingadd"));
							
							if (dao.fetch("node", Cnd.where("status", "=", 1).and("asn", "=", r.getString("asn")).and("sceneid", "=",record.getString("sceneid"))) == null) {
								dao.insert("node", Chain.from(r));
							}
							
						} catch(Exception e) {
							System.out.println(e.getMessage());
						}
					}
					
				}

				cnd = Cnd.where("status", "=", 1).and("sceneid", "=",record.getString("sceneid")).and("asn", "in", String.join(",", asns));
				records = dao.query("node", cnd); //从基础node里查找
				
				if (records != null) {
					for (int i = 0; i < records.size(); i++) {
						Record r = records.get(i);
						if (r.getString("asn").equals(record.getString("srcasn"))) {
							record.put("srcid", r.getString("id"));
						} else {
							record.put("destid", r.getString("id"));
						}	
					}
					
				}
			}
			return record;
		});
	}
	
	/*
	 * 导入链路
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> importLinks() {
		
		return flux -> flux.map(record -> {
			Record rst = new Record();
			
			int type = record.getInt("type");			
			String sceneId = record.getString("sceneid");
			
			List<Record> links = null;
			String nodeName = null;
			if (type == 1) { //国内
				links = dao.query("links", Cnd.where("type1", "=", 1));
				nodeName = "nodes1";
			} else if (type == 2) { //国际
				links = dao.query("links", Cnd.where("type2", "=", 1));
				nodeName = "nodes2";
			} else if (type == 3){
				links = dao.query("links", Cnd.where("type3", "=", 1));
				nodeName = "nodes3";
			}
			
			if (links != null) {
				
				//往node表插入数据
				if (nodeName != null) {
					List<Record> nodes = dao.query(nodeName, Cnd.where("status", "=", 1));
					
					List<Record> nodesExited = dao.query("node", Cnd.where("status", "=", 1).and("sceneid", "=", sceneId));
					List<String> asnsExited = new ArrayList<String>();
					for (int j = 0; j < nodesExited.size(); j++) {
						Record r = nodesExited.get(j);
						if (asnsExited.indexOf(r.getString("asn")) < 0) {
							asnsExited.add(r.getString("asn"));
						}
					}
					
					List<Record> nodesAdd = new ArrayList<Record>();
					for (int j = 0; j < nodes.size(); j++) {
						Record r = nodes.get(j);
						if (asnsExited.indexOf(r.getString("asn")) < 0) {
							r.remove("id");
							r.put(".table", "node");
							r.put("sceneid", sceneId);
							r.put("runingadd", record.getString("runingadd"));
							
							nodesAdd.add(r);
						}
					}
					dao.fastInsert(nodesAdd);
				}
				
				
				//查询node为了获取id
				List<Record> nodesInScene = dao.query("node", Cnd.where("status", "=", 1).and("sceneid", "=", sceneId));
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < nodesInScene.size(); i++ ) {
					Record r = nodesInScene.get(i);
					map.put(r.getString("asn"), r.getString("id"));
				}
				
				//往link表插入数据,先删除再插入
				dao.clear("link", Cnd.where("status", "=", 1).and("sceneid", "=", sceneId).and("type" + type, "=", 1));
				
				for (int i = 0 ; i < links.size(); i++) {
					Record link = links.get(i);
					link.remove("id");
					link.remove("status");
					link.put(".table", "link");
					link.put("sceneid", sceneId);
					link.put("runingadd", record.getString("runingadd"));
					link.put("srcid", map.get(link.getString("srcasn")));
					link.put("destid", map.get(link.getString("destasn")));
					
//					//往link表插入数据
//					Record linkExited = dao.fetch("link", 
//							Cnd.where("status", "=", 1).and("sceneid", "=", sceneId)
//							.and("srcasn", "=", link.getString("srcasn"))
//							.and("destasn", "=", link.getString("destasn")));
//					if (linkExited == null) {
//						dao.insert("link", Chain.from(link));
//					}
				}
				dao.fastInsert(links);
				
				
			}
			
			rst.put("result", 1);
			return rst;
		});
	}
	
	/*
	 * 发布策略
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> publishPolicies() {
		
		return flux -> flux.map(record -> {
			if(!StringUtils.hasText(record.getString("sceneid"))) {
				return record;
			}
			
			String sceneId = record.getString("sceneid");
			List<Record> nodes = dao.query("node", Cnd.where("status", "=", 1).and("sceneid", "=", sceneId), null, "id");
			
			//查询场景节点id
			List<String> nodeIds = new ArrayList<String>();
			for (int i = 0; i < nodes.size(); i++) {
				Record node = nodes.get(i);
				nodeIds.add(node.getString("id"));
			}
			
			//查询节点的statement
			for (int i = 0; i < nodeIds.size(); i++) {
				
				String nodeId = nodeIds.get(i);
				
				publishNodePolicie(nodeId);
				
				
			}
			
			
			return record;
		});
	}
	
	public String publishNodePolicie(String nodeId) {
		
		List<Record> statements = dao.query("statement", Cnd.where("status", "=", 1).and("nodeid", "=", nodeId));
		
		JSONObject assignments = new JSONObject();
		List<JSONObject> jsonstatements = new ArrayList<JSONObject>();
		for (int i = 0; i < statements.size(); i++) {
			Record r = statements.get(i);
			JSONObject statement = new JSONObject();
			statement.put("name", statements.get(i).getString("statement"));
			statement.put("direction", StringUtils.hasText(r.getString("statement")) ? r.getInt("statement") : 0);
			statement.put("defaultAction", StringUtils.hasText(r.getString("action")) ? r.getInt("action") : 0);
			
			List<Record> sets = dao.query("mset", 
					Cnd.where("status", "=", 1).and("nodeid", "=", nodeId).and("statementid", "=", r.getString("id")));
			
			List<JSONObject> policies = new ArrayList<JSONObject>();
			for (int j = 0; j < sets.size(); j++) {
				Record rr = sets.get(j);
				
				JSONObject policy = new JSONObject();
				policy.put("name", rr.getString("mset"));
				
				
				
			}
			
			
		}
		return nodeId;
        
    }
	
	/*
	 * 统计场景信息
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> getStatsInfo() {
		return flux -> flux.map(record -> {
			String sceneid = record.getString("id");
			
			Cnd cnd = Cnd.where("status", "<>", 0).and("sceneid", "=", sceneid);
			int serverCount = dao.count("scene_server", cnd);
			int nodeCount = dao.count("node", cnd);
			int linkCount = dao.count("link", cnd);
			
			record.put("remarks", "节点：" + nodeCount + "个，链路：" + linkCount + "个，服务器："+ serverCount +"个");
			
			int running = dao.count("simulation", cnd.and("running", "=", 1));
			
			record.put("running", running >= 1 ? "正在运行" : "");
	
			return record;
		});
	}
	
	/*
	 * 新建节点时如果是运行中场景，需要nest中增加节点
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> addNodeToNest() {
		return flux -> flux.map(record -> {
			Record result = new Record();
			result.put("result", 1);
			
			//获取sessionid
			Record simulation = dao.fetch("simulation", Cnd.where("status", "=", 1).and("sceneid", "=", record.getString("sceneid")).orderBy("id", "desc"));
			int sessionId = simulation.getInt("sessionid");
			
			int asn = StringUtils.hasText(record.getString("asn")) ? record.getInt("asn") : 0;
			float lon = StringUtils.hasText(record.getString("longitude")) ? Float.parseFloat(record.getString("longitude")) : 10;
			float lat = StringUtils.hasText(record.getString("latitude")) ? Float.parseFloat(record.getString("latitude")) : 10;
			int serverId = StringUtils.hasText(record.getString("serverid"))?record.getInt("serverid") : 0;


			//获取服务器 name
			String serverName = "";
			String serverIP = "";

			try {
				String nestIP = SystemConfig.getStringValue("nestIP");
				if(serverId == 0)
				{
					serverIP = nestIP;
					serverName = "";
				}
				else {
					Record server = dao.fetch("mserver", Cnd.where("id", "=", serverId));
					serverName = server.getString("mserver");
					serverIP = server.getString("ip");
				}
				NestClient client = new NestClient(nestIP, 50051);
				
				Builder nodeBuilder = Node.newBuilder();
				if(nestIP.equals(serverIP))
					nodeBuilder.setServer("");
				else
					nodeBuilder.setServer(serverName);

				System.out.println("serverName:" + serverName);
				System.out.println("nestIP:" + nestIP);
				System.out.println("serverIP:" + serverIP);
				System.out.println("nestIP.equals(serverIP):" + (nestIP.equals(serverIP)));
				
				nodeBuilder
					.setId(asn)
		            .setName("n" + asn)
		            .setType(NodeType.Enum.DEFAULT)
		            .setGeo(Geo.newBuilder().setLon(lon).setLat(lat).setAlt(0))
		            .addConfigServices("zebra");
				
				//监控
				if (record.getInt("monitor") == 1) {
					nodeBuilder.addConfigServices("BgpMonitor");
				}
				
				//协议配置
				Map<String, String> configBgpv4 = new HashMap<String, String>();
				Map<String, String> configOSPFv2 = new HashMap<String, String>();
				if (StringUtils.hasText(record.getString("routetype"))) {
					if ( record.getString("routetype").indexOf("1") > -1 ) { //bgp
						String routeinfobgp = record.getString("routeinfobgp");
						JSONObject json = JSON.parseObject(routeinfobgp);
						for (String key : json.keySet()){							
							configBgpv4.put(key, json.getString(key));
				        }

//						//策略组
//						Record statement = dao.fetch("statement", Cnd.where("status", "=", 1).and("nodeid", "=", r.getString("id")));
//						if (statement != null) {
//							JSONObject jsonObject = nodeAssignments(r);
//							
//							configBgpv4.put("assignments", jsonObject.get("assignments").toString());
//						}
						
						ConfigServiceConfig configServiceConfig = ConfigServiceConfig.newBuilder().setNodeId(record.getInt("asn"))
								.putAllConfig(configBgpv4).build();
						
						nodeBuilder
							.addConfigServices("Bgpv4")
			                .putConfigServiceConfigs("Bgpv4", configServiceConfig);
					}
					if ( record.getString("routetype").indexOf("2") > -1 ) { //ospf
						
						String routeinfoospf = record.getString("routeinfoospf");
						JSONObject json = JSON.parseObject(routeinfoospf);
						for (String key : json.keySet()){
							configOSPFv2.put(key, json.getString(key));
				        }

						ConfigServiceConfig configServiceConfig = ConfigServiceConfig.newBuilder().setNodeId(record.getInt("asn"))
								.putAllConfig(configOSPFv2).build();
						
						nodeBuilder
							.addConfigServices("OSPFv2")
			                .putConfigServiceConfigs("OSPFv2", configServiceConfig);
						
					}
					if ( record.getString("routetype").indexOf("3") > -1 ) { //RIP
						nodeBuilder.addConfigServices("RIP");
					}
				}
				
				
				Node node = nodeBuilder.build();
				
				client.addNode(sessionId, node);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("result", -1);
			}
	
			return result;
		});
	}
	
	/*
	 * 新建节点时如果是运行中场景，需要nest中增加链路
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> addLinkToNest() {
		return flux -> flux.map(record -> {
			Record result = new Record();
			result.put("result", 1);
			
			//获取sessionid
			Record simulation = dao.fetch("simulation", Cnd.where("status", "=", 1).and("sceneid", "=", record.getString("sceneid")).orderBy("id", "desc"));
			int sessionId = simulation.getInt("sessionid");
			
			int srcEthId = 0;
			int destEthId = 0;
			
			List<Record> srcList1 = dao.query("link", 
					Cnd.where("status", "=", 1).and("sceneid", "=", record.getString("sceneid")).and("srcasn", "=", record.getString("srcasn")));
			for (int i = 0; i < srcList1.size(); i++) {
				Record r = srcList1.get(i);
				int eth = r.getInt("srceth");
				if (eth > srcEthId) {
					srcEthId = eth;
				}
			}
			List<Record> srcList2 = dao.query("link", 
					Cnd.where("status", "=", 1).and("sceneid", "=", record.getString("sceneid")).and("destasn", "=", record.getString("srcasn")));
			for (int i = 0; i < srcList2.size(); i++) {
				Record r = srcList2.get(i);
				int eth = r.getInt("desteth");
				if (eth > srcEthId) {
					srcEthId = eth;
				}
			}
			
			List<Record> destList1 = dao.query("link", 
					Cnd.where("status", "=", 1).and("sceneid", "=", record.getString("sceneid")).and("srcasn", "=", record.getString("destasn")));
			for (int i = 0; i < destList1.size(); i++) {
				Record r = destList1.get(i);
				int eth = r.getInt("srceth");
				if (eth > destEthId) {
					destEthId = eth;
				}
			}
			List<Record> destList2 = dao.query("link", 
					Cnd.where("status", "=", 1).and("sceneid", "=", record.getString("sceneid")).and("destasn", "=", record.getString("destasn")));
			for (int i = 0; i < destList2.size(); i++) {
				Record r = destList2.get(i);
				int eth = r.getInt("desteth");
				if (eth > destEthId) {
					destEthId = eth;
				}
			}
			
			try {
				String nestIP = SystemConfig.getStringValue("nestIP");
				NestClient client = new NestClient(nestIP, 50051);
				
				int srcasn = record.getInt("srcasn");
				int destasn = record.getInt("destasn");
				String srcip = record.getString("srcip");
				String destip = record.getString("destip");
				String networksegment = record.getString("networksegment");
				if (networksegment.contains("/")) {
					String[] ss = networksegment.split("/");
					networksegment = ss[ss.length - 1];
				} else {
					networksegment = "24";
				}
				client.addLink(sessionId, srcasn, destasn, srcip, destip, srcEthId + 1, destEthId + 1, networksegment);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("result", -1);
			}
	
			return result;
		});
	}
	
	/*
	 * 运行中发布策略
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> publishStatement() {
		return flux -> flux.map(record -> {
			Record result = new Record();
			result.put("result", 1);
			
			try {
				String asn = record.getString("asn");	
				String serverId = record.getString("serverid");
				String sceneId = record.getString("sceneid");

				//获取sessionid
				Record simulation = dao.fetch("simulation", Cnd.where("status", "=", 1).and("sceneid", "=", sceneId).orderBy("id", "desc"));
				int sessionId = simulation.getInt("sessionid");
				
				JSONObject jsonObject = nodeAssignments(record, sceneId, sessionId, true);
				
				//获取服务器ip
				Record server = dao.fetch("mserver", Cnd.where("id", "=", serverId));
				String ip = server.getString("ip");
				String msg = HttpUtil.postData("http://" + ip +":11223/api/sessions/" + sessionId + "/nodes/" + asn + "/policies", jsonObject);
				result.put("message", msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("result", -1);
			}
	
			return result;
		});
	}
	
	public JSONObject nodeAssignments(Record record, String sceneid, int sessionid, boolean manualPublish) {
		String nodeId = record.getString("id");
		int sceneId = Integer.parseInt(sceneid);

		if(manualPublish == false)
		{
			Record RecStatement = dao.fetch("node", Cnd.where("status", "=", 1).and("id", "=", nodeId).and("sceneid", "=", sceneId));
			int runningadd = 0;

			if (RecStatement != null) {
				if (RecStatement.getInt("runingadd") == 1) {
					runningadd = 1;
				}
				dao.update("statement", Chain.make("published", runningadd == 1 ? 0 : 1), Cnd.where("nodeid", "=", nodeId).and("sceneid", "=", sceneId));
			}
		}
		else{
			dao.update("statement", Chain.make("published", 1), Cnd.where("nodeid", "=", nodeId).and("sceneid", "=", sceneId));
		}

		int count = 0;
		
		List<JSONObject> assignments = new ArrayList<JSONObject>();
		
		//存储statement id值，用于更新
		List<String> statementIds = new ArrayList<String>();
		for (int m = 1; m <=2; m++) {
			//获取节点上的策略-direction = 1
			List<Record> statements = dao.query("statement", 
					Cnd.where("status", "=", 1).and("nodeid", "=", nodeId).and("direction", "=", m).and("sessionid", "!=", sessionid).
					or("status", "=", 1).and("nodeid", "=", nodeId).and("direction", "=", m).and("sessionid", "is", null));
			JSONObject assignment = new JSONObject();
			assignment.put("name", "global");
			assignment.put("direction", m); //UNKNOWN = 0; IMPORT = 1; EXPORT = 2;
			//policies
			List<JSONObject> policies = new ArrayList<JSONObject>();
			//definedSets
			List<JSONObject> definedSets = new ArrayList<JSONObject>();
			
			for (int i = 0; i < statements.size(); i++) {
				
				Record statement = statements.get(i);
				
				statementIds.add(statement.getString("id"));
				
				JSONObject policy = new JSONObject();
				policy.put("name", statement.getString("statement"));
				
				
				List<JSONObject> statementJs = new ArrayList<JSONObject>();
				//0 None 1 接收 2 拒绝
				int action = statement.getInt("action"); 
				if (action == -1) {
					action = 0;
				}
				JSONObject routeAction = new JSONObject();
				routeAction.put("routeAction", action);
				
				int statementId = statement.getInt("id");
				
				List<Record> sets = dao.query("mset", Cnd.where("status", "=", 1).and("nodeid", "=", nodeId).and("statementid", "=", statementId));
				
				//statements条件项
				JSONObject statementJ = new JSONObject();
				//name
				statementJ.put("name", "policy_" + statement.getString("statement") + "_" + statement.getString("id"));
				//actions
				statementJ.put("actions", routeAction);
				//condition
				JSONObject condition = new JSONObject();
				/*1：前缀策略条件项;
				2：邻居策略条件项;
				3：AsPath长度策略条件项;
				4：AsPath策略条件项;
				5:   团体策略
				6：large团体策略
				7:  下一跳策略*/
				for (int j = 0; j < sets.size(); j++) {
					
					Record set = sets.get(j);
					int type = set.getInt("type");
					int matchsetoptions = set.getInt("matchsetoptions");
					int definedType = -1;
					
					JSONObject setJ = new JSONObject();
					String name = "set" + count;
					count = count + 1;
					setJ.put("name", name);
					setJ.put("type", matchsetoptions - 1);	
					if (type == 1) {//ANY = 0; ALL = 1; INVERT = 2;
						condition.put("prefixSet", setJ);
						definedType = 0;
					} else if (type == 2) {
						condition.put("neighborSet", setJ);
						definedType = 1;
					} else if (type == 3) {//特别处理
						JSONObject setJ3 = new JSONObject();
						setJ3.put("type", matchsetoptions - 1);	
						setJ3.put("length",set.getInt("matchings"));
						condition.put("asPathLength", setJ3);
					} else if (type == 4) {
						condition.put("asPathSet", setJ);
						definedType = 3;
					} else if (type == 5) {
						condition.put("communitySet", setJ);
						definedType = 4;
					} else if (type == 6) {
						condition.put("largeCommunitySet", setJ);
						definedType = 6;
					} else if (type == 7) {
						condition.put("nextHopSet", setJ);
					} 
					
					//definedSet
					JSONObject definedSet = new JSONObject();
					definedSet.put("definedType", definedType);
					definedSet.put("name", name);
					String matchings = set.getString("matchings");

					if (matchings != null) {
						if (definedType == 1 || definedType == 3 || definedType == 4 || definedType == 6) {
							List<JSONObject> jsonArray = JSONArray.parseArray(matchings, JSONObject.class);
							List<String> list = new ArrayList<String>();
							for (int n = 0; n < jsonArray.size(); n++) {
								list.add(jsonArray.get(n).getString("name"));
							}
							definedSet.put("list", list);
							definedSets.add(definedSet);
						} else if (definedType == 0) {
							List<JSONObject> list = new ArrayList<JSONObject>();
							List<JSONObject> jsonArray = JSONArray.parseArray(matchings, JSONObject.class);
							for (int n = 0; n < jsonArray.size(); n++) {
								JSONObject prefix = new JSONObject();
								prefix.put("ipPrefix", jsonArray.get(n).getString("name"));
								prefix.put("maskLengthMin", jsonArray.get(n).getInteger("v1"));
								prefix.put("maskLengthMax", jsonArray.get(n).getInteger("v2"));
								
								list.add(prefix);
							}
							definedSet.put("prefixes", list);
							definedSets.add(definedSet);
						}
					}
				}
				statementJ.put("condition", condition);
				statementJs.add(statementJ);	
				
				policy.put("statements", statementJs);
				policies.add(policy);
			}
			
			
			
			assignment.put("policies", policies);
			assignment.put("definedSets", definedSets);
			
			assignments.add(assignment);
		}
		
		//System.out.println(assignments);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("assignments", assignments);
		
		if (statementIds.size() > 0) {
			dao.update("statement", Chain.make("sessionid", sessionid), Cnd.where("id", "in", String.join(",", statementIds)));
		}
		
		return jsonObject;
	}
	
	/*
	 * 导入初始路由
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> importRoutesOld() {
		return flux -> flux.map(record -> {
			Record result = new Record();
			result.put("result", 1);
			
			try {
				String sceneId = record.getString("sceneid");
				
				ArrayList<JSONObject> list = new ArrayList<JSONObject>();
				List<Record> links = dao.query("link", Cnd.where("status", "=", 1).and("sceneid", "=", sceneId));
				for (int i = 0; i < links.size(); i++) {
					Record link = links.get(i);
					
					JSONObject map = new JSONObject();
					map.put("as1", link.getInt("srcasn"));
					map.put("as2", link.getInt("destasn"));
					
					list.add(map);
				}
				
				String routesAndPolicies = SystemConfig.getStringValue("routesAndPolicies");
				String rst = HttpUtil.postDataString("http://" + routesAndPolicies + "/api/sessions/" + sceneId + "/routes" , list.toString());
				
				//初始路由
				if (StringUtils.hasText(rst)) {
//					"nodeRoutes"

					JSONObject json = JSONObject.parseObject(rst);
					
					String rstString = json.getString("nodeRoutes");
					
					JSONArray jsonArray = JSONArray.parseArray(rstString);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jo = jsonArray.getJSONObject(i);
						String asn = jo.getString("as");
						String routeNum = jo.getString("routeNum");
						if (StringUtils.hasText(asn) && StringUtils.hasText(routeNum)) {
							dao.update("node", Chain.make("initroutes", routeNum), Cnd.where("asn", "=", asn).and("sceneid", "=", sceneId));
						}
					}
				}
				
				result.put("message", rst);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("result", -1);
			}
	
			return result;
		});
	}

	@Bean
	public Function<Flux<Record>, Flux<Record>> importRoutes() {
		return flux -> flux.flatMap(record -> Mono.fromCallable(() -> {
			Record result = new Record();
			result.put("result", 1);
			JSONObject routesObj = new JSONObject();

			try {
				String sceneId = record.getString("sceneid");

				// 构造链路列表
				ArrayList<JSONObject> list = new ArrayList<>();
				List<Record> links = dao.query("link", Cnd.where("status", "=", 1).and("sceneid", "=", sceneId));
				for (Record link : links) {
					JSONObject map = new JSONObject();
					map.put("as1", link.getInt("srcasn"));
					map.put("as2", link.getInt("destasn"));
					list.add(map);
				}
				routesObj.put("links", list);
				String prefixMathPartner = SystemConfig.getStringValue("prefixMathPartner");
				int prefixMathPartnerInt = Integer.parseInt(prefixMathPartner);
				routesObj.put("prefixMathPartner", prefixMathPartnerInt);


				// 获取服务器地址列表
				List<Record> servers = dao.query("scene_server_view", Cnd.where("status", "<>", 0).and("sceneid", "=", sceneId));
				List<String> serverUrls = new ArrayList<>();
				serverUrls.clear();
				for (int i = 0; i < servers.size(); i++) {
					Record server = servers.get(i);
					serverUrls.add(server.getString("ip"));
				}


				// 异步请求处理
				List<CompletableFuture<JSONArray>> futures = serverUrls.stream().map(serverUrl ->
						CompletableFuture.supplyAsync(() -> {
							String url = "http://" + serverUrl + ":11223/api/sessions/" + sceneId + "/routes";
//							String url = "http://100.146.4.41:8080/api/" + serverUrl + "/sessions/" + sceneId + "/routes";
//							String rst = HttpUtil.postDataString(url, list.toString());
							String rst = HttpUtil.postData(url, routesObj);
//							System.out.println("routesObj:" + routesObj);
//							System.out.println("rst:" + rst);

							if (!StringUtils.hasText(rst)) {
								throw new RuntimeException("服务器返回空响应: " + serverUrl);
							}

							JSONObject json = JSONObject.parseObject(rst);
							String rstString = json.getString("nodeRoutes");
							return JSONArray.parseArray(rstString);
						})
				).collect(Collectors.toList());

				// 超时控制
				int timeoutSeconds = 120;
				List<JSONObject> allRoutes = new ArrayList<>();
				CompletableFuture<Void> allDoneFuture = CompletableFuture
						.allOf(futures.toArray(new CompletableFuture[0]))
						.orTimeout(timeoutSeconds, TimeUnit.SECONDS);

				// 等待所有任务完成
				allDoneFuture.join();

				// 收集所有结果
				for (CompletableFuture<JSONArray> future : futures) {
					allRoutes.addAll(future.join().stream()
							.map(obj -> (JSONObject) obj)
							.collect(Collectors.toList()));
				}

				// 使用事务管理更新数据库
				Trans.exec(new Atom() {
					@Override
					public void run() {
						Set<JSONObject> uniqueRoutes = new LinkedHashSet<>(allRoutes);
						List<JSONObject> uniqueList = new ArrayList<>(uniqueRoutes);
						for (JSONObject jo : uniqueList) {
							String asn = jo.getString("as");
							String routeNum = jo.getString("routeNum");

							if (StringUtils.hasText(asn) && StringUtils.hasText(routeNum)) {
								dao.update("node", Chain.make("initroutes", routeNum),
										Cnd.where("asn", "=", asn).and("sceneid", "=", sceneId));
							}
						}
					}
				});

				result.put("message", "所有路由更新成功");
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result", -1);
				result.put("message", "任务执行失败：" + e.getMessage());
			}

			return result;
		}).subscribeOn(Schedulers.boundedElastic()));
	}


	/*
	 * 分配商业策略
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> businessPolicies() {
		return flux -> flux.map(record -> {
			Record result = new Record();
			result.put("result", 1);
			
			Map<Integer,Integer> nodeMap = new HashMap<Integer,Integer>();
			
			try {
				String sceneId = record.getString("sceneid");
				
				ArrayList<JSONObject> list = new ArrayList<JSONObject>();
				List<Record> links = dao.query("link", Cnd.where("status", "=", 1).and("sceneid", "=", sceneId));
				for (int i = 0; i < links.size(); i++) {
					Record link = links.get(i);
					
					JSONObject map = new JSONObject();
					map.put("as1", link.getInt("srcasn"));
					map.put("as2", link.getInt("destasn"));
					map.put("ip1", link.getString("srcip"));
					map.put("ip2", link.getString("destip"));
					
					list.add(map);
					
					nodeMap.put(link.getInt("srcasn"), link.getInt("srcid"));
					nodeMap.put(link.getInt("destasn"), link.getInt("destid"));
				}
				
				JSONObject jo = new JSONObject();
				jo.put("links", list);
				
				String routesAndPolicies = SystemConfig.getStringValue("routesAndPolicies");
				String rst = HttpUtil.postDataString("http://"+ routesAndPolicies + "/api/sessions/" + sceneId + "/policies" , jo.toString());
				
				//处理返回数据
				List<Record> statementList = new ArrayList<Record>();
				List<Record> setList = new ArrayList<Record>();
				
				Record statementLatest = dao.fetch("statement", Cnd.NEW().desc("id"));
				
				int largestId = statementLatest.getInt("id") + 3;
				
		        JSONObject jsonObject = JSONObject.parseObject(rst);
		        if (jsonObject.get("policyAssignments") != null) {
		        	
		        	System.out.println(jsonObject.get("policyAssignments"));
		        	JSONArray jsonArray = (JSONArray)jsonObject.get("policyAssignments");
		        	
		        	for (int i = 0; i < jsonArray.size(); i++) {
		        		JSONObject json = (JSONObject) jsonArray.get(i);
		        		
		        		int asn = (int) json.get("as");
		        		int nodeid = nodeMap.get(asn);
		        		JSONArray policies = (JSONArray)json.get("policies");
		        		
		        		//插入策略
		        		for(int j = 0; j < policies.size(); j++) {
		        			JSONObject jsonPolicy = (JSONObject) policies.get(j);
		        			
		        			Record policy = new Record();
			        		policy.put(".table", "statement");
			        		policy.put("nodeid", nodeid);
			        		policy.put("statement", jsonPolicy.getString("name"));
			        		policy.put("direction", jsonPolicy.getString("direction"));
			        		policy.put("action", jsonPolicy.getString("action"));
			        		policy.put("scope", 1);
							if(sceneId!=null)
							{
								policy.put("sceneid", Integer.parseInt(sceneId));
							}
			        		policy.put("id", largestId);
			        		
			        		//如果存在，先删除
					        dao.update("statement", Chain.make("status", 0), Cnd.where("status", "=", 1).and("nodeid", "=", nodeid).and("statement", "=", jsonPolicy.getString("name")));
					        
			        		statementList.add(policy);
			        		
			        		//set
			        		JSONArray definedSets = (JSONArray)jsonPolicy.get("definedSets");
			        		for (int m = 0; m < definedSets.size(); m++) {
			        			JSONObject jsonSet = (JSONObject) definedSets.get(m);
			        			
			        			Record set = new Record();
				        		set.put(".table", "mset");
				        		set.put("nodeid", nodeid);
				        		set.put("statementid", largestId);
				        		set.put("matchsetoptions", 1);
				        		set.put("type", jsonSet.getInteger("type") == 1 ? 2 : 4);
				        		
				        		ArrayList<String> values = jsonSet.getObject("list", ArrayList.class);
				        		List<Record> valueList = new ArrayList<Record>();
				        		for (int n = 0; n < values.size(); n++) {
				        			Record v = new Record();
				        			
				        			v.put("name", values.get(n));
				        			valueList.add(v);
				        		}
				        		set.put("matchings", valueList);
				        		
				        		setList.add(set);
			        		}

			        		largestId++;
		        		}
		        		
		        	}
		        }

				if (jsonObject.get("linkRelationships") != null) {

					System.out.println(jsonObject.get("linkRelationships"));
					JSONArray jsonArray = (JSONArray)jsonObject.get("linkRelationships");

					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject json = (JSONObject) jsonArray.get(i);

						int srcasn = (int) json.get("as1");
						int destasn = (int) json.get("as2");
						int relationship = (int) json.get("relationship");

						//update link中的商业关系
						dao.update("link", Chain.make("relationship", relationship), Cnd.where("status", "=", 1).and("srcasn", "=", srcasn).and("destasn", "=", destasn).and("sceneid", "=", Integer.parseInt(sceneId)));
					}
				}



				dao.fastInsert(statementList);
		        dao.fastInsert(setList);
//				result.put("message", rst);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("result", -1);
			}
	
			return result;
		});
	}
	
	/*
	 * 获取邻居
	 * */
	@Bean
	public Function<Flux<Record>, Flux<List<Record>>> getNeighbors() {
		return flux -> flux.map(record -> {
			List<Record> results = new ArrayList<Record>();
			
			String nodeId = record.getString("nodeid");
			
			List<Record> links = dao.query("link", 
					Cnd.where("status", "=", 1).and("srcid", "=", nodeId)
					.or("status", "=", 1).and("destid", "=", nodeId)
					);
			
			for (int i = 0; i < links.size(); i++) {
				Record link = links.get(i);
				
				if (!link.getString("srcid").equals(nodeId) && StringUtils.hasText(link.getString("srcip"))) {
					Record r = new Record();
					r.put("value", link.getString("srcip"));
					r.put("label", link.getString("srcasn") + "_" + link.getString("srcip"));
					results.add(r);
				} else if (!link.getString("destid").equals(nodeId) && StringUtils.hasText(link.getString("destip"))) {
					Record r = new Record();
					r.put("value", link.getString("destip"));
					r.put("label", link.getString("destasn") + "_" + link.getString("destip"));
					results.add(r);
				}
			}
	
			return results;
		});
	}
	
	
	/*
	 * 场景中批量增加服务器
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> addServersToScene() {
		
		return flux -> flux.map(record -> {
			Record rst = new Record();
			rst.put("result", 1);
			
			String serverids = record.getString("serverids");
			String sceneid = record.getString("sceneid");
			if (StringUtils.hasText(serverids) && StringUtils.hasText(sceneid)) {
				List<String> servers = Arrays.asList(serverids.split(","));
				
				for (int i = 0; i < servers.size(); i++) {
					int count = dao.count("scene_server", Cnd.where("status", "=", 1).and("serverid", "=", servers.get(i)).and("sceneid", "=", sceneid));
					if (count < 1) {
						dao.insert("scene_server", Chain.make("serverid", servers.get(i)).add("sceneid", sceneid));
					}
				}
			}
			
			return rst;
		});
	}
	
	
}
