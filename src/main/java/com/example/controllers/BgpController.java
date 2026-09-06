package com.example.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;

import javax.servlet.http.HttpServletRequest;


import java.io.BufferedReader;
import java.io.FileReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.business.base.SystemConfig;
import com.example.commons.ResponseInfo;
import com.example.influxdb.TimeSeriesRepository;
import com.example.nutz.DBTools;
import com.example.storage.DownloadFile;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxTable;

import reactor.core.publisher.Mono;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/bgp")
public class BgpController {
	
	Dao dao = DBTools.getDao();
	
	@Autowired
	TimeSeriesRepository timeSeriesRepository;
	
	@Autowired
    InfluxDBClient influxDBClient;


	public String sessionStartTime;

	public String getCurTime()
	{
		ZonedDateTime currentDateTime = ZonedDateTime.now().minusMinutes(2);
		// 将时间转换为 UTC 时区
		ZonedDateTime utcTime = currentDateTime.withZoneSameInstant(ZoneId.of("UTC"));

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = utcTime.format(dateTimeFormatter);
		return formattedDateTime.replace(" ", "T");
	}

	@PostMapping(value = "/setStartTime")
	public ResponseInfo setStartTime(@RequestBody Map<String, Object> mapStartTime) {
		System.out.println("mapStartTime:" + mapStartTime);
		String startTime = mapStartTime.get("startTime").toString();
		if(startTime == null || startTime.equals("undefine") || startTime.equals("null") || startTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			sessionStartTime = formattedDateTime;
		}
		else if(startTime.contains(" ") == true) {//2024-11-07 12:00:00
			// 定义日期时间格式
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			// 解析为 LocalDateTime（无时区）
			LocalDateTime localDateTime = LocalDateTime.parse(startTime, formatter);

			// 转换为 ZonedDateTime 并指定时区（假设输入时间是系统默认时区的时间）
			ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

			// 转换为 UTC 时区的 ZonedDateTime
			ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));

