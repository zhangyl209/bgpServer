package com.example.storage.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.example.commons.ResponseInfo;

public interface FileStorageService {
	
	public ResponseInfo<?> uploadFile(MultipartFile file, String minioFileName, String bucketName);
	
	public ResponseInfo<?> queryObjectsByPrefix(String prefix, String bucketName);
	
	public ResponseInfo<?> mergeObjectsByPrefix(String prefix, int totalPieces, String bucketName);
	
	public ResponseInfo<?> deleteObjectsByPrefix(String prefix, int totalPieces, String bucketName);
	
	public boolean bucketExists(String bucketName);
	
	//预览文件
	public ResponseInfo<?> presignedFile(String objectName, Integer expires, String bucketName);
	
	public InputStream getFileStream(String objectName, String bucketName);
}
