package hello

data class Nodes(val nodes: List<String>)
data class NodesV2(val nodes: List<NodeV2>)
data class NodeV2(val uri: String, val lastSeen: Long)
