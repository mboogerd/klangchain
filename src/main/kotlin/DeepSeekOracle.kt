import dev.langchain4j.model.ollama.OllamaChatModel

class DeepSeekOracle(val deepseek: OllamaChatModel) : Oracle {
    val exclusionRegex = Regex("<think>[\\s\\S]*?</think>")
    override fun ask(question: String): String =
        deepseek.chat(question).replace(exclusionRegex, "")
}