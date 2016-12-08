package org.universelight.ul.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by whitesnake on 2016/7/18.
 */
public class FireBaseClass
{
    ProgressDialog progressDialog;
    String path = "";
    FirebaseDatabase  db           = FirebaseDatabase.getInstance();

    public void saveDataToFireBase(final Activity a, final Context c, HashMap hm , int type)
    {
        progressDialog = ProgressDialog.show(a, "請稍等...", "資料更新中...", true);

        switch (type)
        {
            case 1:
                path = "PattyCash";
                break;
            case 2:
                path = "Cash";
                break;
            case 3:
                path = "Estate";
                break;
        }


        SharedPreferences  LoginStatus = c.getSharedPreferences("USER", 0);
        String userUid     = LoginStatus.getString("UID", "");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date             curDate   = new Date(System.currentTimeMillis());
        String           str       = formatter.format(curDate);

        hm.put("ID",str);

        DatabaseReference Ref = db.getReference(path);
        Ref.child(userUid + "-" + str).setValue(hm, new DatabaseReference
                .CompletionListener()

        {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference
                    databaseReference)
            {
                progressDialog.dismiss();


                if(databaseError!=null)
                {
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            a.setResult(Activity.RESULT_CANCELED);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                a.finishAfterTransition();
                            }
                            else
                            {
                                a.finish();
                            }
                        }
                    };

                    Util.showLog(c,"此帳號無權限進行此操作。" , listener);
                }
                else
                {
                    a.setResult(Activity.RESULT_CANCELED);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        a.finishAfterTransition();
                    }
                    else
                    {
                        a.finish();
                    }
                }
            }
        });
    }

    public void updateDataToFireBase(final Activity a, final Context c, HashMap hm , int type)
    {
        progressDialog = ProgressDialog.show(a, "請稍等...", "資料更新中...", true);

        switch (type)
        {
            case 1:
                path = "PattyCash";
                break;
            case 2:
                path = "Cash";
                break;
            case 3:
                path = "Estate";
                break;
        }


        SharedPreferences  LoginStatus = c.getSharedPreferences("USER", 0);
        String userUid     = LoginStatus.getString("UID", "");


        DatabaseReference Ref = db.getReference(path);

        Ref.child(userUid + "-" + hm.get("ID")).updateChildren(hm, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                progressDialog.dismiss();

                if(databaseError!=null)
                {
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            a.setResult(Activity.RESULT_CANCELED);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                a.finishAfterTransition();
                            }
                            else
                            {
                                a.finish();
                            }
                        }
                    };

                    Util.showLog(c,"此帳號無權限進行此操作。" , listener);
                }
                else
                {
                    a.setResult(Activity.RESULT_CANCELED);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        a.finishAfterTransition();
                    }
                    else
                    {
                        a.finish();
                    }
                }
            }
        });
    }

    public void deleteDataToFireBase(final Context c, HashMap hm , int type)
    {
        progressDialog = ProgressDialog.show(c, "請稍等...", "資料更新中...", true);

        switch (type)
        {
            case 1:
                path = "PattyCash";
                break;
            case 2:
                path = "Cash";
                break;
            case 3:
                path = "Estate";
                break;
        }


        SharedPreferences  LoginStatus = c.getSharedPreferences("USER", 0);
        String userUid     = LoginStatus.getString("UID", "");

        DatabaseReference Ref = db.getReference(path + "/" + userUid + "-" + hm.get("ID"));

        Ref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                progressDialog.dismiss();
                if(databaseError!=null)
                {

                    Util.showLog(c,"此帳號無權限進行此操作。");
                }

            }
        });
    }


}
