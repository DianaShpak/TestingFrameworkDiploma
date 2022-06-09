package com.core.servicesApiImplementation.methods.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetResponse {
    private Map<String,String> args;
    private Map<String,String> headers;
    private String origin;
}
