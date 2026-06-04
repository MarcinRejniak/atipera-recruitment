package pl.marcinrejniak.atiperarecruitment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class GithubService {

    private final GithubClient githubClient;

    GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    List<RepositoryDto> getUserRepositoriesWithBranches(String username) {

        List<GithubRepo> repos = githubClient.getUserRepositories(username);

        return repos.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> {

                    List<GithubBranch> branches = githubClient.getRepositoryBranches(username, repo.name());

                    List<BranchDto> branchDtos = branches.stream()
                            .map(branch -> new BranchDto(branch.name(), branch.commit().sha()))
                            .toList();

                    return new RepositoryDto(repo.name(), repo.owner().login(), branchDtos);
                })
                .toList();
    }
}
