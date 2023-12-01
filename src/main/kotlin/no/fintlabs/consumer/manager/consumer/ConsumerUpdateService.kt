package no.fintlabs.consumer.manager.consumer

import no.fintlabs.consumer.manager.GithubService
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class ConsumerUpdateService(private val githubService: GithubService) {

    private val springCache: MutableMap<String, String> = mutableMapOf()

    fun updateSpringBoot(repos: Map<String, String>): Map<String, String> {
        val repositoryStatuses = setStatusOfRepositories(repos)
        repos.forEach { (repo, version) ->
            when (repositoryStatuses[repo]) {
                "ACCEPTED" -> CompletableFuture.runAsync { githubService.updateVersion(repo, version) }
                "PROCESSING" -> handleProcessingRepo(repo)
            }
        }
        return repositoryStatuses
    }

    private fun handleProcessingRepo(repo: String) {
        githubService.checkCIStatus(repo).thenAccept { ciStatus ->
            println(ciStatus)
            when (ciStatus) {
                "clean" -> {
                    githubService.mergePullRequest(repo).thenRun {
                        springCache[repo] = "DONE"
                    }
                }
                "FAILED" -> springCache[repo] = "FAILED"
            }
        }
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
