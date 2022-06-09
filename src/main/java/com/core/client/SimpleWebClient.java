package com.core.client;

import com.core.client.DTO.ResponseDTO;
import com.core.client.DTO.ResponseWithCookiesDTO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.core.constants.SymbolsConstants.*;


@Slf4j
public class SimpleWebClient {
    private final WebClient webClient;
    Function<ClientResponse, Mono<? extends Throwable>> error5xx = response -> {
        log.error("5xx error");
        return Mono.error(new RuntimeException("5xx"));
    };
    Function<ClientResponse, Mono<? extends Throwable>> error4xx = response -> {
        log.error("4xx error");
        return Mono.error(new RuntimeException("4xx"));
    };

    public SimpleWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public static String urlEncodeUTF8(Map<?, ?> map) {
        return QUESTION_MARK + mapParamsToString(map);
    }

    static String mapParamsToString(Map<?, ?> params) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<?, ?> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append(AMPERSAND);
            }
            sb.append(String.format("%s=%s",
                    entry.getKey().toString(),
                    entry.getValue().toString()
            ));
        }
        return sb.toString();
    }

    public String getRequest(String path) {
        String response = getRequestWithNoResponseInLog(path);
        log.info("Receive response: " + response);
        return response;
    }

    public String pathRequest(String path) {
        String response = pathRequestWithNoResponseInLog(path);
        log.info("Receive response: " + response);
        return response;
    }

    public String deleteRequest(String path) {
        String response = deleteRequestWithNoResponseInLog(path);
        log.info("Receive response: " + response);
        return response;
    }

    public String postRequest(String path) {
        String response = postRequestWithNoResponseInLog(path);
        log.info("Receive response: " + response);
        return response;
    }

    public String putRequest(String path) {
        String response = putRequestWithNoResponseInLog(path);
        log.info("Receive response: " + response);
        return response;
    }

    public String getRequestWithNoResponseInLog(String path) {
        return webClient
                .get()
                .uri(path)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
    }

    public String pathRequestWithNoResponseInLog(String path) {
        return webClient
                .patch()
                .uri(path)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
    }

    public String deleteRequestWithNoResponseInLog(String path) {
        return webClient
                .delete()
                .uri(path)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
    }

    public String postRequestWithNoResponseInLog(String path) {
        return webClient
                .post()
                .uri(path)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
    }

    public String putRequestWithNoResponseInLog(String path) {
        return webClient
                .put()
                .uri(path)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
    }

    public String getRequestWithEncode(String path) {
        return webClient
                .get()
                .uri(uriBuilder -> UriComponentsBuilder.fromUri(uriBuilder.build())
                        .path(path)
                        .encode()
                        .buildAndExpand()
                        .toUri())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
    }

    public String getRequestWithEncodedUrl(String url) {
        URI readyUri = UriComponentsBuilder.fromUriString(url).build(true).toUri();

        return webClient
                .get()
                .uri(readyUri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
    }

    public Integer getResponseStatus(String path) {
        ResponseDTO responseObj = new ResponseDTO();
        webClient
                .get()
                .uri(path)
                .exchange()
                .doOnError(e -> {
                    e.printStackTrace();
                    log.error("Got HTTP error: {}", e.getMessage());
                    responseObj.setStatus(404);
                })
                .doOnSuccess(clientResponse -> responseObj.setStatus(clientResponse.rawStatusCode()))
                .block();
        return responseObj.getStatus();
    }

    public Map<String, String> getRequestWithResponseHeader(String path) {
        ResponseDTO responseObj = new ResponseDTO();
        webClient.get()
                .uri(path)
                .exchange()
                .doOnError(e -> {
                    e.printStackTrace();
                    log.error("Got HTTP error: {}", e.getMessage());
                })
                .doOnSuccess(clientResponse -> responseObj.setHeadersMap(clientResponse.headers().asHttpHeaders().toSingleValueMap()))
                .doOnSuccess(clientResponse -> responseObj.setStatus(clientResponse.rawStatusCode()))
                .block();
        return responseObj.getHeadersMap();
    }

    public ResponseDTO getRequestWithResponse(String path) {

        ResponseDTO responseObj = new ResponseDTO();

        String response = webClient.get()
                .uri(path)
                .exchange()
                .doOnError(e -> {
                    e.printStackTrace();
                    log.error("Got HTTP error: {}", e.getMessage());
                })
                .doOnSuccess(clientResponse -> {
                    responseObj.setHeadersMap(clientResponse.headers().asHttpHeaders().toSingleValueMap());
                    responseObj.setLocation(clientResponse.headers().asHttpHeaders().getFirst(HttpHeaders.LOCATION));
                    responseObj.setStatus(clientResponse.rawStatusCode());
                })
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                .block();

        responseObj.setBody(response);
        return responseObj;
    }

    public ResponseDTO postRequestWithResponse(String path, Map<String, String> urlParams) {
        ResponseDTO responseObj = new ResponseDTO();
        String response = webClient.post()
                .uri(path + urlEncodeUTF8(urlParams))
                .exchange()
                .doOnError(e -> {
                    e.printStackTrace();
                    log.error("Got HTTP error: {}", e.getMessage());
                })
                .doOnSuccess(clientResponse -> responseObj.setHeadersMap(clientResponse.headers().asHttpHeaders().toSingleValueMap()))
                .doOnSuccess(clientResponse -> responseObj.setStatus(clientResponse.rawStatusCode()))
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                .block();

        responseObj.setBody(response);
        return responseObj;
    }

    public String getRequest(String path, Map<String, String> urlParam) {
        String response = webClient
                .get()
                .uri(path + urlEncodeUTF8(urlParam))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
        log.info("Receive response: " + response);
        return response;
    }

    public String getRequestBasicAuth(String username, String password, String path, Map<String, String> urlParam, boolean showResponseInLog) {
        String response = webClient
                .get()
                .uri(path + (urlParam == null ? EMPTY_STRING : urlEncodeUTF8(urlParam)))
                .headers(headers -> headers.setBasicAuth(username, password))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();

        if (showResponseInLog) {
            log.info("Receive response: " + response);
        }

        return response;
    }



    public String getRequestBasicAuthWithCookies(String username, String password, String path, Map<String, String> urlParam, MultiValueMap<String, String> cookies) {
        String response = webClient
                .get()
                .uri(path + (urlParam == null ? EMPTY_STRING : urlEncodeUTF8(urlParam)))
                .cookies(c -> c.addAll(cookies))
                .headers(headers -> headers.setBasicAuth(username, password))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
        log.info("Receive response: " + response);
        return response;
    }

    public ResponseWithCookiesDTO getRequestBasicAuthAndGetCookies(String username, String password, String path, Map<String, String> urlParam) {
        MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(path).build(true);
        URI uri = uriComponents.toUri();

        String response = webClient
                .get()
                .uri(uri)
                .headers(headers -> headers.setBasicAuth(username, password))
                .retrieve()
                .onStatus(HttpStatus::is2xxSuccessful, r -> {
                    for (String key : r.cookies().keySet()) {
                        cookies.put(key, Arrays.asList(r.cookies().get(key).get(0).getValue()));
                    }
                    return Mono.empty();
                }).bodyToMono(String.class).block();

        log.info("Receive response: " + response);

        return new ResponseWithCookiesDTO(response, cookies);
    }

    public String getRequestUTF8EncodeParams(String path) {
        String response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(path).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
        log.info("Receive response: " + response);
        return response;
    }

    public String postRequest(String path, Map<String, String> urlParam, String body) {
        String response = webClient
                .post()
                .uri(path, urlParam)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
        log.info("Request body: {}", body);
        log.info("Receive response: " + response);
        return response;
    }

    public String postRequest(String path, Map<String, String> urlParam, String body, MediaType mediaType) {
        String response = webClient
                .post()
                .uri(path + urlEncodeUTF8(urlParam))
                .contentType(mediaType)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
        log.info("Request body: {}", body);
        log.info("Receive response: " + response);
        return response;
    }

    public String postRequestBasicAuth(String username, String password, String path, Map<String, String> urlParam) {
        return postRequestBasicAuth(username, password, path, urlParam, true);
    }


    public String postRequestBasicAuth(String username, String password, String path, Map<String, String> urlParam, boolean showResponseInLog) {
        String response = webClient
                .post()
                .uri(path + urlEncodeUTF8(urlParam))
                .headers(headers -> headers.setBasicAuth(username, password))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();

        if (showResponseInLog) {
            log.info("Receive response: " + response);
        }

        return response;
    }


    public String postRequestWithBasicAuthAndCookies(String username, String password, String path, Map<String, String> urlParam, MultiValueMap<String, String> cookies) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(path + urlEncodeUTF8(urlParam)).build(true);
        URI uri = uriComponents.toUri();

        String response = webClient
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Gson().toJson(urlParam)))
                .headers(headers -> headers.setBasicAuth(username, password))
                .cookies(c -> c.addAll(cookies))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
        log.info("Receive response: " + response);
        return response;
    }

    public String postRequest(String path, Map<String, String> urlParam) {
        String response = postRequestWithNoResponseInLog(path, urlParam);
        log.info("Receive response: " + response);
        return response;
    }

    public String postRequestWithNoResponseInLog(String path, Map<String, String> urlParam) {
        return webClient
                .post()
                .uri(path + urlEncodeUTF8(urlParam))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
    }

    public String postRequestWithFormDataBody(String path, Map<String, String> body) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.setAll(body);
        String response = webClient
                .post()
                .uri(path)
                .body(BodyInserters.fromFormData(multiValueMap))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
        return response;
    }

    public String postRequest(String path, String body, MediaType mediaType) {
        String response = webClient
                .post()
                .uri(path)
                .contentType(mediaType)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error4xx)
                .onStatus(HttpStatus::is5xxServerError, error5xx)
                .bodyToMono(String.class)
                .block();
        log.info("Request body: {}", body);
        log.info("Receive response: " + response);
        return response;
    }


}
