package pl.dzalunin.github.gateway;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GithubRepositoriesGatewayTest {

    private static final String REQUEST_PATTERN = "GET /repositories/%s HTTP/1.1\r\n" +
            "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n" +
            "Host: localhost:%d\r\n";

    private static final String POST_REQUEST_PATTERN = "POST /repositories/lkishalmi/gradle-gatling-plugin HTTP/1.1\r\n" +
            "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n" +
            "Host: localhost:%d\r\n";

    private int port;
    private GithubRepositoriesGateway githubRepositoriesGateway;

    @BeforeClass
    public void init() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        githubRepositoriesGateway = new GithubRepositoriesGateway(serverSocket);
        githubRepositoriesGateway.run();
        port = serverSocket.getLocalPort();
    }

    @AfterClass
    public void tearDown() {
        githubRepositoriesGateway.stop();
    }

    @DataProvider
    public Object[][] repositoriesProvider() {
        return new Object[][]{
                {"/lkishalmi/gradle-gatling-plugin", "{\n" +
                        "  \"fullName\": \"/lkishalmi/gradle-gatling-plugin\",\n" +
                        "  \"description\": \"Gatling Plugin for Gradle\",\n" +
                        "  \"cloneUrl\": \"https://github.com/lkishalmi/gradle-gatling-plugin.gi\",\n" +
                        "  \"stars\": 110,\n" +
                        "  \"createdAt\": \"2015-12-08T09:44:22Z\"\n" +
                        "}"},
                {"/renatoathaydes/rawhttp", "{\n" +
                        "  \"fullName\": \"renatoathaydes/rawhttp\",\n" +
                        "  \"description\": \"HTTP library to make it easy to deal with raw HTTP.\",\n" +
                        "  \"cloneUrl\": \"https://github.com/renatoathaydes/rawhttp.git\",\n" +
                        "  \"stars\": 35,\n" +
                        "  \"createdAt\": \"2017-11-26T20:52:33Z\"\n" +
                        "}"}
        };
    }

    @Test(dataProvider = "repositoriesProvider")
    public void testGetRepositoriesInfoSuccess(String urlPath, String expectedResponse) throws IOException {
        RawHttp rawHttp = new RawHttp();

        String request = String.format(REQUEST_PATTERN, urlPath, port);

        RawHttpRequest httpRequest = rawHttp.parseRequest(request);
        Socket socket = new Socket("localhost", port);
        httpRequest.writeTo(socket.getOutputStream());
        RawHttpResponse<?> response = rawHttp.parseResponse(socket.getInputStream());

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getHeaders().get("Content-Type").size(), 1);
        Assert.assertEquals(response.getHeaders().get("Content-Type").get(0), "application/json");
        String actualJson = response.eagerly().getBody().get().toString();
        Assert.assertEquals(actualJson, expectedResponse);
        socket.close();
    }

    @Test
    public void testGetRepositoriesInfoNotImplemented() throws IOException {
        RawHttp rawHttp = new RawHttp();

        String request = String.format(POST_REQUEST_PATTERN, port);

        RawHttpRequest httpRequest = rawHttp.parseRequest(request);
        Socket socket = new Socket("localhost", port);
        httpRequest.writeTo(socket.getOutputStream());
        RawHttpResponse<?> response = rawHttp.parseResponse(socket.getInputStream());

        Assert.assertEquals(response.getStatusCode(), 501);
        Assert.assertEquals(response.getHeaders().get("Content-Type").size(), 1);
        Assert.assertEquals(response.getHeaders().get("Content-Type").get(0), "plain/text");
        socket.close();
    }

    @DataProvider
    private Object[][] urlsProvider() {
        return new Object[][]{
                {"GET", "/repositories/lkishalmi/gradle-gatling-plugin", true},
                {"GET", "/lkishalmi", false},
                {"GET", "/", false},
                {"POST", "/lkishalmi/gradle-gatling-plugin", false},
                {"PUT", "/lkishalmi", false},
                {"HEAD", "/lkishalmi", false},
                {"OPTIONS", "/lkishalmi", false}
        };
    }

    @Test(dataProvider = "urlsProvider")
    public void testMatchUri(String method, String url, boolean expected) {
        boolean actual = GithubRepositoriesGateway.match(method, url);
        Assert.assertEquals(actual, expected);
    }
}