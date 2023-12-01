package no.fintlabs.consumer.manager

import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GithubService {

    @Value("\${fint.github.token:")
    private lateinit var token: String

    @Value("\${fint.github.username:FINTLabs}")
    private lateinit var username: String

    private val github: GitHub = GitHub.connectUsingOAuth(token)

    fun updateVersion(repo: String, version: String) {
        val repository: GHRepository = github.getRepository("$username/$repo")
        createBranch(repository)
        // Update version in gradle.properties
        // Commit and push to github
        // Create pull request
    }

    private fun createBranch(repo: GHRepository, branchName: String) {
        repo.createRef("refs/heads/$branchName", repo.getRef("heads/main").getObject().sha)
    }

    private fun updateBuildGradle(repo: String, branch: String, version: String) {
        // Update version in build.gradle
    }

    private fun createPullRequest(repo: String, branch: String) {
        // Create pull request
    }

}