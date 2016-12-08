package org.universelight.ul.util;

import java.util.HashMap;

/**
 * Created by hsinhenglin on 16/9/9.
 */
public class CardViewGetID
{
    public interface CardOnClickListener {
        void OnClickListener(HashMap<String, String> id);
    }

    public static CardOnClickListener cardOnClickListener;

    public void  setCardOnClickListener(CardOnClickListener l)
    {
        cardOnClickListener   =  l ;
    }
}
