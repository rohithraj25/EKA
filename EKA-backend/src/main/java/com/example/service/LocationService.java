package com.example.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Value("${ipinfo.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getLocations(List<String> ipAddresses) {
        return ipAddresses.stream()
                .map(this::getLocationForSingleIp)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getLocationForSingleIp(String ipAddress) {
        String url = UriComponentsBuilder.fromHttpUrl("https://ipinfo.io/" + ipAddress + "/json")
                                         .queryParam("token", apiKey)
                                         .toUriString();
        return restTemplate.getForObject(url, Map.class);
    }
}
