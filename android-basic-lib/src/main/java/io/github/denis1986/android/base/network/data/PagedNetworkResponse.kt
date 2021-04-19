package io.github.denis1986.android.base.network.data

/**
 * Created by Denis Druzhinin on 06.04.2020.
 */
class PagedNetworkResponse<Request, Response>(
        val offset: Int,
        val request: Request? = null
): NetworkResponse<Response>() {

    @Volatile
    var responsePagination: Pagination? = null
}