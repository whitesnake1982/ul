package org.universelight.ul.page;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import org.universelight.ul.R;
import org.universelight.ul.objects.MobileGlobalVariable;
import org.universelight.ul.page.dialog.FABUpdateDialogActivity;
import org.universelight.ul.ui.adapter.SectionsPagerAdapter;
import org.universelight.ul.page.dialog.FABDialogActivity;
import org.universelight.ul.page.fragment.CashFragment;
import org.universelight.ul.page.fragment.EstateFragment;
import org.universelight.ul.page.fragment.PattyCashFragment;
import org.universelight.ul.ui.dialog.CustomDatePickerDialog;
import org.universelight.ul.ui.dialog.SettingDialog;
import org.universelight.ul.util.CardViewGetDeleteID;
import org.universelight.ul.util.CardViewGetID;
import org.universelight.ul.util.FilterCondition;
import org.universelight.ul.util.FireBaseClass;
import org.universelight.ul.util.Util;

import java.util.Calendar;
import java.util.HashMap;

public class MainPage extends BaseActivity implements View.OnClickListener, CardViewGetID.CardOnClickListener, CardViewGetDeleteID.CardOnDeleteClickListener{

    public static final int FAB_DIALOG_NO_DATA = 0;
    public static final int FAB_DIALOG_WITH_DATA = 1;
    public static final int PAGE_TITLE_PC = 0;
    public static final int PAGE_TITLE_C = 1;
    public static final int PAGE_TITLE_E = 2;

    private FloatingActionButton fab;
    private int onPageScrolledPrePosition = -1;

    public static String TAB_TITLE;
    public HashMap<String, String> m_hmData = null;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables(MainPage.this);

