package domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class IssueUpdateTest {

    @Test
    fun `Creating an update where both state and comment is null should throw Illegal argument exception`() {
        val exception =
            assertThrows<IllegalArgumentException> { IssueUpdate() }
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(exception).hasMessage("Both state and comment can't be null")
    }
}
