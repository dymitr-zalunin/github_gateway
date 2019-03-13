package pl.dzalunin.github.gateway;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GithubRepositoriesGateway {

    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private GithubService githubService = new GithubService(GithubService.GSON);

    private volatile boolean stop = false;

    public GithubRepositoriesGateway(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.stop = false;
    }

    public void run() {
        RawHttp rawHttp = new RawHttp();

        executorService.submit(() -> {
                    while (!stop) {
                        try {
                            Socket socket = serverSocket.accept();

                            RawHttpRequest httpRequest = rawHttp.parseRequest(socket.getInputStream());

                            if (match(httpRequest.getMethod(), httpRequest.getUri().getPath())) {

                                String body = null;
                                try {
                                    body = githubService.getRepositoryInfoJson(parseQuery(httpRequest.getUri().getPath()));

                                    rawHttp.parseResponse("HTTP/1.1 200 OK\r\n" +
                                            "Content-Type: application/json"
                                    ).withBody(
                                            new StringBody(body)
                                    ).writeTo(socket.getOutputStream());

                                } catch (RepositoryNotFoundException e) {
                                    rawHttp.parseResponse("HTTP/1.1 404 Not Found\r\n" +
                                            "Content-Type: plain/text"
                                    ).writeTo(socket.getOutputStream());
                                } catch (ServiceUnavailableException e) {
                                    rawHttp.parseResponse("HTTP/1.1 503 Service Unavailable\n\r\n" +
                                            "Content-Type: plain/text"
                                    ).writeTo(socket.getOutputStream());
                                }
                            } else {
                                rawHttp.parseResponse("HTTP/1.1 501 Method Not Implemented\r\n" +
                                        "Content-Type: plain/text"
                                )
                                        .writeTo(socket.getOutputStream());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
        );
    }

    public void stop() {
        stop = true;
        executorService.shutdownNow();
    }

    //may be extract Router logic into dedicated class?
    static boolean match(String method, String url) {
        String[] parts = url.split("/");
        if (parts.length < 3 || "repositories".equals(parts[0]) || !"GET".equals(method)) {
            return false;
        }

        return true;
    }

    static GithubService.RepositoryQuery parseQuery(String url) {
        String parts[] = url.split("/");
        return new GithubService.RepositoryQuery(parts[2], parts[3]);
    }
}
