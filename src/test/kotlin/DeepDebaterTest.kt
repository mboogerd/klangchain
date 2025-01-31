import TestConfig.deepSeekOracle
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.beBlank
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeepDebaterTest {

    val debater = DeepDebater(deepSeekOracle())

    @Test
    fun `it should find an argument against a position`() = runTest {
        val attackingArguments = debater.attackingArguments("There is exactly one God")
        val arguments = attackingArguments.take(1).toList()
        arguments shouldHaveSize 1

        println(arguments)
        val first = arguments[0]

        first shouldNot beNull()
        first shouldNot beBlank()
    }
}