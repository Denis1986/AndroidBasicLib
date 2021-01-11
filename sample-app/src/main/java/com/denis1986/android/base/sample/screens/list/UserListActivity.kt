package com.denis1986.android.base.sample.screens.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.denis1986.android.base.mvvm.component.BasicActivity
import com.denis1986.android.base.sample.R
import com.denis1986.android.base.util.extensions.viewModelProvider
import kotlinx.android.synthetic.main.activity_biometry.toolbar
import kotlinx.android.synthetic.main.activity_user_list.*

/** Demonstrates how to use RecyclerView adapter, derived from [com.denis1986.android.base.ui.recyclerview.BasicListAdapter].
 *
 * Created by Denis Druzhinin on 07.01.2021.
 */
class UserListActivity : BasicActivity<UserListScreenModel>() {

    override val viewModel by viewModelProvider<UserListScreenModel>()

    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        super.initializeViews(savedInstanceState)

        setSupportActionBar(toolbar)
        displayHomeAsUp(true)

        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        usersAdapter = UsersAdapter { userId ->
            viewModel.onItemClicked(userId)
        }
        recyclerView.adapter = usersAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_resort -> {
                viewModel.onResortButtonClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun addViewModelObservers() {
        super.addViewModelObservers()

        viewModel.users.observe(this) { users ->
            usersAdapter.swapData(users)
        }
    }
}