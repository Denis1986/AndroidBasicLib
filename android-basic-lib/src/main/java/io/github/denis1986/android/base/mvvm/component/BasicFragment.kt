package io.github.denis1986.android.base.mvvm.component

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import io.github.denis1986.android.base.mvvm.BasicViewModelEventManager
import com.google.android.material.textfield.TextInputEditText
import io.github.denis1986.android.base.mvvm.BasicViewModelEventHandlerImpl
import io.github.denis1986.android.base.mvvm.data.LoadingInfo

/** Basic class for Fragments, which implements reactions to common ViewModel's events.
 *
 * Created by denis.druzhinin on 10.10.2019.
 */
abstract class BasicFragment<VM>(private val isNotifyViewModelOnStartStop: Boolean = false
) : Fragment() where VM: BasicViewModel {

    open val viewModel: VM? = null
    private val viewModelEventManager = BasicViewModelEventManager<VM>()
    protected var isApplyingChangesFromViewModel = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        addViewModelObservers()
        addViewListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        releaseViews()
    }

    override fun onStart() {
        super.onStart()
        if (isNotifyViewModelOnStartStop) {
            viewModel?.onViewStart()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isNotifyViewModelOnStartStop) {
            viewModel?.onViewStop()
        }
    }

    @CallSuper
    protected open fun initializeViews() {
    }

    @CallSuper
    protected open fun releaseViews() {}

    protected open fun initializeViewModel() { }

    protected open fun addViewListeners() { }

    protected open fun getPasswordEdits(): List<TextInputEditText>? = null

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    open fun onBackPressed(): Boolean = viewModel?.onBack() ?: false

    open fun onToolbarBack(): Boolean = viewModel?.onToolbarBack() ?: false


    @CallSuper
    protected open fun addViewModelObservers() {
        viewModelEventManager.addViewModelObservers(getCastedActivity(), viewLifecycleOwner, viewModel, object: BasicViewModelEventHandlerImpl() {
            override fun showLoading(context: Context, loadingInfo: LoadingInfo): Boolean {
                if (this@BasicFragment.showLoading(loadingInfo))
                    return true

                return super.showLoading(context, loadingInfo)
            }

            override fun getCoordinatorLayout(): CoordinatorLayout? = getCastedActivity().getCoordinatorLayout()
        })
    }

    open fun showLoading(loadingInfo: LoadingInfo): Boolean = false

    private fun getCastedActivity(): BasicActivity<out BasicViewModel> {
        return context as BasicActivity<out BasicViewModel>
    }
}