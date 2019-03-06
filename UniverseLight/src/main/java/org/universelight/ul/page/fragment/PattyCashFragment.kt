package org.universelight.ul.page.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.firebase.client.Firebase
import kotlinx.android.synthetic.main.fragment_patty_cash_recycleview.view.*
import org.universelight.ul.Base.BaseFragment

import org.universelight.ul.R
import org.universelight.ul.objects.MobileGlobalVariable
import org.universelight.ul.ui.adapter.RecycleViewGroupAdapter

/**
 * Created by hsinheng on 16/7/19.
 */
class PattyCashFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_patty_cash_recycleview, container, false)

        rootView.recycler_view.layoutManager = LinearLayoutManager(activity)
        rootView.recycler_view.setHasFixedSize(true)
        rootView.recycler_view.itemAnimator = DefaultItemAnimator()

        val m_ProgressBar = rootView.findViewById<ProgressBar>(R.id.progress_bar)
        m_ProgressBar.visibility = View.VISIBLE

        val mgv = activity.applicationContext as MobileGlobalVariable

        Firebase.setAndroidContext(context)
        val ref = Firebase(PATTY_CASH_REF)
        val adapter = RecycleViewGroupAdapter(activity, ref, m_ProgressBar, mgv.strSearchYear, mgv.strSearchMonth)

        rootView.recycler_view.adapter = adapter

        return rootView
    }

    companion object {
        var PATTY_CASH_REF = "https://universelight-e10c7.firebaseio.com/PattyCash"
        fun newInstance(): PattyCashFragment {
            return PattyCashFragment()
        }
    }
}
