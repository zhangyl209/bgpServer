package com.example.storage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.commons.ResponseInfo;
import com.example.storage.service.FileStorageService;
import com.example.storage.util.StorageUtils;
import com.example.utils.Utils;
import com.google.common.collect.Sets;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UploadFile {
	
	@Autowired
	FileStorageService fileStorageService;

	/**
	 * 文件上传
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 上传结果
	 */
	public ResponseInfo<?> uploadFile(String filePath, String bucketName) {

		// 源文件
		File sourceFile = new File(filePath);
		String md5 = Utils.getMD5(filePath);
		// 块文件大小 10M
		long chunkFileSize = 10485760; //10 * 1024 * 1024
		if (sourceFile.exists()) {
			//大文件
			if (sourceFile.length() > chunkFileSize) {
				return this.uploadBigFile(filePath, md5, bucketName);
			} 
			//小文件
			else {
				return this.uploadSmallFile(filePath, md5, bucketName);
			}
		} else {
			return ResponseInfo.not("文件不存在：" + filePath); 
		}

		
	}
	
	/**
	 * 小文件上传
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 上传结果
	 */
	public ResponseInfo<?> uploadSmallFile(String filePath, String md5, String bucketName) {
		File sourceFile = new File(filePath);
		return fileStorageService.uploadFile(StorageUtils.getMultipartFile(sourceFile), md5, bucketName);
	}
	
	/**
	 * 大文件上传
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 上传结果
	 */
	public ResponseInfo<?> uploadBigFile(String filePath, String md5, String bucketName) {
		try {
			List<File> files = this.chunkFile(filePath);
			//总分片数
			int chunks = files.size();
			if (files != null && files.size() > 0) {
				int chunk = 0;
				for (File file : files) {
					ResponseInfo<?> rsp = fileStorageService.uploadFile(StorageUtils.getMultipartFile(file), md5.concat("-").concat(Integer.toString(chunk)), bucketName);
					if (rsp.getCode() == 100) {
						log.info("{} 上传成功", file.getName());
						// 删除分块文件
						file.delete();
						chunk++;
					} else {
						log.error("{} 上传失败", file.getName());
						return ResponseInfo.not("failed");
					}
				}
			}
			
			 //判断是否全部上传完成
	        if(isUploadComplete(md5, chunks, bucketName)) {
	            //合并文件
	        	ResponseInfo<?> rsp = fileStorageService.mergeObjectsByPrefix(md5, chunks, bucketName);
	        	if (rsp.getCode() == 100) {
	        		// 删除所有的分片文件
	        		fileStorageService.deleteObjectsByPrefix(md5, chunks, bucketName);
		            log.info("完成上传");
	        	}
	        	return ResponseInfo.ok(rsp.getResult());
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseInfo.not("failed");

    }
	
	/**
	 * 切割文件
	 * 
	 */
	public List<File> chunkFile(String filePath) throws IOException {
		List<File> retValue = new ArrayList<File>();
		// 源文件
		File sourceFile = new File(filePath);
		// 块文件目录
		String chunkFileFolder = sourceFile.getParent() + File.separator + "chunks" + File.separator;
		File directory = new File(chunkFileFolder);
		if (directory.exists() || directory.mkdirs()) {
			// 块文件大小 10M
			long chunkFileSize = 10485760; //10 * 1024 * 1024
			
			long sourcelength = sourceFile.length();
			// 块数
			long chunkFileNum = (long) Math.ceil(sourcelength / (double)chunkFileSize);
			// 创建读文件的对象
			RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
			// 缓冲区
			byte[] b = new byte[1024];
			for (int i = 0; i < chunkFileNum; i++) {
				// 块文件
				File chunkFile = new File(chunkFileFolder + sourceFile.getName() + "." + i);
				if (chunkFile.exists()) {
					chunkFile.delete();
				}
				// 创建向块文件的写对象
				RandomAccessFile raf_write = new RandomAccessFile(chunkFile, "rw");
				int len = -1;
				while ((len = raf_read.read(b)) != -1) {
					raf_write.write(b, 0, len);
					// 如果块文件的大小达到 10M开始写下一块儿
					if (chunkFile.length() >= chunkFileSize) {
						break;
					}
				}
				raf_write.close();
				retValue.add(chunkFile);
			}
			raf_read.close();
		}

		return retValue;
	}	
 
    /**
     * 是否上传完成
	 *
     */
    private boolean isUploadComplete(String md5,int totalPieces, String bucketName){
    	try {
    		ResponseInfo<?> rsp = fileStorageService.queryObjectsByPrefix(md5, bucketName);
            Iterable<Result<Item>> results = (Iterable<Result<Item>>) rsp.getResult();
            Set<String> objectNames = Sets.newHashSet();
            for (Result<Item> item : results) {
                objectNames.add(item.get().objectName());
            }
            return objectNames.size()==totalPieces;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }     
    }

}
