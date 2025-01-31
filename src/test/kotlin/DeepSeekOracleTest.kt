import dev.langchain4j.model.ollama.OllamaChatModel
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.Test

class DeepSeekOracleTest {
    val ollamaBaseUrl = "http://localhost:11434"
    val MODEL_NAME: String = "deepseek-r1:1.5b"

    @Test
    fun `responses exclude the 'think' tags`() {
        val oracle = make_oracle()
        val answer = oracle.ask("To think, or not to think, therefore I am.")
        answer shouldNotContain "<think>"
        answer shouldNotContain "</think>"
    }

    private fun make_oracle(): Oracle {
        val chatModel = OllamaChatModel.builder()
            .baseUrl(ollamaBaseUrl)
            .modelName(MODEL_NAME)
            .logRequests(true)
            .build()

        return DeepSeekOracle(chatModel)
    }
}