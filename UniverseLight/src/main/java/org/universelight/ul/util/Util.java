package org.universelight.ul.util;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;

/**
 * Created by whitesnake on 2016/7/12.
 */
public class Util
{
    public static void showLog(Context c, String msg)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle("注意");
        dialog.setCancelable(false);
        dialog.create().setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton("確定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        dialog.show();
    }

    public static void showLog(Context c, String msg, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle("注意");
        dialog.setCancelable(false);
        dialog.create().setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton("確定", listener);
        dialog.show();
    }

    public static void showConfirm(Context c, String msg, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle("刪除選取項目");
        dialog.setCancelable(false);
        dialog.create().setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton("是", listener);
        dialog.setNegativeButton("否", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static boolean checkFingerPrintService(Context c, KeyguardManager km, FingerprintManager fm) {
        return km.isKeyguardSecure() && fm.isHardwareDetected() && fm.hasEnrolledFingerprints();
    }
}
