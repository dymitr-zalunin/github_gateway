package pl.dzalunin.github.gateway;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.dzalunin.github.utils.HttpUtils;
import pl.dzalunin.github.utils.Logger;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;

public class GithubService {

    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(RepositoryInfo.class, new RepositoryInfoTranslator())
            .setPrettyPrinting().create();

    private final Gson gson;
    private GithubRequestSender githubRequestSender;

    public GithubService(Gson gson, GithubRequestSender githubRequestSender) {
        this.gson = gson;
        this.githubRequestSender = githubRequestSender;
    }

    public RawHttpResponse processRepositoryInfoQuery(String owner, String repoName) {
        RawHttpResponse response;
        try {
            response = HttpUtils.create200JsonResponse();
            RawHttpResponse githubResponse = githubRequestSender.getRepositoryInfo(owner, repoName);
            if (githubResponse.getStatusCode() == 200) {
                String body = githubResponse.getBody().map(Object::toString).orElse("");
                String json = gson.toJson(gson.fromJson(body, RepositoryInfo.class), RepositoryInfo.class);
                response = response.withBody(new StringBody(json));
            } else if (githubResponse.getStatusCode() == 404) {
                //given repository doesn't exist
                response = HttpUtils.create404Response();
            } else if (githubResponse.getStatusCode() == 403) {
                //API rate limit exceeded
                response = HttpUtils.create403Response();
            }
        } catch (IOException e) {
            //other reason
            Logger.logStackTrace(e);
            response = HttpUtils.create500Response();
        }
        return response;
    }

}
