package edu.iit.cs442.team7.iitbazaar.database;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import android.os.SystemClock;
import android.preference.PreferenceManager;

import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import edu.iit.cs442.team7.iitbazaar.BazaarActivity;
import edu.iit.cs442.team7.iitbazaar.CursorAdapter;
import edu.iit.cs442.team7.iitbazaar.IITBazaar;
import edu.iit.cs442.team7.iitbazaar.Item;
import edu.iit.cs442.team7.iitbazaar.NewItemsListener;
import edu.iit.cs442.team7.iitbazaar.R;
import edu.iit.cs442.team7.iitbazaar.common.IntentKeys;


public class DBUpdaterService extends IntentService implements NewItemsListener, RemoteUpdateListener {

    public static String TAG = "BAZAAR_UPDATE_SERVICE";



    private NotificationCompat.Builder b;
    public static final int NOTIFICATION_ID = 1;

    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_NOTIF = "PREF_NOTIFICATIONS_ON";
    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";

    private boolean notifOn = false;

    private String newItems = null;


    public DBUpdaterService() {
        super("BazaarUpdateService");
    }

    public DBUpdaterService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("onHandleIntent","sync service called");

        //this is called during creation/registration

        //if notifications, send request

        //else just updatedb



        Context context = IITBazaar.getAppContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int updateFreq = Integer.parseInt(prefs.getString(PREF_UPDATE_FREQ, "60"));



        boolean autoUpdateChecked = prefs.getBoolean(PREF_AUTO_UPDATE, false);

        notifOn = prefs.getBoolean(PREF_NOTIF, false);


        if (notifOn) {
            Log.d("onHandleIntent","Notifications = On");
            DBController dbc = IITBazaar.getDBController();
            dbc.findNewItemsAndNotify(this);
        }
        else{
            Log.d("onHandleIntent","Notifications = Off");
            DBController dbController = IITBazaar.getDBController();
            dbController.syncRemoteItemsSynchronously(this);
        }


        //schedules next alarm
        scheduleNextUpdate(updateFreq,autoUpdateChecked);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();



     //   Icon icon = Icon.createWithResource(this, R.drawable.notification_icon_large);

/*        earthquakeNotificationBuilder = new Notification.Builder(this);
        earthquakeNotificationBuilder
                .setAutoCancel(true)

                .setTicker("New Items Listed")
                .setSmallIcon(R.drawable.notification_icon_white);
           //     .setLargeIcon(icon);*/

        b = new NotificationCompat.Builder(this);

        b.setSmallIcon(R.drawable.notification_icon_large)
                .setContentTitle("New Items Listed !");
            //    .setContentText("Hello World!");







    }

    private void scheduleNextUpdate(final int updateFreq, final boolean autoUpdateChecked) {
        /*Toast.makeText(IITBazaar.getAppContext(), "-Refresh Request Sent!-",
                Toast.LENGTH_SHORT).show();*/


        Intent intent = new Intent(this, this.getClass());
        alarmIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Log.e("frequence", "(min) " + updateFreq);


        long timeElapsed = SystemClock.elapsedRealtime();
        long nextUpdateTimeMillis = timeElapsed + updateFreq * 60 * 1000;
        Log.e("frequence","totalMillis: " +  (updateFreq * 60 * 1000));
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (autoUpdateChecked) {

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextUpdateTimeMillis, alarmIntent);
        }
        else {
            alarmManager.cancel(alarmIntent);
        }

    }



    @Override
    public void notifyNewItems(String arrayOfItemNumbers) {


        if (null != arrayOfItemNumbers && !arrayOfItemNumbers.isEmpty()) {
            newItems = arrayOfItemNumbers;
        }


        DBController dbController = IITBazaar.getDBController();
        dbController.syncRemoteItemsSynchronously(this);

    }

    @Override
    public void notifyNoNewItems() {

        DBController dbController = IITBazaar.getDBController();
        dbController.syncRemoteItemsSynchronously(this);

    }

    @Override
    public void notifyNewItemError(int statusCode) {

        Log.e("notifyNewItemError", "error received when trying to get new items = " + statusCode);

        DBController dbController = IITBazaar.getDBController();
        dbController.syncRemoteItemsSynchronously(this);

    }

    @Override
    public void notifyDataChange() {


        if (notifOn){

            Log.d("onHandleIntent", "Generating notifications");

            if (null != newItems && !newItems.isEmpty()){

                Log.d("onHandleIntent", "Notifications are not null");

                DBController dbController = IITBazaar.getDBController();

                ArrayList<Item> itemsList = dbController.getItemsNotIn(newItems);

                processNotifications(itemsList);

                newItems = null;


            }
            else{
                Log.d("onHandleIntent","notifications are empty/null");
            }




        }

        Log.d("onHandleIntent","Broadcasting refresh action !");
        //check if notifications enabled
        //check if new items are available
        //run notification action

        //sends broadcast to activity..if its there
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(IntentKeys.REFRESH_ACTION.getString());
        sendBroadcast(broadcastIntent);



    }

    private void processNotifications(ArrayList<Item> itemsList){

        int itemSize = itemsList.size();

        if (0<itemSize) {
            Log.d("processNotifications", "processing notification");

            Intent startActivityIntent = new Intent(this, BazaarActivity.class);
            startActivityIntent.putExtra(IntentKeys.TYPE.getString(), IntentKeys.NOTIFICATION_ACTION.getString());

            startActivityIntent.putExtra(IntentKeys.NOTIFICATION_ACTION_ITEM.getString(), -1);


            PendingIntent launchIntent =
                    PendingIntent.getActivity(this, 0, startActivityIntent, 0);

            b.setContentIntent(launchIntent);


            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            // Sets a title for the Inbox in expanded layout
            inboxStyle.setBigContentTitle("IIT Bazaar New Items:");

// Moves events into the expanded layout
            for (int i = 0; i < itemSize; i++) {
                Log.d("notice","Adding new notification = "+itemsList.get(i).getItemNumber());

                inboxStyle.addLine(itemsList.get(i).getTitle());
            }
// Moves the expanded layout object into the notification object.
            b.setStyle(inboxStyle);

// Issue the notification here.




        /*
    if (quake.getMagnitude() > 6) {
        Uri ringURI =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        earthquakeNotificationBuilder.setSound(ringURI);
    }*/


            int vfactor = 1;
            double vibrateLength = 100 * Math.exp(0.53 * vfactor);
            long[] vibrate = new long[]{100, 100, (long) vibrateLength};
            b.setVibrate(vibrate);

            int color;
/*     if (quake.getMagnitude() < 5.4)
    color = Color.GREEN;
    else if (quake.getMagnitude() < 6)
    color = Color.YELLOW;
    else*/
            color = Color.RED;

            b.setLights(
                    color,
                    (int) vibrateLength,
                    (int) vibrateLength);


            NotificationManager notificationManager
                    = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(NOTIFICATION_ID,
                    b.build());

        }
        else{

            Log.d("processNotifications","no notifications to process");
        }


    }


    private boolean isTommorrow(Date d){

        //d.getTime()+86400000


              return false;
    }

    @Override
    public void registerDataChangeListeners(CursorAdapter adapter) {
       throw new UnsupportedOperationException();
    }
}