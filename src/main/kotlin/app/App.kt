package app

import controller.IssueController
import controller.UserController

class App(val issueController: IssueController, val userController: UserController){

    fun assignUserToIssue(issueId: Long, userId: Long){
        issueController.assignUser(userController.getUser(userId), issueId)
    }

}
