package com.example.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

@Component
public class Utils {
	
	public static JSONArray resultSetToJsonArry(ResultSet rs) throws SQLException,JSONException 
    { 
       // json数组 
       JSONArray array = new JSONArray(); 	
       
       if (rs != null) {
    	// 获取列数 
           ResultSetMetaData metaData = rs.getMetaData(); 
           int columnCount = metaData.getColumnCount(); 
           // 遍历ResultSet中的每条数据 
            while (rs.next()) { 
                JSONObject jsonObj = new JSONObject(); 
                // 遍历每一列 
                for (int i = 1; i <= columnCount; i++) { 
                	String columnName =metaData.getColumnLabel(i); 
                	String value = rs.getString(columnName); 
                	jsonObj.put(columnName, value); 
                }  
     
                array.add(jsonObj);  
     
            } 
       }
       
        return array;
    }
	
	/**
	 * 读取配置文件
	 * @param key
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static String getValue(String key) throws FileNotFoundException{  
  
//		BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/application.properties"));
		Properties props = new Properties();  
		
		try {  
			props=PropertiesLoaderUtils.loadAllProperties("application.properties");
//		    props.load(br);  
		    return (String) props.get(key);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return null;
	}
	
	
//	/**
//	 * 判断对象是不是undifiened或者null(不对)
//	 * @param key
//	 * @return
//	 */
//	public static boolean checkobjectFieldIsNull(Object o) throws IllegalAccessException {
//		
//		if ( o == null ) {
//			return false;
//		}
//		
//		for(Field f:o.getClass().getDeclaredFields()){
//			f.setAccessible(true);
//			if (f.get(o) == null){
//				continue;
//			}
//			if(f.get(o).equals("null") || f.get(o).equals("undefined")){
//				return true;
//			}
//		}
//		return false;
//		
//	}
//	
	
	public static String getMD5(String path) {
		String md5Hex = null;
		FileInputStream inputStream = null; 
		try {
			inputStream = new FileInputStream(path);
			md5Hex = DigestUtils.md5Hex(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return md5Hex;
	}
	
	//object转list
	public static <T> List<T> objToList(Object obj, Class<T> cla){
		List<T> list = new ArrayList<T>();
    	if (obj instanceof ArrayList<?>) {
	        for (Object o : (List<?>) obj) {
	            list.add(cla.cast(o));
	        }
	        return list;
        }
        return null;
	}

	public static void main(String args[])throws IOException, IllegalAccessException{
		  
//		  String ip = getValue("ndt.exataIp");
//		JSONObject jo = new JSONObject();
//		  System.out.println(checkobjectFieldIsNull(jo.get("test")));
	 }  
}
