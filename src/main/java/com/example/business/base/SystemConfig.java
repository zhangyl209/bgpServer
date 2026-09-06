package com.example.business.base;

import lombok.extern.slf4j.Slf4j;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SystemConfig {
	
	/**
     * 系统配置
     */
    private static final Map<String, String> CONFIG_MAP = new ConcurrentHashMap<>();

    /**
     * 初始化系统配置
     */
    public static synchronized void initConfigMap() {
    	Dao dao = com.example.nutz.DBTools.getDao();
    	
    	// 查询配置
    	List<Record> list = dao.query("config", Cnd.where("status","=","1"));
    	if (!list.isEmpty()) {
    		
    		//循环添加到缓存Map中
            for (Record r : list) {
            	if (r.getString("keyword") == null || r.get("value") == null) {
            		continue;
            	}
            	CONFIG_MAP.put(r.getString("keyword"), r.get("value").toString());
            }
    		
    	}
         
    }

    public static void updateConfigMap(String key, String value) {
        //存入缓存
    	CONFIG_MAP.put(key, value);
    }

    public static String getStringValue(String key) {
        String value = CONFIG_MAP.get(key);
        // 获取到为空，则重新初始化
        if (value == null) {
            initConfigMap();
            log.debug("重新加载系统配置");
        }
        value = CONFIG_MAP.get(key);
        if (value == null) {
            log.error("请检查系统配置 == key:{},value:{}", key, value);
//            throw new Exception("系统配置异常");
        }
        return value;
    }

    public static Integer getIntegerValue(String key) {
        String value = CONFIG_MAP.get(key);
        // 获取到的机构为空，则重新初始化
        if (value == null) {
            initConfigMap();
            log.debug("重新加载系统配置");
        }
        value = CONFIG_MAP.get(key);
        if (value == null) {
            log.error("请检查系统配置 == key:{},value:{}", key, value);
//            throw new Exception("系统配置异常");
        }
        
        try {
        	int res = Integer.parseInt(value);
        	return res;
        }catch(Exception e) {
        	return null;
        }
    }


    /**
     * 获取所有配置信息
     *
     * @return Map
     */
    public static Map<String, String> getAll() {
        return CONFIG_MAP;
    }

}
