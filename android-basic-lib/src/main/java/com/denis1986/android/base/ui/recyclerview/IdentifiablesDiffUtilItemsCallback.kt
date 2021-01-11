package com.denis1986.android.base.ui.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Denis Druzhinin on 09.04.2020.
 */
class IdentifiablesDiffUtilItemsCallback<T>: DiffUtil.ItemCallback<T>() where T: Identifiable<*> {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return (oldItem.id == newItem.id)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return (oldItem == newItem)
    }
}