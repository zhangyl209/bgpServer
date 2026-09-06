package com.example.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.example.business.base.SystemConfig;
import com.example.nutz.DBTools;
import com.example.storage.DownloadFile;

import reactor.core.publisher.Mono;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/f")
public class FileController {

	@Autowired
	DBTools DBTools;
	
	@Autowired
	DownloadFile downloadFile;
	
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> requestBodyFlux(@RequestPart("file") FilePart filePart) throws IOException {
        System.out.println(filePart.filename());
        String filename = filePart.filename();
        int start = filename.lastIndexOf(".");
        Path tempFile = Paths.get("d:\\uploadtest\\" + new Date().getTime() + filename.substring(start));//Files.createTempFile("test", filePart.filename());
        tempFile.toFile().createNewFile();
//        Files.createFile(tempFile);
//        Path tempFile = Files.createTempFile("test", filePart.filename());
        //NOTE 方法一
        AsynchronousFileChannel channel =
                AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);
        DataBufferUtils.write(filePart.content(), channel, 0)
                .doOnComplete(() -> {
                    System.out.println("finish");
                    //存数据库
                    try {
						channel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                })
            .subscribe();
 
        //NOTE 方法二
//        filePart.transferTo(tempFile.toFile());
 
        System.out.println(tempFile.toString());
        String storageFile = tempFile.toString();
        String str = storageFile.replaceAll("\\\\", "\\\\\\\\");
        return Mono.just(str);
    }
	
//	@GetMapping(value = "/download/{uuid}")
//	public ResponseEntity<Resource> downloadByUuid(HttpServletRequest request, @PathVariable String uuid) {
//		String bucketName = SystemConfig.getStringValue("minioBucket");
//		return downloadFile.download(request, uuid, bucketName);
//	}
	
	@GetMapping(value = "/download1")
	public Mono<Void> downloadByWriteWith1(@RequestParam String filename, ServerHttpResponse response) throws IOException {
        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename);
//        response.getHeaders().setContentType(MediaType.ALL);
        
        File file = new File(filename);
		
        return zeroCopyResponse.writeWith(file, 0, file.length());
    }
	
	@GetMapping(value = "/hello")
	public String hello(ServerHttpResponse response) {
        return "hello";
    }
	
	
}
