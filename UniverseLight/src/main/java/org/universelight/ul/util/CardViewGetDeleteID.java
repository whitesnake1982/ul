package org.universelight.ul.util;

import java.util.HashMap;

/**
 * Created by hsinhenglin on 16/9/9.
 */
public class CardViewGetDeleteID
{
    public interface CardOnDeleteClickListener {
        void OnDeleteClickListener(HashMap<String, String> id);
    }

    public static CardOnDeleteClickListener cardOnDeleteClickListener;

    public void  setCardOnDeleteClickListener(CardOnDeleteClickListener l)
    {
        cardOnDeleteClickListener   =  l ;
    }
}
