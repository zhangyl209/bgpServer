package com.example.storage.util;

import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.errors.*;
import io.minio.messages.Part;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 继承MinioClient，增加分片处理
 * @author maiz
 * @date 2023-11-23
 */
public class CustomMinioClient extends MinioClient {

    public CustomMinioClient(MinioClient client) {
        super(client);
    }
    
    

}
