package com.strive.rich.http;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jason on 18-4-8.
 */
public interface HttpClientTemplate {
    public String get(String url);

    public String get(String uri, Map<String, String> params);

    public String post(String uri, String data) throws IOException;

}
