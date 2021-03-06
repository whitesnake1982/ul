package org.universelight.ul.ui.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.universelight.ul.R;

/**
 * Created by hsinhenglin on 2016/12/22.
 */

public class CustomDatePickerDialog {
    public static DatePickerDialog createMonthYearDatePicker(Context ctx, int id, int year, int month, int day, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dpd = new DatePickerDialog(ctx, AlertDialog.THEME_HOLO_LIGHT, listener, year, month, day) {
            //複寫onStop，讓取消時 ，不呼叫 onDateSet方法
            @Override
            protected void onStop() {
                //super.onStop();
            }

            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {

            }
        };
        dpd.setButton(DialogInterface.BUTTON_NEGATIVE, ctx.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ((ViewGroup) dpd.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        switch (id)
        {
            case R.id.action_search:
                dpd.setTitle(ctx.getString(R.string.dialog_query_title));
                break;
            case R.id.action_output:
                dpd.setTitle(ctx.getString(R.string.dialog_output_title));
                break;
        }

        return dpd;
    }
}