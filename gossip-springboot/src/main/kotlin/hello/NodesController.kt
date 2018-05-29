package hello

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
class NodesController(private val repository: NodesRepository) {

	@GetMapping("/members")
	fun findAll() = repository.nodes()

    @PostMapping("/members")
    fun receiveGossip(@RequestBody nodes: Nodes) {
        log.info("Receved gossip $nodes")
        repository.addNodes(nodes.nodes)
        log.info("all nodes: ${repository.nodesSet}")
        repository.nodes()
    }

    companion object {
        val log = LoggerFactory.getLogger(NodesController::class.java)
    }

}