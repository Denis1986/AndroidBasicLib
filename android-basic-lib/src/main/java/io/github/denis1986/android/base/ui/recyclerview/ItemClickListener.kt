package io.github.denis1986.android.base.ui.recyclerview

/** Currently, SAM conversion to lambda is not supported for Kotlin interfaces, hence it is simpler to create Java interface.
 * Created by denis.druzhinin on 12.09.2019.
 */
fun interface ItemClickListener<T> {
    fun onItemClicked(id: T)
}