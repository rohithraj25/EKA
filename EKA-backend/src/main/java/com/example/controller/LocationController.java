
package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.config.NetworkUtils;

@RestController
@RequestMapping("/location")
public class LocationController {

	 @Value("${spring.application.name}")
	    private String applicationName;

	    @Value("${ipinfo.api.key}")
	    private String apiKey;

	    @GetMapping("/ip")
	    public ModelAndView getIpAddress(RedirectAttributes redirectAttributes) {
	        String ipAddress = NetworkUtils.getLocalIpAddress();
	        redirectAttributes.addAttribute("ip", ipAddress);
	        return new ModelAndView(new RedirectView("/location"));
	    }

	    @GetMapping("/testing-location-service")
	    public String getIpAddress1() {
	        return "LOCATION SERVICE STARTED";
	    }

	    @GetMapping("/location")
	    public String getLocation(@RequestParam String ip) {
	        String apiUrl = "https://ipinfo.io/" + ip + "?token=" + apiKey;

	        RestTemplate restTemplate = new RestTemplate();
	        String response = restTemplate.getForObject(apiUrl, String.class);

	        return response;
	    }

	    public String fetchLocationForIp(String ip) {
	        String apiUrl = "https://ipinfo.io/" + ip + "?token=" + apiKey;

	        RestTemplate restTemplate = new RestTemplate();
	        return restTemplate.getForObject(apiUrl, String.class);
	    }
	}
