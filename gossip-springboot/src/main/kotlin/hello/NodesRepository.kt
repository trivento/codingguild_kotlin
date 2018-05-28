package hello

import org.springframework.stereotype.Repository

@Repository
class NodesRepository {

    // For thread safety we do not mutate the set itself, but rather mutate the variable instead
    var nodesSet: Set<String> = emptySet()

    fun nodes(): Nodes = Nodes(nodesSet.toList())

    fun addNodes(nodesToAdd: List<String>) {
        val result = mutableSetOf<String>()
        result.addAll(nodesSet)
        result.addAll(nodesToAdd)
        nodesSet = result
    }
}