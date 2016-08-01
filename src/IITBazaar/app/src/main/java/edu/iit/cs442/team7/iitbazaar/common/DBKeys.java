package edu.iit.cs442.team7.iitbazaar.common;

import android.util.Log;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public enum DBKeys {

    USER_EMAIL("user_email", "TEXT", "PRIMARY KEY"),
    USER_FIRST_NAME("user_first_name", "TEXT", "NOT NULL"),
    USER_LAST_NAME("user_last_name", "TEXT", "NOT NULL"),
    USER_MAJOR_DEPARTMENT("user_major_department", "TEXT", "NOT NULL"),
    USER_PICTURE_THUMBNAIL("user_picture_thumbnail", "BLOB", "NOT NULL"),
    USER_PICTURE("user_picture", "BLOB", "NOT NULL"),
    ITEM_ITEM_NUMBER("item_number", "INTEGER", "PRIMARY KEY"),
    ITEM_LISTING_START_DATE("item_listing_start_date", "INTEGER", "NOT NULL"),
    ITEM_LISTING_END_DATE("item_listing_end_date", "INTEGER", "NOT NULL"),
    ITEM_TITLE("item_title", "TEXT", "NOT NULL"),
    ITEM_DESCRIPTION("item_description", "TEXT", "NOT NULL"),
    ITEM_LISTING_USER_EMAIL("item_listing_user_email", "TEXT", "NOT NULL"),
    ITEM_PICTURE_THUMBNAIL("item_picture_thumbnail", "BLOB", "NOT NULL"),
    ITEM_PICTURE("item_picture", "BLOB", "NOT NULL"),
    ITEM_PRICE("item_price", "TEXT", "NOT NULL"),
    ITEM_CATEGORY("category_number", "INTEGER", "NOT NULL"),
    CATEGORY_NUMBER("category_number", "INTEGER", "PRIMARY KEY"),
    CATEGORY_PARENT_NUMBER("parent_category_number", "INTEGER", "NOT NULL"),
    CATEGORY_NAME("category_name", "TEXT", "NOT NULL");


    private final String column_name;
    private final String column_type;
    private final String column_options;

    DBKeys(String column_name, String column_type, String column_options) {
        this.column_name = column_name;
        this.column_type = column_type;
        this.column_options = column_options;
    }


    public String getColumnName(){
        return column_name;
    }




    public String getColumnType(){
        return column_type;
    }



    public String getColumnOptions(){
        return column_options;
    }



}
