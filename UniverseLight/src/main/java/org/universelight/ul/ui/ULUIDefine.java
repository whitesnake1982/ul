package org.universelight.ul.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.universelight.ul.R;

/**
 * Created by hsinhenglin on 2017/1/9.
 */

public class ULUIDefine {

    // 字型定義，內容需同 FCBui_mlayout_templates 字型定義
    /** 字型「4U」定義 */
    public static final double FontSize_4u = 4;
    /** 字型「5U」定義 */
    public static final double FontSize_5u = 5;
    /** 字型「6U」定義 */
    public static final double FontSize_6u = 6;
    /** 字型「8U」定義 */
    public static final double FontSize_8u = 8;
    /** 字型「10U」定義 */
    public static final double FontSize_10u = 10;
    /** 字型「11U」定義 */
    public static final double FontSize_11u = 11;
    /** 字型「12U」定義 */
    public static final double FontSize_12u = 12;
    /** 字型「13U」定義 */
    public static final double FontSize_13u = 13;
    /** 字型「14U」定義 */
    public static final double FontSize_14u = 14;
    /** 字型「15U」定義 */
    public static final double FontSize_15u = 15;
    /** 字型「16U」定義 */
    public static final double FontSize_16u = 16;
    /** 字型「30U」定義 */
    public static final double FontSize_30u = 30;

    private static ULUIDefine m_instance = null;

    private int m_iMaxLayoutHeightWeight = 0; // 長度單位最大值
    private int m_iMaxLayoutWidthWeight = 0; // 寬度單位最大值
    private float m_fHeightUnit = 0; // 長度單位
    private float m_fWidthUnit = 0; // 寬度單位
    private float m_fFontUnit = 0; // 字型單位

    private float m_fScreenScaleDensity = 0; // 螢幕密度

    private DisplayMetrics m_displayMetrics = null; // 取的螢幕解析度

    public static ULUIDefine getInstance(Context context)
    {
        if (null == m_instance)
        {
            m_instance = new ULUIDefine();
            m_instance.initial(context);
        }

        return m_instance;
    }

    private void initial(Context context)
    {
        int iH = 0;

        // 設定「長度、寬度單位」最大值
        m_iMaxLayoutHeightWeight = context.getResources().getInteger(R.integer.ul_ui_integer_activity_weight_sum_vertical);
        m_iMaxLayoutWidthWeight = context.getResources().getInteger(R.integer.ul_ui_integer_activity_weight_sum_vertical);

        // 設定「長度、寬度、字型單位」
        m_displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(m_displayMetrics);

        m_fHeightUnit = (float) (m_displayMetrics.heightPixels - iGetStatusBarHeight(context)) / m_iMaxLayoutHeightWeight;
        m_fWidthUnit = (float) m_displayMetrics.widthPixels / m_iMaxLayoutWidthWeight;
        m_fFontUnit = Math.min(m_fHeightUnit + 0.5f, m_fWidthUnit + 0.5f);

        m_fScreenScaleDensity = m_displayMetrics.scaledDensity; // snoykuo
        // 2014/05/02
    }

    /** @return 取出經長寬比運算出的高度 */
    public DisplayMetrics getDisplayMetrics()
    {
        return m_displayMetrics;
    }

    /**
     * @return 螢幕密度
     */
    public float getScaleDensity()
    {
        return m_fScreenScaleDensity;
    }

    /**
     * @param dWeight
     *            高度比例，參數範圍為「1 ~ 480」
     * @return 取出經長寬比運算出的高度
     */
    public int getLayoutHeight(double dWeight)
    {
        return (int) (dWeight * m_fHeightUnit);
    }

    /**
     * @param dWeight
     *            寬度比例，參數範圍為「1 ~ 270」
     * @return 取出經長寬比運算出的寬度
     */
    public int getLayoutWidth(double dWeight)
    {
        return (int) (dWeight * m_fWidthUnit);
    }

    /**
     * @param dTextSizeDefine
     *            參數範圍為「{@link ULUIDefine#FontSize_10u FontSize_10u} ~ {@link ULUIDefine#FontSize_30u FontSize_30u}」
     * @param iTextLength
     *            總行數
     * @return 取出經長寬比運算出的總行高度
     */
    public int getLayoutHeightByTextSize(double dTextSizeDefine, int iTextLength)
    {
        if (iTextLength <= 0)
        {
            return 0;
        }

        int iLayoutHeight = getTextSize(dTextSizeDefine) * iTextLength;
        int iMaxLayoutHeight = getLayoutHeight(m_iMaxLayoutHeightWeight);
        if (iMaxLayoutHeight <= iLayoutHeight)
        {
            return iMaxLayoutHeight;
        }

        return iLayoutHeight;
    }

