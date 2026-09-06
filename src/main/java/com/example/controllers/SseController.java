package com.example.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.Chat;
import com.example.commons.InParamsDb;
import com.example.functions.db.STQuery;
import com.example.nutz.DBTools;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/sse")
public class SseController {
	
	private int count_up_sec=0;

	@Autowired
	DBTools DBTools;
	
	@GetMapping(value="/countUp",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<Object>> countUp() {
		
		return Flux.interval(Duration.ofSeconds(1))
			.map(seq -> Tuples.of(seq, getCountSec()))
			.map(data -> ServerSentEvent.<Object>builder()
					.event("countUp")
					.id(Long.toString(data.getT1()))
					.data(data.getT2().toString())
					.build());
	}
	
	private String getCountSec() {
		if (count_up_sec<6*60*60) {
			
			int h = count_up_sec/(60*60);
			int m = (count_up_sec%(60*60))/60;
			int s = (count_up_sec%(60*60))%60;
			count_up_sec++;
			System.out.println(h+" 小时 "+m+" 分钟 "+s+" 秒");
			return h+"小时 "+m+"分钟 "+s+"秒";
		}
		return "0小时  0分钟 0秒";
	}
	
	@GetMapping(value="/chat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<Object>> chat(@RequestParam String exercise, @RequestParam String me) {
		
		return Flux.interval(Duration.ofSeconds(1))
			.map(seq -> {
//				System.out.println(seq);
				return Tuples.of(seq, getChatList(exercise, me, seq));
				})
			.map(data -> ServerSentEvent.<Object>builder()
					.event("chatList")
					.id(Long.toString(data.getT1()))
					.data(data.getT2().toString())
					.build());
	}
	
	@GetMapping(value="/exata",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<Object>> exata() {
		
		return Flux.interval(Duration.ofSeconds(1))
			.map(seq -> {
//				System.out.println(seq);
				return Tuples.of(seq, getExata());
				})
			.map(data -> ServerSentEvent.<Object>builder()
					.event("exata")
					.id(Long.toString(data.getT1()))
					.data(data.getT2().toString())
					.build());
	}
	
	// 获取全部的
	private String getChatList(String exercise, String me, Long seq) {
		
		Dao dao = DBTools.getDao();
		Condition c1 = Cnd.where("exercise","=",exercise);
		List<Record> results = dao.query("chat", c1);
		
		JSONObject chat = new JSONObject();
		for(int i = 0; i<results.size(); i++) {
			Record r = results.get(i);
			
			if (r.get("room") != null) {
				if (chat.get(r.get("room").toString()) == null) {
					chat.put( r.get("room").toString(), new ArrayList<String>());
				}
				
				List<JSONObject> chatList = (List<JSONObject>) chat.get(r.get("room").toString());
				JSONObject message=new JSONObject();
				
				String author = "";
				if (r.get("username") != null) {
					author = r.get("username").toString();
					if (author.equals(me)) {
						author = "me";
					}
				}
				
				String type = r.get("type") != null ? r.get("type").toString() : null;
				String id =  r.get("id") != null ? r.get("id").toString() : null;
				
				JSONObject data=new JSONObject();
				String text = r.get("text") != null ? r.get("text").toString() : null;
				String datetime = r.get("createtime") != null ? r.get("createtime").toString() : null;
				data.put("text", text);
				data.put("meta", datetime);
				
				message.put("author", author);
				message.put("type", type);
				message.put("id", id);
				message.put("data", data);
				

//				Chat chat = Chat.builder().author(author).type(type).id(id).data(data).build();
//				System.out.println(message.toString());
				chatList.add(message);
	
			}
			
			
		}
		System.out.println(seq);
		System.out.println(chat.toString());
		
		return chat.toString();//org.apache.commons.lang.StringUtils.join(results.toArray(),',');
	}
	
	// 获取增加的
	private String getChatListAdd(String start, Long seq) {
		Dao dao = DBTools.getDao();
		
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

		String starttime = null;
		Condition c1 = null;
		if (seq == 0) {
			starttime = start;
		} else {
			Date now = new Date();
			Calendar calendar = new GregorianCalendar(); 
			calendar.setTime(now); 
			calendar.add(calendar.SECOND,-1); //往前推一秒 
			now=calendar.getTime(); //这个时间就是日期往后推一天的结果 
			starttime = df.format(now);	
		}
		
		System.out.println(seq);
		
		c1 = Cnd.where("createtime",">=",starttime);
		List<Record> results = dao.query("chat", c1);
		
		System.out.println(results.toString());
		
		return results.toString();//org.apache.commons.lang.StringUtils.join(results.toArray(),',');
	}
	
	private String getExata() {
		
		Dao dao = DBTools.getDao();
		
		Record r = dao.fetch("exata", Cnd.where("id","=",1));
		
		return r.toString();
		
	}

}