        TAB_TITLE = mPage.getString(R.string.activity_main_page_title_pc);

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
        mSectionsPagerAdapter.addFragment(PattyCashFragment.newInstance(), getString(R.string.activity_main_page_title_pc));
        mSectionsPagerAdapter.addFragment(CashFragment.newInstance(), getString(R.string.activity_main_page_title_c));
        mSectionsPagerAdapter.addFragment(EstateFragment.newInstance(), getString(R.string.activity_main_page_title_e));

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onPageScrolledPrePosition = position;
                mViewPager.setCurrentItem(onPageScrolledPrePosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                onPageScrolledPrePosition = tab.getPosition();
                mViewPager.setCurrentItem(onPageScrolledPrePosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            DatePickerDialog dpd = getDatePicker(id);
            dpd.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.button_neutral), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mgv.strSearchYear = "";
                    mgv.strSearchMonth = "";
                    FilterCondition.onSearchConditionListener.getSearchOptions(mgv.strSearchYear, mgv.strSearchMonth);
                    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    mSectionsPagerAdapter.addFragment(PattyCashFragment.newInstance(), getString(R.string.activity_main_page_title_pc));
                    mSectionsPagerAdapter.addFragment(CashFragment.newInstance(), getString(R.string.activity_main_page_title_c));
                    mSectionsPagerAdapter.addFragment(EstateFragment.newInstance(), getString(R.string.activity_main_page_title_e));

                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    mViewPager.setCurrentItem(onPageScrolledPrePosition);
                }
            });
            dpd.show();
            return true;
        }
        else if (id == R.id.action_output)
        {
            DatePickerDialog dpd = getDatePicker(id);
            dpd.show();
            return true;
        }
        else if (id == R.id.action_option)
        {
            SettingDialog sd = new SettingDialog(mPage);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private DatePickerDialog getDatePicker(final int id)
    {
        Calendar c = Calendar.getInstance();
        int      iYear = c.get(Calendar.YEAR);
        int      iMonth = c.get(Calendar.MONTH);
        int      iDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog d;
        d = CustomDatePickerDialog.createMonthYearDatePicker(mPage, id, iYear, iMonth, iDay, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mgv.strSearchYear = String.valueOf(year);

                String month;

                if(monthOfYear + 1 < 10)
                {
                    month = "0" + String.valueOf(monthOfYear + 1);
                }
                else
                {
                    month = String.valueOf(monthOfYear + 1);
                }
                mgv.strSearchMonth = month;

                switch (id)
                {
                    case R.id.action_search:

                        FilterCondition.onSearchConditionListener.getSearchOptions(mgv.strSearchYear, mgv.strSearchMonth);
                        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                        mSectionsPagerAdapter.addFragment(PattyCashFragment.newInstance(), getString(R.string.activity_main_page_title_pc));
                        mSectionsPagerAdapter.addFragment(CashFragment.newInstance(), getString(R.string.activity_main_page_title_c));
                        mSectionsPagerAdapter.addFragment(EstateFragment.newInstance(), getString(R.string.activity_main_page_title_e));

                        mViewPager.setAdapter(mSectionsPagerAdapter);
                        mViewPager.setCurrentItem(onPageScrolledPrePosition);

                        break;
                    case R.id.action_output:
                        //TODO PDF輸出處理
                        break;
                }

            }
        });

        return d;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.fab:
                if(null == m_hmData)
                {
                    openFABDialog(FAB_DIALOG_NO_DATA);
                }
                else
                {
                    openFABDialog(FAB_DIALOG_WITH_DATA);
                }
                break;
        }
    }

    @Override
    public void OnClickListener(HashMap<String, String> id) {
        m_hmData = id;
        fab.performClick();
    }

    public void openFABDialog(int type)
    {
        int iTab = onPageScrolledPrePosition;
        int RC_LOGIN = 100;

        setTabTitle(iTab);

        Intent intent;
        ActivityOptions options;
        Bundle b = new Bundle();

        if(0 == type) {

            intent = new Intent(MainPage.this, FABDialogActivity.class);

            if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_pc))) {
                b.putInt("DIALOG_TYPE", 1);
                intent.putExtras(b);
            } else if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_c))) {
                b.putInt("DIALOG_TYPE", 2);
                intent.putExtras(b);
            } else if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_e))) {
                b.putInt("DIALOG_TYPE", 3);
                intent.putExtras(b);
            }
        }
        else
        {

            intent = new Intent(MainPage.this, FABUpdateDialogActivity.class);

            if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_pc))) {
                b.putInt("DIALOG_TYPE", 1);
                b.putString("CostNo", m_hmData.get("CostNo"));
                b.putString("Cost", m_hmData.get("Cost"));
                b.putString("Description", m_hmData.get("Description"));
                b.putString("Date", m_hmData.get("Date"));
                b.putString("Type", m_hmData.get("Type"));
                b.putString("ID", m_hmData.get("ID"));
            } else if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_c))) {
                b.putInt("DIALOG_TYPE", 2);
                b.putString("CostNo", m_hmData.get("CostNo"));
                b.putString("Cost", m_hmData.get("Cost"));
                b.putString("Description", m_hmData.get("Description"));
                b.putString("Date", m_hmData.get("Date"));
                b.putString("Type", m_hmData.get("Type"));
                b.putString("ID", m_hmData.get("ID"));
            } else if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_e))) {
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

        Util.showConfirm(mPage, "\t\t" + mPage.getString(R.string.section_title) + "\t：\t" + hm.get("Description") + "\n\t\t" + mPage.getString(R.string.month_title) + "\t：\t" + hm.get("Date") + "\n\t\t" + mPage.getString(R.string.no_title) + "\t：\t" + hm.get("CostNo") , new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int iTab = onPageScrolledPrePosition;
                int type = -1;

                setTabTitle(iTab);

                if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_pc))) {
                    type = 1;
                } else if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_c))) {
                    type = 2;
                } else if (TAB_TITLE.equals(getString(R.string.activity_main_page_title_e))) {
                    type = 3;
                }

                FireBaseClass fbc = new FireBaseClass();
                fbc.deleteDataToFireBase(mPage, hm, type);
            }
        });
    }

    private void setTabTitle(int iTab)
    {
        if (iTab < 0) {
            iTab = 0;
        }

        switch (iTab)
        {
            case PAGE_TITLE_PC:
                TAB_TITLE = getString(R.string.activity_main_page_title_pc);
                break;
            case PAGE_TITLE_C:
                TAB_TITLE = getString(R.string.activity_main_page_title_c);
                break;
            case PAGE_TITLE_E:
                TAB_TITLE = getString(R.string.activity_main_page_title_e);
                break;
        }
    }
}
