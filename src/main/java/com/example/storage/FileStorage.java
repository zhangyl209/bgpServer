package com.example.storage;

import org.springframework.stereotype.Component;

import com.example.business.base.SystemConfig;
import com.example.storage.util.CustomMinioClient;

import io.minio.MinioClient;

@Component
public class FileStorage {
	
	private CustomMinioClient customMinioClient;
	
	private String endpoint;

    private String accesskey;

    private String secretkey;
	
	public FileStorage() throws Exception {
	
        this.endpoint = SystemConfig.getStringValue("minioEndpoint");
        this.accesskey = SystemConfig.getStringValue("minioAccesskey");
        this.secretkey = SystemConfig.getStringValue("minioSecretKey");
        
        MinioClient minioClient = MinioClient.builder()
                .endpoint(this.endpoint)
                .credentials(this.accesskey, this.secretkey)
                .build();
        customMinioClient = new CustomMinioClient(minioClient);

    }
	
	 public CustomMinioClient getCustomMinioClient(){
		 return this.customMinioClient;
   }
	
    

}
