package domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IssueTest {

    private val firstIssue: Issue = Issue("Loading icon does not show when app is starting")
    private val secondIssue: Issue =
        Issue("app crashes when user navigates back and forth between profile and browsing")


    @Test
    fun `Each new Issue gets an unique identifier while same program is running`() {
        assertThat(firstIssue.id == 0L)
        assertThat(secondIssue.id == 1L)
        assertThat(Issue.id == 1L)
    }

    @Test
    fun `When user is specified in constructor issue gets user as assigned user`(){
        val ami = User("Ami")
        val thirdIssue = Issue("Screen goes black when backing from profile")
        thirdIssue.assignedUser = ami
        assertThat(thirdIssue.assignedUser).isEqualTo(ami)
    }

    // Seems like a waisted test
    @Test
    fun `When null is set to Issues user, Issues user should be removed`() {
        val noUser = null
        val adaeze = User("Adaeze")
        val forthIssue = Issue("Screen goes white").apply {
            assertThat(assignedUser).isNull()
            assignedUser = adaeze
            assertThat(assignedUser).isEqualTo(adaeze)
            assignedUser = noUser
        }
        assertThat(forthIssue.assignedUser).isNull()
    }
}