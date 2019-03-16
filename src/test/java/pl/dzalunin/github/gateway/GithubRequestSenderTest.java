package pl.dzalunin.github.gateway;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rawhttp.core.EagerHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GithubRequestSenderTest {

    private final static String RAWHTTP_200_REPO_RESPONSE = "{\n" +
            "  \"id\": 112115759,\n" +
            "  \"node_id\": \"MDEwOlJlcG9zaXRvcnkxMTIxMTU3NTk=\",\n" +
            "  \"name\": \"rawhttp\",\n" +
            "  \"full_name\": \"renatoathaydes/rawhttp\",\n" +
            "  \"private\": false,\n" +
            "  \"owner\": {\n" +
            "    \"login\": \"renatoathaydes\",\n" +
            "    \"id\": 1901277,\n" +
            "    \"node_id\": \"MDQ6VXNlcjE5MDEyNzc=\",\n" +
            "    \"avatar_url\": \"https://avatars2.githubusercontent.com/u/1901277?v=4\",\n" +
            "    \"gravatar_id\": \"\",\n" +
            "    \"url\": \"https://api.github.com/users/renatoathaydes\",\n" +
            "    \"html_url\": \"https://github.com/renatoathaydes\",\n" +
            "    \"followers_url\": \"https://api.github.com/users/renatoathaydes/followers\",\n" +
            "    \"following_url\": \"https://api.github.com/users/renatoathaydes/following{/other_user}\",\n" +
            "    \"gists_url\": \"https://api.github.com/users/renatoathaydes/gists{/gist_id}\",\n" +
            "    \"starred_url\": \"https://api.github.com/users/renatoathaydes/starred{/owner}{/repo}\",\n" +
            "    \"subscriptions_url\": \"https://api.github.com/users/renatoathaydes/subscriptions\",\n" +
            "    \"organizations_url\": \"https://api.github.com/users/renatoathaydes/orgs\",\n" +
            "    \"repos_url\": \"https://api.github.com/users/renatoathaydes/repos\",\n" +
            "    \"events_url\": \"https://api.github.com/users/renatoathaydes/events{/privacy}\",\n" +
            "    \"received_events_url\": \"https://api.github.com/users/renatoathaydes/received_events\",\n" +
            "    \"type\": \"User\",\n" +
            "    \"site_admin\": false\n" +
            "  },\n" +
            "  \"html_url\": \"https://github.com/renatoathaydes/rawhttp\",\n" +
            "  \"description\": \"HTTP library to make it easy to deal with raw HTTP.\",\n" +
            "  \"fork\": false,\n" +
            "  \"url\": \"https://api.github.com/repos/renatoathaydes/rawhttp\",\n" +
            "  \"forks_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/forks\",\n" +
            "  \"keys_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/keys{/key_id}\",\n" +
            "  \"collaborators_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/collaborators{/collaborator}\",\n" +
            "  \"teams_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/teams\",\n" +
            "  \"hooks_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/hooks\",\n" +
            "  \"issue_events_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/issues/events{/number}\",\n" +
            "  \"events_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/events\",\n" +
            "  \"assignees_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/assignees{/user}\",\n" +
            "  \"branches_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/branches{/branch}\",\n" +
            "  \"tags_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/tags\",\n" +
            "  \"blobs_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/git/blobs{/sha}\",\n" +
            "  \"git_tags_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/git/tags{/sha}\",\n" +
            "  \"git_refs_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/git/refs{/sha}\",\n" +
            "  \"trees_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/git/trees{/sha}\",\n" +
            "  \"statuses_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/statuses/{sha}\",\n" +
            "  \"languages_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/languages\",\n" +
            "  \"stargazers_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/stargazers\",\n" +
            "  \"contributors_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/contributors\",\n" +
            "  \"subscribers_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/subscribers\",\n" +
            "  \"subscription_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/subscription\",\n" +
            "  \"commits_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/commits{/sha}\",\n" +
            "  \"git_commits_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/git/commits{/sha}\",\n" +
            "  \"comments_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/comments{/number}\",\n" +
            "  \"issue_comment_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/issues/comments{/number}\",\n" +
            "  \"contents_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/contents/{+path}\",\n" +
            "  \"compare_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/compare/{base}...{head}\",\n" +
            "  \"merges_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/merges\",\n" +
            "  \"archive_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/{archive_format}{/ref}\",\n" +
            "  \"downloads_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/downloads\",\n" +
            "  \"issues_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/issues{/number}\",\n" +
            "  \"pulls_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/pulls{/number}\",\n" +
            "  \"milestones_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/milestones{/number}\",\n" +
            "  \"notifications_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/notifications{?since,all,participating}\",\n" +
            "  \"labels_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/labels{/name}\",\n" +
            "  \"releases_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/releases{/id}\",\n" +
            "  \"deployments_url\": \"https://api.github.com/repos/renatoathaydes/rawhttp/deployments\",\n" +
            "  \"created_at\": \"2017-11-26T20:52:33Z\",\n" +
            "  \"updated_at\": \"2019-03-08T13:20:28Z\",\n" +
            "  \"pushed_at\": \"2018-11-14T19:15:00Z\",\n" +
            "  \"git_url\": \"git://github.com/renatoathaydes/rawhttp.git\",\n" +
            "  \"ssh_url\": \"git@github.com:renatoathaydes/rawhttp.git\",\n" +
            "  \"clone_url\": \"https://github.com/renatoathaydes/rawhttp.git\",\n" +
            "  \"svn_url\": \"https://github.com/renatoathaydes/rawhttp\",\n" +
            "  \"homepage\": null,\n" +
            "  \"size\": 3032,\n" +
            "  \"stargazers_count\": 35,\n" +
            "  \"watchers_count\": 35,\n" +
            "  \"language\": \"Java\",\n" +
            "  \"has_issues\": true,\n" +
            "  \"has_projects\": true,\n" +
            "  \"has_downloads\": true,\n" +
            "  \"has_wiki\": true,\n" +
            "  \"has_pages\": true,\n" +
            "  \"forks_count\": 7,\n" +
            "  \"mirror_url\": null,\n" +
            "  \"archived\": false,\n" +
            "  \"open_issues_count\": 1,\n" +
            "  \"license\": {\n" +
            "    \"key\": \"apache-2.0\",\n" +
            "    \"name\": \"Apache License 2.0\",\n" +
            "    \"spdx_id\": \"Apache-2.0\",\n" +
            "    \"url\": \"https://api.github.com/licenses/apache-2.0\",\n" +
            "    \"node_id\": \"MDc6TGljZW5zZTI=\"\n" +
            "  },\n" +
            "  \"forks\": 7,\n" +
            "  \"open_issues\": 1,\n" +
            "  \"watchers\": 35,\n" +
            "  \"default_branch\": \"master\",\n" +
            "  \"network_count\": 7,\n" +
            "  \"subscribers_count\": 7\n" +
            "}\n";

    public static final String GITHUB_API_200_RESPONSE = "HTTP/1.1 200 OK\n" +
            "Server: GitHub.com\n" +
            "Date: Sat, 16 Mar 2019 12:44:01 GMT\n" +
            "Content-Type: application/json; charset=utf-8\n" +
            "Status: 200 OK\n" +
            "X-RateLimit-Limit: 60\n" +
            "X-RateLimit-Remaining: 51\n" +
            "X-RateLimit-Reset: 1552742783\n" +
            "Cache-Control: public, max-age=60, s-maxage=60\n" +
            "Vary: Accept\n" +
            "ETag: W/\"b98ebb6a339343c4a194f66b1ff0b05d\"\n" +
            "Last-Modified: Fri, 08 Mar 2019 13:20:28 GMT\n" +
            "X-GitHub-Media-Type: unknown, github.v3\n" +
            "Access-Control-Expose-Headers: ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type\n" +
            "Access-Control-Allow-Origin: *\n" +
            "Strict-Transport-Security: max-age=31536000; includeSubdomains; preload\n" +
            "X-Frame-Options: deny\n" +
            "X-Content-Type-Options: nosniff\n" +
            "X-XSS-Protection: 1; mode=block\n" +
            "Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin\n" +
            "Content-Security-Policy: default-src 'none'\n" +
            "Content-Encoding: gzip\n" +
            "X-GitHub-Request-Id: 5780:04FC:126BDC7:262E929:5C8CEF87\n" +
            "Content-Length: 5652\n\n" +
            RAWHTTP_200_REPO_RESPONSE;

    private final static String RAWHTTP_404_REPO_RESPONSE = "{\n" +
            "  \"message\": \"Not Found\",\n" +
            "  \"documentation_url\": \"https://developer.github.com/v3/repos/#get\"\n" +
            "}\n";

    public static final String GITHUB_API_404_RESPONSE = "HTTP/1.1 404 Not Found\n" +
            "Server: GitHub.com\n" +
            "Date: Sat, 16 Mar 2019 13:23:41 GMT\n" +
            "Content-Type: application/json; charset=utf-8\n" +
            "Status: 404 Not Found\n" +
            "X-RateLimit-Limit: 60\n" +
            "X-RateLimit-Remaining: 59\n" +
            "X-RateLimit-Reset: 1552746221\n" +
            "X-GitHub-Media-Type: unknown, github.v3\n" +
            "Access-Control-Expose-Headers: ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type\n" +
            "Access-Control-Allow-Origin: *\n" +
            "Strict-Transport-Security: max-age=31536000; includeSubdomains; preload\n" +
            "X-Frame-Options: deny\n" +
            "X-Content-Type-Options: nosniff\n" +
            "X-XSS-Protection: 1; mode=block\n" +
            "Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin\n" +
            "Content-Security-Policy: default-src 'none'\n" +
            "Content-Encoding: gzip\n" +
            "X-GitHub-Request-Id: 52FB:5552:14854E8:2962BAD:5C8CF8DD\n\n" +
            RAWHTTP_404_REPO_RESPONSE;

    @DataProvider
    public Object[][] githubApiResponseProvider() {
        return new Object[][]{
                {GITHUB_API_200_RESPONSE, 200, RAWHTTP_200_REPO_RESPONSE},
                {GITHUB_API_404_RESPONSE, 404, RAWHTTP_404_REPO_RESPONSE}
        };
    }

    @Test(dataProvider = "githubApiResponseProvider")
    public void testGetRepositoryInfoSuccess(String rawHttpResponse, int expectedStatusCode, String expectedBody) throws IOException {
        GithubRequestSender githubRequestSender = spy(new GithubRequestSender());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawHttpResponse.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(byteArrayInputStream);
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        when(githubRequestSender.openSSLSocket()).thenReturn(socket);

        EagerHttpResponse<?> actual = githubRequestSender.getRepositoryInfo("renatoathaydes", "rawhttp");

        assertEquals(actual.getStatusCode(), expectedStatusCode);
        assertTrue(actual.getBody().isPresent());
        assertEquals(actual.getBody().map(Object::toString).orElse(""), expectedBody);
    }

    @Test(expectedExceptions = IOException.class)
    public void testGetRepositoryInfoExceptionThrown() throws IOException {
        GithubRequestSender githubRequestSender = spy(new GithubRequestSender());

        Socket socket = mock(Socket.class);
        when(socket.getOutputStream()).thenThrow(SocketException.class);
        when(githubRequestSender.openSSLSocket()).thenReturn(socket);

        githubRequestSender.getRepositoryInfo("renatoathaydes", "rawhttp");
    }
}