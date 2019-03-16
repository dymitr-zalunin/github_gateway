package pl.dzalunin.github.gateway;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RequestHandlerTest {

    private static final String VALID_GET_REQUEST = "GET /repositories/lkishalmi/gradle-gatling-plugin HTTP/1.1\r\n" +
            "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n" +
            "Host: localhost\r\n";

    private static final String NOT_IMPLEMENTED_GET_REQUEST = "GET /repositories_not_implemented/lkishalmi/gradle-gatling-plugin HTTP/1.1\r\n" +
            "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n" +
            "Host: localhost\r\n";

    @Test
    public void testRunCatchesSocketException() throws IOException {
        Socket socket = mock(Socket.class, RETURNS_DEEP_STUBS);
        when(socket.getInetAddress().getHostAddress()).thenReturn("127.0.0.1");
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(VALID_GET_REQUEST.getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);

        GithubService githubService = mock(GithubService.class);
        RawHttpResponse<Void> rawHttpResponse = new RawHttp().parseResponse("HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json");
        RawHttpResponse response = rawHttpResponse.withBody(new StringBody("{}"));
        when(githubService.processRepositoryInfoQuery(anyString(), anyString())).thenReturn(response);

        RequestHandler requestHandler = new RequestHandler(githubService, socket);

        //run tested method
        requestHandler.run();

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        rawHttpResponse.withBody(new StringBody("{}")).writeTo(expected);
        Assert.assertEquals(byteArrayOutputStream.toString(), expected.toString());
    }

    @Test
    public void testRunExceptionThrown() throws IOException {
        Socket socket = mock(Socket.class, RETURNS_DEEP_STUBS);
        when(socket.getInetAddress().getHostAddress()).thenReturn("127.0.0.1");
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(VALID_GET_REQUEST.getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        doThrow(SocketException.class).when(socket).setSoTimeout(anyInt());

        GithubService githubService = mock(GithubService.class);
        RequestHandler requestHandler = new RequestHandler(githubService, socket);

        //run tested method
        requestHandler.run();
    }

    @Test
    public void testRunNotImplementedResponseIsSent() throws IOException {
        Socket socket = mock(Socket.class, RETURNS_DEEP_STUBS);
        when(socket.getInetAddress().getHostAddress()).thenReturn("127.0.0.1");
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(NOT_IMPLEMENTED_GET_REQUEST.getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);

        GithubService githubService = mock(GithubService.class);
        RawHttpResponse<Void> rawHttpResponse = new RawHttp().parseResponse("HTTP/1.1 501 Not Implemented\r\n" +
                "Content-Type: plain/text");

        RequestHandler requestHandler = new RequestHandler(githubService, socket);

        //run tested method
        requestHandler.run();
    }

    @DataProvider
    private Object[][] urlsProvider() {
        return new Object[][]{
                {"GET", "/repositories/lkishalmi/gradle-gatling-plugin", true},
                {"GET", "/repositories_not_existing/lkishalmi/gradle-gatling-plugin", false},
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
        boolean actual = RequestHandler.match(method, url);
        Assert.assertEquals(actual, expected);
    }
}