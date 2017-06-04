package org.universelight.ul.page.dialog;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.universelight.ul.R;

public class FABUpdateDialogActivity extends FABDefault implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        m_iType = bundle.getInt("DIALOG_TYPE");

        switch (m_iType) {
            case 1:
                buildPattyCashUI(getString(R.string.activity_modify_patty_title), bundle);
                break;
            case 2:
                buildCashUI(getString(R.string.activity_modify_cash_title), bundle);
                break;
            case 3:
                buildEstateUI(getString(R.string.activity_modify_estate_title), bundle);
                break;
        }
    }

    private void buildEstateUI(String title, Bundle bundle) {
        m_data = new FABData();
        m_data.CostNo = bundle.getString("CostNo");
        m_data.Cost = bundle.getString("Cost");
        m_data.Description = bundle.getString("Description");
        m_data.Date = bundle.getString("Date");
        m_data.Type = bundle.getString("Type");
        m_data.ID = bundle.getString("ID");
        m_data.IncomeType = bundle.getString("IncomeType");

        setContentView(R.layout.activity_fab_estate_dialog);

        container = (ViewGroup) findViewById(R.id.estate_container);

        TextView tvTitle = (TextView) findViewById(R.id.dialog_estate_title);
        tvTitle.setText(title);

        LinearLayout ll_include = (LinearLayout) findViewById(R.id.ll_estate_include);

        m_rgInOut = (RadioGroup) ll_include.findViewById(R.id
                .rg_estate_inOutCome);
        m_rbIn = (RadioButton) ll_include.findViewById(R.id.rb_estate_income);
        m_rbOut = (RadioButton) findViewById(R.id.rb_estate_outcome);

        m_rgType = (RadioGroup) ll_include.findViewById(R.id.rg_estate_type);
        m_rbDD = (RadioButton) ll_include.findViewById(R.id.rb_estate_dd);
        m_rbCD = (RadioButton) findViewById(R.id.rb_estate_cd);
        m_rbID = (RadioButton) findViewById(R.id.rb_estate_id);

        m_tvDate = (TextView) ll_include.findViewById(R.id.tv_estate_dateBtn);
        m_etCost = (EditText) ll_include.findViewById(R.id.et_estate_cost);
        m_etCostNo = (EditText) ll_include.findViewById(R.id.et_estate_costNo);
        m_etDescription = (EditText) ll_include.findViewById(R.id
                .et_estate_description);

        m_rgInOut.setOnCheckedChangeListener(this);
        m_rgType.setOnCheckedChangeListener(this);
        m_tvDate.setOnClickListener(this);

        //方式一
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupSharedEelementTransitions1();
        }
        //方式二
//        setupSharedEelementTransitions2();

        Button btnSave = (Button) container.findViewById(R.id.estate_save);
        Button btnCancel = (Button) container.findViewById(R.id.estate_cancel);

        m_tvDate.setText(m_data.Date);
        m_etCost.setText(m_data.Cost);
        m_etCostNo.setText(m_data.CostNo);
        m_etDescription.setText(m_data.Description);
        m_strType = m_data.Type;
        strYear = m_data.Date.substring(0, 4);
        strMonth = m_data.Date.substring(4, 6);

        if (m_data.Type.equals("存入")) {
            m_rbIn.setChecked(true);
        } else {
            m_rbOut.setChecked(true);
        }

        if (m_data.IncomeType.equals("活存")) {
            m_rbDD.setChecked(true);
        } else if (m_data.IncomeType.equals("定存")) {
            m_rbCD.setChecked(true);
        } else if (m_data.IncomeType.equals("利息")) {
            m_rbID.setChecked(true);
        }

        btnSave.setText("修改");
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void buildPattyCashUI(String title, Bundle bundle) {
        m_data = new FABData();
        m_data.CostNo = bundle.getString("CostNo");
        m_data.Cost = bundle.getString("Cost");
        m_data.Description = bundle.getString("Description");
        m_data.Date = bundle.getString("Date");
        m_data.Type = bundle.getString("Type");
        m_data.ID = bundle.getString("ID");

        setContentView(R.layout.activity_fab_pattycash_dialog);

        container = (ViewGroup) findViewById(R.id.container);

        TextView tvTitle = (TextView) findViewById(R.id.dialog_title);
        tvTitle.setText(title);

        LinearLayout ll_include = (LinearLayout) findViewById(R.id.ll_include);

        m_rgInOut = (RadioGroup) ll_include.findViewById(R.id.rg_inOutCome);
        m_rbIn = (RadioButton) ll_include.findViewById(R.id.rb_income);
        m_rbOut = (RadioButton) findViewById(R.id.rb_outcome);

        m_tvDate = (TextView) ll_include.findViewById(R.id.tv_dateBtn);
        m_etCost = (EditText) ll_include.findViewById(R.id.et_cost);
        m_etCostNo = (EditText) ll_include.findViewById(R.id.et_costNo);
        m_etDescription = (EditText) ll_include.findViewById(R.id.et_description);

        m_rgInOut.setOnCheckedChangeListener(this);
        m_tvDate.setOnClickListener(this);

        //方式一
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupSharedEelementTransitions1();
        }
        //方式二
