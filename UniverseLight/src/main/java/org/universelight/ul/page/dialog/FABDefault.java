package org.universelight.ul.page.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.universelight.ul.R;
import org.universelight.ul.morph.MorphDialogToFab;
import org.universelight.ul.morph.MorphFabToDialog;
import org.universelight.ul.morph.MorphTransition;
import org.universelight.ul.util.FireBaseClass;
import org.universelight.ul.util.Util;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by hsinhenglin on 16/9/19.
 */
public class FABDefault extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener
{
    public ViewGroup container;
    public String m_strType       = "";
    public String m_strIncomeType = "";
    public int    m_iType         = 0;
    public        String  strMonth;
    public        String  strYear;
    public static FABData m_data;

    public RadioGroup m_rgInOut, m_rgType;
    public RadioButton m_rbIn, m_rbOut, m_rbDD, m_rbCD, m_rbID;
    public EditText m_etCostNo, m_etCost, m_etDescription;
    public TextView m_tvDate;

    /**
     * 使用方式一：调用setupSharedEelementTransitions1方法
     * 使用这种方式的话需要的类是 MorphDrawable, MorphFabToDialog, MorphDialogToFab
     */
    public void setupSharedEelementTransitions1()
    {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator
                .fast_out_slow_in);

        MorphFabToDialog sharedEnter = new MorphFabToDialog();
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphDialogToFab sharedReturn = new MorphDialogToFab();
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (container != null)
        {
            sharedEnter.addTarget(container);
            sharedReturn.addTarget(container);
        }
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    /**
     * 使用方式二：调用setupSharedEelementTransitions2方法
     * 使用这种方式的话需要的类是 MorphDrawable, MorphTransition
     */
    public void setupSharedEelementTransitions2()
    {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator
                .fast_out_slow_in);

        //hujiawei 100是随意给的一个数字，可以修改，需要注意的是这里调用container.getHeight()结果为0
        MorphTransition sharedEnter = new MorphTransition(ContextCompat.getColor(this, R.color
                .fab_background_color),
                ContextCompat.getColor(this, R.color.dialog_background_color), 100, getResources
                ().getDimensionPixelSize(R.dimen.dialog_corners), true);
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphTransition sharedReturn = new MorphTransition(ContextCompat.getColor(this, R.color
                .dialog_background_color),
                ContextCompat.getColor(this, R.color.fab_background_color), getResources()
                .getDimensionPixelSize(R.dimen.dialog_corners), 100, false);
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (container != null)
        {
            sharedEnter.addTarget(container);
            sharedReturn.addTarget(container);
        }
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    public void onBackPressed()
    {
        dismiss();
    }

    public void dismiss()
    {
        setResult(Activity.RESULT_CANCELED);
        finishAfterTransition();
    }

    public void setDateDialog(AppCompatActivity f)
    {
        final Calendar c     = Calendar.getInstance();
        int            year  = c.get(Calendar.YEAR);
        int            month = c.get(Calendar.MONTH);
        int            day   = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(f,
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {

                        String dateStr;
                        String month;
                        String day;

                        if ((monthOfYear + 1) < 10)
                        {
                            month = "0" + (monthOfYear + 1);
                        }
                        else
                        {
                            month = String.valueOf(monthOfYear + 1);
                        }

                        if (dayOfMonth < 10)
                        {
                            day = "0" + dayOfMonth;
                        }
                        else
                        {
                            day = String.valueOf(dayOfMonth);
                        }

                        strMonth = month;
                        strYear = String.valueOf(year);

                        dateStr = strYear + strMonth + day;
                        m_tvDate.setText(dateStr);

                        strYear = dateStr.substring(0, 4);
                        strMonth = dateStr.substring(4, 6);


                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public boolean checkInputData(AppCompatActivity f, EditText m_etCost, EditText m_etDescription, TextView m_tvDate) {
        if (m_etDescription.getText().toString().equals("")) {
            Util.showLog(f, getString(R.string.dialog_FABDefault_check_msg1));
            return false;
        } else if (m_strIncomeType.equals("") && m_iType == 3) {
            Util.showLog(f, getString(R.string.dialog_FABDefault_check_msg2));
            return false;
        } else if (m_etCost.getText().toString().equals("")) {
            Util.showLog(f, getString(R.string.dialog_FABDefault_check_msg3));
            return false;
        } else if (m_tvDate.getText().toString().equals(getString(R.string.common_choose))) {
            Util.showLog(f, getString(R.string.dialog_FABDefault_check_msg4));
            return false;
        } else if (m_strType.equals("")) {
            Util.showLog(f, getString(R.string.dialog_FABDefault_check_msg5));
            return false;
        } else {
            return true;
        }
    }

    public void updateData(AppCompatActivity f)
    {
        if (checkInputData(f, m_etCost, m_etDescription, m_tvDate)) {
            HashMap<String, String> hmtp = new HashMap<>();
            hmtp.put("CostNo", m_etCostNo.getText().toString());
            hmtp.put("Cost", m_etCost.getText().toString());
            hmtp.put("Description", m_etDescription.getText().toString());
            hmtp.put("Date", m_tvDate.getText().toString());
            hmtp.put("Type", m_strType);
            hmtp.put("Month", strMonth);
            hmtp.put("Year", strYear);
            hmtp.put("ID", m_data.ID);

            if(!m_strIncomeType.equals(""))
            {
                hmtp.put("IncomeType", m_strIncomeType);
            }

            FireBaseClass fbc = new FireBaseClass();
            fbc.updateDataToFireBase(f, f, hmtp, m_iType);
        }
    }

    public void saveData(AppCompatActivity f)
    {
        if (checkInputData(f, m_etCost, m_etDescription, m_tvDate)) {
            HashMap<String, String> hmtp = new HashMap<>();
            hmtp.put("CostNo", m_etCostNo.getText().toString());
            hmtp.put("Cost", m_etCost.getText().toString());
            hmtp.put("Description", m_etDescription.getText().toString());
            hmtp.put("Date", m_tvDate.getText().toString());
            hmtp.put("Type", m_strType);
            hmtp.put("Month", strMonth);
            hmtp.put("Year", strYear);

            if(!m_strIncomeType.equals(""))
            {
                hmtp.put("IncomeType", m_strIncomeType);
            }

            FireBaseClass fbc = new FireBaseClass();
            fbc.saveDataToFireBase(f, f, hmtp, m_iType);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_estate_income:
                m_rbIn.setChecked(true);
                m_strType = m_rbIn.getText().toString();
                break;
            case R.id.rb_estate_outcome:
                m_rbOut.setChecked(true);
                m_strType = m_rbOut.getText().toString();
                break;
            case R.id.rb_estate_dd:
                m_rbDD.setChecked(true);
                m_strIncomeType = m_rbDD.getText().toString();
                break;
            case R.id.rb_estate_cd:
                m_rbCD.setChecked(true);
                m_strIncomeType = m_rbCD.getText().toString();
                break;
            case R.id.rb_estate_id:
                m_rbID.setChecked(true);
                m_strIncomeType = m_rbID.getText().toString();
                break;
            case R.id.rb_income:
                m_rbIn.setChecked(true);
                m_strType = m_rbIn.getText().toString();
                break;
            case R.id.rb_outcome:
                m_rbOut.setChecked(true);
                m_strType = m_rbOut.getText().toString();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        m_strType = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
