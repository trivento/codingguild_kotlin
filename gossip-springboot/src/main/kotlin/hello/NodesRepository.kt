package hello

import org.springframework.stereotype.Repository

@Repository
class NodesRepository {

    // For thread safety we do not mutate the set itself, but rather mutate the variable instead
    var nodesMap: Map<String, Long> = emptyMap()

    fun nodesMapWithSelf(): Map<String, Long> {
        return nodesMap.plus(Pair(thisNode, System.currentTimeMillis()))
    }

    var thisNode: String = "http//localhost:8081"

    fun nodes(): Nodes = Nodes(nodesMapWithSelf().keys.toList())

    fun nodesV2(): NodesV2 {
        return NodesV2(nodesMapWithSelf().map { entry ->
            NodeV2(entry.key, entry.value)
        })
    }

    fun addNodes(nodesToAdd: List<NodeV2>) {
        val result = mutableMapOf<String, Long>()
        result.putAll(nodesMap)
        nodesToAdd.forEach{ n ->
            result[n.uri] = n.lastSeen
        }
        nodesMap = result.filterValues { it >= (System.currentTimeMillis() - 30_000) }
    }

    fun removeNode(node: String) {
        val newMap = mutableMapOf<String, Long>()
        newMap.putAll(nodesMap)
        newMap.remove(node)
        nodesMap = newMap
    }
}