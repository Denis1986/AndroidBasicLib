package io.github.denis1986.android.base.network.data

/**
 * Created by Denis Druzhinin on 07.04.2020.
 */
data class Pagination(
    val limit: Int = 0,
    val offset: Int = 0,
    val count: Int = 0,
    val total: Int = 0,
    val hasNext: Boolean = false
)