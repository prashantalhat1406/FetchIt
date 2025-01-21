package com.sinprl.fetchit.utils;

public class StringUtils {

    public static String toCamelCase(String input)
    {
        StringBuilder result = new StringBuilder();
        String[] words = input.split(" ");
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            result.append(Character.toUpperCase(word.charAt(0)));
            result.append(word.substring(1).toLowerCase());
            result.append(" ");
        }
        return result.toString();
    }

    public static String getValidMobile(String raw_mobile) {
        String clean_mobile = raw_mobile;

        if (raw_mobile.length() > 10)
        {
            clean_mobile = raw_mobile.replaceAll("\\s+", "");
            clean_mobile = clean_mobile.substring(clean_mobile.length() - 10);
        }

        return clean_mobile;
    }
}
