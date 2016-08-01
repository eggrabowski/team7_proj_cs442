package edu.iit.cs442.team7.iitbazaar.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public enum LevelKeys {


    CONTINUING_EDUCATION("Continuing_Education"),
    GRADUATE("Graduate"),
    GRADUATE_BUSINESS("Graduate Business"),
    GRADUATE_CERTIFICATE_PROGRAM("Graduate Certificate Program"),
    GRADUATE_DOCTORAL("Graduate Doctoral"),
    LAW("Law"),
    MASTER_OF_LAWS("Master of Laws"),
    UNDERGRADUATE("Undergraduate"),
    UNKNOWN("--");

    private final String level;


    private static Map<String,LevelKeys> levelLookup = new HashMap<>();



    static{
        for(final LevelKeys level: LevelKeys.values() ){

            levelLookup.put(level.level, level);

        }

    }

    LevelKeys(String level) {
        this.level = level;

    }


    public String getLevel() { return level; }


    public LevelKeys getByLevelName(String levelName){

        final LevelKeys lKey = levelLookup.get(levelName);
        return null == lKey ? UNKNOWN : lKey;
    }


    }









