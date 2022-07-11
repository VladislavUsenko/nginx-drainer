package com.demo.demo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PeerServiceImpl implements PeerService{

    String baseUrl = "https://demo.nginx.com/api/7/http/upstreams/";

    Logger logger = LoggerFactory.getLogger(PeerServiceImpl.class);

    @Override
    public List<Peer> getAllPeers() {

        List<Peer> result = null;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Upstream> response = restTemplate.getForEntity(baseUrl, Upstream.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            logger.info(response.getBody().getPeers().toString());
            result = response.getBody().getPeers();
        }
        return result;
    }

    @Override
    public void downPeer(Peer peer, String upstreamName) {
        
        String url = baseUrl + upstreamName + "/servers/" + peer.getId();
        logger.info("Call down Peer");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);

        Map<String, Object> map = new HashMap<>();
        map.put("down", true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity , String.class);

        logger.info(response.getBody().toString());
    }

    @Override
    public void drainPeer(Peer peer, String upstreamName) {

        String url = baseUrl + upstreamName + "/servers/" + peer.getId();

        logger.info("Call drain Peer");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);

        Map<String, Object> map = new HashMap<>();
        map.put("drain", true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity , String.class);

        logger.info(response.getBody().toString());
    }

    @Override
    public Upstream getUpstream(String name) {

        Upstream result = null;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Upstream> response = restTemplate.getForEntity(baseUrl + name, Upstream.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            logger.info(response.getBody().toString());
            result = response.getBody();
        }
        return result;
    }
    
}
