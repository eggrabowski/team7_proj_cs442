package edu.iit.cs442.team7.iitbazaar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Swathi Shenoy on 11/7/2015.
 */
public class CustomDBAdapter {
    private Context context;
    private SQLiteDatabase db;
    private DBController helper;

    //Database Fields
    private static final String USER_DATABASE_TABLE = "users"; //TABLE NAME
    //Database Fields
    private static final String ITEM_DATABASE_TABLE = "item"; //TABLE NAME

    public static final String ID = "id_user";
    public static final String NAME = "user_name";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String MAJOR_DEPARTMENT = "major_department";
    public static final String PICTURE = "picture";
    public static final String PICTURE_THUMBNAIL = "picture_thumbnail";
    public static final String UPDATE_STATUS = "udpateStatus";

    public static final String ITEM_NUMBER = "item_number";
    public static final String LISTING_START_DATE = "listing_start_date";
    public static final String LISTING_END_DATE = "listing_end_date";
    public static final String ITEM_NAME = "item_name";
    public static final String DESCRIPTION = "description";
    public static final String RATING = "rating";
    public static final String LISTING_USER_EMAIL = "listing_user_email";
    public static final String QRCODE_VECTOR = "qrcode_vector";
    /**
     *  CONSTRUCTOR
     */
    public CustomDBAdapter(Context context){
        this.context = context;
    }

    /**
     * OPEN A NEW CONNECTION (R/W) TO THE DB
     *
     * RETURNS TRUE IF THE CONNECTION SUCCEEDED
     */
    public CustomDBAdapter open() throws SQLException {
        helper = new DBController(context);
        db = helper.getWritableDatabase();
        return this;
    }

    /**
     *  CLOSE THE CONNECTION TO THE DB
     */
    public void close(){
        helper.close();
    }

    /**
     * CREATE CONTENT VALUES FOR CONTENT RESOLVERS
     */
    private ContentValues createUserContentValues(String name, String email,String first_name,String last_name,String major_department){
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(EMAIL, email);
        cv.put(FIRST_NAME, first_name);
        cv.put(LAST_NAME, last_name);
        cv.put(MAJOR_DEPARTMENT, major_department);
        return cv;
    }

    /**
     * CREATE CONTENT VALUES FOR CONTENT RESOLVERS
     */
    private ContentValues createItemContentValues(String startDate, String endDate,String item_name,String description,String rating,String listing_user_email,String qrCodeVector){
        ContentValues cv = new ContentValues();
        cv.put(LISTING_START_DATE, startDate);
        cv.put(LISTING_END_DATE, endDate);
        cv.put(ITEM_NAME, item_name);
        cv.put(DESCRIPTION, description);
        cv.put(RATING,rating);
        cv.put(LISTING_USER_EMAIL,listing_user_email);
        cv.put(QRCODE_VECTOR,qrCodeVector);
        return cv;
    }

    /**
     * CREATE A NEW RECORD INTO THE DB
     */
    public long createUser(String name,String email,String first_name,String last_name,String major_department){
        ContentValues initialValues = createUserContentValues(name,email,first_name,last_name,major_department);
        return db.insert(USER_DATABASE_TABLE, null, initialValues);
    }

    /**
     * UPDATE A RECORD INTO THE DB
     */
    public boolean updateUser(long _id, String name,String email,String first_name,String last_name,String major_department){
        ContentValues updateValues = createUserContentValues(name,email,first_name,last_name,major_department);
        return db.update(USER_DATABASE_TABLE, updateValues, ID + "=" + _id, null) > 0;
    }

    /**
     * DELETE A RECORD
     */
    public boolean deleteUser(long _id){
        return db.delete(USER_DATABASE_TABLE, ID + "=" + _id, null) > 0;
    }

    /**
     * RETURNS ALL TABLE RECORDS
     */
    public Cursor selectAllUserRecords(){
        String[] columns = {ID,NAME,EMAIL,FIRST_NAME,LAST_NAME,MAJOR_DEPARTMENT,PICTURE,PICTURE_THUMBNAIL,UPDATE_STATUS};
        Cursor myCursor = db.query(USER_DATABASE_TABLE, columns, null, null, null, null, null);
        if (myCursor != null) {
            myCursor.moveToFirst();
        }
        return myCursor;
    }

    /**
     * RETURNS A SINGLE RECORD
     */
    public Cursor selectSingleUserRecord(long _id) throws SQLException{
        String[] columns = {ID,NAME,EMAIL,FIRST_NAME,LAST_NAME,MAJOR_DEPARTMENT,PICTURE,PICTURE_THUMBNAIL,UPDATE_STATUS};
        return db.query(USER_DATABASE_TABLE, columns, ID + "=" + _id, null, null, null, null);
    }
    /**
     * CREATE A NEW RECORD INTO THE DB
     */
    public long createItem(String startDate, String endDate,String item_name,String description,String rating,String listing_user_email,String qrCodeVector){
        ContentValues initialValues = createItemContentValues(startDate, endDate, item_name, description, rating, listing_user_email, qrCodeVector);
        return db.insert(ITEM_DATABASE_TABLE, null, initialValues);
    }

    /**
     * UPDATE A RECORD INTO THE DB
     */
    public boolean updateItem(long _id, String startDate, String endDate,String item_name,String description,String rating,String listing_user_email,String qrCodeVector){
        ContentValues updateValues = createItemContentValues(startDate, endDate, item_name, description, rating, listing_user_email, qrCodeVector);
        return db.update(ITEM_DATABASE_TABLE, updateValues, ITEM_NUMBER + "=" + _id, null) > 0;
    }

    /**
     * DELETE A RECORD
     */
    public boolean deleteItem(long _id){
        return db.delete(ITEM_DATABASE_TABLE, ITEM_NUMBER + "=" + _id, null) > 0;
    }

    /**
     * RETURNS ALL TABLE RECORDS
     */
    public Cursor selectAllItemRecords(){
        String[] columns = {ITEM_NUMBER,LISTING_START_DATE,LISTING_END_DATE,ITEM_NAME,DESCRIPTION,RATING,LISTING_USER_EMAIL,QRCODE_VECTOR};
        Cursor myCursor = db.query(ITEM_DATABASE_TABLE, columns, null, null, null, null, null);
        if (myCursor != null) {
            myCursor.moveToFirst();
        }
        return myCursor;
    }

    /**
     * RETURNS A SINGLE RECORD
     */
    public Cursor selectSingleItemRecord(long _id) throws SQLException{
        String[] columns = {ITEM_NUMBER,LISTING_START_DATE,LISTING_END_DATE,ITEM_NAME,DESCRIPTION,RATING,LISTING_USER_EMAIL,QRCODE_VECTOR};
        return db.query(ITEM_DATABASE_TABLE, columns, ITEM_NUMBER + "=" + _id, null, null, null, null);
    }
}
