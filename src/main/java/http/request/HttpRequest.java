package http.request;

import http.constants.HttpHeader;
import http.constants.HttpMethod;
import http.util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static http.constants.HttpHeader.CONTENT_LENGTH;
import static http.constants.HttpHeader.COOKIE;
import static http.util.IOUtils.readData;

public class HttpRequest {
    private final HttpRequestStartLine startLine;
    private final HttpHeaders headers;
    private final String body;
    private final HttpCookie cookies;

    private HttpRequest(final HttpRequestStartLine startLine, final HttpHeaders headers, final String body, final HttpCookie cookie) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
        this.cookies = cookie;
    }

    public static HttpRequest from(final BufferedReader bufferedReader) throws IOException {
        final String startLine = bufferedReader.readLine();
        if (startLine == null) {
            throw new IllegalArgumentException("request가 비어있습니다.");
        }

        final HttpRequestStartLine httpRequestStartLine = HttpRequestStartLine.from(startLine);
        final HttpHeaders headers = HttpHeaders.from(bufferedReader);
        final String body = readBody(bufferedReader, headers);
        String cookieValue = headers.get(COOKIE);
        final HttpCookie httpCookie = new HttpCookie(cookieValue);

        return new HttpRequest(httpRequestStartLine, headers, body, httpCookie);
    }

    private static String readBody(final BufferedReader bufferedReader,
                                   final HttpHeaders headers) throws IOException {

        if (!headers.contains(CONTENT_LENGTH)) {
            return "";
        }

        final int contentLength = convertIntFromContentLength(headers.get(CONTENT_LENGTH));
        return readData(bufferedReader, contentLength);
    }

    private static int convertIntFromContentLength(final String contentLength) {
        return Integer.parseInt(contentLength);
    }

    public final String getQueryParameter(String key) {
        return startLine.getQueryParameter(key);
    }

    public final Map<String,String> getQueryParametersFromBody() {
        return HttpRequestUtils.parseQueryParameter(body);
    }

    public String getUrl() {
        if (startLine.getPath().equals("/")) {
            return RequestURL.INDEX.getUrl();
        }
        return startLine.getPath();
    }

    public HttpMethod getMethod() {
        return startLine.getHttpMethod();
    }

    public String getHeader(HttpHeader headerType) {
        return headers.get(headerType);
    }

    public HttpCookie getCookies() {
        return cookies;
    }

    public HttpSession getSession(){
        return HttpSessions.getSession(getCookies().getValue("session"));
    }
}