package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.example.business.base.SystemConfig;
import com.example.businesses.EchoUDPServer;
import com.example.commons.ResponseInfo;
import com.example.nutz.DBTools;
import com.example.utils.Utils;

import reactor.core.publisher.Flux;

@Component
public class StartService implements CommandLineRunner{

	Dao dao = DBTools.getDao();
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//从配置文件中获取exata的ip和port
		String serverOnline = SystemConfig.getStringValue("serverOnline");
		String httpUrl = "http://" + serverOnline + "/v1/health/service/BgpClusterManage?dc=dc1";
		HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuffer result = new StringBuffer();
        try {
            //创建连接
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //设置连接超时时间
            connection.setReadTimeout(15000);
            //开始连接
            connection.connect();
            //获取响应数据
            if (connection.getResponseCode() == 200) {
                //获取返回的数据
                is = connection.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String temp = null;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                    }
                }
        		
        		List<Map<String,Object>> list = JSONArray.parseObject(result.toString(),List.class);
        		List<String> ips = new ArrayList<String>();
    			for (int i = 0; i < list.size(); i++) {
    				Map<String,Object> map = list.get(i);
    				Map<String, Object> jo = (Map<String, Object>) map.get("Service");
    				String ip = "'" + jo.get("Address").toString() + "'";
    				
    				ips.add(ip);
    			}
    			
    			String ipsString = ips.stream().map(String::valueOf).collect(Collectors.joining(","));
    			if (StringUtils.hasText(ipsString) && ipsString.length() > 0) {
    				int i = dao.update("mserver", Chain.make("isworking", 1), Cnd.where("ip", "in", ipsString));
        			int j = dao.update("mserver", Chain.make("isworking", 0), Cnd.where("ip", "not in", ipsString));
        			
        			System.out.println("共有" + i + "台服务器在线！");
        			System.out.println("共有" + j + "台服务器不在线！");
    			} else {
    				dao.update("mserver", Chain.make("isworking", 0), Cnd.where("1", "=", "1"));
    				System.out.println("没有服务器在线！");
    			}
    			
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭远程连接
            connection.disconnect();
        }
	}
	
	/*
	 * 分配节点到服务器
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> getServerStatus() {
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
 
}