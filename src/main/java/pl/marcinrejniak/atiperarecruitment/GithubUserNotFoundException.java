package pl.marcinrejniak.atiperarecruitment;

class GithubUserNotFoundException extends RuntimeException {
    GithubUserNotFoundException(String message) {
        super(message);
    }
}
