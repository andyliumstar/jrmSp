
package com.jrm.skype.util;

import com.jrm.skype.ui.R;

import android.content.Context;

public class CountryInfoHelper {
    private static String[] COUNTRY_NAMES;

    private static String[] COUNTRY_SHORT_NAMES;

    private static String[] LANGUAGE_NAMES;

    private static String[] LANGUAGE_SHORT_NAMES;

    public static int getCountryNameIdByShortName(Context context, String shortName) {
        COUNTRY_SHORT_NAMES = context.getResources().getStringArray(R.array.countryShortName);

        if (null == shortName || shortName.equals("")) {
            return 0;
        } else {
            int pos;
            for (pos = 0; pos < COUNTRY_SHORT_NAMES.length; ++pos) {
                if (shortName.toLowerCase().equals(COUNTRY_SHORT_NAMES[pos]))
                    break;
            }
            if (pos == COUNTRY_SHORT_NAMES.length)
                pos = 0;
            return pos;
        }
    }

    public static String getCountryNameStrByShortName(Context context, String shortName) {
        COUNTRY_NAMES = context.getResources().getStringArray(R.array.countryName);
        int id = getCountryNameIdByShortName(context, shortName);
        if (0 == id) {
            return shortName;
        } else {
            return COUNTRY_NAMES[id];
        }
    }

    public static String getCountryShortNameById(Context context, int id) {
        COUNTRY_SHORT_NAMES = context.getResources().getStringArray(R.array.countryShortName);
        if (id < 0 || id >= COUNTRY_SHORT_NAMES.length) {
            return "";
        }
        return COUNTRY_SHORT_NAMES[id];
    }

    public static int getLanguageNameIdByShortName(Context context, String shortName) {
        LANGUAGE_SHORT_NAMES = context.getResources().getStringArray(R.array.languageShortName);

        if (null == shortName || shortName.equals("")) {
            return 0;
        } else {
            int pos;
            for (pos = 0; pos < LANGUAGE_SHORT_NAMES.length; ++pos) {
                if (shortName.toLowerCase().equals(LANGUAGE_SHORT_NAMES[pos]))
                    break;
            }
            if (pos == LANGUAGE_SHORT_NAMES.length)
                pos = 0;
            return pos;
        }
    }

    public static String getLanguageNameStrByShortName(Context context, String shortName) {
        LANGUAGE_NAMES = context.getResources().getStringArray(R.array.languageName);
        int id = getLanguageNameIdByShortName(context, shortName);
        if (0 == id) {
            return "";
        } else {
            return LANGUAGE_NAMES[id];
        }
    }

    public static String getLanguageShortNameById(Context context, int id) {
        LANGUAGE_SHORT_NAMES = context.getResources().getStringArray(R.array.languageShortName);
        if (id < 0 || id >= LANGUAGE_SHORT_NAMES.length) {
            return "";
        }
        return LANGUAGE_SHORT_NAMES[id];
    }

}
