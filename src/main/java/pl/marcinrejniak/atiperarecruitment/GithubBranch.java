package pl.marcinrejniak.atiperarecruitment;

record GithubBranch(String name, Commit commit) {
    record Commit(String sha) {}
}
