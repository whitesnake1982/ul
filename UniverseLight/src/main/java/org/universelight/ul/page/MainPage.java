package org.universelight.ul.page;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import org.universelight.ul.R;
import org.universelight.ul.objects.MobileGlobalVariable;
import org.universelight.ul.page.dialog.FABUpdateDialogActivity;
import org.universelight.ul.ui.adapter.SectionsPagerAdapter;
import org.universelight.ul.page.dialog.FABDialogActivity;
import org.universelight.ul.page.fragment.CashFragment;
import org.universelight.ul.page.fragment.EstateFragment;
import org.universelight.ul.page.fragment.PattyCashFragment;
import org.universelight.ul.ui.dialog.CustomDatePickerDialog;
import org.universelight.ul.util.CardViewGetDeleteID;
import org.universelight.ul.util.CardViewGetID;
import org.universelight.ul.util.FireBaseClass;
import org.universelight.ul.util.Util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MainPage extends AppCompatActivity implements View.OnClickListener, CardViewGetID.CardOnClickListener, CardViewGetDeleteID.CardOnDeleteClickListener{
    public Context mPage;
    private FloatingActionButton fab;
    private int onPageScrolledPrePosition = -1;
    private String[] m_strTitle = new String[]{"零用金明細",
            "現金明細",
            "動資產明細"};

    public static String TAB_TITLE = "零用金明細";
    public HashMap<String, String> m_hmData = null;
    private MobileGlobalVariable mgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPage = MainPage.this;
        mgv = (MobileGlobalVariable) mPage.getApplicationContext();
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        CardViewGetID cardViewGetID = new CardViewGetID();
        cardViewGetID.setCardOnClickListener(this);

        CardViewGetDeleteID cardViewGetDeleteID = new CardViewGetDeleteID();
        cardViewGetDeleteID.setCardOnDeleteClickListener(this);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(PattyCashFragment.newInstance(), m_strTitle[0]);
        mSectionsPagerAdapter.addFragment(CashFragment.newInstance(), m_strTitle[1]);
        mSectionsPagerAdapter.addFragment(EstateFragment.newInstance(), m_strTitle[2]);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != onPageScrolledPrePosition) {
                    onPageScrolledPrePosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = CustomDatePickerDialog.createMonthYearDatePicker(mPage, mYear, mMonth, mDay, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Util.showLog(mPage, "Year:" + String.valueOf(year) + " " + "Month:" + String.valueOf(monthOfYear + 1));
                }
            });
            dpd.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.fab:
                if(null == m_hmData)
                {
                    openFABDialog(0);
                }
                else
                {
                    openFABDialog(1);
                }
                break;
        }
    }

    @Override
    public void OnClickListener(HashMap<String, String> id) {
        Log.e("HasClick" , id.get("ID"));
        m_hmData = id;
        fab.performClick();
    }

    public void openFABDialog(int type)
    {
        int iTab = onPageScrolledPrePosition;
        int RC_LOGIN = 100;

        if (iTab < 0) {
            iTab = 0;
        }

        TAB_TITLE = m_strTitle[iTab];
        Intent intent;
        ActivityOptions options;
        Bundle b = new Bundle();

        if(0 == type) {

            intent = new Intent(MainPage.this, FABDialogActivity.class);

            if (TAB_TITLE.equals(m_strTitle[0])) {
                b.putInt("DIALOG_TYPE", 1);
                intent.putExtras(b);
            } else if (TAB_TITLE.equals(m_strTitle[1])) {
                b.putInt("DIALOG_TYPE", 2);
                intent.putExtras(b);
            } else if (TAB_TITLE.equals(m_strTitle[2])) {
                b.putInt("DIALOG_TYPE", 3);
                intent.putExtras(b);
            }
        }
        else
        {

            intent = new Intent(MainPage.this, FABUpdateDialogActivity.class);

            if (TAB_TITLE.equals(m_strTitle[0])) {
                b.putInt("DIALOG_TYPE", 1);
                b.putString("CostNo", m_hmData.get("CostNo"));
                b.putString("Cost", m_hmData.get("Cost"));
                b.putString("Description", m_hmData.get("Description"));
                b.putString("Date", m_hmData.get("Date"));
                b.putString("Type", m_hmData.get("Type"));
                b.putString("ID", m_hmData.get("ID"));
            } else if (TAB_TITLE.equals(m_strTitle[1])) {
                b.putInt("DIALOG_TYPE", 2);
                b.putString("CostNo", m_hmData.get("CostNo"));
                b.putString("Cost", m_hmData.get("Cost"));
                b.putString("Description", m_hmData.get("Description"));
                b.putString("Date", m_hmData.get("Date"));
                b.putString("Type", m_hmData.get("Type"));
                b.putString("ID", m_hmData.get("ID"));
            } else if (TAB_TITLE.equals(m_strTitle[2])) {
                b.putInt("DIALOG_TYPE", 3);
                b.putString("CostNo", m_hmData.get("CostNo"));
                b.putString("Cost", m_hmData.get("Cost"));
                b.putString("Description", m_hmData.get("Description"));
                b.putString("Date", m_hmData.get("Date"));
                b.putString("Type", m_hmData.get("Type"));
                b.putString("ID", m_hmData.get("ID"));
                b.putString("IncomeType", m_hmData.get("IncomeType"));
            }
            intent.putExtras(b);
        }

        m_hmData = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(MainPage.this, fab, getString(R.string.title_activity_patty_cash_dialog));
            startActivityForResult(intent, RC_LOGIN, options.toBundle());
        }
        else
        {
            startActivityForResult(intent, RC_LOGIN);
        }

    }

    @Override
    public void OnDeleteClickListener(HashMap<String, String> id)
    {

        final HashMap hm = id;

        Util.showConfirm(mPage, "\t\t項目\t：\t" + hm.get("Description") + "\n\t\t日期\t：\t" + hm.get("Date") + "\n\t\t單號\t：\t" + hm.get("CostNo") + "\n\n\t\t是否刪除此筆資料？" , new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int iTab = onPageScrolledPrePosition;
                int type = -1;

                if (iTab < 0) {
                    iTab = 0;
                }

                TAB_TITLE = m_strTitle[iTab];

                if (TAB_TITLE.equals(m_strTitle[0])) {
                    type = 1;
                } else if (TAB_TITLE.equals(m_strTitle[1])) {
                    type = 2;
                } else if (TAB_TITLE.equals(m_strTitle[2])) {
                    type = 3;
                }

                FireBaseClass fbc = new FireBaseClass();
                fbc.deleteDataToFireBase(mPage, hm, type);
            }
        });
    }
}