			// 输出格式化的 UTC 时间字符串
			String formattedUtcTime = utcDateTime.format(formatter);
			sessionStartTime = formattedUtcTime.replace(" ", "T");
		}
		else{//2024-11-07T12:00:00
			startTime = startTime.replace("T", " ");
			// 定义日期时间格式
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			// 解析为 LocalDateTime（无时区）
			LocalDateTime localDateTime = LocalDateTime.parse(startTime, formatter);

			// 转换为 ZonedDateTime 并指定时区（假设输入时间是系统默认时区的时间）
			ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

			// 转换为 UTC 时区的 ZonedDateTime
			ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));

			// 输出格式化的 UTC 时间字符串
			String formattedUtcTime = utcDateTime.format(formatter);
			sessionStartTime = formattedUtcTime.replace(" ", "T");
		}
		return ResponseInfo.ok("ok");
	}

	@PostMapping(value = "/prefixNumState")//中断攻击前缀历史数量变化状态查询
	public ResponseInfo prefixNumState(@RequestBody Map<String, List<String>> request) {

		Map<String, Object> mapRes = new HashMap<String,Object>();
		try {
			Object res = null;
			String startTime = "";
			if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
				String formattedDateTime = getCurTime();
				startTime = formattedDateTime;
			}
			else{
				startTime = sessionStartTime;
			}

			// 从 Map 中获取 servers 列表
			List<String> servers = request.get("servers");
			System.out.println("Servers: " + servers);

			for (int i = 0; i < servers.size(); i++) {
				String server = servers.get(i).toString();
				Map<String, Object> IPMap = new HashMap<>();
				IPMap = (Map<String, Object>)timeSeriesRepository.queryServerByIP("serverstate", "ip", server, startTime);
				System.out.println("res:" + res);

				String mserver = "";
				// 根据 ip 查询数据库mserver并获取对应的服务器名称
				Dao dao = DBTools.getDao();
				List<Record> serversList = dao.query("mserver", Cnd.where("ip","=",server));

				if (serversList.size() == 0) {
					mserver = "";
				}

				for (int j = 0; j < serversList.size(); j++) {
					mserver = serversList.get(j).get("mserver").toString();
				}

				IPMap.put("mserver", mserver);

				mapRes.put(server, IPMap);

			}

			return ResponseInfo.ok(mapRes);
		} catch (Exception e) {
			return ResponseInfo.not("error");
		}
	}

	@PostMapping(value = "/serversState")
	public ResponseInfo serversState(@RequestBody Map<String, List<String>> request) {

		Map<String, Object> mapRes = new HashMap<String,Object>();
		try {
			Object res = null;
			String startTime = "";
			if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
				String formattedDateTime = getCurTime();
				startTime = formattedDateTime;
			}
			else{
				startTime = sessionStartTime;
			}

			// 从 Map 中获取 servers 列表
			List<String> servers = request.get("servers");
			System.out.println("Servers: " + servers);

			for (int i = 0; i < servers.size(); i++) {
				String server = servers.get(i).toString();
				Map<String, Object> IPMap = new HashMap<>();
				IPMap = (Map<String, Object>)timeSeriesRepository.queryServerByIP("serverstate", "ip", server, startTime);
				System.out.println("res:" + res);

				String mserver = "";
				// 根据 ip 查询数据库mserver并获取对应的服务器名称
				Dao dao = DBTools.getDao();
				List<Record> serversList = dao.query("mserver", Cnd.where("ip","=",server));

				if (serversList.size() == 0) {
					mserver = "";
				}

				for (int j = 0; j < serversList.size(); j++) {
					mserver = serversList.get(j).get("mserver").toString();
				}

				IPMap.put("mserver", mserver);

				mapRes.put(server, IPMap);

			}

			return ResponseInfo.ok(mapRes);
		} catch (Exception e) {
			return ResponseInfo.not("error");
		}
	}

	@PostMapping(value = "/servicewatch")
    public ResponseInfo servicewatch(@RequestBody List<Map<String,Object>> list) {

		try {
			List<String> ips = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				Map<String,Object> map = list.get(i);
				Map<String, Object> jo = (Map<String, Object>) map.get("Service");
				String ip = "'" + jo.get("Address").toString() + "'";
				
				ips.add(ip);
			}
			
			String ipsString = ips.stream().map(String::valueOf).collect(Collectors.joining(","));
			int i = dao.update("mserver", Chain.make("isworking", 0), Cnd.where("ip", "not in", ipsString));
			
			return ResponseInfo.ok(i);
		} catch (Exception e) {
			return ResponseInfo.not("error");
		}		
    }
	
	@PostMapping(value = "/serverstate")
    public ResponseInfo serverstate(@RequestBody List<Map<String,Object>> list) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("ip", map.get("ip").toString());
			map.remove("sessionId");
			
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);
			
			records.add(record);
		}
		
		try {
			timeSeriesRepository.insertPoints("serverstate", records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}    
    }

	@PostMapping(value = "/global-prefixes-num")
	public ResponseInfo globalPrefixesNum(@RequestBody Map<String,Object> objectMap) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		System.out.println("globalPrefixesNum objectMap:" + objectMap);
