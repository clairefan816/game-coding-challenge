package com.claire.mind.master.interactive.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class HttpClientProvider {
    public HttpClient get() {
            // Create a client
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();

            return client;
    }
}
