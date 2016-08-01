package edu.iit.cs442.team7.iitbazaar.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public enum IntentKeys {

    REFRESH_ACTION("BAZAAR_REFRESH"),
    NOTIFICATION_ACTION("BAZAAR_NOTIFICATION"),
    TYPE("BAZAAR_TYPE"),
    NOTIFICATION_ACTION_ITEM("ITEM"),
    LINKIFY_ACTION("BAZAAR_LINKIFY"),
    LINKIFY_ACTION_ITEM("ITEM"),
    INVALID_INTENT(null);


    private static Map<String,IntentKeys> intentLookup = new HashMap<>();



    static{
        for(final IntentKeys f: IntentKeys.values() ){

            intentLookup.put(f.getString(),f);


        }


    }


    private final String intentkey;


    IntentKeys(String intentkey) {
        this.intentkey = intentkey;
    }

    public String getString() { return intentkey; }


    public IntentKeys getIntentByName(final String fontName){

        final IntentKeys itentConstants = intentLookup.get(fontName);
        return null == itentConstants ? INVALID_INTENT : itentConstants;
    }






    }
