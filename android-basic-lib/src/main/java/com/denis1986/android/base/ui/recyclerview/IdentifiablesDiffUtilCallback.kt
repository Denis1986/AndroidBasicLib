package com.denis1986.android.base.ui.recyclerview

import androidx.recyclerview.widget.DiffUtil

/** DiffUtil.Callback implementation for 2 lists of [Identifiable].
 * Created by denis.druzhinin on 02.09.2019.
 */
class IdentifiablesDiffUtilCallback(
        private val oldItems: List<Identifiable<*>>,
        private val newItems: List<Identifiable<*>>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}