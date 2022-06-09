package com.core.servicesApiImplementation.methods.response;

import lombok.Data;

import java.util.Map;

@Data
public class DeleteResponse {
    private Map<String,String> args;
    private String data;
    private Map<String,String> files;
    private Map<String,String> form;
    private Map<String,String> headers;
    private Map<String,String> json;
    private String origin;
    private String url;
}
