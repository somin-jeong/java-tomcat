package http.request;

import http.util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {
    private Map<String, String> cookies;

    public HttpCookie(String cookieValue) {
        cookies = HttpRequestUtils.parseCookie(cookieValue);
    }

    public String getValue(String key){
        return cookies.get(key);
    }
}
