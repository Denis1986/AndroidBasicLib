package com.denis1986.android.base.sample.screens.list

import android.app.Application
import com.denis1986.android.base.mvvm.MutableLiveDataWrapper
import com.denis1986.android.base.mvvm.component.BasicViewModel
import com.denis1986.android.base.mvvm.data.SnackbarEventInfo
import com.denis1986.android.base.sample.R
import com.denis1986.android.base.sample.model.User
import com.denis1986.android.base.sample.service.UserGenerator
import kotlin.collections.ArrayList
import kotlin.random.Random

/**
 * Created by Denis Druzhinin on 07.01.2021.
 */
class UserListScreenModel(app: Application) : BasicViewModel(app) {

    val users = MutableLiveDataWrapper<List<User>>()

    init {
        users.asMutable().value = UserGenerator.generateUserList(10)
    }

    fun onResortButtonClicked() {
        users.asMutable().value = createResortedList(users.get().value!!)
    }

    private fun createResortedList(oldList: List<User>): List<User> {
        // Algorithm is not effective, but works fast enough for our 10 items.
        val oldListCopy = ArrayList(oldList)
        val initialSize = oldListCopy.size
        val newList = ArrayList<User>(initialSize)
        for (i in 0 until initialSize) {
            val oldListIndex = Random.nextInt(oldListCopy.size)
            newList.add(oldListCopy[oldListIndex])
            oldListCopy.removeAt(oldListIndex)
        }
        return newList
    }

    fun onItemClicked(userId: String) {
        val user = users.get().value!!.first { item -> item.id == userId}
        postSnackbarEvent(SnackbarEventInfo(textResId = R.string.message_user_item_clicked, textFormatArgs = listOf<Any>(user.name)))
    }
}