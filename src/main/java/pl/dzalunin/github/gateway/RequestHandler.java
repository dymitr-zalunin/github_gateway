package pl.dzalunin.github.gateway;

import pl.dzalunin.github.utils.HttpUtils;
import pl.dzalunin.github.utils.Logger;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.Socket;

public class RequestHandler extends Thread {

    private GithubService githubService;
    private Socket socket;

    public RequestHandler(GithubService githubService, Socket socket) {
        super();
        this.githubService = githubService;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Logger.log("accepted new connection from: " + socket.getInetAddress().getHostAddress());
            socket.setSoTimeout(5000);
            RawHttpRequest httpRequest = HttpUtils.parseRequest(socket.getInputStream());
            Logger.log("incoming request: " + httpRequest.toString());
            RawHttpResponse response;
            if (match(httpRequest.getMethod(), httpRequest.getUri().getPath())) {
                String parts[] = httpRequest.getUri().getPath().split("/");
                response = githubService.processRepositoryInfoQuery(parts[2], parts[3]);
            } else {
                response = HttpUtils.create501Response();
            }
            response.writeTo(socket.getOutputStream());
            socket.close();
            Logger.log("sent response: " + response.getStartLine().toString());
        } catch (IOException e) {
            Logger.logStackTrace(e);
        }
    }

    //may be extract Router logic into dedicated class?
    static boolean match(String method, String url) {
        String[] parts = url.split("/");
        if (parts.length < 3 || !"repositories".equals(parts[1]) || !"GET".equals(method)) {
            return false;
        }

        return true;
    }

}
