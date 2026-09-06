package com.example.storage.service.impl;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.commons.ResponseInfo;
import com.example.storage.FileStorage;
import com.example.storage.service.FileStorageService;
import com.example.storage.util.CustomMinioClient;

import io.minio.BucketExistsArgs;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;

@Service
public class FileStorageServiceImpl implements FileStorageService {
	
	private static final int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;
	
	@Autowired
	FileStorage fileStorage;
	
	private CustomMinioClient client;

	@Override
	public ResponseInfo<?> uploadFile(MultipartFile file, String minioFileName, String bucketName) {
		// TODO Auto-generated method stub
        try {
        	// 判断上传文件是否为空
            if (null == file || 0 == file.getSize()) {
            	return ResponseInfo.not("上传文件不能为空");
            }
            
            client = fileStorage.getCustomMinioClient();
            // 判断存储桶是否存在
            if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build()))  {
            	return ResponseInfo.not(bucketName + "不存在");
            }
            // 开始上传
            ObjectWriteResponse owr = client.putObject(PutObjectArgs.builder()
            		.bucket(bucketName)
            		.object(minioFileName)
            		.stream(file.getInputStream(), file.getSize(), -1)
            		.contentType("application/octet-stream")//file.getContentType() 解决大于5M文件限制
            		.build());
            return ResponseInfo.ok(minioFileName);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseInfo.not("failed");
        }
	}

	@Override
	public ResponseInfo<?> queryObjectsByPrefix(String prefix, String bucketName) {
		// TODO Auto-generated method stub
		try {
			client = fileStorage.getCustomMinioClient();
			
			Iterable<Result<Item>> results = client.listObjects(
	                ListObjectsArgs.builder().bucket(bucketName)
	                        .prefix(prefix.concat("-")).build());
            return ResponseInfo.ok(results);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseInfo.not("failed");
        }
	}

	@Override
	public ResponseInfo<?> mergeObjectsByPrefix(String prefix, int totalPieces, String bucketName) {
		// TODO Auto-generated method stub
		try {
			client = fileStorage.getCustomMinioClient();
			
			List<ComposeSource> sourceObjectList = Stream.iterate(0, i -> ++i)
	                .limit(totalPieces)
	                .map(i -> ComposeSource.builder()
	                        .bucket(bucketName)
	                        .object(prefix.concat("-").concat(Integer.toString(i)))
	                        .build())
	                .collect(Collectors.toList());
	 
			ObjectWriteResponse owr = client.composeObject(
	                ComposeObjectArgs.builder()
	                        .bucket(bucketName)
	                        .object(prefix)
	                        .sources(sourceObjectList)
	                        .build());
            return ResponseInfo.ok(prefix);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseInfo.not("failed");
        }
		
	}

	@Override
	public ResponseInfo<?> deleteObjectsByPrefix(String prefix, int totalPieces, String bucketName) {
		// TODO Auto-generated method stub
		try {
			client = fileStorage.getCustomMinioClient();
			
			List<DeleteObject> delObjects = Stream.iterate(0, i -> ++i)
	                .limit(totalPieces)
	                .map(i -> new DeleteObject(prefix.concat("-").concat(Integer.toString(i))))
	                .collect(Collectors.toList());
	        
			 Iterable<Result<DeleteError>> results = client.removeObjects(
	                RemoveObjectsArgs.builder().bucket(bucketName)
	                        .objects(delObjects).build());
			 for (Result<DeleteError> result : results) {
				   DeleteError error = result.get();
				   System.out.println(
				       "Error in deleting object " + error.objectName() + "; " + error.message());
				 }
            return ResponseInfo.ok(results);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseInfo.not("failed");
        }
		
		
	}

	@Override
	public boolean bucketExists(String bucketName) {
		boolean flag = false;
		
		try {
			client = fileStorage.getCustomMinioClient();
			
			flag = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            return flag;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
	}

	@Override
	public ResponseInfo<?> presignedFile(String objectName, Integer expires, String bucketName) {
		try {
			boolean flag = this.bucketExists(bucketName);
	        if (flag) {
	            if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
	                return ResponseInfo.not("expires must be in range of 1 to " + DEFAULT_EXPIRY_TIME);
	            }
	            
	            client = fileStorage.getCustomMinioClient();
	            String url = client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
	            		.method(Method.GET).bucket(bucketName).object(objectName).expiry(expires).build());
	            
	            return ResponseInfo.ok(url);
	        }
	        return ResponseInfo.not("bucket不存在：" + bucketName);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseInfo.not("创建minio分享失败：" + e.getMessage());
        }
		
	}
	
	@Override
	public InputStream getFileStream(String objectName, String bucketName) {
		
		try {
			boolean flag = bucketExists(bucketName);
			
			client = fileStorage.getCustomMinioClient();
	        if (flag) {
	        	
	        	try (InputStream stream = client.getObject(GetObjectArgs.builder()
	        			.bucket(bucketName).object(objectName).build())) {
	        		   // Read data from stream
	        			return stream;
	        	} catch (Exception e) {
	            	e.printStackTrace();
	            	return null;
	            }
	        }
	        return null;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

}
