class DeepDebater(val oracle: Oracle) : Debater {
    private val initialAttackingPrompt = """
        The following is a position: %s
        
        Develop ONE, and only ONE single self-contained argument against it.""".trimIndent()

    private val recursiveAttackingPrompt = """
        The following is a position: %s
        
        These are arguments against the position that have been generated previously:
        %s
        
        Try to come up with ONE, and only ONE additional, self-contained attacking argument that hasn't been mentioned before.""".trimIndent()

    fun attackingArgument(position: String, arguments: List<String>): String {
        return if (arguments.isEmpty()) {
            val prompt = initialAttackingPrompt.format(position)
            println("Initial question: $prompt")
            oracle.ask(prompt)
        } else {
            val prompt = recursiveAttackingPrompt.format(position, arguments.joinToString("\n -", " -"))
            println("Recursive question: $prompt")
            oracle.ask(prompt)
        }
    }

    override fun attackingArguments(position: String): Sequence<String> =
        generateSequence(listOf<String>()) { it + attackingArgument(position, it) }
            .drop(1)
            .takeWhile { it.last().isNotEmpty() }
            .map { it.last() }
}