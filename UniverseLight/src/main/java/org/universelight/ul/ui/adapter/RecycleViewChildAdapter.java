package org.universelight.ul.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.universelight.ul.R;
import org.universelight.ul.ui.ULUIDefine;
import org.universelight.ul.util.CardViewGetDeleteID;
import org.universelight.ul.util.CardViewGetID;

import java.util.ArrayList;
import java.util.HashMap;

import static org.universelight.ul.ui.ULUIDefine.FontSize_6u;
import static org.universelight.ul.ui.ULUIDefine.FontSize_8u;

/**
 * Created by hsinheng on 16/7/19.
 */
class RecycleViewChildAdapter extends RecyclerView.Adapter<RecycleViewChildAdapter
        .ChildViewHolder>
{

    private ArrayList<HashMap<String, String>> alData;

    private static ULUIDefine mUIDefine;

    RecycleViewChildAdapter(Context c, ArrayList<HashMap<String, String>> hashMaps)
    {
        this.alData = hashMaps;
        mUIDefine = ULUIDefine.getInstance(c);
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder
    {

        CardView cvCard;
        LinearLayout llContent;
        TextView tvChildTitle;
        TextView tvDateValue, tvDateTitle;
        TextView tvCostNOValue, tvCostNOTitle;
        TextView tvTypeValue, tvTypeTitle;
        TextView tvCostValue, tvCostTitle;

        ChildViewHolder(View itemView)
        {
            super(itemView);
            this.cvCard = (CardView) itemView.findViewById(R.id.card_view);
            this.llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            this.tvChildTitle = (TextView) itemView.findViewById(R.id.tv_child_title);
            this.tvDateValue = (TextView) itemView.findViewById(R.id.tv_child_date_value);
            this.tvDateTitle = (TextView) itemView.findViewById(R.id.tv_child_date_title);
            this.tvCostNOValue = (TextView) itemView.findViewById(R.id.tv_child_cost_number_value);
            this.tvCostNOTitle = (TextView) itemView.findViewById(R.id.tv_child_cost_number_title);
            this.tvTypeValue = (TextView) itemView.findViewById(R.id.tv_child_type_value);
            this.tvTypeTitle = (TextView) itemView.findViewById(R.id.tv_child_type_title);
            this.tvCostValue = (TextView) itemView.findViewById(R.id.tv_child_cost_value);
            this.tvCostTitle = (TextView) itemView.findViewById(R.id.tv_child_cost_title);
        }
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_child, parent, false);

        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int listPosition)
    {
        final CardView cvCard        = holder.cvCard;
        TextView       tvChildTitle  = holder.tvChildTitle;
        TextView       tvDateValue   = holder.tvDateValue;
        TextView       tvDateTitle   = holder.tvDateTitle;
        TextView       tvCostNOValue = holder.tvCostNOValue;
        TextView       tvCostNOTitle = holder.tvCostNOTitle;
        TextView       tvTypeValue   = holder.tvTypeValue;
        TextView       tvTypeTitle   = holder.tvTypeTitle;
        TextView       tvCostValue   = holder.tvCostValue;
        TextView       tvCostTitle   = holder.tvCostTitle;

        mUIDefine.setTextSize(FontSize_8u, tvChildTitle);
        mUIDefine.setTextSize(FontSize_6u, tvDateValue);
        mUIDefine.setTextSize(FontSize_6u, tvDateTitle);
        mUIDefine.setTextSize(FontSize_6u, tvCostNOValue);
        mUIDefine.setTextSize(FontSize_6u, tvCostNOTitle);
        mUIDefine.setTextSize(FontSize_6u, tvTypeValue);
        mUIDefine.setTextSize(FontSize_6u, tvTypeTitle);
        mUIDefine.setTextSize(FontSize_6u, tvCostValue);
        mUIDefine.setTextSize(FontSize_6u, tvCostTitle);

        int    i = listPosition - 1;
        String strMonth;
        int income = 0;
        int outcome = 0;
        String strHtml;

        final int iPosition = listPosition;

        for(int j = 0; j < alData.size(); j++)
        {
            if (alData.get(listPosition).get("Month").equals(alData.get(j).get("Month")))
            {
                if(alData.get(j).get("Type").contains("收入") || alData.get(j).get("Type").contains("存入"))
                {
                        income = income + Integer.parseInt(alData.get(j).get("Cost"));
                }
                else
                {
                    outcome = outcome + Integer.parseInt(alData.get(j).get("Cost"));
                }
            }
        }

        if (alData.get(listPosition).containsKey("IncomeType"))
        {
            strMonth = alData.get(listPosition).get("Month") + "月收支" +
                    "\n餘額：" + String.valueOf(income - outcome) +
                    "\n項目：" + alData.get(listPosition).get("Description") + " - " + alData.get(listPosition).get
                    ("IncomeType");

            strHtml = "<html><body><a><font color=\"#125688\">" + alData.get(listPosition).get("Month") + "月收支" + "</a>" +
                    "<a><font color=\"#125688\">" + "<br>餘額：" + String.valueOf(income - outcome) + "</a><br>" +
                    "<br>項目：" + alData.get(listPosition).get("Description") + " - " + alData.get(listPosition).get
                    ("IncomeType") + "</body></html>";
//            textView.setText(Html.fromHtml(html1));

        }
        else
        {
            strMonth = alData.get(listPosition).get("Month") + "月收支" +
                    "\n餘額：" + String.valueOf(income - outcome) +
                    "\n項目：" + alData.get(listPosition).get("Description");

            strHtml = "<html><body><a><font color=\"#125688\">" + alData.get(listPosition).get("Month") + "月收支" + "</a>" +
                    "<a><font color=\"#125688\">" + "<br>餘額：" + String.valueOf(income - outcome) + "</a><br>" +
                    "<br>項目：" + alData.get(listPosition).get("Description") + "</body></html>";

        }


        if (i >= 0)
        {
            if (alData.get(listPosition).get("Month").equals(alData.get(i).get("Month")))
            {
                if (alData.get(listPosition).containsKey("IncomeType"))
                {
                    strMonth = "項目：" + alData.get
                            (listPosition).get("Description") + " - " + alData.get(listPosition)
                            .get("IncomeType");
                }
                else
                {
                    strMonth = "項目：" + alData.get
                            (listPosition).get("Description");
                }
                tvChildTitle.setText(strMonth);
            }
            else
            {
//                tvChildTitle.setText(strMonth);
                tvChildTitle.setText(Html.fromHtml(strHtml));
            }
        }
        else
        {
//            tvChildTitle.setText(strMonth);
            tvChildTitle.setText(Html.fromHtml(strHtml));
        }


        tvDateValue.setText(alData.get(listPosition).get("Date"));
        tvCostNOValue.setText(alData.get(listPosition).get("CostNo"));

        if(alData.get(listPosition).get("Type").contains("收入"))
        {
            tvTypeValue.setTextColor(Color.DKGRAY);
            tvCostValue.setTextColor(Color.DKGRAY);
        }
        else if (alData.get(listPosition).get("Type").contains("存入"))
        {
            tvTypeValue.setTextColor(Color.DKGRAY);
            tvCostValue.setTextColor(Color.DKGRAY);
        }
        else
        {
            tvTypeValue.setTextColor(Color.RED);
            tvCostValue.setTextColor(Color.RED);
        }

        tvTypeValue.setText(alData.get(listPosition).get("Type"));

        tvCostValue.setText(alData.get(listPosition).get("Cost"));
        cvCard.setTag(alData.get(listPosition).get("ID"));
        cvCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CardViewGetID.cardOnClickListener.OnClickListener(alData.get(iPosition));
            }
        });

        cvCard.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                CardViewGetDeleteID.cardOnDeleteClickListener.OnDeleteClickListener(alData.get(iPosition));
                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return alData.size();
    }
}