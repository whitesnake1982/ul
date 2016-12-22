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

import java.lang.reflect.Field;

/**
 * Created by hsinhenglin on 2016/12/22.
 */

public class CustomDatePickerDialog {
    public static DatePickerDialog createMonthYearDatePicker(Context ctx, int year, int month, int day, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dpd = new DatePickerDialog(ctx, AlertDialog.THEME_HOLO_LIGHT, listener, year, month, day) {
            //複寫onStop，讓取消時 ，不呼叫 onDateSet方法
            @Override
            protected void onStop() {
                //super.onStop();
            }
        };
        dpd.setButton(DialogInterface.BUTTON_NEGATIVE, ctx.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ((ViewGroup) dpd.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        dpd.setTitle("");

        return dpd;
    }

    ;


}