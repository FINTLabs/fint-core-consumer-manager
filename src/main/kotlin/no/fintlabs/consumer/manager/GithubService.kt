package no.fintlabs.consumer.manager

import no.fintlabs.consumer.manager.config.GithubConfig
import org.kohsuke.github.GHIssueState
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture


@EnableAsync
@Service
class GithubService(private val githubConfig: GithubConfig) {

    private val github: GitHub = GitHub.connectUsingOAuth(githubConfig.token)

    @Async
    fun updateVersion(repo: String, version: String): CompletableFuture<Void> {
        val repository: GHRepository = github.getRepository("${githubConfig.username}/$repo")
        val branchName = "update-spring-$version"

        createBranch(repository, branchName)
        updateBuildGradle(repository, branchName, version)
        createPullRequest(repository, branchName)

        return CompletableFuture.completedFuture(null)
    }

    @Async
    fun checkCIStatus(repoName: String): CompletableFuture<String> {
        return try {
            val repository = github.getRepository("${githubConfig.username}/$repoName")
            val pullRequests = repository.getPullRequests(GHIssueState.OPEN)
            if (pullRequests.isNotEmpty()) {
                CompletableFuture.completedFuture(pullRequests[0].mergeableState)
            } else {
                CompletableFuture.completedFuture("NO_PULL_REQUEST")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            CompletableFuture.completedFuture("ERROR")
        }
    }

    @Async
    fun mergePullRequest(repoName: String?): CompletableFuture<Void?> {
        try {
            val repository = github.getRepository("${githubConfig.username}/$repoName")
            val pullRequests = repository.getPullRequests(GHIssueState.OPEN)
            if (pullRequests.isNotEmpty()) {
                val pullRequest = pullRequests[0]
                pullRequest.merge("Merged by FINT Consumer Manager")
            }
            return CompletableFuture.completedFuture(null)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return CompletableFuture.completedFuture(null)
    }

    private fun createBranch(repo: GHRepository, branchName: String) {
        repo.createRef("refs/heads/$branchName", repo.getRef("heads/main").getObject().sha)
    }

    private fun updateBuildGradle(repo: GHRepository, branch: String, version: String) {
        val content = repo.getFileContent("build.gradle", branch)
        val inputStream = content.read()

        val oldContent = inputStream.bufferedReader(StandardCharsets.UTF_8).use(BufferedReader::readText)
        val versionRegex = "id ['\"]org.springframework.boot['\"] version ['\"].*['\"]".toRegex()
        val newVersionString = "id 'org.springframework.boot' version '$version'"
        val updatedContent = versionRegex.replace(oldContent, newVersionString)

        content.update(updatedContent, "Update Spring Boot version to $version", branch)
    }

    private fun createPullRequest(repo: GHRepository, branch: String) {
        repo.createPullRequest("Update Spring Boot version", branch, "main", "Updating Spring Boot to latest version")
    }

}
