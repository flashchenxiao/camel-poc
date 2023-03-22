package com.cxpoc.http.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;


public interface AssetsService {

    Map<String,String> uploadFile( MultipartFile file, String fileName, HttpServletRequest request) throws IOException;

}
