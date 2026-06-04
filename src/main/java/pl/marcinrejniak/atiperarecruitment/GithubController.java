package pl.marcinrejniak.atiperarecruitment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;

    GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}")
    List<RepositoryDto> getUserRepositories(@PathVariable String username) {
        return githubService.getUserRepositoriesWithBranches(username);
    }

    @ExceptionHandler(GithubUserNotFoundException.class)
    ResponseEntity<ErrorResponse> handleUserNotFound(GithubUserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, e.getMessage()));
    }
}
