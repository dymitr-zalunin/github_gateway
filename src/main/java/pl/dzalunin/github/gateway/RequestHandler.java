package pl.dzalunin.github.gateway;

import pl.dzalunin.github.utils.Logger;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

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
            RawHttpRequest httpRequest = new RawHttp().parseRequest(socket.getInputStream());
            Logger.log("incoming request: " + httpRequest.toString());

            if (match(httpRequest.getMethod(), httpRequest.getUri().getPath())) {
                String parts[] = httpRequest.getUri().getPath().split("/");
                RawHttpResponse response = githubService.processRepositoryInfoQuery(parts[2], parts[3]);
                Logger.log("response: " + response.getStartLine().toString());

                response.writeTo(socket.getOutputStream());

            } else {
                new RawHttp().parseResponse("HTTP/1.1 501 Not Implemented\r\n" +
                        "Content-Type: plain/text")
                        .withBody(new StringBody("{}"))
                        .writeTo(socket.getOutputStream());
            }
            socket.close();
        } catch (IOException e) {
            Logger.logStackTrace(e);
        }
    }

    //may be extract Router logic into dedicated class?
    static boolean match(String method, String url) {
        String[] parts = url.split("/");
        if (parts.length < 3 || "repositories".equals(parts[0]) || !"GET".equals(method)) {
            return false;
        }

        return true;
    }
}
