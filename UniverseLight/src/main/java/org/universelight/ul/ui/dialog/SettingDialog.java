package org.universelight.ul.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.universelight.ul.R;

/**
 * Created by whitesnake on 2016/12/29.
 */

public class SettingDialog
{
    public SettingDialog(Context c)
    {
        final SharedPreferences LoginStatus = c.getSharedPreferences("USER", Context.MODE_PRIVATE);

        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.dialog_setting);

        Switch sw = (Switch) dialog.findViewById(R.id.sw_fingerprint);

        if(LoginStatus.getString("FingerPrint", "0").equals("1"))
        {
            sw.setChecked(true);
        }
        else
        {
            sw.setChecked(false);
        }

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    LoginStatus.edit().putString("FingerPrint", "1").apply();
                }
                else
                {
                    LoginStatus.edit().putString("FingerPrint", "0").apply();
                }
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvBtn = (TextView) dialog.findViewById(R.id.tv_close);
        tvBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