//        setupSharedEelementTransitions2();

        Button btnSave = (Button) container.findViewById(R.id.save);
        Button btnCancel = (Button) container.findViewById(R.id.cancel);

        m_tvDate.setText(m_data.Date);
        m_etCost.setText(m_data.Cost);
        m_etCostNo.setText(m_data.CostNo);
        m_etDescription.setText(m_data.Description);
        m_strType = m_data.Type;
        strYear = m_data.Date.substring(0, 4);
        strMonth = m_data.Date.substring(4, 6);

        if (m_data.Type.equals("收入")) {
            m_rbIn.setChecked(true);
        } else {
            m_rbOut.setChecked(true);
        }

        btnSave.setText("修改");
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    private void buildCashUI(String title, Bundle bundle) {
        m_data = new FABData();
        m_data.CostNo = bundle.getString("CostNo");
        m_data.Cost = bundle.getString("Cost");
        m_data.Description = bundle.getString("Description");
        m_data.Date = bundle.getString("Date");
        m_data.Type = bundle.getString("Type");
        m_data.ID = bundle.getString("ID");

        setContentView(R.layout.activity_fab_cash_dialog);

        container = (ViewGroup) findViewById(R.id.cash_container);

        TextView tvTitle = (TextView) findViewById(R.id.dialog_cash_title);
        tvTitle.setText(title);

        LinearLayout ll_include = (LinearLayout) findViewById(R.id.ll_cash_include);

        final Spinner m_spList = (Spinner) ll_include.findViewById(R.id.spinner_cash);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.details,
                R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        m_spList.setAdapter(adapter);
        m_spList.setOnItemSelectedListener(this);

        m_tvDate = (TextView) ll_include.findViewById(R.id.tv_cash_dateBtn);
        m_etCost = (EditText) ll_include.findViewById(R.id.et_cash_cost);
        m_etCostNo = (EditText) ll_include.findViewById(R.id.et_cash_costNo);
        m_etDescription = (EditText) ll_include.findViewById(R.id
                .et_cash_description);

        m_tvDate.setOnClickListener(this);

        //方式一
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupSharedEelementTransitions1();
        }
        //方式二
//        setupSharedEelementTransitions2();

        Button btnSave = (Button) container.findViewById(R.id.cash_save);
        Button btnCancel = (Button) container.findViewById(R.id.cash_cancel);

        m_tvDate.setText(m_data.Date);
        m_etCost.setText(m_data.Cost);
        m_etCostNo.setText(m_data.CostNo);
        m_etDescription.setText(m_data.Description);
        m_strType = m_data.Type;
        strYear = m_data.Date.substring(0, 4);
        strMonth = m_data.Date.substring(4, 6);

        btnSave.setText("修改");
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_cash_dateBtn:
            case R.id.tv_dateBtn:
            case R.id.tv_estate_dateBtn:
                setDateDialog(FABUpdateDialogActivity.this);
                break;

            case R.id.cash_save:
            case R.id.save:
            case R.id.estate_save:
                updateData(FABUpdateDialogActivity.this);
                break;

            case R.id.cash_cancel:
            case R.id.cancel:
            case R.id.estate_cancel:
                dismiss();
                break;
        }
    }
}
