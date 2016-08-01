package edu.iit.cs442.team7.iitbazaar;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import edu.iit.cs442.team7.iitbazaar.database.DBController;
import edu.iit.cs442.team7.iitbazaar.user.User;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class IITBazaar extends Application {

    private static Context context;

    private static GoogleApiClient googleApiClient;

    private static User currentUser;

    private static DBController dbController;
    private static Item currentItem;

    private static ConnectivityManager cm;

    final public static int PICTURE = 500;
    final public static int PICTURE_THUMB = 50;


    public static void setCurrentItem(Item inputItem) {
        currentItem = inputItem;
    }


    public static Item getCurrentItem() {
       return currentItem;
    }

    public void onCreate(){
        super.onCreate();
        IITBazaar.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return IITBazaar.context;
    }


    public void registerGoogleApi(GoogleApiClient googleApiClient){
        this.googleApiClient = googleApiClient;
    }


    public static void signOut(){

        if (null!= googleApiClient){

            if (googleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
            }

        }

    }

    public static void signOutAndDisconnect(){

        if (null!= googleApiClient){

        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
            googleApiClient.disconnect();
        }

        }
    }




    public static boolean isConnected(){
        boolean isConnectionAvail = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(netInfo != null)
                return netInfo.isConnected();
            else
                return isConnectionAvail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnectionAvail;
    }





    public static void setCurrentUser(final User user){
            currentUser = user;
    }


    public static User getCurrentUser(){
        return currentUser;
    }




    public static DBController getDBController(){

        if (null == dbController){
            dbController = new DBController(context);
        }

            return dbController;
    }
}