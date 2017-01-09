package org.universelight.ul.page;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import org.universelight.ul.objects.MobileGlobalVariable;
import org.universelight.ul.ui.ULUIDefine;

/**
 * Created by hsinhenglin on 2017/1/9.
 */

public class BaseActivity extends AppCompatActivity {
    public Context mPage;
    public MobileGlobalVariable mgv;
    public ULUIDefine mUIDefine;

    public void initVariables(AppCompatActivity activity)
    {
        mPage = activity;
        mgv = (MobileGlobalVariable)mPage.getApplicationContext();
        mUIDefine = ULUIDefine.getInstance(mPage);
    }

}
