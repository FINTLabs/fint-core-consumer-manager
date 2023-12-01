package no.fintlabs.consumer.manager.consumer

import no.fintlabs.consumer.manager.GithubService
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class ConsumerUpdateService(githubService: GithubService) {

    val githubService: GithubService = githubService
    val springCache: MutableMap<String, String> = mutableMapOf()

    fun updateSpringBoot(repos: Map<String, String>): Map<String, String> {
        val repositoryStatuses = setStatusOfRepositories(repos)
        repos.forEach { (repo, version) ->
            if (repositoryStatuses[repo] == "ACCEPTED") {
                CompletableFuture.runAsync {
                    githubService.updateVersion(repo, version)
                }
            }
        }
        return repositoryStatuses
    }

    fun setStatusOfRepositories(repos: Map<String, String>): Map<String, String> {
        val statusOfRepos: MutableMap<String, String> = mutableMapOf()
        repos.forEach { (repo) ->
            if (springCache.containsKey(repo)) {
                statusOfRepos[repo] = springCache[repo]!!
            } else {
                springCache[repo] = "PROCESSING"
                statusOfRepos[repo] = "ACCEPTED"
            }
        }
        return statusOfRepos
    }

}