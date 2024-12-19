package com.hrms.generalsetupservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url= "http://localhost:8080/api/auth")
public interface AuthClient {
	
	@GetMapping("/validate")
    Boolean validateToken(@RequestHeader("Authorization") String token);
}
