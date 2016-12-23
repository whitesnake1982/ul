package org.universelight.ul.util;

/**
 * Created by hsinhenglin on 2016/12/23.
 */

public class FilterCondition
{
    public interface OnSearchConditionListener {
        void getSearchOptions(String sYear, String sMonth);
    }

    public static OnSearchConditionListener onSearchConditionListener;

    public void  setOnSearchConditionListener(OnSearchConditionListener l)
    {
        onSearchConditionListener   =  l ;
    }
}
