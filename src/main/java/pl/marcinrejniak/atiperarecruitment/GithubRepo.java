package pl.marcinrejniak.atiperarecruitment;

record GithubRepo(String name, Owner owner, boolean fork) {
    record Owner(String login){}
}
