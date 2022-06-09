package com.core.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.MultiValueMap;

@Data
@AllArgsConstructor
public class ResponseWithCookiesDTO {
    private String response;
    private MultiValueMap<String, String> cookies;

}