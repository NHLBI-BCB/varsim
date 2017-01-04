package com.bina.varsim.util;

import com.bina.varsim.types.ChrString;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by guoy28 on 10/10/16.
 */
public class StringUtilities {
    /**
     * convert integer array to delimited string
     * @param a
     * @return
     */
    public static String concatenateArray(final int[] a, final CharSequence delimiter) {
        if (a == null) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(delimiter.toString());
        for (int i = 0; i < a.length; i++) {
            joiner.add(Integer.toString(a[i]));
        }
        return joiner.toString();
    }

    /**
     * convert String array to delimitted string
     * @param a
     * @param delimiter
     * @return
     */
    public static String concatenateArray(final String[] a, final CharSequence delimiter) {
        if (a == null) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(delimiter.toString());
        for (int i = 0; i < a.length; i++) {
            joiner.add(a[i]);
        }
        return joiner.toString();
    }

    /**
     * convert ChrString array to delimited string
     * @param a
     * @param delimiter
     * @return
     */
    public static String concatenateArray(final ChrString[] a, final CharSequence delimiter) {
        if (a == null) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(delimiter.toString());
        for (int i = 0; i < a.length; i++) {
            joiner.add(a[i].toString());
        }
        return joiner.toString();
    }
}
