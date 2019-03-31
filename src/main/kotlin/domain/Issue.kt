package domain

import java.time.Instant

enum class State {
    TO_DO,
    IN_PROGRESS,
    DONE
}

data class Issue(
    val title: String, var state: State = State.TO_DO, var assignedUser: User? = null,
    // Not feasible if connected to a server with multiple clients but works with current requirement.
    val id: Long = Companion.id++,
    val createdAt: Instant = Instant.now()
) {

    @Suppress("UNUSED_PARAMETER")
    constructor(
        title: String,
        state: State? = null,
        comment: String? = null,
        assignedUser: User? = null,
        id: Long? = null,
        createdAt: Instant? = null
    )
            : this(title, assignedUser = assignedUser) {
        addIssueUpdate(state, comment)
    }

    private val issueUpdates = mutableListOf(IssueUpdate(State.TO_DO, "Created"))
    val immutableIssueUpdates: List<IssueUpdate>
        get() = issueUpdates


    fun addIssueUpdate(newState: State? = null, comment: String? = null) {
        state = newState ?: state // State is only updated if is set as argument
        try {
            issueUpdates.add(0, IssueUpdate(newState, comment))
        } catch (exception: IllegalArgumentException) {
            // Could be handled further with feedback to user
            exception.printStackTrace()
        }
    }

    fun copyAndRemoveHistory(): Issue {
        return Issue(this.title, this.state, this.assignedUser, this.id, this.createdAt).apply { removeHistory() }
    }

    private fun removeHistory() {
        issueUpdates.clear()
    }

    companion object {
        var id: Long = 0L
    }
}
