package io.github.denis1986.android.base.sample.screens.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.github.denis1986.android.base.sample.R
import io.github.denis1986.android.base.sample.databinding.ItemUserBinding
import io.github.denis1986.android.base.sample.model.User
import io.github.denis1986.android.base.ui.recyclerview.BasicListAdapter
import io.github.denis1986.android.base.ui.recyclerview.ItemClickListener
import io.github.denis1986.android.base.ui.recyclerview.UpdatableViewHolder

/**
 * Created by Denis Druzhinin on 07.01.2021.
 */
class UsersAdapter(itemClickListener: ItemClickListener<String>
) : BasicListAdapter<User, String>(itemClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatableViewHolder<User> {
        val resultView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)

        val holder = ViewHolder(resultView)
        addClickListeners(holder)
        return holder
    }

    class ViewHolder(itemView: View) : UpdatableViewHolder<User>(itemView) {
        private val binding = DataBindingUtil.bind<ItemUserBinding>(itemView)

        override fun update(item: User) {
            binding?.let {
                it.nameView.text = item.name
                it.ageView.text = item.age.toString()
            }

        }
    }
}