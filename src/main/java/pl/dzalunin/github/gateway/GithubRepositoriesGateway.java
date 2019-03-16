package pl.dzalunin.github.gateway;

import pl.dzalunin.github.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GithubRepositoriesGateway {

    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private GithubService githubService = new GithubService(GithubService.GSON, new GithubRequestSender());

    private volatile boolean stop = false;

    public GithubRepositoriesGateway(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.stop = false;
    }

    public void run() {
        executorService.submit(() -> {
                    while (!stop) {
                        try {
                            executorService.submit(new RequestHandler(githubService, serverSocket.accept()));
                        } catch (IOException e) {
                            Logger.logStackTrace(e);
                        }
                    }
                }
        );
    }

    public void stop() {
        stop = true;
        executorService.shutdownNow();
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
