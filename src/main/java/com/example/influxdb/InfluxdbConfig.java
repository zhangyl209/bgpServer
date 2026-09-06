package com.example.influxdb;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.business.base.SystemConfig;
import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;

/**
 * 时序数据库配置
 */
@Configuration
public class InfluxdbConfig {

    private static String influxDBUrl;
    private static String token;
    private static WriteApi writeApi;

    @Bean
    public InfluxDBClient influxDBClient() {
    	
    	if ( influxDBUrl == null ) { 
    		influxDBUrl = SystemConfig.getStringValue("influxDBUrl");
		}
    	if ( token == null ) { 
    		token = SystemConfig.getStringValue("influxDBToken");
		}
    	
    	if (influxDBUrl != null && token != null) {
    		InfluxDBClient influxDBClient = InfluxDBClientFactory.create(influxDBUrl, token.toCharArray());
            influxDBClient.setLogLevel(LogLevel.BASIC);
            return influxDBClient;
    	}
        return null;
    }
    
    static {
    	WriteOptions writeOptions = WriteOptions.builder()
                .batchSize(5000)
                .flushInterval(1000)
                .bufferLimit(10000)
                .jitterInterval(1000)
                .retryInterval(5000)
                .build();
    	
    	if ( influxDBUrl == null ) { 
    		influxDBUrl = SystemConfig.getStringValue("influxDBUrl");
		}
    	if ( token == null ) { 
    		token = SystemConfig.getStringValue("influxDBToken");
		}
    	
    	if (influxDBUrl != null && token != null) {
    		InfluxDBClient influxDBClient = InfluxDBClientFactory.create(influxDBUrl, token.toCharArray());
            influxDBClient.setLogLevel(LogLevel.BASIC);
            
            writeApi = influxDBClient.makeWriteApi(writeOptions);
    	}
    	
    	     
    }
    
    public static WriteApi getWriteApi(){
      
      return writeApi;
  }
}
