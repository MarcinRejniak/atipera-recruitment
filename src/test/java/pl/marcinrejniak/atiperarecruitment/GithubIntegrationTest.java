package pl.marcinrejniak.atiperarecruitment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@WireMockTest(httpPort = 8089)
class GithubIntegrationTest {

    @DynamicPropertySource
    static void overriderGithubUrl(DynamicPropertyRegistry registry) {
        registry.add("github.api.url", () -> "http://localhost:8089");
    }

    @Autowired
    private GithubService githubService;

    @Test
    void shouldReturnOnlyNonForkRepositoriesWithBranches() {
//        given
        String username = "testuser";

        stubFor(get(urlPathEqualTo("/users/" + username + "/repos"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {"name": "my-repo", "owner": {"login": "testuser"}, "fork": false},
                                  {"name": "forked-repo", "owner": {"login": "testuser"}, "fork": true}
                                ]
                                """)));

        stubFor(get(urlPathEqualTo("/repos/" + username + "/my-repo/branches"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {"name": "main", "commit": {"sha": "12345abcde"}}
                                ]
                                """)));

//        when
        List<RepositoryDto> result = githubService.getUserRepositoriesWithBranches(username);

//        then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        RepositoryDto repo = result.getFirst();
        assertThat(repo.repositoryName()).isEqualTo("my-repo");
        assertThat(repo.ownerLogin()).isEqualTo("testuser");
        assertThat(repo.branches()).hasSize(1);

        assertThat(repo.branches().getFirst().name()).isEqualTo("main");
        assertThat(repo.branches().getFirst().lastCommitSha()).isEqualTo("12345abcde");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
//        given
        String username = "nonexisting";

        stubFor(get(urlPathEqualTo("/users/" + username + "/repos"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")));

//        when, then
        GithubUserNotFoundException exception = assertThrows(
                GithubUserNotFoundException.class,
                () -> githubService.getUserRepositoriesWithBranches(username)
        );

        assertThat(exception.getMessage()).contains("not found");
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoRepositories() {
//        given
        String username = "emptyuser";

        stubFor(get(urlPathEqualTo("/users/" + username + "/repos"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

//        when
        List<RepositoryDto> result = githubService.getUserRepositoriesWithBranches(username);

//        then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}
