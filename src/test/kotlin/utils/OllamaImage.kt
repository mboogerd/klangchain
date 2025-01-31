package utils

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Image
import org.testcontainers.DockerClientFactory
import org.testcontainers.utility.DockerImageName

object OllamaImage {
    const val OLLAMA_IMAGE: String = "ollama/ollama:latest"

    fun localOllamaImage(modelName: String?): String {
        return String.format("tc-%s-%s", OLLAMA_IMAGE, modelName)
    }

    const val LLAMA_3_1: String = "llama3.1"

    fun resolve(baseImage: String?, localImageName: String?): DockerImageName {
        val dockerImageName: DockerImageName = DockerImageName.parse(baseImage)
        val dockerClient: DockerClient = DockerClientFactory.instance().client()
        val images: List<Image> = dockerClient.listImagesCmd().withReferenceFilter(localImageName).exec()
        if (images.isEmpty()) {
            return dockerImageName
        }
        return DockerImageName.parse(localImageName).asCompatibleSubstituteFor(baseImage)
    }
}