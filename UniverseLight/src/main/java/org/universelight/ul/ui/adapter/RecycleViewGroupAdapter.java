package org.universelight.ul.ui.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.universelight.ul.R;
import org.universelight.ul.objects.MobileGlobalVariable;
import org.universelight.ul.util.CustomItemDecoration;
import org.universelight.ul.util.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by hsinheng on 16/7/19.
 */
public class RecycleViewGroupAdapter extends RecyclerView.Adapter<RecycleViewGroupAdapter
        .MyViewHolder>
{
    private ArrayList<HashMap<String, String>>                  alData              = new
            ArrayList<>();
    private ArrayList<String>                                   alYearList          = new
            ArrayList<>();
    private HashMap<String, ArrayList<HashMap<String, String>>> hmYearDataArrayList = new
            HashMap<>();
    private Context m_Context;
    private MobileGlobalVariable mgv;

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView     textViewName;
        RecyclerView recyclerView;

        MyViewHolder(View itemView)
        {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.tv_groupTitle);
            this.recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview_month);
        }
    }

    public RecycleViewGroupAdapter(Context c, Firebase ref, final ProgressBar pb)
    {

        this.m_Context = c;
        mgv = (MobileGlobalVariable) m_Context.getApplicationContext();
        ref.addValueEventListener(new ValueEventListener()
        {
            public void onDataChange(DataSnapshot snapshot)
            {
                alData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    HashMap<String, String> hmtp = new HashMap<>();
                    hmtp.put("CostNo", dataSnapshot.child("CostNo").getValue().toString());
                    hmtp.put("Cost", dataSnapshot.child("Cost").getValue().toString());
                    hmtp.put("Description", dataSnapshot.child("Description").getValue().toString
                            ());
                    hmtp.put("Date", dataSnapshot.child("Date").getValue().toString());
                    hmtp.put("Type", dataSnapshot.child("Type").getValue().toString());
                    hmtp.put("Month", dataSnapshot.child("Month").getValue().toString());
                    hmtp.put("Year", dataSnapshot.child("Year").getValue().toString());
                    hmtp.put("ID", dataSnapshot.child("ID").getValue().toString());

                    if (dataSnapshot.hasChild("IncomeType"))
                    {
                        hmtp.put("IncomeType", dataSnapshot.child("IncomeType").getValue()
                                .toString());
                    }

                    alData.add(hmtp);
                }

                sortData();
                notifyDataSetChanged();
                pb.setVisibility(View.GONE);
            }

            public void onCancelled(FirebaseError firebaseError)
            {

            }
        });
    }

    private void sortData()
    {

        alYearList = new ArrayList<>();

        //整理年份放入alYearList--ex:2016、2014、2015、2010、2012
        for (HashMap<String, String> hm : alData)
        {
            alYearList.add(hm.get("Year"));
        }

        //排序年份--ex:2016、2015、2014、2013
        compareArrayList(alYearList);

        //月份清單放入alM
        ArrayList<String> alM = new ArrayList<>();

        int i = 12;
        while (i > 0)
        {
            String s;
            if (i < 10)
            {
                s = "0" + String.valueOf(i);
            }
            else
            {
                s = String.valueOf(i);
            }
            alM.add(s);
            i = i - 1;
        }


        //按年份塞入資料
        for (String s1 : alYearList)
        {

            ArrayList<HashMap<String, String>> alTemp   = new ArrayList<>();//年份
            ArrayList<HashMap<String, String>> tempData1 = new ArrayList<>();//月份
            ArrayList<HashMap<String, String>> tempData2 = new ArrayList<>();//日期

            for (HashMap<String, String> hm : alData)
            {
                if (s1.equals(hm.get("Year")))
                {
                    alTemp.add(hm);
                }
            }

            //按月份塞入資料
            for (String s2 : alM)
            {
                for (HashMap<String, String> hm : alTemp)
                {
                    if (s2.equals(hm.get("Month")))
                    {
                        tempData1.add(hm);
                    }
                }
            }

            ArrayList<String> alDate = new ArrayList<>();

            for (HashMap<String, String> hm : tempData1)
            {
                alDate.add(hm.get("Date"));
            }

            compareArrayList(alDate);

            for (String s3 : alDate)
            {
                for (HashMap<String, String> hm : tempData1)
                {
                    if (s3.equals(hm.get("Date")))
                    {
                        tempData2.add(hm);
                    }
                }
            }

            hmYearDataArrayList.put(s1, tempData2);
        }

        mgv.alData = alData;
        mgv.alYearList = alYearList;
        mgv.hmYearDataArrayList = hmYearDataArrayList;
    }

    private void compareArrayList(ArrayList<String> al)
    {
        Object[] st = al.toArray();
        for (Object s : st)
        {
            if (al.indexOf(s) != al.lastIndexOf(s))
            {
                al.remove(al.lastIndexOf(s));
            }
        }

        Collections.sort(al, new Comparator<String>()
        {
            @Override
            public int compare(String str2, String str1)
            {

                return str1.compareTo(str2);
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_group, parent, false);

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {

        TextView     textViewName = holder.textViewName;
        RecyclerView recyclerView = holder.recyclerView;

        textViewName.setText(alYearList.get(listPosition));

        recyclerView.setLayoutManager(new CustomLinearLayoutManager(m_Context));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecycleViewChildAdapter adapter = new RecycleViewChildAdapter(m_Context,
                hmYearDataArrayList.get(alYearList.get(listPosition)));

        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new
                CustomItemDecoration(m_Context, CustomItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public int getItemCount()
    {
        return alYearList.size();
    }
}