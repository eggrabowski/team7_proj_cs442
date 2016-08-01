package edu.iit.cs442.team7.iitbazaar.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public enum FontConstants {

    CENTURY_SCHOOLBOOK("Century Schoolbook","CENSCBK.TTF"),
    CENTURY_SCHOOLBOOK_ITALIC("Century Schoolbook Italic","SCHLBKI.TTF"),
    CENTURY_SCHOOLBOOK_BOLD_ITALIC("Century Schoolbook Bold Italic","SCHLBKBI.TTF"),
    CENTURY_SCHOOLBOOK_BOLD("Century Schoolbook Bold","SCHLBKB.TTF"),
    FUTURA_MEDIUM_CONDENSED_ITALIC("Futura MdCn BT-Italic","FUTURMCI.TTF"),
    FUTURA_EXTRA_BLACK("Futura Extra Black BT","FUTURAXK.TTF"),
    FUTURA_MEDIUM_ITALIC("Futura Medium Italic BT","FUTURAMI.TTF"),
    FUTURA_MEDIUM_CONDENSED("Futura Medium Condensed BT","FUTURAMC.TTF"),
    FUTURA_MEDIUM("Futura Medium BT", "fonts/FUTURAM.TTF"),
    FUTURA_LIGHT_ITALIC("Futura Light Italic BT","FUTURALI.TTF"),
    FUTURA_LIGHT("Futura Light BT","FUTURAL.TTF"),
    FUTURA_BLACK( "Futura Black BT","FUTURAK.TTF"),
    FUTURA_HEAVY("Futura Heavy BT","FUTURAH.TTF"),
    FUTURA_BOLD_ITALIC( "Futura Bold Italic BT","FUTURABI.TTF"),
    FUTURA_BOLD_CONDENSED( "Futura Bold Condensed BT","FUTURABC.TTF"),
    FUTURA_BOLD("Futura Bold BT","FUTURAB.TTF"),
    INVALID_FONT(null,null);


    private static Map<String,FontConstants> fontNameLookup = new HashMap<>();
    private static Map<String,FontConstants> fontFileNameLookup = new HashMap<>();


    static{
        for(final FontConstants f: FontConstants.values() ){

            fontNameLookup.put(f.fontName,f);
            fontFileNameLookup.put(f.fontFileName,f);

        }


    }


    private final String fontName;
    private final String fontFileName;

    FontConstants(String fontName, String fontFileName) {
        this.fontName = fontName;
        this.fontFileName = fontFileName;
    }

    public String getFontName() { return fontName; }
    public String getFontFileName() { return fontFileName; }

    public FontConstants getByFontName(String fontName){

        final FontConstants fontConstants = fontNameLookup.get(fontName);
        return null == fontConstants ? INVALID_FONT : fontConstants;
    }

    public FontConstants getByFileName (String fileName){
        final FontConstants fontConstants = fontFileNameLookup.get(fileName);
        return null == fontConstants ? INVALID_FONT : fontConstants;

    }





    }
