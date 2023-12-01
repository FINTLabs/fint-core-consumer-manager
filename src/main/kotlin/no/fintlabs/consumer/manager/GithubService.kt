package no.fintlabs.consumer.manager

import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.nio.charset.StandardCharsets

@Service
class GithubService {

    @Value("\${fint.github.token:")
    private lateinit var token: String

    @Value("\${fint.github.username:FINTLabs}")
    private lateinit var username: String

    private val github: GitHub = GitHub.connectUsingOAuth(token)

    fun updateVersion(repo: String, version: String) {
        val repository: GHRepository = github.getRepository("$username/$repo")
        val branchName = "update-spring-$version"
        createBranch(repository, branchName)
        updateBuildGradle(repository, branchName, version)
        createPullRequest(repository, branchName)
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