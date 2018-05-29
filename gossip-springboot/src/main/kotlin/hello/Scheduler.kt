package hello

import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory


@Component
class Scheduler(private val repository: NodesRepository) {

    private fun clientHttpRequestFactory(): ClientHttpRequestFactory {
        val result = SimpleClientHttpRequestFactory()
        result.setReadTimeout(2000)
        result.setConnectTimeout(2000)
        return result
    }
    private val rest: RestTemplate = RestTemplate(clientHttpRequestFactory())

    @Scheduled(fixedRate = 5000)
    fun gossip() {
        log.info("executing gossip")

        val nodes = repository.nodesSet
        val gossipTo = nodes.toList().shuffled().take(3)
        gossipTo.forEach(this::executeGossip)
    }

    fun executeGossip(node: String) {
        val url = "$node/members"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        try {
            val entity = HttpEntity(repository.nodes(), headers)
            log.info("Sending gossip to $url, with entity $entity")
            val response = rest.exchange(url, HttpMethod.POST, entity, String::class.java)
            if (response.statusCodeValue == 200) {
                log.info("Gossipped nodes to $node")
            } else {
                log.warn("failed to gossip to $node, got response $response")
            }
        } catch (e: Throwable) {
            log.warn("Failed to communicate with node $node, removing (cause $e)")
            repository.removeNode(node)

        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(Scheduler::class.java)
    }
}