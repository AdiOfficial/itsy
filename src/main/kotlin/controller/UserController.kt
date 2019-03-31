package controller

import domain.User

class UserController {

    private val usersStored = mutableMapOf<Long, User>()

    val users: Map<Long, User>
        get() = usersStored

    fun addUser(name: String): Long {
        val newUser = User(name)
        usersStored[newUser.id] = newUser
        return newUser.id
    }

    fun getUser(id: Long): User {
        return usersStored[id] ?: throw NoSuchElementException("User with id:[$id] does not exist")
    }

}
