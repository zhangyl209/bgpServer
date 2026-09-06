package com.example.routers.handlers;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.business.base.SystemConfig;
import com.example.commons.ResponseInfo;
import com.example.functions.BusinessManager;
import com.example.nutz.DBTools;
import com.example.storage.FileStorage;
import com.example.storage.UploadFile;
import com.example.storage.util.CustomMinioClient;

import io.minio.GetObjectArgs;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(maxAge = 3600)
@Component
@Slf4j
public class FileHandler {
	
	@Autowired
	private FileStorage fileStorage;
	
	@Autowired
	private UploadFile uploadFile;
	
	private static String uploadDirectory;
	
	private final Mono<SecurityContext> context  = ReactiveSecurityContextHolder.getContext();
	
	private Dao dao = DBTools.getDao();
	
	private Mono<User> extractUserSeqIdFromJwtToken(Mono<SecurityContext> context) {
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
          .map(s -> s.getAuthentication().getPrincipal())
          .cast(User.class);
    }
	
	public Mono<ServerResponse> download(ServerRequest request) {
		
		try {
			String uuid = request.pathVariable("uuid");
			if ( ! StringUtils.hasText(uuid) ) {
				return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有uuid参数"));
			}
			
			String bucketName = SystemConfig.getStringValue("minioBucket");
			if ( ! StringUtils.hasText(uuid) ) {
				return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("请配置系统minioBucket参数"));
			}
			
			CustomMinioClient client = fileStorage.getCustomMinioClient();
			InputStream stream = client.getObject(GetObjectArgs.builder()
	    			.bucket(bucketName).object(uuid.substring(0,32)).build());//uuid32位，去掉后面文件后缀
			Resource resource = new InputStreamResource(stream);
			return ServerResponse.ok().header("Content-Type", "application/octet-stream")
					.header("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(uuid, "utf-8"))
					.header("charset", "utf-8")
					.body(BodyInserters.fromResource(resource));
		} catch (Exception e) {
//			e.printStackTrace();
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("没有该文件"));
		}
	
    }
	
	public Mono<ServerResponse> upload(ServerRequest request) {//@RequestPart ("file") Flux<FilePart> file
		if ( uploadDirectory == null ) { 
			uploadDirectory = SystemConfig.getStringValue("uploadDirectory");
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
			if (userRecord != null) {
				String userId = userRecord.getString("id");
				
				return request.body(BodyExtractors.toMultipartData()).map(parts -> {
					Map<String, Part> map = parts.toSingleValueMap();
		            // ‘files’ 为客户端上传文件key
		            FilePart filePart = (FilePart) map.get("file");
		            String filename = filePart.filename();
		           
		            try {
		            	String EncodedFilename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
		            	File file = null;
		            	if (StringUtils.hasText(uploadDirectory)) {
		            		file = new File(uploadDirectory + File.separator + currentTimeMillis + "_" +EncodedFilename).getAbsoluteFile();
		            	} else {
		            		file = Files.createTempFile("museum", EncodedFilename).toFile();
		            	}
		            	
		                filePart.transferTo(file).subscribe(); //一定要加subscribe
		                
		                while (file.length() == 0) {
		                	Thread.sleep(100);
		                }
		                
		        		Chain ch = Chain.make("create_time",new Date());
		        		ch.add("filename", EncodedFilename);
		        		ch.add("filepath", file.getAbsoluteFile());
		        		ch.add("amount", (int)(file.length() / 1024)); //kb
		        		ch.add("account", userId);
		        		ch.add("status", 1);
		        		
//		                //上传到统一存储
//		                String bucketName = SystemConfig.getStringValue("minioBucket");
//		        		if (bucketName.isBlank()) {
//		        			bucketName = "museum";
//		        		}
//		                
//		                ResponseInfo<?> rsp = uploadFile.uploadFile(file.getAbsolutePath(), bucketName);
//		    			if (rsp.getCode() == 100) {
//		    				ch.add("uuid", rsp.getResult().toString());
//		    			} else {
//		    				log.error("上传统一存储错误，无法获得uuid: {}", file.getAbsolutePath());
//		    				ch.add("status",2);
//		    			}
		        		ch.add("uuid", currentTimeMillis);
		        		
		    			dao.insert("upload", ch);
		    			
		                return Long.toString(currentTimeMillis);
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            return "";
				}).map(list -> ResponseInfo.url(list))
				.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));

				
			}
			
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
			
		});
		