    /**
     * @param dTextSizeDefine
     *            參數範圍為「{@link ULUIDefine#FontSize_10u FontSize_10u} ~ {@link ULUIDefine#FontSize_30u FontSize_30u}」
     * @param iTextLength
     *            總字數
     * @return 取出經長寬比運算出的總字寬度
     */
    public int getLayoutWidthByTextSize(double dTextSizeDefine, int iTextLength)
    {
        if (iTextLength <= 0)
        {
            return 0;
        }

        int iLayoutWidth = getTextSize(dTextSizeDefine) * iTextLength;
        int iMaxLayoutWidth = getLayoutWidth(m_iMaxLayoutWidthWeight);
        if (iMaxLayoutWidth <= iLayoutWidth)
        {
            return iMaxLayoutWidth;
        }

        return iLayoutWidth;
    }

    /**
     * @param dTextSizeDefine
     *            參數範圍為「{@link ULUIDefine#FontSize_10u FontSize_10u} ~ {@link ULUIDefine#FontSize_30u FontSize_30u}」
     * @return 取出字型大小
     */
    public int getTextSize(double dTextSizeDefine)
    {
        return (int) ((dTextSizeDefine - 0.5) * (int) m_fFontUnit);
    }

    /**
     * 設定「字型」以及「LayoutParams」
     *
     * @param textView
     *            要設定的 TextView
     */
    public void setTextSizeAndLayoutParams(TextView textView)
    {
        setLayoutParams(textView);
        setTextSize(textView.getTextSize(), textView);
    }

    /**
     * 設定「字型」以及「LayoutParams」
     *
     * @param button
     *            要設定的 Button
     */
    public void setTextSizeAndLayoutParams(Button button)
    {
        setLayoutParams(button);
        setTextSize(button.getTextSize(), button);
    }

    /**
     * 設定「字型」以及「LayoutParams」
     *
     * @param editText
     *            要設定的 EditText
     */
    public void setTextSizeAndLayoutParams(EditText editText)
    {
        setLayoutParams(editText);
        setTextSize(editText.getTextSize(), editText);
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     *            參數範圍為「{@link ULUIDefine#FontSize_10u FontSize_10u} ~ {@link ULUIDefine#FontSize_30u FontSize_30u}」
     * @param textView
     *            設定textView文字大小
     */
    public void setTextSize(double dTextSizeDefine, TextView textView)
    {
        textView.setIncludeFontPadding(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     *            參數範圍為「{@link ULUIDefine#FontSize_10u FontSize_10u} ~ {@link ULUIDefine#FontSize_30u FontSize_30u}」
     * @param button
     *            設定button文字大小
     */
    public void setTextSize(double dTextSizeDefine, Button button)
    {
        button.setIncludeFontPadding(false);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     *            參數範圍為「{@link ULUIDefine#FontSize_10u FontSize_10u} ~ {@link ULUIDefine#FontSize_30u FontSize_30u}」
     * @param editText
     *            設定editText文字大小
     */
    public void setTextSize(double dTextSizeDefine, EditText editText)
    {
        editText.setIncludeFontPadding(false);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     *            參數範圍為「{@link ULUIDefine#FontSize_10u FontSize_10u} ~ {@link ULUIDefine#FontSize_30u FontSize_30u}」
     * @param checkbox
     *            設定checkbox文字大小
     */
    public void setTextSize(double dTextSizeDefine, CheckBox checkbox)
    {
        checkbox.setIncludeFontPadding(false);
        checkbox.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     *            參數範圍為「{@link ULUIDefine#FontSize_10u FontSize_10u} ~ {@link ULUIDefine#FontSize_30u FontSize_30u}」
     * @param radioButton
     *            設定radioButton文字大小
     */
    public void setTextSize(double dTextSizeDefine, RadioButton radioButton)
    {
        radioButton.setIncludeFontPadding(false);
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    /**
     * 設定「LayoutParams」，包含「width」、「height」、「Margins」、「Padding」等參數
     *
     * @param view
     *            要設定 LayoutParams 的 View
     * @author kai-yu
     */
    public void setLayoutParams(View view)
    {
        if (view.getLayoutParams().width != 0 && view.getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT && view.getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            view.getLayoutParams().width = getLayoutWidth(view.getLayoutParams().width);
        }

        if (view.getLayoutParams().height != 0 && view.getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT && view.getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            view.getLayoutParams().height = getLayoutHeight(view.getLayoutParams().height);
        }

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(getLayoutWidth(params.leftMargin), getLayoutHeight(params.topMargin), getLayoutWidth(params.rightMargin), getLayoutHeight(params.bottomMargin));
        }

        view.setPadding(getLayoutWidth(view.getPaddingLeft()), getLayoutHeight(view.getPaddingTop()), getLayoutWidth(view.getPaddingRight()), getLayoutHeight(view.getPaddingBottom()));
    }

    /**
     * 取得StatusBar的高度
     *
     * @param context
     * @return height
     */
    private static int iGetStatusBarHeight(Context context)
    {
        if (null == context)
            return 0;

        int iH = 0;

        // 法一
        // http://blog.csdn.net/devilnov/article/details/9309659
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        iH = frame.top;

        if (0 == iH)
        {// 法二
            // http://stackoverflow.com/questions/3407256/height-of-status-bar-in-android
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
            {
                iH = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return iH;
    }
}
