package edu.iit.cs442.team7.iitbazaar.rankrequester;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmn on 11/4/15.
 */
public enum PhoneBookKeys {

    STAFF_AND_FACULTY("Staff & Faculty Results"),
    STUDENT("Student Results"),
    NO_MATCHES("No Matches"),
    BLANK(" "),
    INVALID("");


    private final String text;

    private static final Map<String, PhoneBookKeys> mMap = Collections.unmodifiableMap(initializeMapping());

    PhoneBookKeys(final String text) {
        this.text = text;
    }

    private static Map<String, PhoneBookKeys> initializeMapping() {
        final Map<String, PhoneBookKeys> mMap = new HashMap<String, PhoneBookKeys>();
        for (PhoneBookKeys s : PhoneBookKeys.values()) {
            mMap.put(s.text, s);
        }
        return mMap;
    }

    public static PhoneBookKeys getByValue(final String text){

        PhoneBookKeys activityKeys = mMap.get(text);
        if (null != activityKeys){
            return activityKeys;
        }
        else{
            return INVALID;
        }

    }


    @Override
    public String toString() {
        return text;
    }
}
