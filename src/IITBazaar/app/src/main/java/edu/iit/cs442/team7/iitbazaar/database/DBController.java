package edu.iit.cs442.team7.iitbazaar.database;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import edu.iit.cs442.team7.iitbazaar.BuildConfig;
import edu.iit.cs442.team7.iitbazaar.Category;
import edu.iit.cs442.team7.iitbazaar.CategoryUpdateListener;
import edu.iit.cs442.team7.iitbazaar.FollowStatusListener;
import edu.iit.cs442.team7.iitbazaar.IITBazaar;
import edu.iit.cs442.team7.iitbazaar.Item;
import edu.iit.cs442.team7.iitbazaar.ItemNumberListener;
import static edu.iit.cs442.team7.iitbazaar.common.DBKeys.*;

import edu.iit.cs442.team7.iitbazaar.ItemRetrievalListener;
import edu.iit.cs442.team7.iitbazaar.MenuKeys;
import edu.iit.cs442.team7.iitbazaar.NewItemsListener;
import edu.iit.cs442.team7.iitbazaar.SellItemUpdateListener;
import edu.iit.cs442.team7.iitbazaar.UserRetrievalListener;
import edu.iit.cs442.team7.iitbazaar.WatchlistUpdateListener;
import edu.iit.cs442.team7.iitbazaar.common.DBKeys;
import edu.iit.cs442.team7.iitbazaar.user.User;

public class DBController extends SQLiteOpenHelper {


    final private String insertUserEndPointString = BuildConfig.insertUserEndPointString;
    final private String insertItemEndPointString = BuildConfig.insertItemEndPointString;
    final private String insertSellItemEndPointString = BuildConfig.insertSellItemEndPointString;
    final private String getItemEndPointString = BuildConfig.getItemEndPointString;
    final private String getSellItemEndPointString = BuildConfig.getSellItemEndPointString;
    final private String getUserEndPointString = BuildConfig.getUserEndPointString;
    final private String syncRemoteItemsEndPointString = BuildConfig.syncRemoteItemsEndPointString;
    final private String diffRemoteItemsEndPointString = BuildConfig.diffRemoteItemsEndPointString;
    final private String addItemToCurrentUserWatchListEndPointString = BuildConfig.addItemToCurrentUserWatchListEndPointString;
    final private String removeItemFromCurrentUserWatchListEndPointString = BuildConfig.removeItemFromCurrentUserWatchListEndPointString;
    final private String syncCurentUserWatchListEndPointString = BuildConfig.syncCurentUserWatchListEndPointString;
    final private String unlistItemEndPointString = BuildConfig.unlistItemEndPointString;
    final private String categoryEndpointString = BuildConfig.categoryEndpointString;

    final private String USER_TABLE = "user";
    final private String ITEM_TABLE = "item";
    final private String CATEGORY_TABLE = "category";
    final private String SELL_ITEM_TABLE = "selling_item";
    final private String WATCHLIST_TABLE = "watchlist";


    public final String SPACE = " ";
    public final String CREATE_TABLE = "CREATE TABLE";
    public final String LEFT_BRACKET = "(";
    public final String RIGHT_BRACKET = ")";
    public final String COMMA = ",";
    public final String SEMICOLON = ";";
    public final String EMPTY = "";
    public final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    public final String TIMING_QUERY = "where ? > ?";
    public final String[] timing_args = new String[]{
        ITEM_LISTING_END_DATE.getColumnName(),""
    };

    public final String GROUPBY_ASCENDING = "ASC";
    public final String GROUPBY_DESCENDING = "DESC";


    private final DBKeys item_columns[] = new DBKeys[]{
            ITEM_ITEM_NUMBER,
            ITEM_LISTING_END_DATE,
            ITEM_TITLE,
            ITEM_DESCRIPTION,
            ITEM_PICTURE_THUMBNAIL,
            ITEM_PRICE,
            ITEM_CATEGORY
    };

    private final DBKeys selling_item_columns[] = new DBKeys[]{
            ITEM_ITEM_NUMBER,
            ITEM_LISTING_END_DATE,
            ITEM_TITLE,
            ITEM_DESCRIPTION,
            ITEM_PICTURE_THUMBNAIL,
            ITEM_PRICE,
            ITEM_CATEGORY
    };

    private final DBKeys user_columns[] = new DBKeys[]{
            USER_EMAIL,
            USER_FIRST_NAME,
            USER_LAST_NAME,
            USER_MAJOR_DEPARTMENT,
            USER_PICTURE_THUMBNAIL
    };


    private final DBKeys category_columns[] = new DBKeys[]{
            CATEGORY_NUMBER,
            CATEGORY_PARENT_NUMBER,
            CATEGORY_NAME
    };

    private final DBKeys watchlist_columns[] = new DBKeys[] {
            ITEM_ITEM_NUMBER,
            ITEM_LISTING_END_DATE,
            ITEM_TITLE,
            ITEM_DESCRIPTION,
            ITEM_PICTURE_THUMBNAIL,
            ITEM_PRICE,
            ITEM_CATEGORY
    };

    private final String item_selection_columns[];
    private final String user_selection_columns[];
    private final String category_selection_columns[];
    

    public DBController(final Context applicationcontext) {


        //this is where to increment the version
        super(applicationcontext, "iit_bazaar.db", null, 24);
        Log.i("DBControllerConfig", "insertUserEndPointString -> " + insertUserEndPointString);
        Log.i("DBControllerConfig","insertItemEndPointString -> "+insertItemEndPointString);
        Log.i("DBControllerConfig", "getUserEndPointString -> " + getUserEndPointString);
        Log.i("DBControllerConfig", "syncRemoteItemsEndPointString -> " + syncRemoteItemsEndPointString);


        int icLength = item_columns.length;
        item_selection_columns = new String[icLength];

        for (int i = 0; i<icLength; i++){
            item_selection_columns[i] = item_columns[i].getColumnName();
        }

        int ucLength = user_columns.length;
        user_selection_columns = new String[ucLength];

        for (int u = 0; u<ucLength; u++){
           user_selection_columns[u] = user_columns[u].getColumnName();
        }

        int cLength = category_columns.length;
        category_selection_columns = new String[cLength];
        for (int c = 0; c<cLength; c++){
            category_selection_columns[c] = category_columns[c].getColumnName();
        }

    }

