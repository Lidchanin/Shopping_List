package com.lidchanin.crudindiploma.filters;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class <code>DecimalDigitsInputFilter</code> implements {@link InputFilter} and filters the value
 * entry form EditTexts.
 */
public class DecimalDigitsInputFilter implements InputFilter {

    private Pattern pattern;

    // FIXME: 15.05.2017 regex expression
    public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        pattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0,"
                + (digitsAfterZero - 1) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        Matcher matcher = pattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    }
}
