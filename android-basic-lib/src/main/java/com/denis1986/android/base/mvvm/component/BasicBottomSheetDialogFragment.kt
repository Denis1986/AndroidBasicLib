package com.denis1986.android.base.mvvm.component

import android.content.Context
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.annotation.CallSuper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.denis1986.android.base.mvvm.BasicViewModelEventManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.denis1986.android.base.R
import com.denis1986.android.base.mvvm.BasicViewModelEventHandlerImpl

/**
 * Created by denis.druzhinin on 26.08.2019.
 */
open class BasicBottomSheetDialogFragment<VM>: BottomSheetDialogFragment() where VM: BasicViewModel {

    open val viewModel: VM? = null
    private val viewModelEventManager = BasicViewModelEventManager<VM>()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        initializeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        addViewModelObservers()
        addViewListeners()
    }

    protected open fun initializeViews() { }

    protected open fun initializeViewModel() { }

    protected open fun addViewListeners() { }

    protected open fun getPasswordEdits(): List<TextInputEditText>? = null

    open fun onBackPressed(): Boolean = viewModel?.onBack() ?: false

    open fun onToolbarBack(): Boolean = viewModel?.onToolbarBack() ?: false

    override fun onPause() {
        // Avoid showing passwords in Recent Apps.
        getPasswordEdits()?.let {  edits ->
            edits.forEach { edit -> edit.transformationMethod = PasswordTransformationMethod() }
        }
        super.onPause()
    }


    @CallSuper
    protected open fun addViewModelObservers() {
        viewModelEventManager.addViewModelObservers(getCastedActivity(), viewLifecycleOwner, viewModel, object: BasicViewModelEventHandlerImpl() {
            override fun getCoordinatorLayout(): CoordinatorLayout? = view?.findViewById(R.id.coordinatorLayout)
        })
    }

    private fun getCastedActivity(): BasicActivity<out BasicViewModel> {
        return context as BasicActivity<out BasicViewModel>
    }
}