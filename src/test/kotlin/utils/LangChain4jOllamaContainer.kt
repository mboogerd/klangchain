package utils

import com.github.dockerjava.api.command.InspectContainerResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.Container
import org.testcontainers.ollama.OllamaContainer
import org.testcontainers.utility.DockerImageName
import java.io.IOException

class LangChain4jOllamaContainer(dockerImageName: DockerImageName?) : OllamaContainer(dockerImageName) {
    private var model: String? = null

    fun withModel(model: String?): LangChain4jOllamaContainer {
        this.model = model
        return this
    }

    override fun containerIsStarted(containerInfo: InspectContainerResponse?) {
        if (this.model != null) {
            try {
                log.info(
                    "Start pulling the '{}' model ... would take several minutes ...",
                    this.model
                )
                val r: Container.ExecResult = execInContainer("ollama", "pull", this.model)
                log.info("Model pulling competed! {}", r)
            } catch (e: IOException) {
                throw RuntimeException("Error pulling model", e)
            } catch (e: InterruptedException) {
                throw RuntimeException("Error pulling model", e)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(LangChain4jOllamaContainer::class.java)
    }
}