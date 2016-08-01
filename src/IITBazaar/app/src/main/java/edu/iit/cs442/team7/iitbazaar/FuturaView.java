package edu.iit.cs442.team7.iitbazaar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class FuturaView extends TextView {


    Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/FUTURAM.ttf");
    Typeface boldTypeface = Typeface.createFromAsset(getContext().getAssets(),  "fonts/FUTURAM.ttf");

    public FuturaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FuturaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FuturaView(Context context) {
        super(context);
    }

    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(boldTypeface/*, -1*/);
        } else {
            super.setTypeface(normalTypeface/*, -1*/);
        }
    }

}