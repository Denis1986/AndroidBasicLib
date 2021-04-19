package io.github.denis1986.android.base.sample.service

import io.github.denis1986.android.base.sample.model.User
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

/**
 * Created by Denis Druzhinin on 07.01.2021.
 */
object UserGenerator {

    private val names = listOf("David", "Oliver", "William", "James", "Lucas", "Olivia", "Emma", "Sophia", "Isabella", "Mia")

    fun generateUserList(size: Int): List<User> {
        val result = ArrayList<User>()
        for (i in 0 until size) {
            result.add(generateUser(i))
        }
        return result
    }

    private fun generateUser(index: Int): User {
        val nameIndex = index % names.size
        val age = Random.nextInt(100) + 1
        return User(id = UUID.randomUUID().toString(), name = names[nameIndex], age)
    }

    fun generateEmptyUser() = User(id = UUID.randomUUID().toString(), name = "", age = 0)
}