package org.universelight.ul.Base

import android.content.Context
import android.os.Handler
import android.support.v4.app.Fragment
import org.universelight.ul.ui.UIDefine

abstract class BaseFragment : Fragment()
{
    lateinit var uiDefine: UIDefine
    lateinit var mContext: Context
    lateinit var mHandler: Handler

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        uiDefine = UIDefine.getInstance(context)
    }
}