//		for (int i = 0; i < list.size(); i++)
		{
//			Map<String,Object> map = list.get(i);
			Map<String, Object> map = objectMap;
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("timestamp", map.get("time").toString());
			tags.put("prefixNum", map.get("prefixNum").toString());
			tags.put("reason", map.get("reason").toString());
			tags.put("sessionId", map.get("sessionId").toString());
			map.remove("sessionId");
//			map.remove("time");

			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);

			records.add(record);
		}

		try {
			timeSeriesRepository.insertPoints("global-prefixes-num", records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}
	}
	
	@PostMapping(value = "/nodestate")
    public ResponseInfo nodestate(@RequestBody List<Map<String,Object>> list) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("sessionId", map.get("sessionId").toString());
			map.remove("sessionId");
			tags.put("nodeId", map.get("nodeId").toString());
			map.remove("nodeId");
			map.put("state", 1);
			
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);
			
			records.add(record);
		}
		
		try {
			timeSeriesRepository.insertPoints("nodestate", records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}    
    }
	
	@PostMapping(value = "/routestate")
    public ResponseInfo routestate(@RequestBody List<Map<String,Object>> list) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("sessionId", map.get("sessionId").toString());
			map.remove("sessionId");
			tags.put("nodeId", map.get("nodeId").toString());
			map.remove("nodeId");
			
			//hopDistribution
			String hopDistributionString= map.get("hopDistribution").toString();
			map.replace("hopDistribution", hopDistributionString);
			
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);
			
			records.add(record);
		}
		
		try {
			timeSeriesRepository.insertPoints("routestate", records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}    
    }
	
	@PostMapping(value = "/usagestate")
    public ResponseInfo usagestate(@RequestBody List<Map<String,Object>> list) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("sessionId", map.get("sessionId").toString());
			map.remove("sessionId");
			tags.put("nodeId", map.get("nodeId").toString());
			map.remove("nodeId");
			
			//interfaces
			List<Map<String,Object>> interfacesRecords= new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> interfacesList= (List<Map<String, Object>>) map.get("interfaces");
			for (int j = 0; j < interfacesList.size(); j++) {
				Map<String,Object> record = new HashMap<String,Object>();
				record.put("tags", tags);
				record.put("fields", interfacesList.get(j));
				
				interfacesRecords.add(record);
			}
			try {
				timeSeriesRepository.insertPoints("interfacesdetailstate", interfacesRecords);
			} catch( Exception e ) {
				return ResponseInfo.not(e.getMessage());
			} 
			
			map.remove("interfaces");
			
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);
			
			records.add(record);
		}
		
		try {
			timeSeriesRepository.insertPoints("usagestate", records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}    
    }
	
	@PostMapping(value = "/usagestate_old")
    public ResponseInfo usagestate_old(@RequestBody List<Map<String,Object>> list) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("sessionId", map.get("sessionId").toString());
			map.remove("sessionId");
			tags.put("nodeId", map.get("nodeId").toString());
			map.remove("nodeId");
			
			//interfaces
			String interfacesDataString= map.get("interfaces").toString();
			map.replace("interfaces", interfacesDataString);
			
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);
			
			records.add(record);
		}
		
		try {
			timeSeriesRepository.insertPoints("usagestate", records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}    
    }
	
	@PostMapping(value = "/neighborstate")
    public ResponseInfo neighborstate(@RequestBody List<Map<String,Object>> list) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("sessionId", map.get("sessionId").toString());
			map.remove("sessionId");
			tags.put("nodeId", map.get("nodeId").toString());
			map.remove("nodeId");
			
			//neighbors
			List<Map<String,Object>> neighborRecords= new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> neighborList= (List<Map<String, Object>>) map.get("neighbors");
			for (int j = 0; j < neighborList.size(); j++) {
				Map<String,Object> record = new HashMap<String,Object>();
				record.put("tags", tags);
				record.put("fields", neighborList.get(j));
				
				neighborRecords.add(record);
			}
			try {
				timeSeriesRepository.insertPoints("neighbordetailstate", neighborRecords);
			} catch( Exception e ) {
				return ResponseInfo.not(e.getMessage());
			} 
			
			map.remove("neighbors");
			
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);
			
			records.add(record);
		}
		
		try {
			timeSeriesRepository.insertPoints("neighborstate", records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}    
    }
	
	@PostMapping(value = "/neighborstate_old")
    public ResponseInfo neighborstate_old(@RequestBody List<Map<String,Object>> list) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("sessionId", map.get("sessionId").toString());
			map.remove("sessionId");
			tags.put("nodeId", map.get("nodeId").toString());
			map.remove("nodeId");
			
			//neighbors
			String neighborsMetricString= map.get("neighbors").toString();
			map.replace("neighbors", neighborsMetricString);
			
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);
			
			records.add(record);
		}
		
		try {
			timeSeriesRepository.insertPoints("neighborstate", records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}    
    }
	
	//上报-发送业务指标&接收业务指标
	@PostMapping(value = "/flow/{type}")
    public ResponseInfo flow(@PathVariable String type, @RequestBody List<Map<String,Object>> list) {
		List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			Map<String,String> tags = new HashMap<String,String>();
			tags.put("sessionId", map.get("sessionId").toString());
			map.remove("sessionId");
			tags.put("nodeId", map.get("nodeId").toString());
			map.remove("nodeId");
			
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("tags", tags);
			record.put("fields", map);
			
			records.add(record);
		}
		
		String measurement = type.equals("1") ? "sendflow" : "receiveflow";
		
		try {
			timeSeriesRepository.insertPoints(measurement, records);
			return ResponseInfo.ok("ok");
		} catch( Exception e ) {
			return ResponseInfo.not(e.getMessage());
		}    
    }


	@GetMapping(value = "/routesTop10/{sessionId}") //
	public ResponseInfo routesTrend(@PathVariable String sessionId) {
		Object resNeighbors = null;
		Object resRoute = null;
		String startTime = "";
		Map<String, Object> mapRes  = new HashMap<String,Object>();
		if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else{
			startTime = sessionStartTime;
		}

		resNeighbors = timeSeriesRepository.queryNeighborsTop10("neighborstate", "sessionId", sessionId, startTime);
		mapRes.put("neighborstate",resNeighbors);

		resRoute = timeSeriesRepository.queryPrefixNumTop10("routestate", "sessionId", sessionId, startTime);
		mapRes.put("routestate",resRoute);


		return ResponseInfo.ok(mapRes);
	}

	@GetMapping(value = "/globalPrefixesNum/{sessionId}") //
	public ResponseInfo globalPrefixesNum(@PathVariable String sessionId) {
		Object resGlobalPrefixesNum = null;
		String startTime = "";
		Map<String, Object> mapRes  = new HashMap<String,Object>();
		if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else{
			startTime = sessionStartTime;
		}

		resGlobalPrefixesNum = timeSeriesRepository.queryGlobalPrefixes("global-prefixes-num", sessionId,"prefixNum", startTime);
		mapRes.put("globalPrefixesNum",resGlobalPrefixesNum);


		return ResponseInfo.ok(mapRes);
	}

	@GetMapping(value = "/{stateType}/{value}") //
	public ResponseInfo hello(@PathVariable String stateType, @PathVariable String value) {
		Object res = null;
		String startTime = "";
		if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else{
			startTime = sessionStartTime;
		}
		if (stateType.equals("serverstate")) {
			res = timeSeriesRepository.queryServer(stateType, "ip", value, startTime);
		} else if (stateType.equals("neighborstateTop10")) {
			res = timeSeriesRepository.queryNeighborsTop10("neighborstate", "sessionId", value, startTime);
		} else if (stateType.equals("routestateTop10")) {
			res = timeSeriesRepository.queryPrefixNumTop10("routestate", "sessionId", value, startTime);
		} else {
			res = timeSeriesRepository.query(stateType, "sessionId", value, startTime);
		}
		
        return ResponseInfo.ok(res);
    }
	
	@GetMapping(value = "/{measurement}/{sessionId}/{nodeId}/{field}")
	public ResponseInfo hello1(@PathVariable String measurement, @PathVariable String sessionId,@PathVariable String nodeId, @PathVariable String field) {
		System.out.println("sessionStartTime:" + sessionStartTime);
		String startTime = "";
		if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else{
			startTime = sessionStartTime;
		}
		System.out.println("startTime:" + startTime);
		Object res = timeSeriesRepository.queryFiled(measurement, sessionId, nodeId, field, startTime);
		
        return ResponseInfo.ok(res);
    }

	@GetMapping(value = "/routeTrend/{sessionId}/{nodeId}")
	public ResponseInfo routeTrend(@PathVariable String sessionId,@PathVariable String nodeId) {
		System.out.println("sessionStartTime:" + sessionStartTime);
		String startTime = "";
		if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else{
			startTime = sessionStartTime;
		}
		System.out.println("startTime:" + startTime);
		Object resPathChangeNum = timeSeriesRepository.queryFiledTrend("routestate", sessionId, nodeId, "pathChangeNum", startTime);
		Object resPrefixNum = timeSeriesRepository.queryFiledTrend("routestate", sessionId, nodeId, "prefixNum", startTime);
		Map<String, Object> mapRes  = new HashMap<String,Object>();

		mapRes.put("nodeId", nodeId);
		mapRes.put("sessionId", sessionId);
		mapRes.put("pathChangeTrend", resPathChangeNum);
		mapRes.put("prefixNumTrend", resPrefixNum);


		return ResponseInfo.ok(mapRes);
	}
	
	@GetMapping(value = "/{measurement}/{sessionId}/{nodeId}")
	public ResponseInfo hello2(@PathVariable String measurement, @PathVariable String sessionId,@PathVariable String nodeId) {
		String startTime = "";
		if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else {
			startTime = sessionStartTime;
		}
		Object res = timeSeriesRepository.queryNode(measurement, sessionId, nodeId, startTime);
		
        return ResponseInfo.ok(res);
    }
	
	@GetMapping(value = "/period/{measurement}/{sessionId}/{nodeId}")
	public ResponseInfo hello3(@PathVariable String measurement, @PathVariable String sessionId,@PathVariable String nodeId, @RequestParam(name = "period") String period) {
		Object res = timeSeriesRepository.queryNodePeriod(measurement, sessionId, nodeId, period);
		
        return ResponseInfo.ok(res);
    }
	
	@GetMapping(value = "/lastest/{measurement}/{sessionId}/{nodeId}")
	public ResponseInfo hello4(@PathVariable String measurement, @PathVariable String sessionId,@PathVariable String nodeId) {
		Object res = timeSeriesRepository.queryNodeLastest(measurement, sessionId, nodeId);
		
        return ResponseInfo.ok(res);
    }
	
	@GetMapping(value = "/serverperiod/{ip}")
	public ResponseInfo hello31(@PathVariable String ip, @RequestParam(name = "period") String period) {
		Object res = timeSeriesRepository.queryServerPeriod(ip, period);
		
        return ResponseInfo.ok(res);
    }
	
	@GetMapping(value = "/serverperiodbyid/{serverid}")
	public ResponseInfo hello32(@PathVariable String serverid, @RequestParam(name = "period") String period) {
		
		Record r = dao.fetch("mserver", Cnd.where("status", "=", 1).and("id", "=", serverid));
		
		Object res = timeSeriesRepository.queryServerPeriod(r.getString("ip"), period);
		
        return ResponseInfo.ok(res);
    }
	
	@GetMapping(value = "/serverperiodbyip/{serverip}")
	public ResponseInfo hello332(@PathVariable String serverip, @RequestParam(name = "period") String period) {
		
		Object res = timeSeriesRepository.queryServerPeriod(serverip, period);
		
        return ResponseInfo.ok(res);
    }
	
	@GetMapping(value = "/top/{measurement}/{sessionId}/{nodeId}")
	public ResponseInfo hello4(@PathVariable String measurement, @PathVariable String sessionId,@PathVariable String nodeId,@RequestParam(name = "field") String field, @RequestParam(name = "top") int top) {
		String startTime = "";
		if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else {
			startTime = sessionStartTime;
		}
		Object res = timeSeriesRepository.queryTopField(measurement, sessionId, nodeId, field, top, startTime);
		
        return ResponseInfo.ok(res);
    }
	
	@GetMapping(value = "/topWithASN/{measurement}/{sessionId}/{nodeId}")
	public ResponseInfo hello5(@PathVariable String measurement, @PathVariable String sessionId,@PathVariable String nodeId,@RequestParam(name = "field") String field, @RequestParam(name = "top") int top) {
		List<Map<String, Object>> resWithAsn = new ArrayList<Map<String, Object>>();
		String startTime = "";
		if(sessionStartTime != null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else{
			startTime = sessionStartTime;
		}
		
		List<Map<String, Object>> res = (List<Map<String, Object>>) timeSeriesRepository.queryTopField(measurement, sessionId, nodeId, field, top, startTime);
		 
		for (int i = 0; i < res.size(); i++) {
			Map<String, Object> map = res.get(i);
			
			DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

	        // 解析字符串得到ZonedDateTime
	        ZonedDateTime zonedDateTime = ZonedDateTime.parse(map.get("time").toString(), formatter);

	        // 转换为Instant
	        Instant instant = zonedDateTime.toInstant();

	        // 获取Unix时间戳（毫秒）
	        long timestampInMilliseconds = instant.toEpochMilli() * 1000000;
	        
			List<Map<String, Object>> res1 = (List<Map<String, Object>>)timeSeriesRepository.queryFieldBytime(measurement, sessionId, nodeId, "asn", timestampInMilliseconds, startTime);
			if (res1 != null && res1.size() > 0) {
				map.put("asn", res1.get(0).get("asn"));
			}
			
			resWithAsn.add(map);
		}
		
        return ResponseInfo.ok(resWithAsn);
    }
	
	//获取场景中所有服务器及其资源使用情况
	@GetMapping(value = "/serverstate/{sceneid}")
	public ResponseInfo hello5(@PathVariable String sceneid) {
		String startTime = "";
		if(sessionStartTime == null || sessionStartTime.equals("undefine") || sessionStartTime.equals("null") || sessionStartTime.isEmpty()) {
			String formattedDateTime = getCurTime();
			startTime = formattedDateTime;
		}
		else{
			startTime = sessionStartTime;
		}

		List<Record> serverlist = dao.query("scene_server_view", Cnd.where("status", "=", 1).and("sceneid", "=", sceneid));
		
		List<Map<String, Object>> serverstateList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < serverlist.size(); i++) {
			Record server = serverlist.get(i);
			
			String ip = server.getString("ip");
			Object serverstate = timeSeriesRepository.queryServer("serverstate", "ip", ip, startTime);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(ip, serverstate);
			map.put("server", server.getString("mserver"));
			serverstateList.add(map);
		}
		
        return ResponseInfo.ok(serverstateList);
    }
	
	//获取场景中所有业务
	@GetMapping(value = "/business/{sceneid}")
	public ResponseInfo hello6(@PathVariable String sceneid) {

		List<Record> list = dao.query("business", Cnd.where("status", "=", 1).and("sceneid", "=", sceneid));
		
		List<Record> rstList = new ArrayList<Record>();
		for (int i = 0; i < list.size(); i++) {
			Record r = list.get(i);
			
			Record rst = new Record();
			
			rst.put("business", r.getString("business"));
			rst.put("srcid", r.getString("srcid"));
			rst.put("srcasn", r.getString("srcasn"));
			rst.put("destid", r.getString("destid"));
			rst.put("destasn", r.getString("destasn"));
			rst.put("rate", r.getString("lifecycle") + r.getString("typevalue2"));
			rst.put("interval", r.getString("packagelength"));
			rst.put("duration", r.getString("starttime"));
			
			rstList.add(rst);
		}
		
        return ResponseInfo.ok(rstList);
    }
	
	@GetMapping(value = "/lastestNeighborstateAndRoutestate/{sessionId}/{nodeId}")
	public ResponseInfo hello7(@PathVariable String sessionId,@PathVariable String nodeId) {
		Object resNeighborstate = timeSeriesRepository.queryNodeLastest("neighborstate", sessionId, nodeId);
		Object resRoutestate = timeSeriesRepository.queryNodeLastest("routestate", sessionId, nodeId);
		
		List<Object> resNeighborstate0 = (List<Object>) resNeighborstate;
		List<Object> resRoutestate0 = (List<Object>) resRoutestate;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("neighborstate", resNeighborstate0.size() > 0 ? resNeighborstate0.get(0) : null);
		map.put("routestate", resRoutestate0.size() > 0 ? resRoutestate0.get(0) : null);
		
        return ResponseInfo.ok(map);
    }
	
	@GetMapping(value = "/period/usagestateNodeAndServer/{sessionId}/{sceneid}/{nodeId}")
	public ResponseInfo hello8(@PathVariable String sessionId,@PathVariable String sceneid,@PathVariable String nodeId, @RequestParam(name = "period") String period) {
		Object resNode = timeSeriesRepository.queryNodeLastestByPeriod("usagestate", sessionId, nodeId, period);
		
		Record r = dao.fetch("node_view", Cnd.where("status", "=", 1).and("asn", "=", nodeId).and("sceneid", "=", sceneid));
		Object resServer = timeSeriesRepository.queryServerLatestByPeriod(r.getString("ip"), period);
		
		List<Object> resNode0 = (List<Object>) resNode;
		List<Object> resServer0 = (List<Object>) resServer;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("node", resNode0.size() > 0 ? resNode0.get(resNode0.size() -1) : null);
		map.put("server", resServer0.size() > 0 ? resServer0.get(resServer0.size() -1) : null);
		
        return ResponseInfo.ok(map);
    }
		
	
	@GetMapping(value = "/hello")
	public String hello(ServerHttpResponse response) {
        return "hello";
    }


	public void parserMRTFormatFile(String inputFileName, String typeFile) {
//		System.out.println(inputFileName);
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			// 在这里设置要执行的命令
			processBuilder.command("/usr/local/bin/bgpkit-parser", "--pretty", inputFileName);

			Process process = processBuilder.start();

			// 获取命令的输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			int cnt = 0;
			while ((line = reader.readLine()) != null) {
//				System.out.println(line);
				String[] parts = line.split("\\|"); // 使用双反斜杠转义

				if(parts.length >= 12)
				{
					cnt += 1;
					String Type = parts[0];

					long timestampLong = Long.parseLong(parts[1]); // 字符串转long型
					// 将时间戳转换为 LocalDateTime
					LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampLong), ZoneId.systemDefault());
					// 定义日期时间格式化器
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					// 格式化日期时间并输出
					String Timestamp = dateTime.format(formatter);

					String PeerIp = parts[2];
					String PeerAsn = parts[3];
					String Prefix = parts[4];
					String AsPath = parts[5];
					String Orgin = parts[6];
					String NextHop = parts[7];
					String LocalPref = parts[8];
					String Med = parts[9];
					String Communities = parts[10];
					Boolean Atomic = Boolean.parseBoolean(parts[11]);

					// 获取 MD5 消息摘要实例
					MessageDigest md = MessageDigest.getInstance("MD5");
					// 计算 MD5 的字节数组
					byte[] hashInBytes = md.digest(line.getBytes());
					// 将字节数组转换为十六进制格式的字符串
					StringBuilder sb = new StringBuilder();
					for (byte b : hashInBytes) {
						sb.append(String.format("%02x", b));
					}
					String Md5sum = sb.toString();

					Record ribRecord = new Record();
					ribRecord.set("type", Type);
					ribRecord.set("timestamp", Timestamp);
					ribRecord.set("peer_ip", PeerIp);
					ribRecord.set("peer_asn", PeerAsn);
					ribRecord.set("prefix", Prefix);
					ribRecord.set("as_Path", AsPath);
					ribRecord.set("orgin", Orgin);
					ribRecord.set("next_hop", NextHop);
					ribRecord.set("local_pref", LocalPref);
					ribRecord.set("med", Med);
					ribRecord.set("communities", Communities);
					ribRecord.set("atomic", Atomic);
					ribRecord.set("md5sum", Md5sum);

					if(typeFile.equals("rib"))
						dao.insert("rib", Chain.from(ribRecord));
					else if(typeFile.equals("routeupdate"))
						dao.insert("routeupdate", Chain.from(ribRecord));
					if(cnt > 2000) {
						return;
					}
//						break;
						
				}
				else {
					continue;
				}


			}

			return;
			// 等待命令执行完成
