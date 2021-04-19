package io.github.denis1986.android.base.mvvm.component

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.denis1986.android.base.R

/**
 * Created by denis.druzhinin on 08.11.2019.
 */
abstract class BasicFragmentsActivity<VM>: BasicActivity<VM>() where VM: BasicViewModel {

    protected open fun getInitialContentFragment(): Fragment? = null
    protected open fun getInitialContentFragmentTag(): String? = null

    override fun initializeViews(savedInstanceState: Bundle?) {
        super.initializeViews(savedInstanceState)
        if (savedInstanceState != null)
            return

        val initialContentFragment = getInitialContentFragment()
        if (initialContentFragment != null) {
            replaceContentFragment(initialContentFragment, tag = getInitialContentFragmentTag())
        }
    }

    /**
     * @return back stack entry, which was on the top of stack before "pop" operation.
     */
    fun popFragmentBackStack(): FragmentManager.BackStackEntry? {
        val result = if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)
        } else null

        supportFragmentManager.popBackStack()
        return result
    }

    private fun replaceContentFragment(fragment: Fragment, tag: String? = null, @IdRes containerViewId: Int = R.id.fragmentContainer) {
        supportFragmentManager
                .beginTransaction()
                .replace(containerViewId, fragment, tag)
                .commit()
    }

    protected fun replaceContentFragment(fragment: Lazy<Fragment>, tag: String? = null, @IdRes containerViewId: Int = R.id.fragmentContainer, @AnimRes enterAnimation: Int? = null, @AnimRes exitAnimation: Int? = null): Fragment {
        if (tag != null) {
            val fragmentToAdd = supportFragmentManager.findFragmentByTag(tag)
            if (fragmentToAdd != null)
                return fragmentToAdd
        }

        val fragmentValue = fragment.value
        val transaction = supportFragmentManager
                .beginTransaction()

        if (enterAnimation != null && exitAnimation != null) {
            transaction.setCustomAnimations(enterAnimation, exitAnimation)
        }

        transaction.replace(containerViewId, fragmentValue, tag)
                .commit()

        return fragmentValue
    }

    protected fun addFragment(fragment: Fragment, tag: String? = null, @IdRes containerViewId: Int = R.id.fragmentContainer) {
        supportFragmentManager
                .beginTransaction()
                .replace(containerViewId, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    protected fun returnToFragment(tag: String): Boolean {
        val goalFragmentIndex = supportFragmentManager.fragments.indexOfFirst { fragment -> fragment.tag == tag }
        if (goalFragmentIndex < 0)
            return returnToFragmentInBackStack(tag)

        if (goalFragmentIndex < supportFragmentManager.fragments.size - 1) {
            val transaction = supportFragmentManager.beginTransaction()
            for (i in supportFragmentManager.fragments.size - 1 downTo goalFragmentIndex + 1) {
                transaction.remove(supportFragmentManager.fragments[i])
            }
            transaction.commit()
        }
        return true
    }

    private fun returnToFragmentInBackStack(tag: String): Boolean {
        var goalFragmentIndex = -1
        for (i in supportFragmentManager.backStackEntryCount - 1 downTo 0) {
            if (tag == supportFragmentManager.getBackStackEntryAt(i).name) {
                goalFragmentIndex = i
                break
            }
        }

        if (goalFragmentIndex >= 0) {
            val fragmentsToRemoveCount = supportFragmentManager.backStackEntryCount - 1 - goalFragmentIndex
            repeat(fragmentsToRemoveCount) {
                supportFragmentManager.popBackStack()
            }
            return true
        } else {
            repeat(supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStack()
            }
        }
        return false
    }

    fun getContentFragment(@IdRes containerViewId: Int = R.id.fragmentContainer): Fragment? {
        return supportFragmentManager.findFragmentById(containerViewId)
    }

    override fun onBackPressed() {
        val topFragment = supportFragmentManager.fragments.lastOrNull()
        if (!(topFragment is BasicFragment<out BasicViewModel> && topFragment.onBackPressed())) {
            super.onBackPressed()
        }
    }

    override fun onToolbarBack() {
        val topFragment = supportFragmentManager.fragments.lastOrNull()
        if (!(topFragment is BasicFragment<out BasicViewModel> && topFragment.onToolbarBack())) {
            super.onToolbarBack()
        }
    }
}