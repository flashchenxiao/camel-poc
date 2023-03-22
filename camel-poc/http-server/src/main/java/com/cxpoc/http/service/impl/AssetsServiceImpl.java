package com.cxpoc.http.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSON;
import com.cxpoc.http.service.AssetsService;
import com.joinfun.camel.server.util.Md5CaculateUtils;

@Service
public class AssetsServiceImpl implements AssetsService {

	@Value("${file.upload.path}")
	private String uploadPath;

	@Override
	public Map<String, String> uploadFile(MultipartFile file, String fileName, HttpServletRequest request)
			throws IOException {
		HashMap<String, String> result = new HashMap<>();

		File uploaderFile = getFile(file);

		String md5 = Md5CaculateUtils.getMD5(uploaderFile);

		result.put("uuid", UUID.randomUUID().toString());
		result.put("hash", md5);
		result.put("fileName", file.getOriginalFilename());
		result.put("data", Base64.getEncoder().encodeToString(JSON.toJSONString(result).getBytes()));
		return result;
	}

	private File getFile(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		String path = uploadPath + File.separator + fileName;

		File dir = new File(uploadPath);
		// 判断是否存在
		if (dir.exists()) {// 存在
			if (!dir.isDirectory()) {// 为目录
				// 先删除文件, 再新建目录
				dir.delete();
				dir.mkdirs();
			}
		} else {// 不存在
			dir.mkdirs();
		}

		File uploaderFile = new File(path);
		file.transferTo(uploaderFile);
		return uploaderFile;
	}

}
