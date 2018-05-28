package hello

import com.fasterxml.jackson.databind.ObjectMapper

import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.apache.catalina.manager.StatusTransformer.setContentType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType


@Component
class Scheduler(private val repository: NodesRepository) {

    private val rest: RestTemplate = RestTemplate()
    private val mapper = ObjectMapper()


    @Scheduled(fixedDelay = 5000)
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
        val entity = HttpEntity(mapper.writeValueAsString(repository.nodes()), headers)
        log.info("Sending gossip to $url, with entity $entity")
        val response = rest.exchange(url, HttpMethod.POST, entity, String::class.java)
        if (response.statusCodeValue == 200) {
            log.info("Gossipped nodes to $node")
        } else {
            log.warn("failed to gossip to $node, got response $response")
            // TODO remove node if it was unreachable
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(Scheduler::class.java)
    }
}