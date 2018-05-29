package hello

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RestController
class NodesController(private val repository: NodesRepository) {

	@GetMapping("/members")
	fun findAll() = repository.nodes()

    @GetMapping("/membersV2")
    fun findAllV2() = repository.nodesV2()

    @PostMapping("/members")
    fun receiveGossip(@RequestBody nodes: Nodes, request: HttpServletRequest): Nodes {
        log.info("Receved gossip $nodes from ${request.remoteHost}")
        val nodesV2 = nodes.nodes
                .filter { it.contains(request.remoteHost) }
                .filter { n ->
                    var validUri = false
                    try {
                        val uri = URI(n)
                        validUri = n.endsWith(":${uri.port}")
                    } catch (e: Throwable) {
                    }
                    validUri
                }
                .map { NodeV2(it, System.currentTimeMillis()) }
        repository.addNodes(nodesV2)
        log.info("all nodes: ${repository.nodesMap.keys}")
        return repository.nodes()
    }

    @PostMapping("/membersV2")
    fun receiveGossipV2(@RequestBody nodes: NodesV2, request: HttpServletRequest): NodesV2 {
        log.info("Receved gossip $nodes from ${request.remoteHost}")
        repository.addNodes(nodes.nodes)
        log.info("all nodes: ${repository.nodesMap.keys}")
        return repository.nodesV2()
    }

    companion object {
        val log = LoggerFactory.getLogger(NodesController::class.java)
    }

}