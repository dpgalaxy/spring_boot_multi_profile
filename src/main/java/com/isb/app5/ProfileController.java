package com.isb.app5;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

	@Value("${spring.config.activate.on-profile}")
	String activeProfile;
	
	@GetMapping("/profile")
	public String getProfile() {
		return "Currently active profile: " + activeProfile;
	}
}
