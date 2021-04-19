package io.github.denis1986.android.base.sample.model

import io.github.denis1986.android.base.ui.recyclerview.Identifiable

/**
 * Created by Denis Druzhinin on 02.01.2021.
 */
data class User(override val id: String,
                var name: String,
                var age: Int
) : Identifiable<String>
