package hello

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import java.net.Inet4Address

@SpringBootApplication
@EnableScheduling
class Application {

	private val log = LoggerFactory.getLogger(Application::class.java)

	@Bean
	fun init(repository: NodesRepository, @Value("\${server.port}") port: Int, @Value("\${seed}") seed: String) = CommandLineRunner {
		log.info("Initializing: port = $port")
        val host = Inet4Address.getLocalHost().getHostAddress()

		repository.addNodes(listOf("http://$host:$port", seed))
	}

}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
