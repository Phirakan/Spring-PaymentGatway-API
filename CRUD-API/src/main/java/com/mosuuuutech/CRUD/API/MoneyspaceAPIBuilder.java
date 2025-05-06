package com.mosuuuutech.CRUD.API;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

/**
 * Money Space Java SDK API Buildder abstract class
 * @author Money space company limited
 * @version 2.0.0
 *
 */
public abstract class MoneyspaceAPIBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MoneyspaceAPIBuilder.class);

    /**
     * Perform HTTP POST method request with HttpEntity class
     * @param destURL A string represent HTTP URL for request destination.
     * @param entity (HttpEntity) contains HTTP data payload in HTTP form.
     * @param timeout timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return A string represent HTTP response payload
     * @throws Exception all potential exception
     */
    protected String doPost(final String destURL, final HttpEntity entity, Integer timeout) throws Exception {
        // In-case of timeout has not set. Default is 60 seconds
        if (timeout == null || timeout < 1) timeout = 60;
        // Initial HTTP Request config for specific timeout.
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000)
                .build();
        // Initial HTTP client with HTTP Request config by timeout.
        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .build();
        // Initial HTTP POST Request
        HttpPost httpPost = new HttpPost(destURL);
        // Set HTTP Request entity
        httpPost.setEntity(entity);
        // HTTP POST method request execute.
        HttpResponse httpResponse = client.execute(httpPost);
        // Parsing HTTP response into a string
        if (httpResponse == null) {
            throw new Exception("System could not process HTTP client request right now!");
        } else {
            if (httpResponse.getStatusLine().getStatusCode() >= 300) {
                throw new Exception("HTTP response has not well, HTTP code: " + httpResponse.getStatusLine().getStatusCode());
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(out);
                return out.toString();
            }
        }
    }
}