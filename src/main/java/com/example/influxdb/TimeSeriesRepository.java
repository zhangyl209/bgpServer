package com.example.influxdb;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.business.base.SystemConfig;
import com.example.commons.ResponseInfo;
import com.example.nutz.DBTools;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

@Repository
public class TimeSeriesRepository {
	
	@Autowired
    InfluxDBClient influxDBClient;

    private String org;

    private String bucket;
    
    WriteApi writeApi = InfluxdbConfig.getWriteApi();

    public String getCurTime()
    {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        // 将时间转换为 UTC 时区
        ZonedDateTime utcTime = currentDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = utcTime.format(dateTimeFormatter);
        return formattedDateTime.replace(" ", "T");
    }
    
    public TimeSeriesRepository() {
    	this.org = SystemConfig.getStringValue("influxDBOrg");

    	this.bucket = SystemConfig.getStringValue("influxDBBucket");
    }

    /**
     * 保存
     * @param measurement 表名
     * @param fields
     * @param tags
     */
    public void insertPoint(String measurement, Map<String,Object> fields, Map<String,String> tags){
//        WriteOptions writeOptions = WriteOptions.builder()
//                .batchSize(5000)
//                .flushInterval(1000)
//                .bufferLimit(10000)
//                .jitterInterval(1000)
//                .retryInterval(5000)
//                .build();
//        try (WriteApi writeApi = influxDBClient.makeWriteApi(writeOptions)) {  //
////            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
//        	Instant instant = null;
//        	if (fields.get("time") != null) {
//        		instant = Instant.ofEpochMilli(Long.parseLong(fields.get("time").toString()));
//        		fields.remove("time");
//        	}
//        	
//            Point point = Point
//                .measurement(measurement)
//                .addTags(tags)
//                .addFields(fields)
//                .time(instant != null ? instant : Instant.now(), WritePrecision.MS);
//            writeApi.writePoint(bucket, org, point);
//        }
    	Instant instant = null;
    	if (fields.get("time") != null) {
    		instant = Instant.ofEpochMilli(Long.parseLong(fields.get("time").toString()));
    		fields.remove("time");
    	}
    	
        Point point = Point
            .measurement(measurement)
            .addTags(tags)
            .addFields(fields)
            .time(instant != null ? instant : Instant.now(), WritePrecision.MS);
        writeApi.writePoint(bucket, org, point);
    }
    
    /**
     * 批量新增
     * @throws Exception 
     */
    public void insertPoints(String measurement, List<Map<String,Object>> records) throws Exception{
        List<Point> batchPoints = new ArrayList<Point>();
        records.forEach(record->{
            
            Map<String,String> tags = (Map<String, String>) record.get("tags");
            Map<String,Object> fields = (Map<String, Object>) record.get("fields");
            
            Instant instant = null;
        	if (fields.get("time") != null) {
        		instant = Instant.ofEpochMilli(Long.parseLong(fields.get("time").toString()));
        		fields.remove("time");
        	}
        	
            Point point = Point
                .measurement(measurement)
                .addTags(tags)
                .addFields(fields)
                .time(instant != null ? instant : Instant.now(), WritePrecision.MS);
            
            batchPoints.add(point);
        });
        
//        WriteOptions writeOptions = WriteOptions.builder()
//                .batchSize(5000)
//                .flushInterval(1000)
//                .bufferLimit(10000)
//                .jitterInterval(1000)
//                .retryInterval(5000)
//                .build();
//        
//        try {
//        	try (WriteApi writeApi = influxDBClient.makeWriteApi(writeOptions)) {  //
//            	writeApi.writePoints(bucket, org, batchPoints);
//            }
//        }catch( Exception e ) {
//			throw new Exception(e.getMessage());
//		} 
        writeApi.writePoints(bucket, org, batchPoints);
        
    }
    
