import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.chat.request.ResponseFormat
import dev.langchain4j.model.chat.request.ResponseFormatType
import dev.langchain4j.model.chat.request.json.JsonObjectSchema
import dev.langchain4j.model.chat.request.json.JsonSchema
import dev.langchain4j.model.chat.response.ChatResponse
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.service.AiServices
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OllamaChatModelTest {

    data class Person(val name: String, val age: Int)

    interface PersonExtractor {
        fun extractPersonFrom(text: String?): Person
    }

    val ollamaBaseUrl = "http://localhost:11434"
    val MODEL_NAME: String = "deepseek-r1:32b"

    /**
     * If you have Ollama running locally,
     * please set the OLLAMA_BASE_URL environment variable (e.g., http://localhost:11434).
     * If you do not set the OLLAMA_BASE_URL environment variable,
     * Testcontainers will download and start Ollama Docker container.
     * It might take a few minutes.
     */
    @Test
    fun simple_example() {
        val chatModel: ChatLanguageModel = OllamaChatModel.builder()
            .baseUrl(ollamaBaseUrl)
            .modelName(MODEL_NAME)
            .logRequests(true)
            .build()

        val answer: String = chatModel.chat("Provide 3 short bullet points explaining why Java is awesome")
        println(answer)

        assertThat(answer).isNotBlank()
    }

    @Test
    fun json_schema_with_AI_Service_example() {


        val chatModel: ChatLanguageModel = OllamaChatModel.builder()
            .baseUrl(ollamaBaseUrl)
            .modelName(MODEL_NAME)
            .temperature(0.0)
            .supportedCapabilities(RESPONSE_FORMAT_JSON_SCHEMA)
            .logRequests(true)
            .build()

        val personExtractor: PersonExtractor = AiServices.create(PersonExtractor::class.java, chatModel)

        val person = personExtractor.extractPersonFrom("John Doe is 42 years old")
        println(person)

        assertThat(person).isEqualTo(Person("John Doe", 42))
    }

    @Test
    fun json_schema_with_low_level_chat_api_example() {
        val chatModel: ChatLanguageModel = OllamaChatModel.builder()
            .baseUrl(ollamaBaseUrl)
            .modelName(MODEL_NAME)
            .temperature(0.0)
            .logRequests(true)
            .build()

        val chatRequest: ChatRequest = ChatRequest.builder()
            .messages(UserMessage.from("John Doe is 42 years old"))
            .responseFormat(
                ResponseFormat.builder()
                    .type(ResponseFormatType.JSON)
                    .jsonSchema(
                        JsonSchema.builder()
                            .rootElement(
                                JsonObjectSchema.builder()
                                    .addStringProperty("name")
                                    .addIntegerProperty("age")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()

        val chatResponse: ChatResponse = chatModel.chat(chatRequest)
        System.out.println(chatResponse)

        assertThat(toMap(chatResponse.aiMessage().text())).isEqualTo(
            java.util.Map.of(
                "name",
                "John Doe",
                "age",
                42
            )
        )
    }

    @Test
    fun json_schema_with_low_level_model_builder_example() {
        val chatModel: ChatLanguageModel = OllamaChatModel.builder()
            .baseUrl(ollamaBaseUrl)
            .modelName(MODEL_NAME)
            .temperature(0.0)
            .responseFormat(
                ResponseFormat.builder()
                    .type(ResponseFormatType.JSON)
                    .jsonSchema(
                        JsonSchema.builder()
                            .rootElement(
                                JsonObjectSchema.builder()
                                    .addStringProperty("name")
                                    .addIntegerProperty("age")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .logRequests(true)
            .build()

        val json: String = chatModel.chat("Extract: John Doe is 42 years old")
        println(json)

        assertThat(toMap(json)).isEqualTo(java.util.Map.of("name", "John Doe", "age", 42))
    }

    @Test
    fun json_mode_with_low_level_model_builder_example() {
        val chatModel: ChatLanguageModel = OllamaChatModel.builder()
            .baseUrl(ollamaBaseUrl)
            .modelName(MODEL_NAME)
            .temperature(0.0)
            .responseFormat(ResponseFormat.JSON)
            .logRequests(true)
            .build()

        val json: String = chatModel.chat("Give me a JSON object with 2 fields: name and age of a John Doe, 42")
        println(json)

        assertThat(toMap(json)).isEqualTo(mapOf("name" to "John Doe", "age" to 42))
    }

    companion object {
        private fun toMap(json: String): Map<String, Any> {
            try {
                return ObjectMapper().readValue(json, MutableMap::class.java) as MutableMap<String, Any>
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
        }
    }
}