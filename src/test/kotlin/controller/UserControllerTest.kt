package controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserControllerTest{

    private lateinit var userController: UserController

    @BeforeEach
    fun setUp(){
        userController = UserController()
    }

    // Tests both add and get separately ( = Can be improved)
    @Test
    fun `User that is created is stored and can be retrieved by id`(){
        assertThat(userController.users.isEmpty())
        val firstUserId = userController.addUser("Henrik")
        assertThat(userController.users.isNotEmpty())
        assertThat(userController.getUser(firstUserId).id).isEqualTo(firstUserId)
    }

}
