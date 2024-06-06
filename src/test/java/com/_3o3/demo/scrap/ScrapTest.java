package com._3o3.demo.scrap;

import com._3o3.demo.api.financial.application.dto.webClientDto.RequestBodyDto;
import com._3o3.demo.util.WebClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScrapTest {

    @Autowired
    WebClientUtil webClientUtil;
    @Test
    public void 스크래핑_연동테스트() throws Exception {
        String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJrZXc2OCIsImlhdCI6MTcxNzM0MTY5NSwiZXhwIjoxODAzNzQxNjk1fQ.XKFDfL3ghQ_ADNOWqIPmNtWqZyBYa1aDeZuc0_1zYZ4";
        String requestBody = "{\"name\":\"동탁\", \"regNo\":\"921108-1582816\"}";

        URL url = new URL("https://codetest-v4.3o3.co.kr/scrap");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 요청 메서드 설정
        conn.setRequestMethod("POST");

        // 헤더 설정
        conn.setRequestProperty("Authorization", "Bearer " + authToken);
        conn.setRequestProperty("X-API-KEY", "aXC8zK6puHIf9153L8TiQg==");
        //conn.setRequestProperty("Content-Type", "application/json");

        // 바디 설정
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 응답 코드 확인
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + conn.getResponseMessage());
     }

     @Test
     public void restTemplate_연동테스트() throws Exception {
         String url = "https://codetest-v4.3o3.co.kr/scrap/";
         Map<String, Object> result = new HashMap<String, Object>();
         ResponseEntity<Map> resMap = null;

         // 스크랩 api 호출
         HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
         factory.setConnectTimeout(5000);
         RestTemplate restTemplate = new RestTemplate(factory);
         restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

         HttpHeaders header = new HttpHeaders();
         //header.setContentType(MediaType.APPLICATION_JSON);
         header.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJrZXc2OCIsImlhdCI6MTcxNzM0MTY5NSwiZXhwIjoxODAzNzQxNjk1fQ.XKFDfL3ghQ_ADNOWqIPmNtWqZyBYa1aDeZuc0_1zYZ4");
         header.set("X-API-KEY", "aXC8zK6puHIf9153L8TiQg==");


         MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
         params.put("name", Collections.singletonList("동탁"));
         params.put("regNo", Collections.singletonList("921108-1582816"));

         HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(params, header);

         UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

         resMap = restTemplate.postForEntity(uri.toUri(), entity, Map.class);

         result.put("status", resMap.getStatusCodeValue());
         result.put("header", resMap.getHeaders());
         result.put("result", resMap.getBody());

         System.out.println(resMap.getStatusCode());
         System.out.println(resMap.getHeaders());
         System.out.println(resMap.getBody());
     }

     @Test
     public void webclient_연동테스트() throws Exception {
         RequestBodyDto dto = RequestBodyDto.builder()
                 .name("동탁")
                         .regNo("921108-1582816")
                                 .build();
         //given
         webClientUtil.requestPostAnualFinancialData("Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJhc2QiLCJpYXQiOjE3MTc1MDA4NjIsImV4cCI6MTgwMzkwMDg2Mn0.mnEldpdkjP3PrgA5k7UxMBSgtjFR35pWkgxiOESb9UI", "/scrap",dto);
         //when

         //then

      }
}
