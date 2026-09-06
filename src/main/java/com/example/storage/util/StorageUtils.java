package com.example.storage.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Component
public class StorageUtils {

	public static MultipartFile getMultipartFile(File file) {
		
        FileItem item = new DiskFileItemFactory().createItem("file"
            , MediaType.MULTIPART_FORM_DATA_VALUE
            , true
            , file.getName());
        
        InputStream input = null;
        OutputStream os = null;
        try {
        	input = new FileInputStream(file);
        	os = item.getOutputStream();
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        } finally {
        	if (input != null) {
        		try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if (os != null) {
        		try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }

        return new CommonsMultipartFile(item);
    }
}