    /**
     * 查询语法说明

     * https://blog.52itstyle.vip

     * 1、bucket 桶
     * 2、range 指定起始时间段
     *    range有两个参数start，stop，stop不设置默认为当前。
     *    range可以是相对的（使用负持续时间）或绝对（使用时间段）
     * 3、filter 过滤条件查询 _measurement 表  _field 字段
     * 4、yield()函数作为查询结果输出过滤的tables。
     * 更多参考：https://docs.influxdata.com/influxdb/v2.0/query-data/flux/
     * @return
     */
    public Object query(String measurement, String stage, String sessionId, String startTime){
    	
    	//查询
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -60d)\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")"
//    			;
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")"
                ;
        String stopTime = getCurTime();
    	String flux = String.format(sql, startTime, stopTime, measurement, stage, sessionId);
        System.out.println("flux:" + flux);
    	
//        sql = "from(bucket: \"%s\") |> range(start: -1m)";
//        sql +="  |> filter(fn: (r) => r._measurement == \"%s\" and";
//        sql +="  r._field == \"%s\")";
//        sql +="  |> yield()";
//        String flux = String.format(sql, bucket,"dnc_humidity","humidity");
       
        return formate(flux);
    }
    
    private Object formate(String sql){
    	
   	 QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(sql,org);
        
        List<Map<String, Object>> mapList = new ArrayList<>();

        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                
                for (FluxRecord fluxRecord : records) {
               	 Map<String, Object> map = new HashMap<>();
                    Map<String, Object> values = fluxRecord.getValues();
                    map.put("sessionId", values.get("sessionId"));
                    map.put("nodeId", values.get("nodeId"));
                    map.put("time", values.get("_time"));
                    mapList.add(map);
                }
            }
        }
        return mapList;
   }
    
    public Object queryFiled(String measurement, String sessionId, String nodeId, String field, String startTime){
    	
    	//查询
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -10d)\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_field\"] == \"%s\")"
//    			;
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"_field\"] == \"%s\")"
                ;
        String stopTime = getCurTime();
    	String flux = String.format(sql, startTime, stopTime, measurement, sessionId, nodeId, field);
        System.out.println("flux:" + flux);
    	
