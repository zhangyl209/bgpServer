////File file = new File("D://abc.zip");
////        response.setContentType("application/octet-stream;charset=utf-8");
////        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(FileUtil.getName(file), "UTF-8"));
////        FileInputStream fileInputStream = new FileInputStream(file);
////        int len = 0;
////        byte[] buffer = new byte[1024];
////        OutputStream outputStream = response.getOutputStream();
////        while((len=fileInputStream.read(buffer))>0) {
////            outputStream.write(buffer, 0, len);
////        }
////        fileInputStream.close();
////        outputStream.flush();
////        outputStream.close();
//
//package com.example.controllers;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.channels.AsynchronousFileChannel;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//import java.util.zip.ZipOutputStream;
//
//import org.nutz.dao.Chain;
//import org.nutz.dao.Cnd;
//import org.nutz.dao.Condition;
//import org.nutz.dao.Dao;
//import org.nutz.dao.entity.Record;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ZeroCopyHttpOutputMessage;
//import org.springframework.http.codec.multipart.FilePart;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.example.commons.ResponseInfo;
//import com.example.functions.db.ConditionCRUD;
//import com.example.functions.db.STFetch;
//import com.example.functions.db.STInsert;
//import com.example.nutz.DBTools;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@CrossOrigin(maxAge = 3600)
//@RestController
//@RequestMapping(value = "/f")
//public class FileControllerNdt {
//
//	@Autowired
//	DBTools DBTools;
//	
//	static String tablename = "node";
//	
//	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Mono<String> requestBodyFlux(@RequestPart("file") FilePart filePart) throws IOException {
//        System.out.println(filePart.filename());
//        String filename = filePart.filename();
//        int start = filename.lastIndexOf(".");
//        Path tempFile = Paths.get("d:\\uploadtest\\" + new Date().getTime() + filename.substring(start));//Files.createTempFile("test", filePart.filename());
//        tempFile.toFile().createNewFile();
////        Files.createFile(tempFile);
////        Path tempFile = Files.createTempFile("test", filePart.filename());
//        //NOTE 方法一
//        AsynchronousFileChannel channel =
//                AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);
//        DataBufferUtils.write(filePart.content(), channel, 0)
//                .doOnComplete(() -> {
//                    System.out.println("finish");
//                    //存数据库
//                    try {
//						channel.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//                })
//            .subscribe();
// 
//        //NOTE 方法二
////        filePart.transferTo(tempFile.toFile());
// 
//        System.out.println(tempFile.toString());
//        String storageFile = tempFile.toString();
//        String str = storageFile.replaceAll("\\\\", "\\\\\\\\");
//        return Mono.just(str);
//    }
//	
//	@GetMapping(value = "/download")
//	public Mono<Void> downloadByWriteWith(@RequestParam String filename, ServerHttpResponse response) throws IOException {
//        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
//        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename);
////        response.getHeaders().setContentType(MediaType.ALL);
// 
////        Resource resource = new ClassPathResource("D://git//function//files//logo.png");
////        File file = resource.getFile();
////        File file = new File("D:\\uploadtest\\" + filename);
//        
//        File file = new File("D:\\uploadtest\\" + filename);
//		
//        return zeroCopyResponse.writeWith(file, 0, file.length());
//    }
//	
//	@GetMapping(value = "/download1")
//	public Mono<Void> downloadByWriteWith1(@RequestParam String filename, ServerHttpResponse response) throws IOException {
//        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
//        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename);
////        response.getHeaders().setContentType(MediaType.ALL);
// 
////        Resource resource = new ClassPathResource("D://git//function//files//logo.png");
////        File file = resource.getFile();
////        File file = new File("D:\\uploadtest\\" + filename);
//        
//        File file = new File(filename);
//		
//        return zeroCopyResponse.writeWith(file, 0, file.length());
//    }
//	
//	@GetMapping(value = "/resolve")
//	public Mono<String> resolve(@RequestParam String fileid){
//		Dao dao = DBTools.getDao();
//		//获取文件路径
//		Condition c = Cnd.where("id","=",fileid);
//		Record file = dao.fetch("file", c);
//		
//		String filePath = file.getString("path");
//		
//		String destDirectory = filePath.substring(0, filePath.lastIndexOf("."));
//		
//		if (!filePath.toLowerCase().endsWith(".zip")) {
//			return Mono.just("不是zip文件");
//		}
//		
//		boolean unzip = unZip(filePath, destDirectory);
//		
//		if (!unzip) {
//			return Mono.just("解压失败");
//		}
//		
//		//获取networkconfig id
//		Condition c1 = Cnd.where("file","=",fileid);
//		Record networkconf = dao.fetch("networkconfig", c1);
//		
//		String networkconfig = networkconf.getString("id");
//		
//		List<String> r = findFileInFolder("\\.config", destDirectory);
//		
//		if (r.size() != 1) {
//			return Mono.just("config文件未找到，或者多于1个");
//		}
//		
//		List<String> nodeList = findFileInFolder("\\.nodes", destDirectory);
//		
//		if (nodeList.size() != 1) {
//			return Mono.just("nodeList文件未找到，或者多于1个");
//		}
//		
//		boolean res = resolveNodeInfos(networkconfig, r.get(0) );
//		
//		if (!res) {
//			return Mono.just("config文件解析失败");
//		}
//		
//		res = resolveJingWeiDu(networkconfig, nodeList.get(0));
//		
//		if (!res) {
//			return Mono.just("node文件解析失败");
//		}
//		
//		return Mono.just("ok");
//        
//    }
//	
//	//遍历文件夹，查找文件
//	public static List<String> findFileInFolder(String regex, String folder) {
//        File file = new File(folder);
//        List<String> fileList = new ArrayList<String>();
//        List<String> deepFileList = new ArrayList<String>();
//        Pattern p = Pattern.compile(regex);
//        
//        if (file.exists()) {
//            File[] files = file.listFiles();
//            if (null != files) {
//                for (File file2 : files) {
//                    if (file2.isDirectory()) {
////                        System.out.println("文件夹:" + file2.getAbsolutePath());
//                        deepFileList = findFileInFolder(regex, file2.getAbsolutePath());
//                    } else {
////                        System.out.println("文件:" + file2.getAbsolutePath());
////                        System.out.println("文件:" + file2.getName());
//                        Matcher m = p.matcher(file2.getName());
//                        if (m.find()) {
//                        	fileList.add(file2.getAbsolutePath());	
//                        }
//                    }
//                }
//            }
//        } else {
//            System.out.println("文件不存在!");
//        }
//        
//        //合并
//        fileList.addAll(deepFileList);
//        
//        return fileList;
//    }
//	
//	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
//	    File destFile = new File(destinationDir, zipEntry.getName());
//
//	    String destDirPath = destinationDir.getCanonicalPath();
//	    String destFilePath = destFile.getCanonicalPath();
//
//	    if (!destFilePath.startsWith(destDirPath + File.separator)) {
//	        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
//	    }
//
//	    return destFile;
//	}
//	
//	//unzip
//	public static boolean unZip(String fileZip, String dest) {
//		try {
//			File destDir = null;
//			if (dest != null) {
//				destDir = new File(dest);
//			} else {
//				destDir = new File(dest);
//			}
//	        
//	        byte[] buffer = new byte[1024];
//	        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
//	        ZipEntry zipEntry = zis.getNextEntry();
//	        while (zipEntry != null) {
//	        	File newFile = newFile(destDir, zipEntry);
//	            if (zipEntry.isDirectory()) {
//	                if (!newFile.isDirectory() && !newFile.mkdirs()) {
//	                    throw new IOException("Failed to create directory " + newFile);
//	                }
//	            } else {
//	                // fix for Windows-created archives
//	                File parent = newFile.getParentFile();
//	                if (!parent.isDirectory() && !parent.mkdirs()) {
//	                    throw new IOException("Failed to create directory " + parent);
//	                }
//	                
//	                // write file content
//	                FileOutputStream fos = new FileOutputStream(newFile);
//	                int len;
//	                while ((len = zis.read(buffer)) > 0) {
//	                    fos.write(buffer, 0, len);
//	                }
//	                fos.close();
//	            }
//	            zipEntry = zis.getNextEntry();
//	        }
//	        zis.closeEntry();
//	        zis.close();
//	        
//	        return true;
//			
//		} catch ( Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		
//    }
//
//	//zip单文件
//	public static boolean zipFile(String sourceFile, String destFile) {
//		try {
//			FileOutputStream fos = new FileOutputStream(destFile);
//	        ZipOutputStream zipOut = new ZipOutputStream(fos);
//	        File fileToZip = new File(sourceFile);
//	        FileInputStream fis = new FileInputStream(fileToZip);
//	        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
//	        zipOut.putNextEntry(zipEntry);
//	        byte[] bytes = new byte[1024];
//	        int length;
//	        while((length = fis.read(bytes)) >= 0) {
//	            zipOut.write(bytes, 0, length);
//	        }
//	        zipOut.close();
//	        fis.close();
//	        fos.close();
//	        
//	        return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//        
//    }
//	
//	//zip多文件
//	public static boolean zipMultiFiles(String[] srcFiles, String destFile){
//		
//		try {
//			FileOutputStream fos = new FileOutputStream(destFile);
//	        ZipOutputStream zipOut = new ZipOutputStream(fos);
//	        for (String srcFile : srcFiles) {
//	            File fileToZip = new File(srcFile);
//	            FileInputStream fis = new FileInputStream(fileToZip);
//	            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
//	            zipOut.putNextEntry(zipEntry);
//
//	            byte[] bytes = new byte[1024];
//	            int length;
//	            while((length = fis.read(bytes)) >= 0) {
//	                zipOut.write(bytes, 0, length);
//	            }
//	            fis.close();
//	        }
//	        zipOut.close();
//	        fos.close();
//	        
//	        return true;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		 
//    }
//	
//	//给zip文件夹调用
//	private static void zipDirectoryFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
//        if (fileToZip.isHidden()) {
//            return;
//        }
//        if (fileToZip.isDirectory()) {
//            if (fileName.endsWith("/")) {
//                zipOut.putNextEntry(new ZipEntry(fileName));
//                zipOut.closeEntry();
//            } else {
//                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
//                zipOut.closeEntry();
//            }
//            File[] children = fileToZip.listFiles();
//            for (File childFile : children) {
//            	zipDirectoryFile(childFile, fileName + "/" + childFile.getName(), zipOut);
//            }
//            return;
//        }
//        FileInputStream fis = new FileInputStream(fileToZip);
//        ZipEntry zipEntry = new ZipEntry(fileName);
//        zipOut.putNextEntry(zipEntry);
//        byte[] bytes = new byte[1024];
//        int length;
//        while ((length = fis.read(bytes)) >= 0) {
//            zipOut.write(bytes, 0, length);
//        }
//        fis.close();
//    }
//	
//	//zip文件夹
//	public static boolean zipDirectory(String sourceFile, String destFile) {
//		
//		try {
//			FileOutputStream fos = new FileOutputStream(destFile);
//	        ZipOutputStream zipOut = new ZipOutputStream(fos);
//	        File fileToZip = new File(sourceFile);
//
//	        zipDirectoryFile(fileToZip, fileToZip.getName(), zipOut);
//	        zipOut.close();
//	        fos.close();
//	        
//	        return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		
//    }
//	
//	public static void main(String[] args) {
//		
////		unZip("D:\\uploadtest\\1620359758142.zip", "D:\\uploadtest\\unzip");
////		 List<String> r = findFileInFolder("\\.config", "D:\\uploadtest\\unzip");
////		System.out.println("结果： " + r);
//		
//	}
//	
//	public boolean resolveNodeInfos(String networkconfig, String configFilePath )  {
//		Dao dao = DBTools.getDao();
////		String networkconfig = "1";
//        try {
//        	//读取configname
//        	String configFilename = configFilePath.substring(configFilePath.lastIndexOf("\\") + 1);
//        	String configname = configFilename.substring(0, configFilename.lastIndexOf("."));
//        	dao.update("networkconfig", Chain.make("configname",configname), Cnd.where("id","=",networkconfig));
//        		
//        	
////            BufferedReader in = new BufferedReader(new FileReader("D:\\uploadtest\\unzip\\ConnfigFile\\newScenario.config"));
//        	BufferedReader in = new BufferedReader(new FileReader(configFilePath));
//            StringBuffer sb;
//            
//            BufferedWriter out_NC = new BufferedWriter(new FileWriter(networkconfig + "_NodeConfiguration.txt"));
//            Boolean write_NC = false;
//            String REGEX_NC = "#\\**Node Configuration\\**";
//            Pattern p_NC = Pattern.compile(REGEX_NC);
//            
//            String REGEX_End = "#\\**";
//            Pattern p_End = Pattern.compile(REGEX_End);
//            
//            BufferedWriter out_IC = new BufferedWriter(new FileWriter(networkconfig + "_InterfaceConfiguration.txt"));
//            Boolean write_IC = false;
//            String REGEX_IC = "#\\**Interface Configuration\\**";
//            Pattern p_IC = Pattern.compile(REGEX_IC);
//            
//            while (in.ready()) {
//                sb = (new StringBuffer(in.readLine()));
//                Matcher m_NC = p_NC.matcher(sb);
//                Matcher m_IC = p_IC.matcher(sb);
//                Matcher m_End = p_End.matcher(sb);
//                
//                if (m_End.find()) {
//                	write_NC = false;
//                	write_IC = false;
//                }
//                
//                if (write_NC == true) {
////                	System.out.println(sb);
//                	out_NC.write(sb.toString());
//                	out_NC.newLine();
//                }
//                if (write_IC == true) {
////                	System.out.println(sb);
//                	out_IC.write(sb.toString());
//                	out_IC.newLine();
//                }
//                
//                if (m_NC.find()) {
//                	write_NC = true;
//                }
//                if (m_IC.find()) {
//                	write_IC = true;
//                }
//                
//                
//                
//            }
//            out_NC.flush();
//            out_IC.flush();
//            out_NC.close();
//            out_IC.close();
//            
//            in.close();
//            
//            
//            Map<String,JSONObject> map = new HashMap<String,JSONObject>();
//            
//            resolveNC(map, networkconfig);
//            
//            System.out.println(map);
//            
//            resolveIC(map, networkconfig);
//            
//            System.out.println(map);//{11={"node":"HZ02","ip":["190.0.3.2","190.0.4.2","190.0.7.2","191.0.0.11","192.0.0.11"],"id":"11"}, 12={"node":"Beijing","ip":["191.0.0.12","190.0.15.2","190.0.18.2","190.0.20.2","190.0.22.2"],"id":"12"}, 13={"node":"SanYa","ip":["191.0.0.13","190.0.21.1","190.0.22.1"],"id":"13"}, 14={"node":"Kashi","ip":["191.0.0.14","190.0.14.1","190.0.16.1"],"id":"14"}, 15={"node":"Wulumuqi","ip":["191.0.0.15","190.0.14.2","190.0.15.1"],"id":"15"}, 16={"node":"Kunming","ip":["191.0.0.16","190.0.19.1","190.0.20.1"],"id":"16"}, 17={"node":"Jiamusi","ip":["191.0.0.17","190.0.17.1","190.0.18.1"],"id":"17"}, 18={"node":"Xian","ip":["191.0.0.18","190.0.16.2","190.0.17.2"],"id":"18"}, 19={"node":"Chongqing","ip":["191.0.0.19","190.0.19.2","190.0.21.2"],"id":"19"}, 1={"node":"IGSO01","ip":["190.0.0.1","190.0.1.1","191.0.0.1","192.0.0.1"],"id":"1"}, 2={"node":"IGSO02","ip":["190.0.0.2","190.0.2.1","191.0.0.2","192.0.0.2"],"id":"2"}, 3={"node":"IGSO03","ip":["190.0.1.2","190.0.2.2","191.0.0.3","192.0.0.3"],"id":"3"}, 4={"node":"KD01","ip":["190.0.5.1","190.0.6.1","191.0.0.4","192.0.0.4"],"id":"4"}, 5={"node":"KD02","ip":["190.0.5.2","190.0.11.1","191.0.0.5","192.0.0.5"],"id":"5"}, 6={"node":"KD03","ip":["190.0.4.1","190.0.6.2","191.0.0.6","192.0.0.6"],"id":"6"}, 7={"node":"KQ01","ip":["190.0.8.1","190.0.9.1","191.0.0.7","192.0.0.7"],"id":"7"}, 8={"node":"KQ02","ip":["190.0.8.2","190.0.10.1","191.0.0.8","192.0.0.8"],"id":"8"}, 9={"node":"KQ03","ip":["190.0.7.1","190.0.9.2","191.0.0.9","192.0.0.9"],"id":"9"}, 20={"node":"Usr1","ip":["192.0.0.12"],"id":"20"}, 10={"node":"HZ01","ip":["190.0.3.1","190.0.10.2","190.0.11.2","191.0.0.10","192.0.0.10"],"id":"10"}, 21={"node":"Usr2","ip":["192.0.0.13"],"id":"21"}}
//            
//            //按networkconfig清空
//            Condition c = Cnd.where("networkconfig","=",networkconfig);
//            dao.clear(tablename, c);
//            
//            //直接存储
//            for(JSONObject jo : map.values()){
//            	if (jo.get("id") != null) {
//            		String id = jo.get("id").toString();
//            		
//            		//insert
//            		JSONObject dataMap = new JSONObject();
//    				dataMap.put(".table", tablename);
//    				dataMap.put("nodeid", id);
//    				dataMap.put("virtualip", jo.get("ip").toString());
//    				dataMap.put("node", jo.get("node").toString());
//    				dataMap.put("networkconfig", networkconfig);
//    				dataMap.put("status", 1);
//    				
//    				dao.insert(dataMap);
//         
//            	}
//  	
//                
//            }
//            
////            //根据ip将一条分成多条存储
////            for(JSONObject jo : map.values()){
////            	if (jo.get("id") != null) {
////            		String id = jo.get("id").toString();
////            		
////            		//insert
////            		JSONObject dataMap = new JSONObject();
////    				dataMap.put(".table", tablename);
////    				dataMap.put("nodeid", id);
////    				dataMap.put("node", jo.get("node").toString());
////    				dataMap.put("networkconfig", networkconfig);
////    				dataMap.put("status", 1);
////    				
////            		if (jo.get("ip") != null) {
////                		List<String> ips = (ArrayList<String>) jo.get("ip");
////                		if (ips != null && ips.size() > 0) {
////                        	for (int i = 0 ; i < ips.size() ; i++) {
////                        		dataMap.put("virtualip", ips.get(i));
////                        		dao.insert(dataMap);
//////                        		JSONObject result = dao.insert(dataMap);
//////                        		System.out.println(result);
////                        	}
////                        }
////                	}else { //没有ip
////                    	dao.insert(dataMap);
////                    }
////         
////            	}
////            	
////                
////            }
//            
//        } catch (Exception e) {
//        	return false;
//        }
//        
//        return true;
//        
//    }
//	
//	public static void resolveNC(Map<String,JSONObject> map, String networkconfig) {
//		BufferedReader in = null;
//		try {
//			in = new BufferedReader(new FileReader(networkconfig + "_NodeConfiguration.txt"));
//	        StringBuffer sb;
//	        
//	        String REGEX = "\\[\\d+]";
//            Pattern p = Pattern.compile(REGEX);
//	        
//	        while (in.ready()) {
//                sb = (new StringBuffer(in.readLine()));
//                Matcher m = p.matcher(sb);
//                
//                if (m.find() && m.start() == 0) { //找到行
////                	System.out.println(sb);
//                	String[] sbArray = sb.toString().split("HOSTNAME");
//                	
//                	String a1 = sbArray[0].trim();
//                	String id = a1.substring(1, a1.length()-1);
//                	JSONObject jo = new JSONObject();
//                	jo.put("id", id);
//                	jo.put("node", sbArray[1].trim());
//                	map.put(id, jo);
//                	
//                }
//                
//                
//            }
//	        
//	        in.close();
//	        
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//
//		}
//		
//	}
//	
//	public static void resolveIC(Map<String,JSONObject> map, String networkconfig) {
//		BufferedReader in = null;
//		try {
//			in = new BufferedReader(new FileReader(networkconfig + "_InterfaceConfiguration.txt"));
//	        StringBuffer sb;
//	        
//	        String REGEX = "\\[\\d+]";
//            Pattern p = Pattern.compile(REGEX);
//	        
//	        while (in.ready()) {
//                sb = (new StringBuffer(in.readLine()));
//                Matcher m = p.matcher(sb);
//                
////                [1] NETWORK-PROTOCOL[0]       IP
////                [1] IP-ADDRESS[0]             190.0.0.1
//                if (m.find() && m.start() == 0) { 
////                  [1] IP-ADDRESS[0]             190.0.0.1
//                	if (sb.toString().contains("IP-ADDRESS")) { //找到行
////                		System.out.println(sb);
//                		// [1]       和       [0]             190.0.0.1
//                    	String[] sbArray = sb.toString().split("IP-ADDRESS");
//                    	String a1 = sbArray[0].trim();
//                    	String id = a1.substring(1, a1.length()-1);
//                    	
//                    	String[] ipArray = sbArray[1].split("]");
//                    	String ip = ipArray[1].trim();
//                    	
//                    	JSONObject jo = map.get(id);
//                    	List<String> ips = jo.get("ip") != null ? (ArrayList<String>)jo.get("ip") : new ArrayList<String>();
//                    	ips.add(ip);
//                    	jo.put("ip", ips);
//                	}
//                }
//            }
//	        
//	        in.close();
//	        
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		}
//		
//	}
//
//	public boolean resolveJingWeiDu(String networkconfig, String nodesFilePath) {
//		BufferedReader in = null;
//		Dao dao = DBTools.getDao();
//		
//		try {
//			in = new BufferedReader(new FileReader(nodesFilePath));
//	        StringBuffer sb;
//	       
//	        while (in.ready()) {
//	        	
//	        	sb = new StringBuffer(in.readLine());
//	        	String s = sb.toString();
//	        	int firstSpaceIndex = s.indexOf(" ");
//	        	int leftBracketIndex = s.indexOf("(");
//	        	int rightBracketIndex = s.indexOf(")");
//	        	String nodeId = s.substring(0, firstSpaceIndex);
//	        	String jwd = s.substring(leftBracketIndex+1, rightBracketIndex);
//	        	String[] jsdArray = jwd.split(",");
//	        	String latitude = jsdArray[0].trim();
//	        	String longitude = jsdArray[1].trim();
//	        	String height = jsdArray[2].trim();
//	        	
//	        	dao.update("node", Chain.make("latitude",latitude).add("longitude", longitude).add("height", height), Cnd.where("networkconfig","=",networkconfig).and("nodeid","=",nodeId));
//
//            }
//	        
//	        in.close();
//	        
//			return true;
//		}catch(Exception e) {
//			e.printStackTrace();
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//			return false;
//		}
//		
//	}
//}
