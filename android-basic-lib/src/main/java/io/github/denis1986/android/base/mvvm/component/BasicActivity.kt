package io.github.denis1986.android.base.mvvm.component

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import io.github.denis1986.android.base.R
import io.github.denis1986.android.base.mvvm.BasicViewModelEventHandlerImpl
import io.github.denis1986.android.base.mvvm.EventObserver
import io.github.denis1986.android.base.mvvm.data.LoadingInfo
import io.github.denis1986.android.base.util.extensions.hideKeyboard


/** Basic class for Activities, which implements reactions to common ViewModel's events.
 *
 * Created by denis.druzhinin on 21.08.2019.
 */
abstract class BasicActivity<VM>() : AppCompatActivity() where VM: BasicViewModel {
    open val viewModel: VM? = null

    protected val viewModelEventHandler = object: BasicViewModelEventHandlerImpl() {
        override fun getCoordinatorLayout(): CoordinatorLayout? = this@BasicActivity.getCoordinatorLayout()
    }

    protected var isApplyingChangesFromViewModel = false

    var viewModelObserversAdded: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViewModel()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        initializeViews(savedInstanceState)
        addViewModelObservers()
        addViewListeners()
    }

    @CallSuper
    protected open fun initializeViews(savedInstanceState: Bundle?) {
    }

    protected open fun initializeViewModel() { }

    protected open fun addViewListeners() {}

    @CallSuper
    protected open fun addViewModelObservers() {
        viewModel?.run {
            showToastEvent.observe(this@BasicActivity, EventObserver { text ->
                viewModelEventHandler.showToast(this@BasicActivity, text)
            })

            finishActivityEvent.observe(this@BasicActivity, EventObserver { activityResult ->
                if (activityResult.resultCode != null) {
                    setResult(activityResult.resultCode)
                }
                finish()
            })

            snackbarEvent.observe(this@BasicActivity, EventObserver { snackbarEventInfo ->
                viewModelEventHandler.handleSnackbarEvent(snackbarEventInfo)
            })

            showLoadingViewEvent.observe(this@BasicActivity, EventObserver { loadingInfo ->
                val topFragment = supportFragmentManager.fragments.lastOrNull()
                if (topFragment is BasicFragment<out BasicViewModel>) {
                    topFragment.showLoading(loadingInfo)
                    if (!loadingInfo.isShown) {
                        showLoading(loadingInfo)
                    }
                } else {
                    showLoading(loadingInfo)
                }
            })
        }

        viewModelObserversAdded = true
    }

    protected open fun showLoading(loadingInfo: LoadingInfo) {
        viewModelEventHandler.showLoading(this@BasicActivity, loadingInfo)
    }

    private val defaultCoordinatorLayout: CoordinatorLayout? by lazy {
        findViewById(R.id.coordinatorLayout)
    }

    open fun getCoordinatorLayout(): CoordinatorLayout? {
        return defaultCoordinatorLayout
    }

    private fun disableAutofill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel?.onViewStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel?.onViewStop()
    }

    fun displayHomeAsUp(enable: Boolean = true) {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(enable)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onToolbarBack()
        }

        return super.onOptionsItemSelected(item)
    }

    protected open fun onToolbarBack() {
        if (viewModel?.onToolbarBack() != true) {
            super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (viewModel?.onBack() != true) {
            super.onBackPressed()
        }
    }

    fun isStartedForResult(): Boolean = (callingActivity != null)

    fun hideKeyboard() {
        window.decorView.hideKeyboard()
    }

    private companion object {
        private val TAG = BasicActivity::class.simpleName!!
    }
}