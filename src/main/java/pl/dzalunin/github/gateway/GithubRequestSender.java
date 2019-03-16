package pl.dzalunin.github.gateway;

import pl.dzalunin.github.utils.Logger;
import rawhttp.core.EagerHttpResponse;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import javax.net.ssl.SSLSocketFactory;
import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class GithubRequestSender implements Closeable {

    private final static String GET_RESPOSITORIES_TEMPLATE =
            "GET /repos/%s/%s HTTP/1.1\r\n" +
                    "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n" +
                    "Host: api.github.com";

    private RawHttp rawHttp;

    public GithubRequestSender() {
        rawHttp = new RawHttp();
    }

    public EagerHttpResponse<?> getRepositoryInfo(String owner, String repoName) throws IOException {
        RawHttpRequest request = prepareGetRepositoryRequest(rawHttp, owner, repoName);
        EagerHttpResponse response = null;
        try {
            Logger.log("request to github : " + request.getStartLine().toString());
            Socket socket = SSLSocketFactory.getDefault().createSocket("192.30.255.116", 443);
            socket.setSoTimeout(5000);
            request.writeTo(socket.getOutputStream());
            response = rawHttp.parseResponse(socket.getInputStream()).eagerly();
            Logger.log("response : " + request.getStartLine().toString());
            socket.close();
        } catch (IOException e) {
            Logger.logStackTrace(e);
            throw e;
        }

        return response;
    }

    RawHttpRequest prepareGetRepositoryRequest(RawHttp rawHttp, String owner, String repoName) {
        return rawHttp.parseRequest(String.format(GET_RESPOSITORIES_TEMPLATE, owner, repoName));
    }

    @Override
    public void close() throws IOException {
    }
}
