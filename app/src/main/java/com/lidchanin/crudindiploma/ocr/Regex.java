package com.lidchanin.crudindiploma.ocr;

import android.util.Log;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Regex {

    String[] splitForClean(final String input){
        Log.d("RECOGNIZE",input);
        return input.split("\n");
    }

    ArrayList<String> parseName(final String[] input) {

        final Pattern namePattern = Pattern.compile("[А-Яа-я]{4,999}.*?\\s");
        final ArrayList<String> output = new ArrayList<>();
        int count=0;
        for (final String anInput : input) {
            final Matcher nameParsed = namePattern.matcher(anInput);
            if (nameParsed.find()) {
                output.add(count,nameParsed.group(0));
                count++;
            }
        }
        return output;

    }

    ArrayList<String> parseCost(final String[] input) {
        //1 type
        Pattern costPattern = Pattern.compile("[0-9]\\s{0,999}.*?\\s");
        final ArrayList<String> output = new ArrayList<>();
        int count=0;
        for (final String anInput : input) {
            final Matcher costParsed = costPattern.matcher(anInput);
            if (costParsed.find()) {
                Log.d("Check",costParsed.group(0));
                output.add(count,costParsed.group(0));
                count++;
            }
        }
        if(output.size()==0){
            costPattern = Pattern.compile("\\s[1-9]{0,999}.*?\\s");
            for (final String anInput : input) {
                final Matcher costParsed = costPattern.matcher(anInput);
                if (costParsed.find()) {
                    output.add(count,costParsed.group(0));
                    count++;
                }
            }
            return output;
        }
        else {
            return output;

        }
    }
}