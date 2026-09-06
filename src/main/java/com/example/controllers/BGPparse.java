package com.example.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import java.io.BufferedReader;
import java.io.FileReader;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.ibatis.jdbc.Null;
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

public class BGPparse {
	
	private static String uploadDirectory;
	
	private Dao dao = DBTools.getDao();
	
	public void parserMRTFormatFile(String inputFileName) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			// 在这里设置要执行的命令
			processBuilder.command("C:\\src\\bgpkit-parser\\target\\release\\bgpkit-parser.exe", "--pretty", inputFileName);

			Process process = processBuilder.start();

			// 获取命令的输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			int cnt = 0;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\\|"); // 使用双反斜杠转义

				if(parts.length >= 12)
				{
					cnt += 1;
					String Type = parts[0];

					long timestampLong = Long.parseLong(parts[1]); // 字符串转long型
					// 将时间戳转换为 LocalDateTime
					LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampLong), ZoneId.systemDefault());
					// 定义日期时间格式化器
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					// 格式化日期时间并输出
					String Timestamp = dateTime.format(formatter);

					String PeerIp = parts[2];
					String PeerAsn = parts[3];
					String Prefix = parts[4];
					String AsPath = parts[5];
					String Orgin = parts[6];
					String NextHop = parts[7];
					String LocalPref = parts[8];
					String Med = parts[9];
					String Communities = parts[10];
					Boolean Atomic = Boolean.parseBoolean(parts[11]);

					// 获取 MD5 消息摘要实例
					MessageDigest md = MessageDigest.getInstance("MD5");
					// 计算 MD5 的字节数组
					byte[] hashInBytes = md.digest(line.getBytes());
					// 将字节数组转换为十六进制格式的字符串
					StringBuilder sb = new StringBuilder();
					for (byte b : hashInBytes) {
						sb.append(String.format("%02x", b));
					}
					String Md5sum = sb.toString();

					Record ribRecord = new Record();
					ribRecord.set("Type", Type);
					ribRecord.set("Timestamp", Timestamp);
					ribRecord.set("PeerIp", PeerIp);
					ribRecord.set("PeerAsn", PeerAsn);
					ribRecord.set("Prefix", Prefix);
					ribRecord.set("AsPath", AsPath);
					ribRecord.set("Orgin", Orgin);
					ribRecord.set("NextHop", NextHop);
					ribRecord.set("LocalPref", LocalPref);
					ribRecord.set("Med", Med);
					ribRecord.set("Communities", Communities);
					ribRecord.set("Atomic", Atomic);
					ribRecord.set("Md5sum", Md5sum);

					dao.insert("rib", Chain.from(ribRecord));
					if(cnt > 2000)
						break;
				}
				else {
					continue;
				}


			}

			// 等待命令执行完成
			int exitCode = process.waitFor();
			System.out.println("Exited with code: " + exitCode);
			System.out.println("Parser RIB OR RouteUpdate MRT Finished.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parserCSVFormatFile4IRR(String inputFileName) {
		try {
			// 获取命令的输出
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
			String line;
			line = reader.readLine();
			int cnt = 0;
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("[\"]", "");
				String[] parts = line.split(","); // 使用双反斜杠转义

				if(parts.length >= 9)
				{
					cnt += 1;
					String IpPrefix = parts[0];
					String Asn = parts[1];
					String LastModified = parts[2];
					String LastUpdateTime = parts[3];
					String DataSource = parts[4];
					String CollectId = parts[5];
					String Source = parts[6];
					String Descr = parts[7];
					String OvState = parts[8];

					// 获取 MD5 消息摘要实例
					MessageDigest md = MessageDigest.getInstance("MD5");
					// 计算 MD5 的字节数组
					byte[] hashInBytes = md.digest(line.getBytes());
					// 将字节数组转换为十六进制格式的字符串
					StringBuilder sb = new StringBuilder();
					for (byte b : hashInBytes) {
						sb.append(String.format("%02x", b));
					}
					String Md5sum = sb.toString();

					Record irrRecord = new Record();
					irrRecord.set("IpPrefix", IpPrefix);
					irrRecord.set("Asn", Asn);
					irrRecord.set("LastModified", LastModified);
					irrRecord.set("LastUpdateTime", LastUpdateTime);
					irrRecord.set("DataSource", DataSource);
					irrRecord.set("CollectId", CollectId);
					irrRecord.set("Source", Source);
					irrRecord.set("Descr", Descr);
					irrRecord.set("OvState", OvState);
					irrRecord.set("Md5sum", Md5sum);

					dao.insert("irr", Chain.from(irrRecord));

					if(cnt >= 2000)
						break;
				}
				else {
					continue;
				}

			}

			System.out.println("Parser  IRR CSV Finished.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parserCSVFormatFile4ROA(String inputFileName) {
		try {
			// 获取命令的输出
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
			String line;
			line = reader.readLine();
			int cnt = 0;
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("[\"]", "");
				String[] parts = line.split(","); // 使用双反斜杠转义

				if(parts.length >= 4)
				{
					cnt += 1;
					String Asn = parts[0];
					String IPPrefix = parts[1];
					int MaxLength = Integer.parseInt(parts[2]);
					String TrustAnchor = parts[3];

					// 获取 MD5 消息摘要实例
					MessageDigest md = MessageDigest.getInstance("MD5");
					// 计算 MD5 的字节数组
					byte[] hashInBytes = md.digest(line.getBytes());
					// 将字节数组转换为十六进制格式的字符串
					StringBuilder sb = new StringBuilder();
					for (byte b : hashInBytes) {
						sb.append(String.format("%02x", b));
					}
					String Md5sum = sb.toString();

					Record roaRecord = new Record();
					roaRecord.set("Asn", Asn);
					roaRecord.set("IPPrefix", IPPrefix);
					roaRecord.set("MaxLength", MaxLength);
					roaRecord.set("TrustAnchor", TrustAnchor);
					roaRecord.set("Md5sum", Md5sum);

					dao.insert("roa", Chain.from(roaRecord));
					if(cnt >= 2000)
						break;
				}
				else {
					continue;
				}

			}

			System.out.println("Parser ROA CSV Finished.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
