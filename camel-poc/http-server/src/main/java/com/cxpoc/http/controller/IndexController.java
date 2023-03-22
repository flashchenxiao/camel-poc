package com.cxpoc.http.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cxpoc.http.service.AssetsService;
import com.joinfun.camel.server.util.RetData;
import com.joinfun.camel.server.util.UserMap;

@RestController
@RequestMapping("/v1/home/")
public class IndexController {
	private static Logger logger = LogManager.getLogger(IndexController.class);

	@Autowired
	private AssetsService fileService;

	@PostMapping("uploadFile")
	public RetData<?> uploadFile(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
		logger.info("  ========  uploadFile ===============");
		return RetData.succuess(fileService.uploadFile(file, null, request));
	}

	@GetMapping("user")
	public RetData<?> list(HttpServletRequest request, @RequestParam(required = false) String sev) throws IOException {
		logger.info("  ========  list ===============" + sev);
		return RetData.succuess(UserMap.users, sev);
	}

	@GetMapping("user/{id}")
	public RetData<?> getUser(@PathVariable Integer id) {
		logger.info("  ========  getUser ===============" + id);
		return RetData.succuess(UserMap.users.get(id));
	}

	@PutMapping("user/{id}")
	public RetData<?> updateUser(@PathVariable Integer id, @RequestParam String name) {
		logger.info("  ========  updateUser ===============" + id);
		User u = UserMap.users.get(id);
		u.setName(name);
		return RetData.succuess(UserMap.users.get(id));
	}

	@PostMapping("user")
	public RetData<?> saveUser(@RequestParam String name) {
		logger.info("  ========  saveUser ===============");
		int id = UserMap.users.size() + 1;
		if (id <= 10) {
			User u = new User(id, name);
			UserMap.users.put(id, u);
		}
		return RetData.succuess(UserMap.users.get(id));
	}
}
