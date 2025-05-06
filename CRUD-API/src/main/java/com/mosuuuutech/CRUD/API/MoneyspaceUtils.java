package com.mosuuuutech.CRUD.API;

import java.net.URL;

/**
 * Money Space Java SDK Utility class
 * @author Money space company limited
 * @version 2.0.0
 *
 */
public class MoneyspaceUtils {

    /**
     * To validate the String value is represent a URL
     * @param val string supposed to store URL
     * @return True is the string value is a valid URL, otherwise is False.
     */
    public static boolean isURL(String val) {
        try {
            URL url = new URL(val);
            url.toURI();
            return true;
        } catch (Exception ex) {
            /* Do Nothing */
            return false;
        }
    }

    /**
     * To validate string value, is it empty ?
     * @param val string to be validaed
     * @return True is string value is empty, otherwise is False.
     */
    public static boolean isEmpty(String val) {
        return  (val == null || val.trim().length() == 0);
    }

}
