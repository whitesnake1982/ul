package org.universelight.ul.page.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;

import org.universelight.ul.R;
import org.universelight.ul.ui.adapter.RecycleViewGroupAdapter;

/**
 * Created by hsinheng on 16/7/19.
 */
public class PattyCashFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static String PATTY_CASH_REF = "https://universelight-e10c7.firebaseio.com/PattyCash";

    private RecyclerView m_RecyclerView;
    private ProgressBar m_ProgressBar;

    public PattyCashFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PattyCashFragment newInstance() {

        return new PattyCashFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patty_cash_recycleview, container, false);

        m_RecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        m_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        m_RecyclerView.setHasFixedSize(true);
        m_RecyclerView.setItemAnimator(new DefaultItemAnimator());

        m_ProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        m_ProgressBar.setVisibility(View.VISIBLE);

        Firebase.setAndroidContext(getContext());
        Firebase ref = new Firebase(PATTY_CASH_REF);
        RecycleViewGroupAdapter adapter = new RecycleViewGroupAdapter(getActivity(), ref, m_ProgressBar);
        m_RecyclerView.setAdapter(adapter);

        return rootView;
    }


}
