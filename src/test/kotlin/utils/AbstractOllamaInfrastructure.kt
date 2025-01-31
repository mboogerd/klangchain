package utils

import dev.langchain4j.internal.Utils
import utils.OllamaImage.LLAMA_3_1
import utils.OllamaImage.localOllamaImage

open class AbstractOllamaInfrastructure {
    companion object {
        val OLLAMA_BASE_URL: String = System.getenv("OLLAMA_BASE_URL") ?: "http://localhost:11434"
        val MODEL_NAME: String = LLAMA_3_1

        lateinit var ollama: LangChain4jOllamaContainer

        init {
            if (Utils.isNullOrEmpty(OLLAMA_BASE_URL)) {
                val localOllamaImage: String = localOllamaImage(MODEL_NAME)
                ollama = LangChain4jOllamaContainer(OllamaImage.resolve(OllamaImage.OLLAMA_IMAGE, localOllamaImage))
                    .withModel(MODEL_NAME)
                ollama.start()
                ollama.commitToImage(localOllamaImage)
            }
        }

        fun ollamaBaseUrl(ollama: LangChain4jOllamaContainer): String {
            return if (Utils.isNullOrEmpty(OLLAMA_BASE_URL)) {
                ollama.getEndpoint()
            } else {
                OLLAMA_BASE_URL
            }
        }
    }
}