//		return request.body(BodyExtractors.toMultipartData()).map(parts -> {
//			Map<String, Part> map = parts.toSingleValueMap();
//            // ‘files’ 为客户端上传文件key
//            FilePart filePart = (FilePart) map.get("file");
//            String filename = filePart.filename();
//           
//            try {
//            	String EncodedFilename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
//            	File file = null;
//            	if (StringUtils.hasText(uploadDirectory)) {
//            		file = new File(uploadDirectory + "/"+EncodedFilename).getAbsoluteFile();
//            	} else {
//            		file = Files.createTempFile("museum", EncodedFilename).toFile();
//            	}
//            	
//                filePart.transferTo(file).subscribe(); //一定要加subscribe
//                
//                //上传到统一存储
//                String bucketName = SystemConfig.getStringValue("minioBucket");
//        		if (bucketName.isBlank()) {
//        			bucketName = "museum";
//        		}
//                
//                ResponseInfo<?> rsp = uploadFile.uploadFile(file.getAbsolutePath(), bucketName);
//    			if (rsp.getCode() == 100) {
//    				ch.add("url", rsp.getResult().toString());
//    			} else {
//    				log.error("上传统一存储错误，无法获得uuid: {}", DrawfilePath);
//    				ch.add("status",2);
//    			}
//                
//                return filePart.filename();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return "";
//		}).map(list -> ResponseInfo.ok(list))
//		.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
    }

	//rib
	public Mono<ServerResponse> parserib(ServerRequest request) {//@RequestPart ("file") Flux<FilePart> file
		if ( uploadDirectory == null ) { 
			uploadDirectory = SystemConfig.getStringValue("uploadDirectory");
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
			if (userRecord != null) {
				String userId = userRecord.getString("id");
				
				return request.body(BodyExtractors.toMultipartData()).map(parts -> {
					Map<String, Part> map = parts.toSingleValueMap();
		            // ‘files’ 为客户端上传文件key
		            FilePart filePart = (FilePart) map.get("file");
		            String filename = filePart.filename();
		           
		            try {
		            	String EncodedFilename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
		            	File file = null;
		            	if (StringUtils.hasText(uploadDirectory)) {
		            		file = new File(uploadDirectory + "/"+ currentTimeMillis + "_" +EncodedFilename).getAbsoluteFile();
		            	} else {
		            		file = Files.createTempFile("museum", EncodedFilename).toFile();
		            	}
		            	
		                filePart.transferTo(file).subscribe(); //一定要加subscribe
		                
		                while (file.length() == 0) {
		                	Thread.sleep(100);
		                }
		                
		                //////===================here===================///////
		                //file为文件对象，需要继续补充解析逻辑
		                List<Record> list = new ArrayList<Record>();  //解析的数据写入list
		                
		                //list中每个对象，可以按照下面的写法，继续补充其他字段
		                Record record = new Record();
		                record.put(".table", "rib");
		                
		                
		                //lit准备好了之后，批量写入数据库，返回数据是另一个接口（已经有了，不用再写）
		                dao.fastInsert(list);
		        
		    			
		                return "ok";
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            return "ok";
				}).map(list -> ResponseInfo.url(list))
				.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));

			}
			
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
			
		});
    }
	
	//route
	public Mono<ServerResponse> parseroute(ServerRequest request) {//@RequestPart ("file") Flux<FilePart> file
		if ( uploadDirectory == null ) { 
			uploadDirectory = SystemConfig.getStringValue("uploadDirectory");
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
			if (userRecord != null) {
				String userId = userRecord.getString("id");
				
				return request.body(BodyExtractors.toMultipartData()).map(parts -> {
					Map<String, Part> map = parts.toSingleValueMap();
		            // ‘files’ 为客户端上传文件key
		            FilePart filePart = (FilePart) map.get("file");
		            String filename = filePart.filename();
		           
		            try {
		            	String EncodedFilename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
		            	File file = null;
		            	if (StringUtils.hasText(uploadDirectory)) {
		            		file = new File(uploadDirectory + "/"+ currentTimeMillis + "_" +EncodedFilename).getAbsoluteFile();
		            	} else {
		            		file = Files.createTempFile("museum", EncodedFilename).toFile();
		            	}
		            	
		                filePart.transferTo(file).subscribe(); //一定要加subscribe
		                
		                while (file.length() == 0) {
		                	Thread.sleep(100);
		                }
		                
		                //////===================here===================///////
		                //file为文件对象，需要继续补充解析逻辑
		                List<Record> list = new ArrayList<Record>();  //解析的数据写入list
		                
		                //list中每个对象，可以按照下面的写法，继续补充其他字段
		                Record record = new Record();
		                record.put(".table", "routeupdate");
		                
		                
		                //lit准备好了之后，批量写入数据库，返回数据是另一个接口（已经有了，不用再写）
		                dao.fastInsert(list);
		        
		    			
		                return "ok";
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            return "ok";
				}).map(list -> ResponseInfo.url(list))
				.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));

			}
			
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
			
		});
    }
	
	//irr
	public Mono<ServerResponse> parseirr(ServerRequest request) {//@RequestPart ("file") Flux<FilePart> file
		if ( uploadDirectory == null ) { 
			uploadDirectory = SystemConfig.getStringValue("uploadDirectory");
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
			if (userRecord != null) {
				String userId = userRecord.getString("id");
				
				return request.body(BodyExtractors.toMultipartData()).map(parts -> {
					Map<String, Part> map = parts.toSingleValueMap();
		            // ‘files’ 为客户端上传文件key
		            FilePart filePart = (FilePart) map.get("file");
		            String filename = filePart.filename();
		           
		            try {
		            	String EncodedFilename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
		            	File file = null;
		            	if (StringUtils.hasText(uploadDirectory)) {
		            		file = new File(uploadDirectory + "/"+ currentTimeMillis + "_" +EncodedFilename).getAbsoluteFile();
		            	} else {
		            		file = Files.createTempFile("museum", EncodedFilename).toFile();
		            	}
		            	
		                filePart.transferTo(file).subscribe(); //一定要加subscribe
		                
		                while (file.length() == 0) {
		                	Thread.sleep(100);
		                }
		                
		                //////===================here===================///////
		                //file为文件对象，需要继续补充解析逻辑
		                List<Record> list = new ArrayList<Record>();  //解析的数据写入list
		                
		                //list中每个对象，可以按照下面的写法，继续补充其他字段
		                Record record = new Record();
		                record.put(".table", "irr");
		                
		                
		                //lit准备好了之后，批量写入数据库，返回数据是另一个接口（已经有了，不用再写）
		                dao.fastInsert(list);
		        
		    			
		                return "ok";
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            return "ok";
				}).map(list -> ResponseInfo.url(list))
				.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));

			}
			
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
			
		});
    }
	
	//roa
	public Mono<ServerResponse> parseroa(ServerRequest request) {//@RequestPart ("file") Flux<FilePart> file
		if ( uploadDirectory == null ) { 
			uploadDirectory = SystemConfig.getStringValue("uploadDirectory");
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
			if (userRecord != null) {
				String userId = userRecord.getString("id");
				
				return request.body(BodyExtractors.toMultipartData()).map(parts -> {
					Map<String, Part> map = parts.toSingleValueMap();
		            // ‘files’ 为客户端上传文件key
		            FilePart filePart = (FilePart) map.get("file");
		            String filename = filePart.filename();
		           
		            try {
		            	String EncodedFilename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
		            	File file = null;
		            	if (StringUtils.hasText(uploadDirectory)) {
		            		file = new File(uploadDirectory + "/"+ currentTimeMillis + "_" +EncodedFilename).getAbsoluteFile();
		            	} else {
		            		file = Files.createTempFile("museum", EncodedFilename).toFile();
		            	}
		            	
		                filePart.transferTo(file).subscribe(); //一定要加subscribe
		                
		                while (file.length() == 0) {
		                	Thread.sleep(100);
		                }
		                
		                //////===================here===================///////
		                //file为文件对象，需要继续补充解析逻辑
		                List<Record> list = new ArrayList<Record>();  //解析的数据写入list
		                
		                //list中每个对象，可以按照下面的写法，继续补充其他字段
		                Record record = new Record();
		                record.put(".table", "roa");
		                
		                
		                //lit准备好了之后，批量写入数据库，返回数据是另一个接口（已经有了，不用再写）
		                dao.fastInsert(list);
		        
		    			
		                return "ok";
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            return "ok";
				}).map(list -> ResponseInfo.url(list))
				.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));

			}
			
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
			
		});
    }
}
