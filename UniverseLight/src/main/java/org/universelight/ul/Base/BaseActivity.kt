package org.universelight.ul.Base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import org.universelight.ul.R

import org.universelight.ul.objects.MobileGlobalVariable
import org.universelight.ul.ui.UIDefine

/**
 * Created by hsinhenglin on 2017/1/9.
 */

internal abstract class BaseActivity : AppCompatActivity() {
    lateinit var mPage: Context
    lateinit var mgv: MobileGlobalVariable
    lateinit var mUIDefine: UIDefine

    fun initVariables(activity: AppCompatActivity) {
        mPage = activity
        mgv = mPage.applicationContext as MobileGlobalVariable
        mUIDefine = UIDefine.getInstance(mPage)
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

        //設定不使用瀏海區
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            window.attributes = lp
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().setCustomAnimations(
                R.anim.fragment_right_in,
                R.anim.fragment_left_out,
                R.anim.fragment_left_in,
                R.anim.fragment_right_out
        ).func().commit()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { replace(frameId, fragment) }
    }

    override fun finish() {
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out)
        super.finish()
    }

}
