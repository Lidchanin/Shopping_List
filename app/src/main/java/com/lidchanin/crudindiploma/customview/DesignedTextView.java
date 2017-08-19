package com.lidchanin.crudindiploma.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Alexander Destroyed on 19.08.2017.
 */

public class DesignedTextView extends android.support.v7.widget.AppCompatTextView {

    public DesignedTextView(Context context) {
        super(context);
        init();
    }

    public DesignedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DesignedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "MyriadPro-Regular.otf");
        setTypeface(tf ,1);

    }
}
