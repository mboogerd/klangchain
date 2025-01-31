import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.ollama.OllamaChatModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeliberationTest {

    data class DeliberationGraph(val text: String, private val oracle: Oracle) {
        val attackingArguments: Flow<DeliberationGraph> = flowOf()
    }

    val ollamaBaseUrl = "http://localhost:11434"
    val MODEL_NAME: String = "deepseek-r1:32b"

    val chatModel: ChatLanguageModel = OllamaChatModel.builder()
        .baseUrl(ollamaBaseUrl)
        .modelName(MODEL_NAME)
        .logRequests(true)
        .build()

    @Test
    fun `it should find an argument against a position`() = runTest {
//        val expression = Expression("Trump is flirting with autocracy")
//
//        val chat: String = chatModel.chat("Produce one argument against the following expression: \"${expression.text}\"")
//
//        println(chat)

    }
}