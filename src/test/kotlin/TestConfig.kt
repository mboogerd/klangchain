import dev.langchain4j.model.ollama.OllamaChatModel

object TestConfig {
    val ollamaBaseUrl = "http://localhost:11434"
    val MODEL_NAME: String = "deepseek-r1:1.5b"

    fun deepSeekOracle(): Oracle {
        val chatModel = OllamaChatModel.builder()
            .baseUrl(ollamaBaseUrl)
            .modelName(MODEL_NAME)
            .logRequests(true)
            .build()

        return DeepSeekOracle(chatModel)
    }
}