package com.strive.rich.http;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * Created by jason on 18-4-8.
 */
public class HttpClientTemplateImpl {
    private String ip;
    private int port;
    private String protocol;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String get(String url) {
        return get(url, null);
    }

    public String get(String uri, Map<String, String> params) {
        return request(uri, params, "GET");
    }

    public String post(String uri, String data) throws IOException {
        String url = protocol + "://" + getIp() + ":" + getPort() + uri;
//        logger.info("the url is : " + url)

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        InputStream is = null;
        try {
            RequestBuilder builder = null;
            builder = RequestBuilder.create("POST").setUri(url).addHeader("Accept-Encoding", "gzip, deflate").
                    addHeader("Content-Type", "application/json;charset=UTF-8;");

            StringEntity entity = new StringEntity(data, ContentType.create("plain/text", Consts.UTF_8));
            builder.setEntity(entity);

            HttpUriRequest jsonGet = builder.build();
            httpClient = HttpClients.createDefault();

            response = httpClient.execute(jsonGet);
            is = response.getEntity().getContent();
            return IOUtils.toString(is);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpClient);
        }
    }

    private String request(String uri, Map<String, String> params, String method) {
//        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        String url = protocol + "://" + getIp() + uri;
        System.out.println(url);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        InputStream is = null;
        try {
            HttpHost proxy = new HttpHost("10.22.98.21", 8080, "http");
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

            RequestBuilder builder = null;
            builder = RequestBuilder.create(method).setUri(url).addHeader("Accept-Encoding", "gzip, deflate").
                    addHeader("Content-Type", "application/json").setConfig(config);
            if (MapUtils.isNotEmpty(params)) {
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    builder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            HttpUriRequest jsonGet = builder.build();
            httpClient = HttpClients.createDefault();

            response = httpClient.execute(jsonGet);
            is = response.getEntity().getContent();
            return IOUtils.toString(is);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpClient);
        }
        return null;
    }
}
