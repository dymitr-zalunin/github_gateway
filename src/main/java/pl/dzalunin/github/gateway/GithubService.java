package pl.dzalunin.github.gateway;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.dzalunin.github.utils.Logger;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;

public class GithubService {

    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(RepositoryInfo.class, new RepositoryInfoTranslator())
            .setPrettyPrinting().create();

    private final Gson gson;
    private GithubRequestSender githubRequestSender;

    public GithubService(Gson gson) {
        this.gson = gson;
        this.githubRequestSender = new GithubRequestSender();
    }

    public RawHttpResponse processRepositoryInfoQuery(String owner, String repoName) {
        RawHttpResponse response;
        try {
            response = new RawHttp().parseResponse("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json");
            RawHttpResponse githubResponse = githubRequestSender.getRepositoryInfo(owner, repoName);
            if (githubResponse.getStatusCode() == 200) {
                String body = githubResponse.getBody().map(Object::toString).orElse("");
                String json = gson.toJson(gson.fromJson(body, RepositoryInfo.class), RepositoryInfo.class);
                response = response.withBody(new StringBody(json));
            } else if (githubResponse.getStatusCode() == 404) {
                //given repository doesn't exist
                response = new RawHttp().parseResponse("HTTP/1.1 404 Not Found");
            } else if (githubResponse.getStatusCode() == 403) {
                //API rate limit exceeded
                response = new RawHttp().parseResponse("HTTP/1.1 403 Forbidden");
            }
        } catch (IOException e) {
            //other reason
            Logger.logStackTrace(e);
            response = new RawHttp().parseResponse("HTTP/1.1 500 Internal Server Error");
        }
        return response;
    }

}
