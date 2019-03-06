package org.universelight.ul.ui

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView

import org.universelight.ul.R

/**
 * Created by hsinhenglin on 2017/1/9.
 */

class UIDefine {

    private var m_iMaxLayoutHeightWeight = 0 // 長度單位最大值
    private var m_iMaxLayoutWidthWeight = 0 // 寬度單位最大值
    private var m_fHeightUnit = 0f // 長度單位
    private var m_fWidthUnit = 0f // 寬度單位
    private var m_fFontUnit = 0f // 字型單位

    /**
     * @return 螢幕密度
     */
    var scaleDensity = 0f
        private set // 螢幕密度

    /** @return 取出經長寬比運算出的高度
     */
    var displayMetrics: DisplayMetrics? = null
        private set // 取的螢幕解析度

    private fun initial(context: Context) {
        val iH = 0

        // 設定「長度、寬度單位」最大值
        m_iMaxLayoutHeightWeight = context.resources.getInteger(R.integer.ul_ui_integer_activity_weight_sum_vertical)
        m_iMaxLayoutWidthWeight = context.resources.getInteger(R.integer.ul_ui_integer_activity_weight_sum_vertical)

        // 設定「長度、寬度、字型單位」
        displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        m_fHeightUnit = (displayMetrics!!.heightPixels - iGetStatusBarHeight(context)).toFloat() / m_iMaxLayoutHeightWeight
        m_fWidthUnit = displayMetrics!!.widthPixels.toFloat() / m_iMaxLayoutWidthWeight
        m_fFontUnit = Math.min(m_fHeightUnit + 0.5f, m_fWidthUnit + 0.5f)

        scaleDensity = displayMetrics!!.scaledDensity // snoykuo
        // 2014/05/02
    }

    /**
     * @param dWeight
     * 高度比例，參數範圍為「1 ~ 480」
     * @return 取出經長寬比運算出的高度
     */
    fun getLayoutHeight(dWeight: Double): Int {
        return (dWeight * m_fHeightUnit).toInt()
    }

    /**
     * @param dWeight
     * 寬度比例，參數範圍為「1 ~ 270」
     * @return 取出經長寬比運算出的寬度
     */
    fun getLayoutWidth(dWeight: Double): Int {
        return (dWeight * m_fWidthUnit).toInt()
    }

    /**
     * @param dTextSizeDefine
     * 參數範圍為「[FontSize_10u][UIDefine.FontSize_10u] ~ [FontSize_30u][UIDefine.FontSize_30u]」
     * @param iTextLength
     * 總行數
     * @return 取出經長寬比運算出的總行高度
     */
    fun getLayoutHeightByTextSize(dTextSizeDefine: Double, iTextLength: Int): Int {
        if (iTextLength <= 0) {
            return 0
        }

        val iLayoutHeight = getTextSize(dTextSizeDefine) * iTextLength
        val iMaxLayoutHeight = getLayoutHeight(m_iMaxLayoutHeightWeight.toDouble())
        return if (iMaxLayoutHeight <= iLayoutHeight) {
            iMaxLayoutHeight
        } else iLayoutHeight

    }

    /**
     * @param dTextSizeDefine
     * 參數範圍為「[FontSize_10u][UIDefine.FontSize_10u] ~ [FontSize_30u][UIDefine.FontSize_30u]」
     * @param iTextLength
     * 總字數
     * @return 取出經長寬比運算出的總字寬度
     */
    fun getLayoutWidthByTextSize(dTextSizeDefine: Double, iTextLength: Int): Int {
        if (iTextLength <= 0) {
            return 0
        }

        val iLayoutWidth = getTextSize(dTextSizeDefine) * iTextLength
        val iMaxLayoutWidth = getLayoutWidth(m_iMaxLayoutWidthWeight.toDouble())
        return if (iMaxLayoutWidth <= iLayoutWidth) {
            iMaxLayoutWidth
        } else iLayoutWidth

    }

    /**
     * @param dTextSizeDefine
     * 參數範圍為「[FontSize_10u][UIDefine.FontSize_10u] ~ [FontSize_30u][UIDefine.FontSize_30u]」
     * @return 取出字型大小
     */
    fun getTextSize(dTextSizeDefine: Double): Int {
        return ((dTextSizeDefine - 0.5) * m_fFontUnit.toInt()).toInt()
    }

    /**
     * 設定「字型」以及「LayoutParams」
     *
     * @param textView
     * 要設定的 TextView
     */
    fun setTextSizeAndLayoutParams(textView: TextView) {
        setLayoutParams(textView)
        setTextSize(textView.textSize.toDouble(), textView)
    }

    /**
     * 設定「字型」以及「LayoutParams」
     *
     * @param button
     * 要設定的 Button
     */
    fun setTextSizeAndLayoutParams(button: Button) {
        setLayoutParams(button)
        setTextSize(button.textSize.toDouble(), button)
    }

