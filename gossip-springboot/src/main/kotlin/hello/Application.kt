package hello

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.net.NetworkInterface
import java.util.*


@SpringBootApplication
@EnableScheduling
class Application {

	private val log = LoggerFactory.getLogger(Application::class.java)

	@Bean
	fun init(repository: NodesRepository, @Value("\${server.port}") port: Int, @Value("\${seed}") seed: String) = CommandLineRunner {
		log.info("Initializing: port = $port")

		val host = Collections.list(NetworkInterface.getNetworkInterfaces())
				.filter { iface -> !iface.isLoopback && iface.isUp && !iface.isVirtual && !iface.isPointToPoint}
				.flatMap { Collections.list(it.inetAddresses) }
                .filter { it.address.size == 4 } // must be ipv4 (4 bytes)
                .map { it.hostAddress }
				.firstOrNull() ?: throw Exception("Could not determine host ip")
		repository.addNodes(listOf("http://$host:$port", seed))
	}

}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
