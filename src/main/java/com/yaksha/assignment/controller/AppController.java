package com.yaksha.assignment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppController {

	private final RestTemplate restTemplate;

	public AppController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/fetchData")
	public String fetchDataFromApi(@RequestParam String apiUrl) {
		String response = restTemplate.getForObject(apiUrl, String.class);
		return response;
	}
}
