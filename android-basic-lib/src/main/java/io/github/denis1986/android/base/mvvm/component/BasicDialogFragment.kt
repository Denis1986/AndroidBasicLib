package io.github.denis1986.android.base.mvvm.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment

abstract class BasicDialogFragment<VM> : DialogFragment() where VM : BasicViewModel {

    open val viewModel: VM? = null


    abstract val contentResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(contentResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        addViewModelObservers()
        addViewListeners()
    }

    @CallSuper
    open fun initializeViews() {
        // To be implemented in descendants.
    }

    @CallSuper
    open fun addViewListeners() {
        // To be implemented in descendants.
    }

    @CallSuper
    open fun initializeViewModel() {
        // To be implemented in descendants.
    }

    @CallSuper
    open fun addViewModelObservers() {
        // To be implemented in descendants.
    }
}