    //1 = true, 0 = false
    @Override
    public void onCreate(final SQLiteDatabase database) {
        final String userQuery;
        //Create user table

        userQuery = genCreateQuery(
                USER_TABLE,
                
                user_columns
                );

        database.execSQL(userQuery);

        final String itemQuery;

        itemQuery =  genCreateQuery(
                ITEM_TABLE,
             item_columns
                );

        database.execSQL(itemQuery);
        //Create category table

        final String categoryQuery;
        //Create items table
        categoryQuery =  genCreateQuery(
                CATEGORY_TABLE,
                
                category_columns
                );

        database.execSQL(categoryQuery);

        //Create selling_item table

        final String sellingItemQuery;
        //Create selling_item table
        sellingItemQuery =  genCreateQuery(
                SELL_ITEM_TABLE,

                selling_item_columns
        );

        database.execSQL(sellingItemQuery);

        final String watchlistQuery;

        watchlistQuery = genCreateQuery(
                WATCHLIST_TABLE,

                watchlist_columns
        );

        database.execSQL(watchlistQuery);

        Log.d("DATABASE LOCATION ", database.getPath());

    }

    @Override
    public void onUpgrade(final SQLiteDatabase database, final int version_old, final int current_version) {
        final String dropUserQuery = DROP_TABLE+USER_TABLE;
        database.execSQL(dropUserQuery);
        final String dropItemquery = DROP_TABLE+ITEM_TABLE;
        database.execSQL(dropItemquery);
        final String  dropCategoryQuery = DROP_TABLE+CATEGORY_TABLE;
        database.execSQL(dropCategoryQuery);
        final String  dropSellingItemQuery = DROP_TABLE+SELL_ITEM_TABLE;
        database.execSQL(dropSellingItemQuery);
        final String  dropWatchlistQuery = DROP_TABLE+WATCHLIST_TABLE;
        database.execSQL(dropWatchlistQuery);
        onCreate(database);
    }

    /**
     * Inserts User into SQLite DB
     */

