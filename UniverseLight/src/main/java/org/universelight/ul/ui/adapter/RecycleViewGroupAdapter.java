package org.universelight.ul.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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
import org.universelight.ul.ui.ULUIDefine;
import org.universelight.ul.util.CustomItemDecoration;
import org.universelight.ul.util.CustomLinearLayoutManager;
import org.universelight.ul.util.FilterCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static org.universelight.ul.ui.ULUIDefine.FontSize_10u;

public class RecycleViewGroupAdapter extends RecyclerView.Adapter<RecycleViewGroupAdapter
        .MyViewHolder> implements FilterCondition.OnSearchConditionListener
{
    private ArrayList<HashMap<String, String>> alData     = new
            ArrayList<>();
    private ArrayList<String>                  alYearList = new
            ArrayList<>();

    private ArrayList<HashMap<String, String>> alSearchData = new
            ArrayList<>();

    private HashMap<String, ArrayList<HashMap<String, String>>> hmYearDataArrayList = new
            HashMap<>();
    private Context              m_Context;
    private MobileGlobalVariable mgv;
    private ProgressBar          pb;

    private String searchY = "";
    private String searchM = "";

    @Override
    public void getSearchOptions(final String sYear, final String sMonth)
    {
        pb.setVisibility(View.VISIBLE);
        searchY = sYear;
        searchM = sMonth;

        if(!sYear.equals(""))
        {
            getSearchData();
        }
        else
        {
            resetSearchData();
        }
    }

    private void resetSearchData()
    {
        alData = new ArrayList<>(mgv.alData);
        alYearList.clear();
        alYearList = new ArrayList<>(mgv.alYearList);
        hmYearDataArrayList.clear();
        hmYearDataArrayList = new HashMap<>(mgv.hmYearDataArrayList);


        notifyDataSetChanged();
        pb.setVisibility(View.GONE);

    }

    private void getSearchData()
    {
        alData = new ArrayList<>(mgv.alData);
        alSearchData.clear();
        hmYearDataArrayList .clear();
        alYearList.clear();

        for (HashMap<String, String> hm : alData)
        {
            if (hm.get("Year").equals(searchY))
            {
                if (hm.get("Month").equals(searchM))
                {
                    HashMap<String, String> hmtp = new HashMap<>();
                    hmtp.put("CostNo", hm.get("CostNo"));
                    hmtp.put("Cost", hm.get("Cost"));
                    hmtp.put("Description", hm.get("Description"));
                    hmtp.put("Date", hm.get("Date"));
                    hmtp.put("Type", hm.get("Type"));
                    hmtp.put("Month", hm.get("Month"));
                    hmtp.put("Year", hm.get("Year"));
                    hmtp.put("ID", hm.get("ID"));

                    if (hm.containsKey("IncomeType"))
                    {
                        hmtp.put("IncomeType", hm.get("IncomeType"));
                    }

                    alSearchData.add(hmtp);
                }
            }
        }

        alData = new ArrayList<>(alSearchData);
        alYearList.add(searchY);

        ArrayList<HashMap<String, String>> tempData = new ArrayList<>();//日期
        ArrayList<String> alDate = new ArrayList<>();

        for (HashMap<String, String> hm : alData)
        {
            alDate.add(hm.get("Date"));
        }

        compareArrayList(alDate);

        for (String s3 : alDate)
        {
            for (HashMap<String, String> hm : alData)
            {
                if (s3.equals(hm.get("Date")))
                {
                    tempData.add(hm);
                }
            }
        }

        hmYearDataArrayList.put(searchY, tempData);

        notifyDataSetChanged();
        pb.setVisibility(View.GONE);
    }


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

    public RecycleViewGroupAdapter(Context c, Firebase ref, final ProgressBar pb, String sY, String sM)
    {

        this.m_Context = c;
        this.pb = pb;
        this.searchY = sY;
        this.searchM = sM;

        mgv = (MobileGlobalVariable) m_Context.getApplicationContext();

        FilterCondition fc = new FilterCondition();
        fc.setOnSearchConditionListener(this);

        ref.addValueEventListener(new ValueEventListener()
        {
            public void onDataChange(DataSnapshot snapshot)
            {
                if (!searchY.equals("") && !searchM.equals(""))
                {
                    getSearchData(snapshot, searchY, searchM);
                }
                else
                {
                    getAllData(snapshot);
                }
            }

            public void onCancelled(FirebaseError firebaseError)
            {

            }
        });

        if(!searchY.equals(""))
        {
            getSearchData();
        }
        else
        {
            getAllData();
        }
    }

    private void getAllData()
    {
        alData = new ArrayList<>(mgv.alData);
        alSearchData.clear();
        hmYearDataArrayList = new HashMap<>(mgv.hmYearDataArrayList);
        alYearList = new ArrayList<>(mgv.alYearList);

        notifyDataSetChanged();
        pb.setVisibility(View.GONE);
    }

    private void getAllData(DataSnapshot snapshot)
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

    private void getSearchData(DataSnapshot snapshot, String sYear, String sMonth)
    {
        alData.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren())
        {
            if (dataSnapshot.child("Year").getValue().toString().equals(sYear) && dataSnapshot
                    .child("Month").getValue().toString().equals(sMonth))
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
        }

        sortData();
        notifyDataSetChanged();
        pb.setVisibility(View.GONE);
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

            ArrayList<HashMap<String, String>> alTemp    = new ArrayList<>();//年份
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

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {

        TextView     textViewName = holder.textViewName;
        RecyclerView recyclerView = holder.recyclerView;

        ULUIDefine mUIDefine = ULUIDefine.getInstance(m_Context);
        textViewName.setText(alYearList.get(listPosition));
        mUIDefine.setTextSize(FontSize_10u, textViewName);
        textViewName.setTextColor(Color.parseColor("#FF4081"));

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