//        sql = "from(bucket: \"%s\") |> range(start: -1m)";
//        sql +="  |> filter(fn: (r) => r._measurement == \"%s\" and";
//        sql +="  r._field == \"%s\")";
//        sql +="  |> yield()";
//        String flux = String.format(sql, bucket,"dnc_humidity","humidity");
       
        return formate1(flux);
    }

    private Object formateGlobalPrefixes(String sql){

        QueryApi queryApi = influxDBClient.getQueryApi();

        List<FluxTable> tables = queryApi.query(sql,org);

        List<Map<String, Object>> mapList = new ArrayList<>();

        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();

                for (FluxRecord fluxRecord : records) {
                    Map<String, Object> map = new HashMap<>();
                    Map<String, Object> values = fluxRecord.getValues();
//                    map.put("sessionId", values.get("sessionId"));
                    map.put("time", values.get("_time"));
                    map.put(fluxRecord.getField(), fluxRecord.getValue());
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }
    public Object queryGlobalPrefixes(String measurement, String sessionId, String field, String startTime){

        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"_field\"] == \"%s\")\r\n"
                + "  |> sort(columns: [\"_time\"])"  // 按时间排序
                ;
        String stopTime = getCurTime();
        String flux = String.format(sql, startTime, stopTime, measurement, sessionId, field);
        System.out.println("flux:" + flux);

//        sql = "from(bucket: \"%s\") |> range(start: -1m)";
//        sql +="  |> filter(fn: (r) => r._measurement == \"%s\" and";
//        sql +="  r._field == \"%s\")";
//        sql +="  |> yield()";
//        String flux = String.format(sql, bucket,"dnc_humidity","humidity");

        return formateGlobalPrefixes(flux);
    }

    public Object queryFiledTrend(String measurement, String sessionId, String nodeId, String field, String startTime){

        //查询
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -10d)\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_field\"] == \"%s\")"
//    			;
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"_field\"] == \"%s\")"
                ;
        String stopTime = getCurTime();
        String flux = String.format(sql, startTime, stopTime, measurement, sessionId, nodeId, field);
        System.out.println("flux:" + flux);

//        sql = "from(bucket: \"%s\") |> range(start: -1m)";
//        sql +="  |> filter(fn: (r) => r._measurement == \"%s\" and";
//        sql +="  r._field == \"%s\")";
//        sql +="  |> yield()";
//        String flux = String.format(sql, bucket,"dnc_humidity","humidity");

//        return formate1(flux);
        return formateTrend(flux);
    }

    private Object formateTrend(String sql){

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(sql,org);

        List<Map<String, Object>> mapList = new ArrayList<>();

        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();

                for (FluxRecord fluxRecord : records) {
                    Map<String, Object> map = new HashMap<>();
                    Map<String, Object> values = fluxRecord.getValues();
//                    map.put("sessionId", values.get("sessionId"));
//                    map.put("nodeId", values.get("nodeId"));
                    map.put("time", values.get("_time"));
                    map.put(fluxRecord.getField(), fluxRecord.getValue());
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }

    private Object formate1(String sql){
    	
   	 QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(sql,org);
        
        List<Map<String, Object>> mapList = new ArrayList<>();

        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                
                for (FluxRecord fluxRecord : records) {
               	 Map<String, Object> map = new HashMap<>();
                    Map<String, Object> values = fluxRecord.getValues();
                    map.put("sessionId", values.get("sessionId"));
                    map.put("nodeId", values.get("nodeId"));
                    map.put("time", values.get("_time"));
                    map.put(fluxRecord.getField(), fluxRecord.getValue());
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }
    
    
    public Object queryNode(String measurement, String sessionId, String nodeId, String startTime){
    	
    	//查询
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -10d)\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")"
//    			;
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")"
                ;
        String stopTime = getCurTime();
    	String flux = String.format(sql, startTime, stopTime, measurement, sessionId, nodeId);
        System.out.println("flux:" + flux);
    	
//        sql = "from(bucket: \"%s\") |> range(start: -1m)";
//        sql +="  |> filter(fn: (r) => r._measurement == \"%s\" and";
//        sql +="  r._field == \"%s\")";
//        sql +="  |> yield()";
//        String flux = String.format(sql, bucket,"dnc_humidity","humidity");
       
        return formateServer(flux);
    }
    
    public Object queryNodePeriod(String measurement, String sessionId, String nodeId, String period){
    	
    	//查询
    	String sql = "from(bucket: \"bgp\")\r\n"
    			+ "  |> range(start: -%sh)\r\n"
    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")"
    			;
    	String flux = String.format(sql, period, measurement, sessionId, nodeId);
        System.out.println("flux:" + flux);
    	
//        sql = "from(bucket: \"%s\") |> range(start: -1m)";
//        sql +="  |> filter(fn: (r) => r._measurement == \"%s\" and";
//        sql +="  r._field == \"%s\")";
//        sql +="  |> yield()";
//        String flux = String.format(sql, bucket,"dnc_humidity","humidity");
       
        return formateServer(flux);
    }
    
public Object queryNodeLastestByPeriod(String measurement, String sessionId, String nodeId, String period){
    	
    	//查询
    	String sql = "from(bucket: \"bgp\")\r\n"
    			+ "  |> range(start: -%sh)\r\n"
    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")\r\n"
    			+ "  |> last()"
    			;
    	String flux = String.format(sql, period, measurement, sessionId, nodeId);
    System.out.println("flux:" + flux);
    	
//        sql = "from(bucket: \"%s\") |> range(start: -1m)";
//        sql +="  |> filter(fn: (r) => r._measurement == \"%s\" and";
//        sql +="  r._field == \"%s\")";
//        sql +="  |> yield()";
//        String flux = String.format(sql, bucket,"dnc_humidity","humidity");
       
        return formateServer(flux);
    }
    
    public Object queryNodeLastest(String measurement, String sessionId, String nodeId){
    	
    	//查询
    	String sql = "from(bucket: \"bgp\")\r\n"
    			+ "  |> range(start: -%sh)\r\n"
    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")\r\n"
    			+ "  |> last()"
    			;
    	String flux = String.format(sql, 1000, measurement, sessionId, nodeId); //正式环境需要修改1000为较短时间范围
        System.out.println("flux:" + flux);
    	
//        sql = "from(bucket: \"%s\") |> range(start: -1m)";
//        sql +="  |> filter(fn: (r) => r._measurement == \"%s\" and";
//        sql +="  r._field == \"%s\")";
//        sql +="  |> yield()";
//        String flux = String.format(sql, bucket,"dnc_humidity","humidity");
       
        return formateServer(flux);
    }
    
    public Object queryServerPeriod(String ip, String period){
    	
    	//查询
    	String sql = "from(bucket: \"bgp\")\r\n"
    			+ "  |> range(start: -%sh)\r\n"
    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
    			+ "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
//    			+ "  |> window(every: 5m)\r\n"   //5分钟一个窗口取平均
//    			+ "  |> mean()"
				;
    	String flux = String.format(sql, period, "serverstate", "ip", ip);
        System.out.println("flux:" + flux);
        return formateServer(flux);
    }
    
    public Object queryServerLatestByPeriod(String ip, String period){
    	
    	//查询
    	String sql = "from(bucket: \"bgp\")\r\n"
    			+ "  |> range(start: -%sh)\r\n"
    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
    			+ "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
//    			+ "  |> window(every: 5m)\r\n"   //5分钟一个窗口取平均
    			+ "  |> last()"
				;
    	String flux = String.format(sql, period, "serverstate", "ip", ip);
        System.out.println("flux:" + flux);
        return formateServer(flux);
    }
    
    public Object queryServer(String measurement, String stage, String sessionId, String startTime){
    	
    	//查询
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -100h)\r\n"  //这里测试把时间调大，正式应该不用这么大
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
//    			+ "  |> last()";
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
                + "  |> last()";
        String stopTime = getCurTime();
    	String flux = String.format(sql, startTime, stopTime, measurement, stage, sessionId);
        System.out.println("flux:" + flux);
        return formateServer(flux);
    }
    
    @SuppressWarnings("unchecked")
	private Object formateServer(String sql){
    	
   	 	QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(sql,org);
        
        List<Map<String, Object>> mapList = new ArrayList<>();

        Map<String, Object> timeMap = new HashMap<>();
        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                for (FluxRecord fluxRecord : records) {
                	Instant time = fluxRecord.getTime();
                	Map<String, Object> map = new HashMap<>();
                	if ( timeMap.get(time.toString()) == null ) {
                		map.put("time", time);//Instant
                	} else {
                		map = (Map<String, Object>) timeMap.get(time.toString());
                	}
                    
                    map.put(fluxRecord.getField(), fluxRecord.getValue());
                    
//                    map.put("value", fluxRecord.getValue());
//                    map.put("valueTime", fluxRecord.getTime());//Instant
//                    map.put("field", fluxRecord.getField());
//                    map.put("values", fluxRecord.getValues());
                    timeMap.put(time.toString(), map);
                }
            }
        }
        for (String key : timeMap.keySet()) {
        	mapList.add((Map<String, Object>) timeMap.get(key));
        }
        	
        
        return mapList;
    }

    public Object queryServerByIP(String measurement, String stage, String sessionId, String startTime){

        //查询
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -100h)\r\n"  //这里测试把时间调大，正式应该不用这么大
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
//    			+ "  |> last()";
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
                + "  |> last()";
        String stopTime = getCurTime();
        String flux = String.format(sql, startTime, stopTime, measurement, stage, sessionId);
        System.out.println("flux:" + flux);
        return formateServerWithIP(flux);
    }

    @SuppressWarnings("unchecked")
    private Object formateServerWithIP(String sql){

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(sql,org);

        List<Map<String, Object>> mapList = new ArrayList<>();

        Map<String, Object> IPMap = new HashMap<>();
        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                for (FluxRecord fluxRecord : records) {
                    Map<String, Object> values = fluxRecord.getValues();
                    IPMap.put("ip", values.get("ip"));

                    IPMap.put(fluxRecord.getField(), fluxRecord.getValue());

                }
            }
        }

        return IPMap;
    }
    
    public Object queryNeighborsTop10(String measurement, String stage, String value, String startTime){
    	
    	//查询  注意这里需要修改一下时间，正式可以缩短到很近的时间
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -10d)\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r._field == \"neighborNum\")\r\n"
//    			+ "  |> group(columns: [\"nodeId\"], mode: \"by\")\r\n"
//    			+ "  |> last()"
//    			;
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r._field == \"neighborNum\")\r\n"
                + "  |> group(columns: [\"nodeId\"], mode: \"by\")\r\n"
                + "  |> last()"
                ;
        String stopTime = getCurTime();
    	String flux = String.format(sql, startTime, stopTime, measurement, stage, value);
        System.out.println("flux:" + flux);
    	
        return formateAndTop10(flux, 1);
    }
    
    public Object queryPrefixNumTop10(String measurement, String stage, String value, String startTime){
    	
    	//查询  注意这里需要修改一下时间，正式可以缩短到很近的时间
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -10d)\r\n"     //这里正式环境需要调小
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r._field == \"pathNum\")\r\n"
//    			+ "  |> group(columns: [\"nodeId\"], mode: \"by\")\r\n"
//    			+ "  |> last()"
//    			;
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"     //这里正式环境需要调小
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"%s\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r._field == \"pathNum\")\r\n"
                + "  |> group(columns: [\"nodeId\"], mode: \"by\")\r\n"
                + "  |> last()"
                ;

        String stopTime = getCurTime();
    	String flux = String.format(sql, startTime, stopTime, measurement, stage, value);
        System.out.println("flux:" + flux);
    	
        return formateAndTop10(flux, 2);
    }
    
    private Object formateAndTop10(String sql, int type){
    	
   	 QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(sql,org);
        
        List<Map<String, Object>> mapList = new ArrayList<>();

        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                
                for (FluxRecord fluxRecord : records) {
               	 Map<String, Object> map = new HashMap<>();
                    Map<String, Object> values = fluxRecord.getValues();
//                    map.put("sessionId", values.get("sessionId"));
                    map.put("nodeId", values.get("nodeId"));
                    map.put("time", values.get("_time"));
                    map.put(fluxRecord.getField(), fluxRecord.getValue());
                    mapList.add(map);
                }
            }
        }
        
        if (type == 1) {
        	Collections.sort(mapList, new Comparator<Map<String, Object>>(){

        		@Override
        		public int compare(Map<String, Object> m1, Map<String, Object> m2) {
        			//return o1.getScore() - o2.getScore();//升序
        			return Integer.parseInt(m2.get("neighborNum").toString()) - Integer.parseInt(m1.get("neighborNum").toString()); //降序
        		}
        		
        	});
        } else if (type == 2){
        	Collections.sort(mapList, new Comparator<Map<String, Object>>(){

        		@Override
        		public int compare(Map<String, Object> m1, Map<String, Object> m2) {
        			//return o1.getScore() - o2.getScore();//升序
        			return Integer.parseInt(m2.get("pathNum").toString()) - Integer.parseInt(m1.get("pathNum").toString()); //降序
        		}
        		
        	});
        }
        
        
        
        return mapList.subList(0, mapList.size() > 10 ? 10 : mapList.size());
   }
    
