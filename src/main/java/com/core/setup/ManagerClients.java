package com.core.setup;

import com.core.constants.ConfigConstants;
import com.core.servicesApiImplementation.methods.MethodsClient;
import com.core.utils.Configs;
import com.epam.reportportal.annotations.Step;
import io.cucumber.java.Before;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.core.constants.SymbolsConstants.COLON_SEPARATOR;
import static com.core.constants.SymbolsConstants.PROTOCOL_SEPARATOR;

@Slf4j
@Data
public class ManagerClients {
    private  String host;
    private  String port;
    private  String protocol;
    private  String path;

    private MethodsClient methodsClient;


    public ManagerClients() {
        protocol = Configs.getConfig(ConfigConstants.PROTOCOL);
        host = Configs.getConfig(ConfigConstants.HOST);
        port = Configs.getConfig(ConfigConstants.PORT);

        init(protocol, host, port);
    }

    @Step("init")
    private void init(String protocol, String host, String port) {
        log.info("Input params:" +
                        "\nprotocol = {}" +
                        "\nhost = {}" +
                        "\nport = {} \n",
                protocol,
                host,
                port);

        this.protocol = protocol;
        this.host = host;
        this.port = port;

        path = "".concat(protocol).concat(PROTOCOL_SEPARATOR).concat(host).concat(COLON_SEPARATOR).concat(port);

    }

    public MethodsClient getMethodsClient() {
        return new MethodsClient(path);
    }
}
