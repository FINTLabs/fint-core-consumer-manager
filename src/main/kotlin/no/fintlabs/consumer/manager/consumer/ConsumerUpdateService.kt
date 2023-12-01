package no.fintlabs.consumer.manager.consumer

import no.fintlabs.consumer.manager.GithubService
import org.springframework.stereotype.Service

@Service
class ConsumerUpdateService(githubService: GithubService) {

    val githubService: GithubService = githubService
    val springCache: MutableMap<String, String> = mutableMapOf()

    fun updateSpringBoot(repos: Map<String, String>): Map<String, String>{
        val repositoryStatuses = setStatusOfRepositories(repos)
        repos.forEach { (repo, version) ->
            if (repositoryStatuses[repo] == "PROCESSING") {
                // Check if github actions is done
                // If done, set status to DONE in cache
                // If failed, set status to FAILED in cache
            }
            if (repositoryStatuses[repo] == "ACCEPTED") {
                githubService.updateVersion(repo, version)
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