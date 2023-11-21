package com.javadeveloper.advancequeue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadeveloper.advancequeue.service.AQConsumerService;

@RestController
public class AdvanceQueueController {
	
	@Autowired
	AQConsumerService serivce;
	
	@GetMapping("/consume-message/")
	public void displayProperties() {
		serivce.consumeMessage();
	}
}
