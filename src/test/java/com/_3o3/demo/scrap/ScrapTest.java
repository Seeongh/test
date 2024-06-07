package com._3o3.demo.scrap;

import com._3o3.demo.api.financial.application.dto.webClientDto.RequestBodyDto;
import com._3o3.demo.api.financial.application.dto.webClientDto.ResponseBodyDto;
import com._3o3.demo.util.WebClientUtil;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
     public void webclient_연동테스트() throws Exception {
         RequestBodyDto dto = RequestBodyDto.builder()
                 .name("동탁")
                         .regNo("921108-1582816")
                                 .build();
         //given
         ResponseBodyDto responseDto = webClientUtil.requestPostAnualFinancialData("Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJhc2QiLCJpYXQiOjE3MTc1MDA4NjIsImV4cCI6MTgwMzkwMDg2Mn0.mnEldpdkjP3PrgA5k7UxMBSgtjFR35pWkgxiOESb9UI", "/scrap",dto);
         //when
         Assertions.assertThat(responseDto.getStatus()).isEqualTo("success");


      }
}
