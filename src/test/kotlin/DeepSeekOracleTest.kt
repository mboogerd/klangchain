import TestConfig.deepSeekOracle
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.Test

class DeepSeekOracleTest {
    @Test
    fun `responses exclude the 'think' tags`() {
        val oracle = deepSeekOracle()
        val answer = oracle.ask("To think, or not to think, therefore I am.")
        answer shouldNotContain "<think>"
        answer shouldNotContain "</think>"
    }
}