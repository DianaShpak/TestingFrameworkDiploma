package com.core.client.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ResponseDTO {
    private Map<String, String> headersMap;
    private String location;
    private Integer status;
    private String body;
}
