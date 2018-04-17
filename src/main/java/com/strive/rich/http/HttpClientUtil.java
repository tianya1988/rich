package com.strive.rich.http;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jason on 18-4-8.
 */
public class HttpClientUtil {

    public static String get(String uri, boolean proxyFlag, String proxyIp, int proxyPort) {
        String result = null;
        try {
            URLConnection conn = null;
            URL url = new URL(uri);
            if (proxyFlag) {
                // 创建代理服务器
                InetSocketAddress addr = new InetSocketAddress(proxyIp, proxyPort);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
                // 如果我们知道代理server的名字, 可以直接使用
                // 结束
                conn = url.openConnection(proxy);
            } else {
                conn = url.openConnection();
            }
            InputStream in = conn.getInputStream();
            // InputStream in = url.openStream();
            result = IOUtils.toString(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
