package pl.dzalunin.github.utils;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.io.InputStream;

public class HttpUtils {

    private static final RawHttp rawHttp = new RawHttp();

    private final static String GET_RESPOSITORIES_TEMPLATE =
            "GET %s HTTP/1.1\r\n" +
                    "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n" +
                    "Host: %s";

    public static RawHttpRequest createGetRawRequest(String urlPath, String host) {
        return rawHttp.parseRequest(String.format(GET_RESPOSITORIES_TEMPLATE, urlPath, host));
    }

    public static RawHttpResponse parseResponse(InputStream inputStream) throws IOException {
        return rawHttp.parseResponse(inputStream);
    }

    public static RawHttpRequest parseRequest(InputStream inputStream) throws IOException {
        return rawHttp.parseRequest(inputStream);
    }

    public static RawHttpResponse create200JsonResponse() {
        return rawHttp.parseResponse("HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json");
    }

    public static RawHttpResponse create404Response() {
        return rawHttp.parseResponse("HTTP/1.1 404 Not Found");
    }

    public static RawHttpResponse create403Response() {
        return rawHttp.parseResponse("HTTP/1.1 403 Forbidden");
    }

    public static RawHttpResponse create500Response() {
        return rawHttp.parseResponse("HTTP/1.1 500 Internal Server Error");
    }

    public static RawHttpResponse create501Response() {
        return rawHttp.parseResponse("HTTP/1.1 501 Not Implemented");
    }
}
