package com.example.commons;

import com.alibaba.fastjson.JSONObject;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class Chat {
	
	private String author;
	
	private String type;
	
	private String id;

	private boolean isEditd;
	
	private JSONObject data;
	
}
