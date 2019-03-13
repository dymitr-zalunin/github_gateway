package pl.dzalunin.github.gateway;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RepositoryInfoTranslatorTest {

    private static final String EXAMPLE_RESPONSE = "{\n" +
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
            "    \"MAP_TYPE_TOKEN\": \"User\",\n" +
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
            "}";
    private static final String INVALID_JSON = "qwdwdq";

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(RepositoryInfo.class, new RepositoryInfoTranslator())
            .setPrettyPrinting()
            .create();


    @Test
    public void testDeserializeSuccess() {
        RepositoryInfo actual = gson.fromJson(EXAMPLE_RESPONSE, RepositoryInfo.class);

        RepositoryInfo expected = new RepositoryInfo("renatoathaydes/rawhttp", "HTTP library to make it easy to deal with raw HTTP.",
                "https://github.com/renatoathaydes/rawhttp.git", 35, "2017-11-26T20:52:33Z");

        Assert.assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] repositoryObjectsprovider() {
        return new Object[][]{
                {new RepositoryInfo("renatoathaydes/rawhttp", "HTTP library to make it easy to deal with raw HTTP.",
                        "https://github.com/renatoathaydes/rawhttp.git", 35, "2017-11-26T20:52:33Z"), "{\n" +
                        "  \"fullName\": \"renatoathaydes/rawhttp\",\n" +
                        "  \"description\": \"HTTP library to make it easy to deal with raw HTTP.\",\n" +
                        "  \"cloneUrl\": \"https://github.com/renatoathaydes/rawhttp.git\",\n" +
                        "  \"stars\": 35,\n" +
                        "  \"createdAt\": \"2017-11-26T20:52:33Z\"\n" +
                        "}"},
                {new RepositoryInfo(null, "HTTP library to make it easy to deal with raw HTTP.",
                        "https://github.com/renatoathaydes/rawhttp.git", 35, "2017-11-26T20:52:33Z"), "{\n" +
                        "  \"description\": \"HTTP library to make it easy to deal with raw HTTP.\",\n" +
                        "  \"cloneUrl\": \"https://github.com/renatoathaydes/rawhttp.git\",\n" +
                        "  \"stars\": 35,\n" +
                        "  \"createdAt\": \"2017-11-26T20:52:33Z\"\n" +
                        "}"},
                {new RepositoryInfo(null, "HTTP library to make it easy to deal with raw HTTP.",
                        "https://github.com/renatoathaydes/rawhttp.git", null, "2017-11-26T20:52:33Z"), "{\n" +
                        "  \"description\": \"HTTP library to make it easy to deal with raw HTTP.\",\n" +
                        "  \"cloneUrl\": \"https://github.com/renatoathaydes/rawhttp.git\",\n" +
                        "  \"createdAt\": \"2017-11-26T20:52:33Z\"\n" +
                        "}"}
        };
    }

    @Test(dataProvider = "repositoryObjectsprovider")
    public void testSerializeSuccess(RepositoryInfo input, String expected) {
        String actual = gson.toJson(input, RepositoryInfo.class);
        Assert.assertEquals(actual, expected);
    }

    @Test(expectedExceptions = JsonSyntaxException.class)
    public void testDeserializeInvalidJsonExceptionThrown() {
        gson.fromJson(INVALID_JSON + "{}", RepositoryInfo.class);
    }
}