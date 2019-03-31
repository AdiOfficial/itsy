package controller

import domain.Issue
import domain.State
import domain.User
import java.time.Instant

class IssueController {

    private val issuesStored = mutableMapOf<Long, Issue>()
    val issues: Map<Long, Issue>
        get() = issuesStored

    fun addIssue(title: String, state: State? = null, comment: String? = null, assignedUser: User? = null): Long {
        val newIssue = if (state != null || comment != null) Issue(
            title,
            state,
            comment,
            assignedUser
        ) else Issue(title, assignedUser = assignedUser)
        issuesStored[newIssue.id] = newIssue
        return newIssue.id
    }

    fun assignUser(user: User, issueId: Long) {
        getIssueForUpdate(issueId).assignedUser = user
    }

    fun removeIssue(issueId: Long) {
        issuesStored.remove(issueId)
    }

    fun setIssueState(issueId: Long, state: State, comment: String? = null) {
        try {
            getIssueForUpdate(issueId).addIssueUpdate(state, comment)
        } catch (exception: NoSuchElementException) {
            exception.printStackTrace()
        }
    }

    fun addIssueComment(issueId: Long, state: State? = null, comment: String) {
        try {
            getIssueForUpdate(issueId).addIssueUpdate(state, comment)
        } catch (exception: NoSuchElementException) {
            exception.printStackTrace()
        }
    }

    private fun getIssueForUpdate(issueId: Long) =
        issuesStored[issueId] ?: throw NoSuchElementException("Issue with id:[$issueId] does not exist")

    fun getIssue(issueId: Long) =
        issuesStored[issueId]/*?.copy(id = issueId)*/ // Could be returned as copy but not required
            ?: throw NoSuchElementException("Issue with id:[$issueId] does not exist")

    // startDate should preferably be of type LocalDate and then converted into instant.
    fun getIssues(state: State? = null, userId: Long? = null, startDate: Instant? = null, endDate: Instant? = null)
            : List<Issue> {
        return issuesStored
            .map { it.value.copyAndRemoveHistory() }
            .filter { state == null || it.state == state }
            .filter { userId == null || it.assignedUser?.id == userId }
            .filter { startDate == null || it.createdAt.isAfter(startDate) }
            .filter { endDate == null || it.createdAt.isBefore(endDate) }.toList()
    }

}
