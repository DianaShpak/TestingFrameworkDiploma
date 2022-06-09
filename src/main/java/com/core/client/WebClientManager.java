package com.core.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;

import static com.core.constants.SymbolsConstants.SPACE_SEPARATOR;
import static com.core.constants.SymbolsConstants.SPACE_SEPARATOR_ENCODED;


@Slf4j
@Component
public class WebClientManager {
    private final int TIMEOUT_SECONDS = 30;

    public WebClient defaultWebClient(String baseUrl) {
        return defaultWebClientBuilder(baseUrl, WebClientType.SIMPLE).build();
    }

    public WebClient autoRedirectWebClient(String baseUrl) {
        return urlEncodedWebClientBuilder(baseUrl, WebClientType.AUTO_REDIRECT).build();
    }

    public WebClient autoRedirectEncodeSpacesWebClient(String baseUrl) {
        return defaultWebClientBuilder(baseUrl, WebClientType.AUTO_REDIRECT_ENCODE_SPACE).build();
    }

    public WebClient.Builder defaultWebClientBuilder(String baseUrl, WebClientType type) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build();
        String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36 OPR/58.0.3135.68";

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(buildReactorClientHttpConnector(type))
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .exchangeStrategies(exchangeStrategies)
                .filter(logRequest());
    }

    public WebClient.Builder urlEncodedWebClientBuilder(String baseUrl, WebClientType type) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build();
        String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36 OPR/58.0.3135.68";
        String CONTENT_TYPE = "application/x-www-form-urlencoded";
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(buildReactorClientHttpConnector(type))
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
                .exchangeStrategies(exchangeStrategies)
                .filter(logRequest());
    }


    @SneakyThrows
    private ReactorClientHttpConnector buildReactorClientHttpConnector(WebClientType type) {
        SslContext sslContext = getDefaultSslContext();
        TcpClient tcpClient = getDefaultTcpClient(sslContext);
        switch (type) {
            case AUTO_REDIRECT:
                return new ReactorClientHttpConnector(HttpClient.from(tcpClient).followRedirect(true));

            case AUTO_REDIRECT_ENCODE_SPACE:
                return new ReactorClientHttpConnector(HttpClient.from(tcpClient).followRedirect((req, res) -> {
                    if (res.responseHeaders().contains(HttpHeaders.LOCATION)) {
                        res.responseHeaders().set(HttpHeaders.LOCATION, res.responseHeaders().get(HttpHeaders.LOCATION).replaceAll(SPACE_SEPARATOR, SPACE_SEPARATOR_ENCODED));
                        log.info("GET: {}", res.responseHeaders().get(HttpHeaders.LOCATION));
                    }
                    return HttpResponseStatus.FOUND.equals(res.status());
                }));

            default:
                return new ReactorClientHttpConnector(HttpClient.from(tcpClient).followRedirect(false));
        }
    }

    private SslContext getDefaultSslContext() throws SSLException {
        return SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
    }

    private TcpClient getDefaultTcpClient(SslContext sslContext) {
        return TcpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_SECONDS * 1000)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT_SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT_SECONDS)))
                .runOn(LoopResources.create("reactor-webclient-autotests"));
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            return next.exchange(clientRequest);
        };
    }

    private enum WebClientType {SIMPLE, AUTO_REDIRECT, AUTO_REDIRECT_ENCODE_SPACE}
}
