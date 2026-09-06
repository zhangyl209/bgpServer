package com.example.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.businesses.EchoUDPClient;
import com.example.businesses.EchoUDPServer;
import com.example.commons.ResponseInfo;
import com.example.functions.db.ConditionCRUD;
import com.example.functions.db.STFetch;
import com.example.functions.db.STInsert;
import com.example.nutz.DBTools;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/udp")
public class UDPController {

	@Autowired
	DBTools DBTools;
	
	
	private static byte synsheadhigh = (byte)0x12;
    private static byte synsheadlow = (byte)0x34;
	
	
	@GetMapping(value = "/cmd")
	public void cmd(@RequestParam(name = "code", defaultValue = "1") int code,@RequestParam(name = "exercise", defaultValue = "1") int exercise) throws IOException {
		Dao dao = DBTools.getDao();
		
		Condition c = Cnd.where("id","=",exercise).and("status","=",1);
		Record r_e = dao.fetch("exercise", c);
		int networkconfigId = r_e.getInt("networkconfig");
		
		Condition cc = Cnd.where("id","=",networkconfigId).and("status","=",1);
		Record r_n = dao.fetch("networkconfig", cc);
		
		String networkconfig = r_n.getString("networkconfig");
		
		byte[] buf = new byte[2048];
		
		buf[0] = synsheadhigh;
		buf[1] = synsheadlow;
		buf[2] = (byte)0x03;
		buf[3] = (byte)code;
		buf[4] = (byte)(exercise>>8);
		buf[5] = (byte)exercise;
		buf[6] = (byte)(networkconfig.length());
		byte[] temp = networkconfig.getBytes();
//		String[] result = Arrays.copyOf(first, first.length + second.length); 
//		System.arraycopy(second, 0, result, first.length, second.length); 
		byte[] result = new byte[7+temp.length];
        System.arraycopy(buf, 0, result, 0, 7);  
        System.arraycopy(temp, 0, result, 7, temp.length);  
        
        new EchoUDPClient().send(result);
		
		return;
		
    }
	
	@GetMapping(path="/script/{id}")
	public String scripte(@PathVariable String id) throws IOException {
		Dao dao = DBTools.getDao();
		
		EchoUDPClient client = new EchoUDPClient();
        
		Condition c = Cnd.where("id","=",id).and("status","=",1);
		Record r_a = dao.fetch("attackscript", c);
		int fileId = r_a.getInt("file");
		
		Condition cc = Cnd.where("id","=",fileId);
		Record r_f = dao.fetch("file", cc);
		
		String filePath = r_f.getString("path");
		
		try {
        	//读取脚本文件
        	BufferedReader in = new BufferedReader(new FileReader(filePath));
            StringBuffer sb;
            
            while (in.ready()) {
                sb = (new StringBuffer(in.readLine()));
                String cmd = sb.toString();
                
                System.out.println(cmd);
    			if (cmd != null && !cmd.equals("")) {
    				
    				byte[] buf = new byte[2048];
    				
    				buf[0] = synsheadhigh;
    				buf[1] = synsheadlow;
    				buf[2] = (byte)0x04;
    				buf[3] = (byte)(cmd.length());//攻击指令长度
    				byte[] temp = cmd.getBytes();
//    				String[] result = Arrays.copyOf(first, first.length + second.length); 
//    				System.arraycopy(second, 0, result, first.length, second.length); 
    				byte[] result = new byte[4+temp.length];
    		        System.arraycopy(buf, 0, result, 0, 4);  
    		        System.arraycopy(temp, 0, result, 4, temp.length);  
    		        
    		        client.send(result);
    			}
            }
            
            
            in.close();
            return "ok";
            
        } catch (Exception e) {
        	return "failed";
        }
		
		
		
    }
	
	@GetMapping(path="/cancel")
	public void cancel() {
		
		EchoUDPServer.getInstance().cancleTasks();
		
    }

	@GetMapping(path="/timer/{exercise}") //exercise id 
	public void timer(@PathVariable String exercise) {
		
		try {
			EchoUDPServer.getInstance().autoSendAttack(exercise);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }


}
