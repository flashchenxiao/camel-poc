package com.cxpoc.camel.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ws/")
public class IndexController {
	private static Logger logger = LogManager.getLogger(IndexController.class);

	@PostMapping("user")
	public String saveUser(@RequestParam String name) {
		logger.info("  ========  saveUser ===============");
		return "";
	}
}
