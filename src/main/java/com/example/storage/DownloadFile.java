package com.example.storage;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.commons.ResponseInfo;
import com.example.storage.service.FileStorageService;

@Component
public class DownloadFile {
	
	@Autowired
	FileStorageService fileStorageService;
	
//	/**
//	 * 前端文件下载
//	 * @throws UnsupportedEncodingException 
//	 * 
//	 */
//	public ResponseEntity<Resource> download(String uuid, String bucketName) {
//		// 检查参数
//		if (uuid.isBlank() || bucketName.isBlank()) {
//			return new ResponseEntity <> (null, HttpStatus.BAD_REQUEST);
//		}
//		try {
//			// 创建协议头
//			HttpHeaders headers = new HttpHeaders();
//			headers.add("Content-Type", "application/octet-stream");
//			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//			// 根据不同浏览器进行处理
////			if (agent.toLowerCase().contains("firefox")) {
////				headers.add("Content-Disposition", "attachment;fileName==?UTF-8?B?"
////						+ Base64.getEncoder().encodeToString(uuid.getBytes("utf-8")) + "?=");
////			} else {
////				headers.add("Content-Disposition",
////						"attachment;fileName=" + URLEncoder.encode(uuid, "utf-8"));
////			}
//			headers.add("Content-Disposition",
//					"attachment;fileName=" + URLEncoder.encode(uuid, "utf-8"));
//			headers.add("Pragma", "no-cache");
//			headers.add("Expires", "0");
//			headers.add("charset", "utf-8");
//			
//	//		new ByteArrayOutputStream(fileStorageService.getFileStream(uuid, bucketName).toString())
//			// 输出文件
//			Resource resource = new InputStreamResource(fileStorageService.getFileStream(uuid, bucketName));
//			
//			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
//		} catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity <> (null, HttpStatus.BAD_REQUEST);
//		}
//		
//	}
	

	/**
	 * 获取文件流
	 * 
	 */
	public InputStream getFileStream(String bucketName, String objectName) {
		return fileStorageService.getFileStream(objectName, bucketName);
    }
	
	/**
	 * 文件预览（有过期时间限制）
	 * 
	 */
	public ResponseInfo<?> presignedFile(String objectName, Integer expires, String bucketName) {
        return fileStorageService.presignedFile(objectName, expires, bucketName);
    }

}
