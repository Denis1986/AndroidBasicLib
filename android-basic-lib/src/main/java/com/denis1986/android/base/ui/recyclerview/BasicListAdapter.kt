package com.denis1986.android.base.ui.recyclerview

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/** Basic class for RecyclerView's adapters. Encapsulates swapping data with the help of DiffUtil.
 *
 * Created by Denis Druzhinin on 23.03.2020.
 */
abstract class BasicListAdapter<Item, ItemId>(
        protected val itemClickListener: ItemClickListener<ItemId>? = null
): RecyclerView.Adapter<UpdatableViewHolder<Item>>() where Item: Identifiable<ItemId> {
    var items: List<Item>? = null

    open fun swapData(newItems: List<Item>?) {
        if (items == null || newItems == null) {
            items = newItems
            notifyDataSetChanged()
        } else {
            // Calculate difference between old and new data.
            val resultDif = DiffUtil.calculateDiff(IdentifiablesDiffUtilCallback(items!!, newItems))
            // Finish update.
            items = newItems
            resultDif.dispatchUpdatesTo(this)
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: UpdatableViewHolder<Item>, position: Int) {
        if (items != null) {
            holder.update(items!![position])
            holder.update(items!!, position)
        }
    }

    protected open fun addClickListeners(holder: UpdatableViewHolder<Item>) {
        val listener = itemClickListener
                ?: return

        holder.itemView.setOnClickListener {
            handleClick(holder) { id -> listener.onItemClicked(id) }
        }
    }

    /**
     * Can be called from derived classes, while adding click listener for nested item views.
     */
    protected fun handleClick(holder: UpdatableViewHolder<Item>, action: (ItemId) -> Unit) {
        val adapterPosition = holder.adapterPosition
        val id = items?.get(adapterPosition)?.id
        if (adapterPosition != RecyclerView.NO_POSITION && id != null) {
            action(id)
        }
    }
}