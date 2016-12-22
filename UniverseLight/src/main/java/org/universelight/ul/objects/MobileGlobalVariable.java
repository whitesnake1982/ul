package org.universelight.ul.objects;

import android.support.multidex.MultiDexApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hsinhenglin on 2016/12/22.
 */

public class MobileGlobalVariable extends MultiDexApplication {
    public ArrayList<HashMap<String, String>> alData = new
            ArrayList<>();
    public ArrayList<String> alYearList = new
            ArrayList<>();
    public HashMap<String, ArrayList<HashMap<String, String>>> hmYearDataArrayList = new
            HashMap<>();
}
