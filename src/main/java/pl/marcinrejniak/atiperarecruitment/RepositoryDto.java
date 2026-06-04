package pl.marcinrejniak.atiperarecruitment;

import java.util.List;

record RepositoryDto(String repositoryName, String ownerLogin, List<BranchDto> branches) {
}
