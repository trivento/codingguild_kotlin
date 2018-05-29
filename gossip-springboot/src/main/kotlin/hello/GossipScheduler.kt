package hello

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory


@Component
class GossipScheduler(private val repository: NodesRepository) {

    private fun clientHttpRequestFactory(): ClientHttpRequestFactory {
        val result = SimpleClientHttpRequestFactory()
        // Configure fairly short timeouts so that the scheduler is not blocked for too long
        result.setReadTimeout(2000)
        result.setConnectTimeout(2000)
        return result
    }

    private val rest: RestTemplate = RestTemplate(clientHttpRequestFactory())

    // You can use these headers to make sure that the application/json content type is used
    val httpHeaders = HttpHeaders().also{ it.contentType = MediaType.APPLICATION_JSON}

    // This will run every 5 seconds
    @Scheduled(fixedRate = 5000)
    fun gossip() {
        log.info("executing gossip")
        // TODO take 3 random nodes that we know and sent a gossip POST request to them
        // Optionally we can remove the node if communication failed

        // To execute an http request you can use the method on the RestTemplate, make sure the use the application/json content type
    }

    companion object {
        private val log = LoggerFactory.getLogger(GossipScheduler::class.java)
    }
}