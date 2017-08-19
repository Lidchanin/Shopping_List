package com.lidchanin.crudindiploma.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Alexander Destroyed on 19.08.2017.
 */

public class DesignedEditText extends android.support.v7.widget.AppCompatEditText {

    public DesignedEditText(Context context) {
        super(context);
        init();
    }

    public DesignedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DesignedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "MyriadPro-Regular.otf");
        setTypeface(tf ,1);

    }
}
