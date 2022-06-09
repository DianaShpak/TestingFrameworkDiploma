package com.core.servicesApiImplementation.methods;

import com.core.client.SimpleWebClient;
import com.core.client.WebClientManager;
import com.core.servicesApiImplementation.methods.response.*;
import com.epam.reportportal.annotations.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class MethodsClient {
    private final Gson gson = new Gson();
    private final SimpleWebClient simpleWebClient;

    public MethodsClient(String host) {
        WebClientManager webClientManager = new WebClientManager();
        WebClient webClient = webClientManager.defaultWebClient(host);
        this.simpleWebClient = new SimpleWebClient(webClient);
    }

    @Step("METHOD GET")
    public GetResponse get(){
        String rawResponse = simpleWebClient.getRequest(RequestURLs.GET);
        return gson.fromJson(rawResponse, new TypeToken<GetResponse>() {}.getType());
    }

    @Step("METHOD PATCH")
    public PatchResponse patch(){
        String rawResponse = simpleWebClient.pathRequest(RequestURLs.PATCH);
        return gson.fromJson(rawResponse, new TypeToken<PatchResponse>() {}.getType());
    }

    @Step("METHOD DELETE")
    public DeleteResponse delete(){
        String rawResponse = simpleWebClient.deleteRequest(RequestURLs.DELETE);
        return gson.fromJson(rawResponse, new TypeToken<DeleteResponse>() {}.getType());
    }

    @Step("METHOD POST")
    public PostResponse post(){
        String rawResponse = simpleWebClient.postRequest(RequestURLs.POST);
        return gson.fromJson(rawResponse, new TypeToken<PostResponse>() {}.getType());
    }

    @Step("METHOD PUT")
    public PutResponse put(){
        String rawResponse = simpleWebClient.putRequest(RequestURLs.PUT);
        return gson.fromJson(rawResponse, new TypeToken<PutResponse>() {}.getType());
    }

    private static class RequestURLs {
        static final String GET = "/get";
        static final String DELETE = "/delete";
        static final String PATCH = "/patch";
        static final String POST = "/post";
        static final String PUT = "/put";
    }
}