    /**
     * 設定「字型」以及「LayoutParams」
     *
     * @param editText
     * 要設定的 EditText
     */
    fun setTextSizeAndLayoutParams(editText: EditText) {
        setLayoutParams(editText)
        setTextSize(editText.textSize.toDouble(), editText)
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     * 參數範圍為「[FontSize_10u][UIDefine.FontSize_10u] ~ [FontSize_30u][UIDefine.FontSize_30u]」
     * @param textView
     * 設定textView文字大小
     */
    fun setTextSize(dTextSizeDefine: Double, textView: TextView) {
        textView.includeFontPadding = false
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine).toFloat())
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     * 參數範圍為「[FontSize_10u][UIDefine.FontSize_10u] ~ [FontSize_30u][UIDefine.FontSize_30u]」
     * @param button
     * 設定button文字大小
     */
    fun setTextSize(dTextSizeDefine: Double, button: Button) {
        button.includeFontPadding = false
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine).toFloat())
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     * 參數範圍為「[FontSize_10u][UIDefine.FontSize_10u] ~ [FontSize_30u][UIDefine.FontSize_30u]」
     * @param editText
     * 設定editText文字大小
     */
    fun setTextSize(dTextSizeDefine: Double, editText: EditText) {
        editText.includeFontPadding = false
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine).toFloat())
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     * 參數範圍為「[FontSize_10u][UIDefine.FontSize_10u] ~ [FontSize_30u][UIDefine.FontSize_30u]」
     * @param checkbox
     * 設定checkbox文字大小
     */
    fun setTextSize(dTextSizeDefine: Double, checkbox: CheckBox) {
        checkbox.includeFontPadding = false
        checkbox.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine).toFloat())
    }

    /**
     * 設定字型
     *
     * @param dTextSizeDefine
     * 參數範圍為「[FontSize_10u][UIDefine.FontSize_10u] ~ [FontSize_30u][UIDefine.FontSize_30u]」
     * @param radioButton
     * 設定radioButton文字大小
     */
    fun setTextSize(dTextSizeDefine: Double, radioButton: RadioButton) {
        radioButton.includeFontPadding = false
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine).toFloat())
    }

    /**
     * 設定「LayoutParams」，包含「width」、「height」、「Margins」、「Padding」等參數
     *
     * @param view
     * 要設定 LayoutParams 的 View
     * @author kai-yu
     */
    fun setLayoutParams(view: View) {
        if (view.layoutParams.width != 0 && view.layoutParams.width != ViewGroup.LayoutParams.MATCH_PARENT && view.layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            view.layoutParams.width = getLayoutWidth(view.layoutParams.width.toDouble())
        }

        if (view.layoutParams.height != 0 && view.layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT && view.layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            view.layoutParams.height = getLayoutHeight(view.layoutParams.height.toDouble())
        }

        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(getLayoutWidth(params.leftMargin.toDouble()), getLayoutHeight(params.topMargin.toDouble()), getLayoutWidth(params.rightMargin.toDouble()), getLayoutHeight(params.bottomMargin.toDouble()))
        }

        view.setPadding(getLayoutWidth(view.paddingLeft.toDouble()), getLayoutHeight(view.paddingTop.toDouble()), getLayoutWidth(view.paddingRight.toDouble()), getLayoutHeight(view.paddingBottom.toDouble()))
    }

    companion object {

        // 字型定義，內容需同 FCBui_mlayout_templates 字型定義
        /** 字型「4U」定義  */
        val FontSize_4u = 4.0
        /** 字型「5U」定義  */
        val FontSize_5u = 5.0
        /** 字型「6U」定義  */
        val FontSize_6u = 6.0
        /** 字型「8U」定義  */
        val FontSize_8u = 8.0
        /** 字型「10U」定義  */
        val FontSize_10u = 10.0
        /** 字型「11U」定義  */
        val FontSize_11u = 11.0
        /** 字型「12U」定義  */
        val FontSize_12u = 12.0
        /** 字型「13U」定義  */
        val FontSize_13u = 13.0
        /** 字型「14U」定義  */
        val FontSize_14u = 14.0
        /** 字型「15U」定義  */
        val FontSize_15u = 15.0
        /** 字型「16U」定義  */
        val FontSize_16u = 16.0
        /** 字型「30U」定義  */
        val FontSize_30u = 30.0

        private var m_instance: UIDefine? = null

        fun getInstance(context: Context): UIDefine {
            if (null == m_instance) {
                m_instance = UIDefine()
                m_instance!!.initial(context)
            }

            return m_instance as UIDefine
        }

        /**
         * 取得StatusBar的高度
         *
         * @param context
         * @return height
         */
        private fun iGetStatusBarHeight(context: Context?): Int {
            if (null == context)
                return 0

            var iH: Int

            // 法一
            // http://blog.csdn.net/devilnov/article/details/9309659
            val frame = Rect()
            (context as Activity).window.decorView.getWindowVisibleDisplayFrame(frame)
            iH = frame.top

            if (0 == iH) {// 法二
                // http://stackoverflow.com/questions/3407256/height-of-status-bar-in-android
                val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    iH = context.resources.getDimensionPixelSize(resourceId)
                }
            }

            return iH
        }
    }
}
