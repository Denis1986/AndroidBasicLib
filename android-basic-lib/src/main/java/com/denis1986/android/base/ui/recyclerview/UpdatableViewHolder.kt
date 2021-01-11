package com.denis1986.android.base.ui.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Denis Druzhinin on 15.10.2020.
 */
abstract class UpdatableViewHolder<T>(itemView: View
) : RecyclerView.ViewHolder(itemView), Updatable<T>