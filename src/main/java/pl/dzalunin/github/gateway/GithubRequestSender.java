package pl.dzalunin.github.gateway;

import pl.dzalunin.github.utils.HttpUtils;
import pl.dzalunin.github.utils.Logger;
import rawhttp.core.EagerHttpResponse;
import rawhttp.core.RawHttpRequest;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;

public class GithubRequestSender {

    private final static String GET_RESPOSITORIES_PATH_TEMPLATE =
            "/repos/%s/%s";

    public static final String API_HOSTNAME = "api.github.com";

    public GithubRequestSender() {
    }

    public EagerHttpResponse<?> getRepositoryInfo(String owner, String repoName) throws IOException {
        String urlPath = String.format(GET_RESPOSITORIES_PATH_TEMPLATE, owner, repoName);
        RawHttpRequest request = HttpUtils.createGetRawRequest(urlPath, API_HOSTNAME);
        EagerHttpResponse response = null;
        try {
            Logger.log("request to github : " + request.getStartLine().toString());
            Socket socket = openSSLSocket();
            request.writeTo(socket.getOutputStream());
            response = HttpUtils.parseResponse(socket.getInputStream()).eagerly();
            Logger.log("response from Github: " + response.getStartLine().toString());
            socket.close();
        } catch (IOException e) {
            Logger.logStackTrace(e);
            throw e;
        }

        return response;
    }

    Socket openSSLSocket() throws IOException {
        Socket socket = SSLSocketFactory.getDefault().createSocket("api.github.com", 443);
        socket.setSoTimeout(5000);
        return socket;
    }
}
