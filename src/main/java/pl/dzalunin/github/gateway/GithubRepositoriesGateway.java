package pl.dzalunin.github.gateway;

import pl.dzalunin.github.utils.Logger;
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
                        Socket socket = null;
                        try {
                            socket = serverSocket.accept();
                            socket.setSoTimeout(5000);
                            final Socket newSocket = socket;
                            executorService.submit(() -> {
                                RawHttpRequest httpRequest = null;
                                try {
                                    Logger.log("accepted new connection from: " + newSocket.getInetAddress().getHostAddress());
                                    httpRequest = rawHttp.parseRequest(newSocket.getInputStream());
                                    Logger.log("incoming request: " + httpRequest.toString());

                                    if (match(httpRequest.getMethod(), httpRequest.getUri().getPath())) {

                                        String body = null;
                                        try {
                                            body = githubService.getRepositoryInfoJson(parseQuery(httpRequest.getUri().getPath()));
                                            rawHttp.parseResponse("HTTP/1.1 200 OK\r\n" +
                                                    "Content-Type: application/json"
                                            ).withBody(
                                                    new StringBody(body)
                                            ).writeTo(newSocket.getOutputStream());
                                        } catch (RepositoryNotFoundException e) {
                                            rawHttp.parseResponse("HTTP/1.1 404 Not Found\r\n" +
                                                    "Content-Type: plain/text"
                                            ).withBody(new StringBody("{}")).writeTo(newSocket.getOutputStream());
                                        } catch (ServiceUnavailableException e) {
                                            rawHttp.parseResponse("HTTP/1.1 503 Service Unavailable\n\r\n" +
                                                    "Content-Type: plain/text"
                                            ).withBody(new StringBody("{}")).writeTo(newSocket.getOutputStream());
                                        }
                                    } else {
                                        rawHttp.parseResponse("HTTP/1.1 501 Not Implemented\r\n" +
                                                "Content-Type: plain/text"
                                        ).withBody(new StringBody("{}")).writeTo(newSocket.getOutputStream());
                                    }
                                    newSocket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } catch (IOException e) {
                            Logger.logStackTrace(e);
                            if (socket != null) {
                                try {
                                    socket.close();
                                } catch (IOException e1) {
                                    Logger.logStackTrace(e);
                                }
                            }
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

    public static void main(String[] args) throws IOException {
        Logger.log("Starting server");
        ServerSocket serverSocket = new ServerSocket(8080);
        GithubRepositoriesGateway githubRepositoriesGateway = new GithubRepositoriesGateway(serverSocket);
        githubRepositoriesGateway.run();

        Logger.log("Server listening on port 8080");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.log("Server shutdown");
                githubRepositoriesGateway.stop();
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    Logger.logStackTrace(e);
                }
            }
        }));
    }
}
