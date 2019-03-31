package controller

import app.App
import domain.State
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant


class IssueControllerTest {

    private lateinit var issueController: IssueController

    @BeforeEach
    fun setUp() {
        issueController = IssueController()
    }

    @Test
    fun `Issue can be found in IssueController when added`() {
        assertThat(issueController.issues.isEmpty())
        val firstIssueId = issueController.addIssue("Clean up resources")
        assertThat(issueController.issues.keys).containsExactly(firstIssueId)

        val secondIssueId = issueController.addIssue("Write more tests for issue filtering")
        assertThat(issueController.issues.keys).containsExactly(firstIssueId, secondIssueId)

        val thirdIssueId = issueController.addIssue("Another issue")
        val fourthIssueId = issueController.addIssue("Set up Issue tracker")
        assertThat(issueController.issues.keys)
            .containsExactly(firstIssueId, secondIssueId, thirdIssueId, fourthIssueId)
    }

    @Test
    fun `Issue cannot be found in IssueController when removed`() {
        val firstId = issueController.addIssue("Clean up resources") // Depends on the addIssue to work
        assertThat(issueController.issues.map { it.key }).containsExactly(firstId)
        issueController.removeIssue(firstId)
        assertThat(issueController.issues.isEmpty())
    }

    @Test
    fun `When setIssueState for certain Issue is used the state for the specified Issue changes`() {
        val firstId = issueController.addIssue("Clean up resources") // Depends on the addIssue to work
        assertThat(issueController.issues[firstId]?.state).isEqualTo(State.TO_DO)
        issueController.setIssueState(firstId, State.IN_PROGRESS, "I'm working on it")
        assertThat(issueController.issues[firstId]?.state).isEqualTo(State.IN_PROGRESS)
    }

    @Test
    fun `when User is assigned to Issue the assigned User of the Issue should be changed to the specified User`() {
        val userController = UserController()
        val stinaUser = userController.addUser("Stina").let { userController.getUser(it) }
        val firstIssueId = issueController.addIssue("Refactor IssueController.addIssue")
        issueController.getIssue(firstIssueId).apply {
            assertThat(assignedUser == null)
            assignedUser = stinaUser
            assertThat(assignedUser == stinaUser)
            assignedUser = null
        }.apply { assertThat(assignedUser == null) }
    }


    @Test
    fun `When comment is added to Issue, Issue history should change`() {
        val firstIssueId = issueController.addIssue("Taste other coffee blends for the coffee machine")
        issueController.addIssueComment(firstIssueId, null, "Arvid Nordquist Rules, Zoégas drools!")
        assertThat(issueController.getIssue(firstIssueId).immutableIssueUpdates.first().comment)
            .isEqualTo("Arvid Nordquist Rules, Zoégas drools!")
    }

    @Test
    fun `'getIssues' returns expected issues in accordance with filter 'State'`() {

        with(issueController) {
            assertThat(getIssues().isEmpty())
            val firstIssueId = addIssue("Refactor themes", State.IN_PROGRESS)
            val secondIssueId = addIssue(
                "Get feedback from stakeholders about new onboarding",
                State.IN_PROGRESS
            )
            val thirdIssueId = addIssue("Buy beer for after-work", State.DONE)
            val fourthIssueId = addIssue("Try out new chairs", State.IN_PROGRESS)
            assertThat(getIssues(state = State.IN_PROGRESS)).containsExactly(
                getIssue(firstIssueId), getIssue(secondIssueId), getIssue(fourthIssueId)
            )
            assertThat(getIssues(state = State.TO_DO)).containsExactly()
            assertThat(getIssues(state = State.DONE)).containsExactly(getIssue(thirdIssueId))
        }
    }

    @Test
    fun `'getIssues' returns expected issues in accordance with filter 'User'`() {
        val app = App(issueController, UserController())
        with(app) {
            assertThat(issueController.getIssues().isEmpty())
            val firstUserId = userController.addUser("Johan")
            val secondUserId = userController.addUser("Christin")
            val firstIssue =
                issueController
                    .addIssue("Improve CI test sequence", assignedUser = userController.getUser(firstUserId))
                    .let { issueController.getIssue(it) }
            val secondIssue = issueController.addIssue("Another Issue").let { issueController.getIssue(it) }
            assertThat(issueController.getIssues()).containsExactly(firstIssue, secondIssue)
            assertThat(issueController.getIssues(userId = secondUserId).isEmpty())
            assertThat(issueController.getIssues(userId = firstUserId)).containsExactly(firstIssue)
            assignUserToIssue(secondIssue.id, firstUserId)
            assertThat(issueController.getIssues(userId = firstUserId)).containsExactly(
                firstIssue,
                issueController.getIssue(secondIssue.id)
            )

        }
    }

    @Test
    fun `'getIssues' returns expected issues in accordance with filter startDate&endDate`(){
        with(issueController) {
            val firstIssueId = addIssue("First issue")
            Thread.sleep(1)
            val delimitingTime = Instant.now()
            Thread.sleep(1)
            val secondIssueId = addIssue("Another issue")
            assertThat(getIssues(startDate = delimitingTime)).containsExactly(getIssue(secondIssueId))
        assertThat(getIssues(endDate = delimitingTime)).containsExactly(getIssue(firstIssueId))
        }

    }
}