    public void insertUser(final User user) {


        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL.getColumnName(), user.getEmail());
        values.put(USER_FIRST_NAME.getColumnName(), user.getFirstName());
        values.put(USER_LAST_NAME.getColumnName(), user.getLastName());
        values.put(USER_MAJOR_DEPARTMENT.getColumnName(), user.getMajor_department());
        values.put(USER_PICTURE_THUMBNAIL.getColumnName(), user.getPictureThumbnailByte());
        database.insert(USER_TABLE, null, values);
     //   database.close();

    }


    /**
     * Inserts User into remote
     **/


    public void  insertUserRemotely(final User user, final UserRetrievalListener userRetrievalListener) {

        final ArrayList<String> userArray = new ArrayList<>(6);

        userArray.add(user.getEmail());
        userArray.add(user.getFirstName());
        userArray.add(user.getLastName());
        userArray.add(user.getMajor_department());
        userArray.add(new String(Base64.encode(user.getPictureThumbnailByte(), Base64.DEFAULT)));
        userArray.add(new String(Base64.encode(user.getPictureByte(), Base64.DEFAULT)));

        final Gson gson = new GsonBuilder().create();
        final String userToInsert = gson.toJson(userArray);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("userJSON", userToInsert);

        Log.d("insertUserRemotely", "userJSON -> " + userToInsert);

        client.post(insertUserEndPointString, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {


                Log.i("insertUserRemotely", "User inserted - statcode " + statusCode);


                try {

                    Log.d("insertUserRemotely", "response string was = " + responseString);
                    int resCode = Integer.parseInt(responseString);

                    Log.d("insertUserRemotely", "response code was = " + resCode);

                    if (0 == resCode) {
                        userRetrievalListener.notifyUserInsertion(user.getEmail());
                    } else {
                        userRetrievalListener.notifyUserInsertionError(resCode);
                    }

                } catch (NumberFormatException nfe) {
                    userRetrievalListener.notifyUserInsertionError(-1);
                    Log.e("insertUserRemotely", "Cannot parse raw response from user insertion: " + responseString);
                }


            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("insertUserRemotely", "Failed: " + statusCode);
                Log.e("insertUserRemotely", "Error : " + throwable);


                userRetrievalListener.notifyUserInsertionError(-statusCode);

                if (statusCode == 404)

                {
                    Log.e("insertUserRemotely", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("insertUserRemotely", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("insertUserRemotely", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }


        });


    }







    /**
     * Inserts Item into remote
     **/

    //item number listen

    public void insertItemRemotely(final Item item, final ItemNumberListener currentItemNumberListener) {

        final ArrayList<String> itemArray = new ArrayList<>(7);


        itemArray.add(Long.toString(item.getListingStartDateLong()));
        itemArray.add(Long.toString(item.getListingEndDateLong()));
        itemArray.add(item.getTitle());
        itemArray.add(item.getItemDescription());
        itemArray.add(item.getListingUserEmail());
        itemArray.add(new String(Base64.encode(item.getThumbByte(), Base64.DEFAULT)));
        itemArray.add(new String(Base64.encode(item.getImageByte(), Base64.DEFAULT)));
        itemArray.add(item.getPrice());
        itemArray.add(Integer.toString(item.getCategory()));


        final Gson gson = new GsonBuilder().create();
        final String itemToInsert = gson.toJson(itemArray);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("itemsJSON", itemToInsert);

        Log.d("insertItemRemotely", "itemJson -> " + itemToInsert);

        client.post(insertItemEndPointString, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {


                Log.d("insertItemRemotely", "Success Item inserted - statcode " + statusCode);
                Log.d("insertItemRemotely", "Raw response " + responseString);

                try {
                    final int itemNo = Integer.parseInt(responseString.toString());
                    if (itemNo == 0) {
                        Log.d("insertItemRemotely", "Strange " + responseString);
                    }

                    currentItemNumberListener.notifyItemNumber(itemNo);


                } catch (NumberFormatException e) {

                    currentItemNumberListener.notifyItemNumber(-1);
                    Log.e("insertItemRemotely", "Cannot parse - " + responseString);
                }


            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("insertItemRemotely", "Failed: " + statusCode);
                Log.e("insertItemRemotely", "Error : " + throwable);


                currentItemNumberListener.notifyItemNumber(-statusCode);

                if (statusCode == 404)

                {
                    Log.e("insertItemRemotely", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("insertItemRemotely", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("insertItemRemotely", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }


        });


    }

    public void insertIntoRemoteSellTable(final Item item,final ItemNumberListener currentItemNumberListener){
        final ArrayList<String> itemArray = new ArrayList<>(7);


        itemArray.add(Long.toString(item.getListingStartDateLong()));
        itemArray.add(Long.toString(item.getListingEndDateLong()));
        itemArray.add(item.getTitle());
        itemArray.add(item.getItemDescription());
        itemArray.add(item.getListingUserEmail());
        itemArray.add(new String(Base64.encode(item.getThumbByte(), Base64.DEFAULT)));
        itemArray.add(new String(Base64.encode(item.getImageByte(), Base64.DEFAULT)));
        itemArray.add(item.getPrice());
        itemArray.add(Integer.toString(item.getCategory()));


        final Gson gson = new GsonBuilder().create();
        final String sellItemToInsert = gson.toJson(itemArray);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("sellItemsJSON", sellItemToInsert);

        Log.d("insertSellItemRemotely", "itemJson -> " + sellItemToInsert);

        client.post(insertSellItemEndPointString, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {


                Log.d("insertSellItemRemotely", "Success Item inserted - statcode " + statusCode);
                Log.d("insertSellItemRemotely", "Raw response " + responseString);

                try {
                    final int itemNo = Integer.parseInt(responseString.toString());
                    if (itemNo == 0) {
                        Log.d("insertSellItemRemotely", "Strange " + responseString);
                    }

                    currentItemNumberListener.notifyItemNumber(itemNo);


                } catch (NumberFormatException e) {

                    currentItemNumberListener.notifyItemNumber(-1);
                    Log.e("insertSellItemRemotely", "Cannot parse - " + responseString);
                }


            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("insertSellItemRemotely", "Failed: " + statusCode);
                Log.e("insertSellItemRemotely", "Error : " + throwable);


                currentItemNumberListener.notifyItemNumber(-statusCode);

                if (statusCode == 404)

                {
                    Log.e("insertSellItemRemotely", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("insertSellItemRemotely", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("insertSellItemRemotely", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }


        });
    }


    // java.lang.IllegalStateException: Cannot
    public void insertItemLocally(final Item item) {

        try {
            final SQLiteDatabase database = this.getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(ITEM_ITEM_NUMBER.getColumnName(), item.getItemNumber());
            values.put(ITEM_LISTING_END_DATE.getColumnName(), item.getListingEndDateLong());
            values.put(ITEM_TITLE.getColumnName(), item.getTitle());
            values.put(ITEM_DESCRIPTION.getColumnName(), item.getItemDescription());
            values.put(ITEM_PICTURE_THUMBNAIL.getColumnName(), item.getThumbByte());
            values.put(ITEM_PRICE.getColumnName(), item.getPrice());
            values.put(ITEM_CATEGORY.getColumnName(), item.getCategory());
            database.insert(ITEM_TABLE, null, values);
        //    database.close();
        }catch (IllegalStateException ise){
            Log.e("insertItemLocally","Database in illegal state");
        }

}

    public void insertCategoryLocally(final Category category) {

        try{
        final SQLiteDatabase database = this.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(CATEGORY_NUMBER.getColumnName(), category.getCategoryNumber());
        values.put(CATEGORY_PARENT_NUMBER.getColumnName(), category.getParentCategoryNumber());
        values.put(CATEGORY_NAME.getColumnName(), category.getCategoryName());
        database.insert(CATEGORY_TABLE, null, values);
     //   database.close();
    }catch (IllegalStateException ise){
        Log.e("insertItemLocally","Database in illegal state");
    }

    }


    public void insertSellItemLocally(final Item item) {

        try{
        final SQLiteDatabase database = this.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(ITEM_ITEM_NUMBER.getColumnName(), item.getItemNumber());
        values.put(ITEM_LISTING_END_DATE.getColumnName(), item.getListingEndDateLong());
        values.put(ITEM_TITLE.getColumnName(), item.getTitle());
        values.put(ITEM_DESCRIPTION.getColumnName(), item.getItemDescription());
        //values.put(ITEM_LISTING_USER_EMAIL.getColumnName(), item.getListingUserEmail());
        values.put(ITEM_PICTURE_THUMBNAIL.getColumnName(), item.getThumbByte());
        values.put(ITEM_PRICE.getColumnName(), item.getPrice());
        values.put(ITEM_CATEGORY.getColumnName(), item.getCategory());
        database.insert(SELL_ITEM_TABLE, null, values);
    //    database.close();
        }catch (IllegalStateException ise){
            Log.e("insertItemLocally","Database in illegal state");
        }

    }

    /**
     * Get list of Users from SQLite DB as Array List
     */

    public ArrayList<User> getAllUsers() {
        ArrayList <User> allUsers;
        allUsers = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM user";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                /*
                USER_EMAIL,
                USER_FIRST_NAME,
                USER_LAST_NAME,
                USER_MAJOR_DEPARTMENT,
                USER_PICTURE_THUMBNAIL
                 */
                User user = new User();
                user.setEmail(cursor.getString(0));
                user.setFirstName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setMajor_department(cursor.getString(3));
                user.setPictureThumbnail(cursor.getBlob(4));
                allUsers.add(user);
            } while (cursor.moveToNext());
        }
    //    database.close();
        return allUsers;
    }



    public void getUser(final String email, final UserRetrievalListener userRetrievalListener) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user", email);

        Log.d("getUser", "email -> " + email);

        client.post(getUserEndPointString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final JSONObject response) {


                Log.d("getUser", "User got - statcode: " + statusCode);
                Log.d("getUser", "Raw response: " + response.toString());


                if (response.has("email")) {

                    Log.d("getUser", "Response contain email column");

                    try {
                        Log.d("getUser", "Constructing new user from JSON");
                        final User user = new User();
                        user.setEmail(email);
                        user.setFirstName(response.getString("first_name"));
                        user.setLastName(response.getString("last_name"));
                        user.setMajor_department(response.getString("major_department"));
                        user.setPictureThumbnail(Base64.decode(response.getString("picture"), Base64.DEFAULT));
                        Log.d("getUser", "Returning JSON constructed user");
                        userRetrievalListener.notifyUserFound(user);

                    } catch (JSONException e) {
                        Log.e("getUser", "Error encountered constructing user from response -> " + e.getLocalizedMessage());
                        e.printStackTrace();
                        userRetrievalListener.notifyUserRetrievalError(-1);
                    }
                } else {
                    Log.d("getUser", "User not found");
                    userRetrievalListener.notifyUserNotFound(email);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("getUser", "Failed: " + statusCode);
                Log.e("getUser", "Error: " + throwable);
                Log.e("getUser", "TextResponse: " + responseString);


                userRetrievalListener.notifyUserRetrievalError(-statusCode);

                if (statusCode == 404)

                {
                    Log.e("getUser", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("getUser", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("getUser", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }


        });




    }
/*
    public User getRemoteUser(String email) {




        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor =   database.query(USER_TABLE, null, USER_EMAIL.getColumnName() + "=?", new String[]{email}, null, null, null);

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setEmail(cursor.getString(0));
            user.setFirstName(cursor.getString(1));
            user.setLastName(cursor.getString(2));
            user.setMajor_department(cursor.getString(3));
            user.setPictureThumbnail(cursor.getBlob(4));
            Log.i("User found","Fields: email=" + user.getEmail());
            return user;
        }
        return null;
    }*/




    /**
     * Get list of Items from SQLite DB as Array List
     */

   /* public ArrayList<Item> getAllItems() {
        ArrayList<Item> allItems;
        allItems = new ArrayList<Item>();
        String selectQuery = "SELECT * FROM item";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemNumber(cursor.getInt(0));
                item.setListingEndDate(cursor.getLong(1));
                item.setTitle(cursor.getString(2));
                item.setItemDescription(cursor.getString(3));
                item.setListingUserEmail(cursor.getString(4));
                item.setThumbnail(cursor.getBlob(5));
                item.setPrice(cursor.getString(6));
                allItems.add(item);
            } while (cursor.moveToNext());
        }
        database.close();
        return allItems;
    }
*/


    public ArrayList<Item> getItemsNotIn(String arrayOfItems) {
        ArrayList<Item> allItems;
        allItems = new ArrayList<Item>();
        String selectQuery = "SELECT * FROM item where item_number not in ("+arrayOfItems+")";
        Log.d("getItemsNotIn","Executing query: "+selectQuery);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        Log.d("getItemsNotIn","Get column names = "+Arrays.toString(cursor.getColumnNames()));



        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemNumber(cursor.getInt(0));           // item_number
                item.setListingEndDate(cursor.getLong(1));      // item_listing_end_date
                item.setTitle(cursor.getString(2));                  // item_title
                item.setItemDescription(cursor.getString(3));   // item_description
                item.setThumbnail(cursor.getBlob(4));           // item_picture_thumbnail
                item.setPrice(cursor.getString(5));             // item_price
                item.setCategory(cursor.getInt(6));             // category_number
                allItems.add(item);
            } while (cursor.moveToNext());
        }
    //    database.close();
        return allItems;
    }


    public Cursor getItemsByCategory(final int categoryNumber) {
        SQLiteDatabase database = this.getWritableDatabase();


        Cursor cursor = database.rawQuery("SELECT * FROM item WHERE category_number = "+categoryNumber+" ORDER BY item_listing_end_date DESC", null);
        //database.close();
        return cursor;
    }

    public Cursor getCategories(final int parentCategoryNumber) {
        SQLiteDatabase database = this.getWritableDatabase();


        Cursor cursor = database.rawQuery("SELECT * FROM category WHERE parent_category_number = "+parentCategoryNumber+" ORDER BY category_name ASC", null);
        //database.close();
        return cursor;
    }

    public Cursor getCategoriesForSpinner(final int parentCategoryNumber) {
        SQLiteDatabase database = this.getWritableDatabase();


        Cursor cursor = database.rawQuery("SELECT * FROM category WHERE parent_category_number = "+parentCategoryNumber+" OR category_number = -2 ORDER BY category_name ASC", null);
        //database.close();
        return cursor;
    }



    public Cursor getCurrentItems() {
        SQLiteDatabase database = this.getWritableDatabase();

        //ORDER BY ... ASC;

        //date range
        final Long current = (new Date()).getTime();
        timing_args[1] = Long.toString(current);
        //Log.e("getItemsNotIn() query", "TIMING_QUERY: " + TIMING_QUERY);
        //Log.e("getItemsNotIn() query", "timing_args: " + timing_args[0] + " " + timing_args[1]);
        /*Cursor cursor = database.query(
                ITEM_TABLE,
                item_selection_columns,
                TIMING_QUERY,
                timing_args,
                null,
                null,
                ITEM_LISTING_END_DATE.getColumnName() + SPACE + GROUPBY_ASCENDING);*/
        Cursor cursor = database.rawQuery("SELECT item_number, item_listing_end_date, item_title, item_description, item_picture_thumbnail, item_price, category_number FROM item WHERE item_listing_end_date > '" + timing_args[1] + "' ORDER BY item_listing_end_date DESC", null);
        //database.close();
        return cursor;
    }
    /*Swathi Shenoy: Retrieve the search items by matching item name or description*/
    public Cursor getSearchItems(String search_text){

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT item_number, item_listing_end_date, item_title, item_description, item_picture_thumbnail, item_price, category_number FROM item WHERE item_title like '%" + search_text + "%' OR item_description like '%" + search_text + "%' ORDER BY item_listing_end_date DESC", null);
        //database.close();
        return cursor;
    }
    /*get sell items from local*/
    public Cursor getSellingItems() {
        SQLiteDatabase database = this.getWritableDatabase();

        //ORDER BY ... ASC;

        //date range
        Cursor cursor = database.rawQuery("SELECT item_number, item_listing_end_date, item_title, item_description,item_picture_thumbnail, item_price, category_number FROM selling_item ORDER BY item_listing_end_date DESC", null);
        //database.close();
        return cursor;
    }
    /*Swathi Shenoy: Get the items sold by the user from remote DB*/
    public void getSellItem(final String email,final SellItemUpdateListener remoteUpdateListener){

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_email", email);



        Log.i("getItem", "Connecting to " + getSellItemEndPointString);
        client.post(getSellItemEndPointString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  Header[] headers,
                                  org.json.JSONArray response) {
                Log.i("getSellItem", "Success Items retrieved - statcode " + statusCode);
                Log.i("getSellItem", "Raw response " + response.toString());
                final ArrayList<Item> remoteSellItems = new ArrayList();

                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject itemJSon = response.getJSONObject(i);
                        Item item = new Item();
                        item.setItemNumber(itemJSon.getInt("item_number"));
                        item.setListingEndDate(itemJSon.getLong("listing_end_date"));
                        item.setTitle(itemJSon.getString("item_name"));
                        item.setItemDescription(itemJSon.getString("description"));
                        item.setListingUserEmail(itemJSon.getString("listing_user_email"));
                        item.setThumbnail(Base64.decode(itemJSon.getString("item_picture_thumbnail"), 0));
                        item.setPrice(itemJSon.getString("item_price"));
                        item.setCategory(itemJSon.getInt("category_number"));
                        remoteSellItems.add(item);
                    }

                    upgradeLocalSellItems(remoteSellItems);
                    //here you need to register the callback

                    remoteUpdateListener.notifySellItemUpdated();


                } catch (JSONException e) {
                    Log.e("getSellItem", "Items JSON Can't retrieve JSONObject");
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("getSellItem", "Failed: " + statusCode);
                Log.e("getSellItem", "Error : " + throwable);

                if (statusCode == 404)

                {
                    Log.e("getSellItem", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("getSellItem", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("getSellItem", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }
        });
    }


    public void getRemoteCategories(final CategoryUpdateListener categoryUpdateListener){

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        Log.i("getRemoteCategories", "Connecting to " + categoryEndpointString);
        client.post(categoryEndpointString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  Header[] headers,
                                  org.json.JSONArray response) {
                Log.i("getRemoteCategories", "Success Items retrieved - statcode " + statusCode);
                Log.i("getRemoteCategories", "Raw response " + response.toString());
                final ArrayList<Category> remoteCategories = new ArrayList();

                try {


                    for (int i = 0; i < response.length(); i++) {
                        JSONObject itemJSon = response.getJSONObject(i);

                        Category category = new Category();

                        category.setCategoryNumber(itemJSon.getInt("category_number"));
                        category.setParentCategoryNumber(itemJSon.getInt("parent_category_number"));
                        category.setCategoryName(itemJSon.getString("category_name"));


                        remoteCategories.add(category);
                    }

                    upgradeLocalCategories(remoteCategories);
                    //here you need to register the callback

                    try {
                        if (null != categoryUpdateListener) {
                            Log.i("getRemoteCategories", "Category update listeners notified");
                            categoryUpdateListener.notifyCategoryListUpdated();

                        } else {
                            Log.i("getRemoteCategories", "Category update listeners not set");
                        }
                    }catch (NullPointerException npe){
                        Log.i("getRemoteCategories", "Category update listeners not set -- android is broken");
                    }

                } catch (JSONException e) {
                    Log.e("getRemoteCategories", "Category JSON Can't retrieve JSONObject");
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("getRemoteCategories", "Failed: " + statusCode);
                Log.e("getRemoteCategories", "Error : " + throwable);

                if (statusCode == 404)

                {
                    Log.e("getRemoteCategories", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("getRemoteCategories", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("getRemoteCategories", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }
        });
    }





    /**
     * Get list of Items from SQLite DB as Array List
     */

    public ArrayList<String> getAllCategories() {
        ArrayList<String> allCategories;
        allCategories = new ArrayList<>();
        String selectQuery = "SELECT * FROM category";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                allCategories.add(Integer.toString(cursor.getInt(0)));
                allCategories.add(Integer.toString(cursor.getInt(1)));
                allCategories.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
    //    database.close();
        Log.d("getAllCategories", "Array size is :" + allCategories.size());
        return allCategories;
    }

    /**
     * Get items after a date
     */

    public void syncRemoteItemsAsynchronously(final RemoteUpdateListener remoteUpdateListener) {


        Calendar inCalendar = Calendar.getInstance();
        inCalendar.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        inCalendar.set(Calendar.HOUR_OF_DAY, 0);
        inCalendar.set(Calendar.MINUTE, 0);
        inCalendar.set(Calendar.SECOND, 0);
        inCalendar.set(Calendar.MILLISECOND, 0);
        Date today = Calendar.getInstance().getTime();

        long dateLong = today.getTime();
        String dateString = Long.toString(dateLong);

        final Gson gson = new GsonBuilder().create();
        final String dateParam = gson.toJson(dateString);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("dateJSON", dateParam);

        final ArrayList <Item> remoteItems = new ArrayList();

        Log.i("Connecting to", syncRemoteItemsEndPointString);
        client.post(syncRemoteItemsEndPointString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  cz.msebera.android.httpclient.Header[] headers,
                                  org.json.JSONArray response) {
                Log.i("syncRemoteItemsSync", "Success Items retrieved - statcode " + statusCode);
                Log.i("syncRemoteItemsSync", "Raw response " + response.toString());
                Log.i("syncRemoteItemsSync", "JSONArray length: " + response.length());
                int i = -1;
                try {
                    for (i = 0; i < response.length(); i++) {

                        JSONObject itemJSon = response.getJSONObject(i);
                        Item item = new Item();
                        item.setItemNumber(itemJSon.getInt("item_number"));
                        item.setListingEndDate(itemJSon.getLong("listing_end_date"));
                        item.setTitle(itemJSon.getString("item_name"));
                        item.setItemDescription(itemJSon.getString("description"));
                        item.setThumbnail(Base64.decode(itemJSon.getString("item_picture_thumbnail"), 0));
                        item.setPrice(itemJSon.getString("item_price"));
                        item.setCategory(itemJSon.getInt("category_number"));
                        remoteItems.add(item);


                    }
                    upgradeLocalItems(remoteItems);
                    //here you need to register the callback

                    remoteUpdateListener.notifyDataChange();

                    // upgradeLocalItems(remoteItems);
                } catch (JSONException e) {
                    Log.e("syncRemoteItemsSync", "Items JSON Can't retrieve JSONObject from position " + i);
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("syncRemoteItemsSync", "Failed: " + statusCode);
                Log.e("syncRemoteItemsSync", "Error : " + throwable);


                if (statusCode == 404)

                {
                    Log.e("syncRemoteItemsSync", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("syncRemoteItemsSync", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("syncRemoteItemsSync", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }
        });


    }




    /**
     * Get items after a date
     */

    public void syncRemoteItemsSynchronously(final RemoteUpdateListener remoteUpdateListener) {


        Calendar inCalendar = Calendar.getInstance();
        inCalendar.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        inCalendar.set(Calendar.HOUR_OF_DAY, 0);
        inCalendar.set(Calendar.MINUTE, 0);
        inCalendar.set(Calendar.SECOND, 0);
        inCalendar.set(Calendar.MILLISECOND, 0);
        Date today = Calendar.getInstance().getTime();

        long dateLong = today.getTime();
        String dateString = Long.toString(dateLong);

        final Gson gson = new GsonBuilder().create();
        final String dateParam = gson.toJson(dateString);

        final SyncHttpClient client = new SyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("dateJSON", dateParam);

        final ArrayList <Item> remoteItems = new ArrayList();

        Log.i("Connecting to", syncRemoteItemsEndPointString);
        client.post(syncRemoteItemsEndPointString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  cz.msebera.android.httpclient.Header[] headers,
                                  org.json.JSONArray response) {
                Log.i("syncRemoteItemsSync", "Success Items retrieved - statcode " + statusCode);
                Log.i("syncRemoteItemsSync", "Raw response " + response.toString());
                Log.i("syncRemoteItemsSync", "JSONArray length: " + response.length());
                int i = -1;
                try {
                    for (i = 0; i < response.length(); i++) {

                        JSONObject itemJSon = response.getJSONObject(i);
                        Item item = new Item();
                        item.setItemNumber(itemJSon.getInt("item_number"));
                        item.setListingEndDate(itemJSon.getLong("listing_end_date"));
                        item.setTitle(itemJSon.getString("item_name"));
                        item.setItemDescription(itemJSon.getString("description"));
                        item.setThumbnail(Base64.decode(itemJSon.getString("item_picture_thumbnail"), 0));
                        item.setPrice(itemJSon.getString("item_price"));
                        item.setCategory(itemJSon.getInt("category_number"));
                        remoteItems.add(item);


                    }
                    upgradeLocalItems(remoteItems);
                    //here you need to register the callback

                    remoteUpdateListener.notifyDataChange();

                    // upgradeLocalItems(remoteItems);
                } catch (JSONException e) {
                    Log.e("syncRemoteItemsSync", "Items JSON Can't retrieve JSONObject from position " + i);
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("syncRemoteItemsSync", "Failed: " + statusCode);
                Log.e("syncRemoteItemsSync", "Error : " + throwable);


                if (statusCode == 404)

                {
                    Log.e("syncRemoteItemsSync", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("syncRemoteItemsSync", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("syncRemoteItemsSync", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }
        });


    }

    
    


    private void upgradeLocalItems (ArrayList <Item> remoteItems) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i("Inserting items locally", "" + remoteItems.size());
        database.delete(ITEM_TABLE, null, null);
        for (Item item:remoteItems) {
            this.insertItemLocally(item);
            Log.i("Item inserted locally", item.getTitle() + ": " + item.getItemDescription());
        }
    //    database.close();
    }



    private void upgradeLocalCategories (ArrayList <Category> remoteCategories) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.i("upgradeLocalCategories", "" + remoteCategories.size());

        database.delete(CATEGORY_TABLE, null, null);
        for (Category category:remoteCategories) {
            this.insertCategoryLocally(category);
            Log.i("upgradeLocalCategories", category.getCategoryName() + ": " + category.getCategoryNumber() + ": " +  category.getParentCategoryNumber() );
        }
    //    database.close();
    }



    private String getLocalItemNumbers () {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "SELECT group_concat(item_number) FROM item";
        Cursor cursor = database.rawQuery(query, null, null);

        cursor.moveToFirst();
        String localItemResult = cursor.getString(0);

        if (null != localItemResult){
            Log.d("getLocalItemNumbers","local item "+localItemResult);
        }

    //    database.close();
        return localItemResult;


    }


    private void upgradeLocalSellItems (ArrayList <Item> remoteItems) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i("Inserting items locally", "" + remoteItems.size());
        database.delete(SELL_ITEM_TABLE, null, null);
        for (Item item:remoteItems) {
            this.insertSellItemLocally(item);
            Log.i("Sell inserted locally", item.getTitle() + ": " + item.getItemDescription());
        }
    //    database.close();
    }


    public void getItem(final int itemNumber, final ItemRetrievalListener itemRetrievalListener, final MenuKeys key) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        Log.d("getItem","itemNumber = "+itemNumber);
        params.put("item_number", itemNumber);


        Log.i("getItem", "Connecting to " + getItemEndPointString);
        client.post(getItemEndPointString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  Header[] headers,
                                  JSONObject itemJSon) {
                Log.i("getItem", "Success Items retrieved - statcode " + statusCode);
                Log.i("getItem", "Raw response " + itemJSon.toString());

                Log.i("getItem", "Json Length " + itemJSon.length());
                try {


                    if (0 != itemJSon.length()) {


                        Item item = new Item();
                        item.setItemNumber(itemJSon.getInt("item_number"));
                        item.setListingEndDate(itemJSon.getLong("listing_start_date"));
                        item.setListingEndDate(itemJSon.getLong("listing_end_date"));
                        item.setTitle(itemJSon.getString("item_name"));
                        item.setItemDescription(itemJSon.getString("description"));
                        item.setListingUserEmail(itemJSon.getString("listing_user_email"));
                        item.setThumbnail(Base64.decode(itemJSon.getString("item_picture_thumbnail"), 0));
                        item.setImage(Base64.decode(itemJSon.getString("item_picture"), 0));
                        item.setPrice(itemJSon.getString("item_price"));
                        item.setCategory(itemJSon.getInt("category_number"));
                        itemRetrievalListener.notifyItemFound(item, key);

                    } else {


                        itemRetrievalListener.notifyItemNotFound(itemNumber);


                    }
                    //notifiy return object


                } catch (JSONException e) {
                    Log.e("getItem", "Items JSON Can't retrieve JSONObject");
                    e.printStackTrace();
                    itemRetrievalListener.notifyItemRetrievalError(-1);
                }

            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("getItem", "Failed: " + statusCode);
                Log.e("getItem", "Error : " + throwable);


                itemRetrievalListener.notifyItemRetrievalError(-statusCode);

                if (statusCode == 404)

                {
                    Log.e("getItem", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("getItem", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("getItem", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }
        });


    }

    private String genCreateQuery(String tableName, DBKeys[] keys) {

        int keyLen = keys.length;

        if (0 != keyLen && null != tableName) {
            StringBuilder sb = new StringBuilder();
            sb.append(CREATE_TABLE);
            sb.append(SPACE);
            sb.append(tableName);
            sb.append(SPACE);
            sb.append(LEFT_BRACKET);
            sb.append(SPACE);



            for (int i = 0; i < keyLen; i++) {

                final DBKeys key = keys[i];

                sb.append(key.getColumnName());
                sb.append(SPACE);
                sb.append(key.getColumnType());
                sb.append(SPACE);
                sb.append(key.getColumnOptions());


                if (i+1 != keyLen){
                    sb.append(COMMA);
                    sb.append(SPACE);
                }

            }
            sb.append(SPACE);
            sb.append(RIGHT_BRACKET);
            sb.append(SEMICOLON);
            return sb.toString();
        }


        return EMPTY;

    }

    public boolean isInCurrentUserWatchList(int itemNumber) {
        //sync? Maybe already done when this is executed
        boolean toReturn;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM watchlist WHERE item_number='" + itemNumber + "'";
        Cursor cursor = database.rawQuery(query, null);
        toReturn = cursor.getCount() > 0;
    //    database.close();
        return toReturn;
    }

    public void addItemToCurrentUserWatchList(int itemNumber,
                                              WatchlistUpdateListener remoteUpdateListener,
                                              FollowStatusListener followListener) {
        String userEmail = IITBazaar.getCurrentUser().getEmail();
        insertWatchListItemRemotely(userEmail, itemNumber);
        syncCurrentUserWatchList(remoteUpdateListener, followListener);
    }

    private void insertWatchListItemLocally (Item item) {
        final SQLiteDatabase database = this.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(ITEM_ITEM_NUMBER.getColumnName(), item.getItemNumber());
        values.put(ITEM_LISTING_END_DATE.getColumnName(), item.getListingEndDateLong());
        values.put(ITEM_TITLE.getColumnName(), item.getTitle());
        values.put(ITEM_DESCRIPTION.getColumnName(), item.getItemDescription());
        values.put(ITEM_PICTURE_THUMBNAIL.getColumnName(), item.getThumbByte());
        values.put(ITEM_PRICE.getColumnName(), item.getPrice());
        values.put(ITEM_CATEGORY.getColumnName(), item.getCategory());
        //values.put(ITEM_LISTING_USER_EMAIL.getColumnName(), item.getListingUserEmail());
        database.insert(WATCHLIST_TABLE, null, values);
     //   database.close();
    }

    private void insertWatchListItemRemotely (final String userEmail, final int itemNumber) {
        final ArrayList<String> watchlistPairArray = new ArrayList<>(2);

        watchlistPairArray.add(userEmail);
        watchlistPairArray.add(Integer.toString(itemNumber));

        final Gson gson = new GsonBuilder().create();
        final String watchlistPairToInsert = gson.toJson(watchlistPairArray);
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("watchlistPairJSON", watchlistPairToInsert);
        Log.d("insertWatchListRem", "watchlistPairJson -> " + watchlistPairArray);

        client.post(addItemToCurrentUserWatchListEndPointString, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {


                Log.d("insertWatchListRem", "Success watchlist pair inserted - code " + statusCode);
                Log.d("insertWatchListRem", "Raw response " + responseString);

                String insertionResult = responseString.toString();
                if (insertionResult.equals("OK")) {
                    Log.d("insertWatchListItemRem", "Strange " + responseString);
                } else {
                    Log.e("insertWatchListItemRem", "Unsuccessful insertion");
                }
            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("insertWatchListRem", "Failed: " + statusCode);
                Log.e("insertWatchListRem", "Error : " + throwable);

                if (statusCode == 404)

                {
                    Log.e("insertWatchListRem", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("insertWatchListRem", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("insertWatchListRem", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }


        });
    }

    public void deleteItemFromCurrentUserWatchList(int itemNumber,
                                                   WatchlistUpdateListener remoteUpdateListener,
                                                   FollowStatusListener followListener) {
        String userEmail = IITBazaar.getCurrentUser().getEmail();
        deleteWatchlistItemRemotely(userEmail, itemNumber);
        syncCurrentUserWatchList(remoteUpdateListener, followListener);
    }

    private void deleteWatchlistItemRemotely (final String userEmail, final int itemNumber) {
        final ArrayList<String> watchlistPairArray = new ArrayList<>(2);

        watchlistPairArray.add(userEmail);
        watchlistPairArray.add(Integer.toString(itemNumber));

        final Gson gson = new GsonBuilder().create();
        final String watchlistPairToRemove = gson.toJson(watchlistPairArray);
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("watchlistPairJSON", watchlistPairToRemove);

        Log.d("insertWatchListRem", "watchlistPairJson -> " + watchlistPairArray);

        client.post(removeItemFromCurrentUserWatchListEndPointString, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                Log.d("deleteWatchListRem", "Success watchlist pair inserted - code " + statusCode);
                Log.d("deleteWatchListRem", "Raw response " + responseString);
                String deletionResult = responseString.toString();
                if (deletionResult.equals("OK")) {
                    Log.d("deleteWatchListItemRem", "Strange " + responseString);
                } else {
                    Log.e("deleteWatchListItemRem", "Unsuccessful deletion");
                }
            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                //  super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("deleteWatchListRem", "Failed: " + statusCode);
                Log.e("deleteWatchListRem", "Error : " + throwable);

                if (statusCode == 404)

                {
                    Log.e("deleteWatchListRem", "HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("deleteWatchListRem", "HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("deleteWatchListRem", "HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }


        });
    }
    /*
    private void deleteWatchlistItemLocally (final int itemNumber) {

    }*/

    public void syncCurrentUserWatchList (final WatchlistUpdateListener remoteUpdateListener,
                                          final FollowStatusListener followListener) {

        final String userEmail = IITBazaar.getCurrentUser().getEmail();
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ArrayList <Item> remoteItems = new ArrayList();

        params.put("email", userEmail);

        Log.i("Connecting to", syncCurentUserWatchListEndPointString);
        client.post(syncCurentUserWatchListEndPointString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  cz.msebera.android.httpclient.Header[] headers,
                                  org.json.JSONArray response) {
                Log.i("syncCurUserWList", "(watchlist) Success Items retrieved - statcode " + statusCode);
                Log.i("syncCurUserWList", "(watchlist) Raw response " + response.toString());
                Log.i("syncCurUserWList", "(watchlist) JSONArray length: " + response.length());
                int i = -1;
                try {
                    for (i = 0; i < response.length(); i++) {

                        JSONObject itemJSon = response.getJSONObject(i);
                        Item item = new Item();
                        item.setItemNumber(itemJSon.getInt("item_number"));
                        item.setListingEndDate(itemJSon.getLong("listing_end_date"));
                        item.setTitle(itemJSon.getString("item_name"));
                        item.setItemDescription(itemJSon.getString("description"));
                        item.setThumbnail(Base64.decode(itemJSon.getString("item_picture_thumbnail"), 0));
                        item.setPrice(itemJSon.getString("item_price"));
                        item.setCategory(itemJSon.getInt("category_number"));
                        //item.setListingUserEmail("listing_user_email");
                        remoteItems.add(item);
                    }
                    upgradeLocalWatchlist(remoteItems);
                    remoteUpdateListener.notifyWatchlistUpdated();
                    followListener.notifyFollowStatusUpdated();
                } catch (JSONException e) {
                    Log.e("syncCurUserWList", "(watchlist) Items JSON Can't retrieve JSONObject from position " + i);
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                Log.e("syncCurUserWList", "(watchlist) Failed: " + statusCode);
                Log.e("syncCurUserWList", "(watchlist) Error : " + throwable);


                if (statusCode == 404)

                {
                    Log.e("syncCurUserWList", "(watchlist) HTTP Error Requested resource not found");
                } else if (statusCode == 500)

                {
                    Log.e("syncCurUserWList", "(watchlist) HTTP Error Something went wrong at server end");
                } else

                {
                    Log.e("syncCurUserWList", "(watchlist) HTTP Error Unexpected Error occcured! " +
                            "[Most common Error: Device might not be connected to Internet]");
                }
            }
        });

    }

    private void upgradeLocalWatchlist (ArrayList <Item> remoteItems) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i("Inserting items locally", "(to watchlist) " + remoteItems.size());
        database.delete(WATCHLIST_TABLE, null, null);
        for (Item item:remoteItems) {
            this.insertWatchListItemLocally(item);
            Log.i("item inserted locally", item.getTitle() + " (in watchlist): " + item.getItemDescription());
        }
     //   database.close();
    }

    public Cursor getAllItemsInWatchList () {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT item_number,item_listing_end_date,item_title,item_description,item_picture_thumbnail,item_price,category_number " + "FROM watchlist", null);
        return cursor;
    }

    public void unlistItem(final int itemNumber, final UnlistItemListener unlistItemListener) {



            final AsyncHttpClient client = new AsyncHttpClient();
            final RequestParams params = new RequestParams();

            params.put("item_number", itemNumber);
            Log.d("unlistItem", "item_number -> " + itemNumber);

            client.post(unlistItemEndPointString, params, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(final int statusCode, final Header[] headers, final String unlistingResult) {


                    Log.d("unlistItem", "Success watchlist pair inserted - code " + statusCode);
                    Log.d("unlistItem", "Raw response " + unlistingResult);



                    if ("1".equals(unlistingResult)){
                        unlistItemListener.notifyItemUnlisted(itemNumber);
                    }
                    else{
                        unlistItemListener.notifyItemNotUnlisted(itemNumber, unlistingResult);
                    }


                }


                @Override
                public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                    //  super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e("unlistItem", "Failed: " + statusCode);
                    Log.e("unlistItem", "Error : " + throwable);


                    unlistItemListener.notifyItenNotUnlistedError(itemNumber, statusCode);

                    if (statusCode == 404)

                    {
                        Log.e("unlistItem", "HTTP Error Requested resource not found");
                    } else if (statusCode == 500)

                    {
                        Log.e("unlistItem", "HTTP Error Something went wrong at server end");
                    } else

                    {
                        Log.e("unlistItem", "HTTP Error Unexpected Error occcured! " +
                                "[Most common Error: Device might not be connected to Internet]");
                    }
                }


            });
        }

    public void findNewItemsAndNotify(final NewItemsListener newItemsListener) {
        Calendar inCalendar = Calendar.getInstance();
        inCalendar.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        inCalendar.set(Calendar.HOUR_OF_DAY, 0);
        inCalendar.set(Calendar.MINUTE, 0);
        inCalendar.set(Calendar.SECOND, 0);
        inCalendar.set(Calendar.MILLISECOND, 0);
        Date today = Calendar.getInstance().getTime();

        long dateLong = today.getTime();
        String dateString = Long.toString(dateLong);

        final Gson gson = new GsonBuilder().create();
        final String dateParam = gson.toJson(dateString);

        final SyncHttpClient client = new SyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("date", dateParam);
        String currentItems = getLocalItemNumbers();




          if (null != currentItems && !currentItems.isEmpty()){

        params.put("list", currentItems);

              Log.d("fnin", "Current items "+currentItems);


        //final ArrayList<Item> remoteItems = new ArrayList();

        Log.i("Connecting to", diffRemoteItemsEndPointString);
        client.post(diffRemoteItemsEndPointString, params, new TextHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {


                Log.d("fnin","diffRemoteItemsEndPointString -> "+responseString);

                responseString = responseString.trim();


                if (null != responseString) {
                    newItemsListener.notifyNewItems(responseString);
                }
                else{
                    newItemsListener.notifyNoNewItems();
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                Log.d("fnin","diffRemoteItemsEndPointString -> fail");
                newItemsListener.notifyNewItemError(statusCode);

            }


        });

    }else{
              Log.d("fnin", "No items in diff !" );
              newItemsListener.notifyNoNewItems();
       //no items in the database
    }

    }


    public Category getCategory(final int category_number) {

        Category category = null;

            String selectQuery = "SELECT  * FROM category WHERE category_number = "+category_number;
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {

                category = new Category();
                category.setCategoryNumber(cursor.getInt(0));
                category.setParentCategoryNumber(cursor.getInt(1));
                category.setCategoryName(cursor.getString(2));

            }
        //    database.close();



        return category;


    }
}
