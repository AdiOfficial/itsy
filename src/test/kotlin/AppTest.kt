import app.App
import controller.IssueController
import controller.UserController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AppTest {

    // TODO: full test story like creating users, assigning bunch of issues, changing bunch of details.

    private lateinit var app: App

    @BeforeEach
    fun setup() {
        app = App(IssueController(), UserController())
    }

    @Test
    fun `User is assigned to issue`() {
        with(app) {
            val firstIssueId = issueController.addIssue("Try out new language in a few test classes of ours")
            val lillyUser = userController.addUser("Lilly")
            assertThat(issueController.getIssue(firstIssueId).assignedUser == null)
            assignUserToIssue(firstIssueId, lillyUser)
            assertThat(issueController.getIssue(firstIssueId).assignedUser != null)
        }
    }

}