//			int exitCode = process.waitFor();
//			System.out.println("Exited with code: " + exitCode);
//			System.out.println("Parser RIB OR RouteUpdate MRT Finished.");

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void parserCSVFormatFile4IRR(String inputFileName) {
		try {
			// 获取命令的输出
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
			String line;
			line = reader.readLine();
			int cnt = 0;
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("[\"]", "");
				String[] parts = line.split(","); // 使用双反斜杠转义

				if(parts.length >= 9)
				{
					cnt += 1;
					String IpPrefix = parts[0];
					String Asn = parts[1];
					String LastModified = parts[2];
					String LastUpdateTime = parts[3];
					String DataSource = parts[4];
					String CollectId = parts[5];
					String Source = parts[6];
					String Descr = parts[7];
					String OvState = parts[8];

					// 获取 MD5 消息摘要实例
					MessageDigest md = MessageDigest.getInstance("MD5");
					// 计算 MD5 的字节数组
					byte[] hashInBytes = md.digest(line.getBytes());
					// 将字节数组转换为十六进制格式的字符串
					StringBuilder sb = new StringBuilder();
					for (byte b : hashInBytes) {
						sb.append(String.format("%02x", b));
					}
					String Md5sum = sb.toString();

					Record irrRecord = new Record();
					irrRecord.set("ip_prefix", IpPrefix);
					irrRecord.set("asn", Asn);
					irrRecord.set("last_modified", LastModified);
					irrRecord.set("last_updatetime", LastUpdateTime);
					irrRecord.set("data_source", DataSource);
					irrRecord.set("collect_id", CollectId);
					irrRecord.set("source", Source);
					irrRecord.set("descr", Descr);
					irrRecord.set("ov_state", OvState);
					irrRecord.set("md5sum", Md5sum);

					dao.insert("irr", Chain.from(irrRecord));

					if(cnt >= 2000) {
						return;
//						break;
					}
						
				}
				else {
					continue;
				}

			}

			System.out.println("Parser  IRR CSV Finished.");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void parserCSVFormatFile4ROA(String inputFileName) {
		try {
			// 获取命令的输出
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
			String line;
			line = reader.readLine();
			int cnt = 0;
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("[\"]", "");
				String[] parts = line.split(","); // 使用双反斜杠转义

				if(parts.length >= 4)
				{
					cnt += 1;
					String Asn = parts[0];
					String IPPrefix = parts[1];
					int MaxLength = Integer.parseInt(parts[2]);
					String TrustAnchor = parts[3];

					// 获取 MD5 消息摘要实例
					MessageDigest md = MessageDigest.getInstance("MD5");
					// 计算 MD5 的字节数组
					byte[] hashInBytes = md.digest(line.getBytes());
					// 将字节数组转换为十六进制格式的字符串
					StringBuilder sb = new StringBuilder();
					for (byte b : hashInBytes) {
						sb.append(String.format("%02x", b));
					}
					String Md5sum = sb.toString();

					Record roaRecord = new Record();
					roaRecord.set("asn", Asn);
					roaRecord.set("ip_prefix", IPPrefix);
					roaRecord.set("max_length", MaxLength);
					roaRecord.set("trust_anchor", TrustAnchor);
					roaRecord.set("md5sum", Md5sum);

					dao.insert("roa", Chain.from(roaRecord));
					if(cnt >= 2000) {
						return;
//						break;
					}
						
				}
				else {
					continue;
				}

			}

			System.out.println("Parser ROA CSV Finished.");
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	//解析文件
	@GetMapping(value = "/rib/{uuid}")
	public ResponseInfo ribParse(@PathVariable String uuid) {
		System.out.println(uuid);
		//根据uuid获取文件
		Record fileRecord = dao.fetch("upload", Cnd.where("uuid", "=", uuid));
		String filePath = fileRecord.getString("filepath");
		System.out.println(filePath);
		//解析文件存入数据库
		parserMRTFormatFile(filePath, "rib");
		
		
        return ResponseInfo.ok("ok");
    }
	//解析文件
	@GetMapping(value = "/roa/{uuid}")
	public ResponseInfo roaParse(@PathVariable String uuid) {

		//根据uuid获取文件
		Record fileRecord = dao.fetch("upload", Cnd.where("uuid", "=", uuid));
		String filePath = fileRecord.getString("filepath");

		//解析文件存入数据库
		parserCSVFormatFile4ROA(filePath);


		return ResponseInfo.ok("ok");
	}
	//解析文件
	@GetMapping(value = "/routeUpdate/{uuid}")
	public ResponseInfo routeUpdateParse(@PathVariable String uuid) {

		//根据uuid获取文件
		Record fileRecord = dao.fetch("upload", Cnd.where("uuid", "=", uuid));
		String filePath = fileRecord.getString("filepath");

		//解析文件存入数据库
		parserMRTFormatFile(filePath, "routeupdate");


		return ResponseInfo.ok("ok");
	}
	//解析文件
	@GetMapping(value = "/irr/{uuid}")
	public ResponseInfo irrParse(@PathVariable String uuid) {

		//根据uuid获取文件
		Record fileRecord = dao.fetch("upload", Cnd.where("uuid", "=", uuid));
		String filePath = fileRecord.getString("filepath");

		//解析文件存入数据库
		parserCSVFormatFile4IRR(filePath);


		return ResponseInfo.ok("ok");
	}
	
	@GetMapping(value = "/msets/{nodeId}/{statement}")
	public ResponseInfo helloMsets(@PathVariable String nodeId, @PathVariable String statement) {
		Record statementRecord = dao.fetch("statement", Cnd.where("nodeid", "=", nodeId).and("statement", "=", statement).and("status", "=", 1));
		if (statementRecord == null) {
			return ResponseInfo.ok("无法查到statement信息");
		}
		
		String statementId = statementRecord.getString("id");
		
		List<Record> msets = dao.query("mset", Cnd.where("nodeid", "=", nodeId).and("statementid", "=", statementId).and("status", "=", 1));
		
        return ResponseInfo.ok(msets);
    }
	
}
