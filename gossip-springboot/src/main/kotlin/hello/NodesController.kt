package hello

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
class NodesController(private val repository: NodesRepository) {

    // This is how to implement a http GET request in spring, by default the return type is automatically converted to json
	@GetMapping("/members")
	fun getMembers(): String /* TODO the return type should probably be different */= TODO()

    @PostMapping("/members")
    fun receiveGossip(@RequestBody nodes: Any /* TODO The argument probably should not be of type any*/) {
        TODO()
        // In the response return all members (optional)
        getMembers()
    }

    companion object {
        val log = LoggerFactory.getLogger(NodesController::class.java)
    }

}