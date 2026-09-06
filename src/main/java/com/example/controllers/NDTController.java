package com.example.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import com.example.commons.ResponseInfo;
import com.example.functions.db.ConditionCRUD;
import com.example.functions.db.STFetch;
import com.example.functions.db.STInsert;
import com.example.nutz.DBTools;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/ndt")
public class NDTController {

	@Autowired
	DBTools DBTools;
	
	static String tablename = "node";
	
	@GetMapping(path="/evaluate/{exercise}")
	public ResponseInfo evaluate(@PathVariable String exercise) {
		Dao dao = DBTools.getDao();
		//查训练中的用户
		Condition c = Cnd.where("exercise","=",exercise).and("username","is not",null).and("status","=",1);
		List<Record> lr = dao.query("nodeuser", c);
		
		for (int i = 0; i < lr.size(); i++) {
			int username = lr.get(i).getInt("username");
			Condition cnd = Cnd.where("exercise","=",exercise).and("username","=",username).and("status","=",1);
			Record r = dao.fetch("score", cnd);
			if (r == null) {
				dao.insert("score", Chain.make("exercise",exercise).add("username", username));
			}
		}
		
		return ResponseInfo.ok();
    }
}