//    public Object queryTopField(String measurement, String sessionId, String nodeId, String field, int top){
//    	
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -10d)\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_field\"] == \"%s\")"
//    			;
//    	String flux = String.format(sql, measurement, sessionId, nodeId, field);
//    	
//        return formateAndTop(flux, field, top);
//    }
    
    public Object queryTopField(String measurement, String sessionId, String nodeId, String field, int top, String startTime){
    	
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -10d)\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\") \r\n"
//    			+ "  |> filter(fn: (r) => r[\"_field\"] == \"%s\") \r\n"  //or r[\"_field\"] == \"asn\"
//    			+ "  |> sort(columns: [\"_value\"], desc: true) \r\n"
//    			+ "  |> limit(n: %s) "
//    			;
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\") \r\n"
                + "  |> filter(fn: (r) => r[\"_field\"] == \"%s\") \r\n"  //or r[\"_field\"] == \"asn\"
                + "  |> sort(columns: [\"_value\"], desc: true) \r\n"
                + "  |> limit(n: %s) "
                ;
        String stopTime = getCurTime();
    	String flux = String.format(sql, startTime, stopTime, measurement, sessionId, nodeId, field, top);
        System.out.println("flux:" + flux);
    	
        return formate1(flux);
    }
    
    public Object queryFieldBytime(String measurement, String sessionId, String nodeId, String field, long time, String startTime){
    	
//    	String sql = "from(bucket: \"bgp\")\r\n"
//    			+ "  |> range(start: -10d)\r\n"
//    			+ "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
//    			+ "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\") \r\n"
//    			+ "  |> filter(fn: (r) => r[\"_field\"] == \"%s\") \r\n"  //or r[\"_field\"] == \"asn\"
//    			+ "  |> filter(fn: (r) => r[\"_time\"] == time(v: %s))"
//    			;
        String sql = "from(bucket: \"bgp\")\r\n"
                + "  |> range(start: time(v:\"%sZ\"), stop: time(v: \"%sZ\"))\r\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"sessionId\"] == \"%s\")\r\n"
                + "  |> filter(fn: (r) => r[\"nodeId\"] == \"%s\") \r\n"
                + "  |> filter(fn: (r) => r[\"_field\"] == \"%s\") \r\n"  //or r[\"_field\"] == \"asn\"
                + "  |> filter(fn: (r) => r[\"_time\"] == time(v: %s))"
                ;
        String stopTime = getCurTime();
    	String flux = String.format(sql, startTime, stopTime, measurement, sessionId, nodeId, field, time);
        System.out.println("flux:" + flux);

        return formate1(flux);
    }
    
    private Object formateAndTop(String sql, String field, int top){
    	
      	 QueryApi queryApi = influxDBClient.getQueryApi();
           List<FluxTable> tables = queryApi.query(sql,org);
           
           List<Map<String, Object>> mapList = new ArrayList<>();

           if (!tables.isEmpty()) {
               for (FluxTable table : tables) {
                   List<FluxRecord> records = table.getRecords();
                   
                   for (FluxRecord fluxRecord : records) {
                  	 Map<String, Object> map = new HashMap<>();
                       Map<String, Object> values = fluxRecord.getValues();
                       map.put("sessionId", values.get("sessionId"));
                       map.put("nodeId", values.get("nodeId"));
                       map.put("time", values.get("_time"));
                       map.put(fluxRecord.getField(), fluxRecord.getValue());
                       mapList.add(map);
                   }
               }
           }
           
           Collections.sort(mapList, new Comparator<Map<String, Object>>(){

          		@Override
          		public int compare(Map<String, Object> m1, Map<String, Object> m2) {
          			//return o1.getScore() - o2.getScore();//升序
          			return Integer.parseInt(m2.get(field).toString()) - Integer.parseInt(m1.get(field).toString()); //降序
          		}
          		
          	});
           
           
           
           return mapList.subList(0, mapList.size() > top ? top : mapList.size());
      }
    
    private Map<String, List<Map<String, Object>>> formate0(String sql){
    	
   	 	QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(sql,org);

        Map<String, List<Map<String, Object>>> resulMap = new HashMap<>();

        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                List<Map<String, Object>> mapList = new ArrayList<>();
                for (FluxRecord fluxRecord : records) {
               	 System.out.println(fluxRecord.toString());
                    Map<String, Object> map = new HashMap<>();
                    map.put("value", fluxRecord.getValue());
                    map.put("valueTime", fluxRecord.getTime());//Instant
                    map.put("field", fluxRecord.getField());
                    map.put("values", fluxRecord.getValues());
                    mapList.add(map);
                }
                String name = (String) mapList.get(0).get("field");
                resulMap.put(name, mapList);
            }
        }
        return resulMap;
   }

}
