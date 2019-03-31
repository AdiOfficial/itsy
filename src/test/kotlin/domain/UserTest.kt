package domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class UserTest {

    private val firstUser = User("Lotta")
    private val secondUser = User("Carl")

    @Test
    fun `Each new Issue gets an unique identifier while same program is running`() {
        Assertions.assertThat(firstUser.id == 0L)
        Assertions.assertThat(secondUser.id == 1L)
        Assertions.assertThat(Issue.id == 1L)
    }

}