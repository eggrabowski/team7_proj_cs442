package edu.iit.cs442.team7.iitbazaar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.iit.cs442.team7.iitbazaar.common.IntentKeys;
import edu.iit.cs442.team7.iitbazaar.database.DBController;
import edu.iit.cs442.team7.iitbazaar.database.DBUpdaterService;
import edu.iit.cs442.team7.iitbazaar.database.RemoteUpdateListener;
import edu.iit.cs442.team7.iitbazaar.fragments.AboutDialogBoxFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.BuyFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.BuySellButtonFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.ItemDescriptionButtonFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.ItemDescriptionFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.ItemEntryFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.NotificationsDialogBoxFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.RecyclerViewCategoryItemListFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.RecyclerViewCategoryListFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.RecyclerViewItemListFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.RecyclerViewSellingListFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.RecyclerViewWatchlistFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.SellFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.SellItemInitEntryFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.SellNavKeys;
import edu.iit.cs442.team7.iitbazaar.fragments.UpdaterDialogBoxFragment;
import edu.iit.cs442.team7.iitbazaar.user.User;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */

public class BazaarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ItemRetrievalListener,
        SellItemUpdateListener,
        RemoteUpdateListener,
        WatchlistUpdateListener,
        FollowStatusListener,
        CategoryUpdateListener
{

    private User user;

    private String mCurrentPhotoPath;

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int SELECT_PICTURE = 2;


    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";


    private ConcurrentMap<String,RadioButton> radioButtons;

    private ItemNumberListener currentItemNumberListener = null;

    private CursorAdapter categoryAdapter = null;
    private CursorAdapter adapterListener = null;
    private CursorAdapter cursorAdapterWishlist = null;
    private CursorAdapter adapterListenerSell = null;

    private Cursor itemCursor;

    private ItemDescriptionButtonFragment itemDescriptionButtons = null;
    private BuySellButtonFragment buySellButtonFragment = null;

    private int barCodeReturn = -1;

    private boolean clearedButtonState = false;

    private BroadcastReceiver receiver;
    private RadioGroup mainMenuRadioGroup;

    private boolean rebalancingButtonMode = false;
    private Intent mServiceIntent;





    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //nothing to see heres


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bazaar);

        Intent intent = getIntent();
        boolean bypass = intent.getBooleanExtra("BYPASS", false);
        DBController controller = IITBazaar.getDBController();

        //Only for test


        user = IITBazaar.getCurrentUser();

        if (bypass) {

            //See the Login activity or IITBazaar class for dbController
            /* Update DB version comment/uncomment if needed
            userController.onUpgrade(userController.getWritableDatabase(),
                    userController.getWritableDatabase().getVersion(),
                    userController.getWritableDatabase().getVersion()+1);
                                                */

            /* Debug Lines */
            ArrayList<User> users = controller.getAllUsers();
            Log.i("BYPASS","Users in database");
            for (User u : users) {
                Log.i("USER","User email: " + u.getEmail());
            }
            /* End of debug lines*/
        }

        //Sets up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FrameLayout frameLayout = (FrameLayout)toolbar.findViewById(R.id.toolbar_frame);


        final String appName = getResources().getString(R.string.app_name);

        final String space = getResources().getString(R.string.space);

        final SearchView searchView = (SearchView)frameLayout.findViewById(R.id.action_search);

        final TextView titleTextView = (TextView)frameLayout.findViewById(R.id.toolbar_title);


        final BazaarActivity baz = this;

        /*Swathi Shenoy: To clear title on click of search icon*/
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTextView.setText(space);
                menuSelected(MenuKeys.BUY);
            }
        });



        titleTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                titleTextView.setText(space);
                searchView.setIconified(false);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleTextView.setText(appName);
                return false;
            }
        });







        final ImageButton refreshButton = (ImageButton)frameLayout.findViewById(R.id.btn_refresh);





        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("BroadcastReceiver", "notifyDataChange");
                notifyDataChange();

            }
        };

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Refreshing",Toast.LENGTH_SHORT).show();

                DBController controller = IITBazaar.getDBController();
                controller.syncRemoteItemsAsynchronously(baz);
                controller.getRemoteCategories(null);

            }
        });
        /*Search implementation*/
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                titleTextView.setText(space);
                DBController controller = IITBazaar.getDBController();
                Cursor mCursor = controller.getSearchItems(newText);
                mCursor.moveToFirst();

                adapterListener.notifyCursorChanged(mCursor);

                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here you can get the value "query" which is entered in the search box.
                DBController controller = IITBazaar.getDBController();
                Cursor mCursor = controller.getSearchItems(query);
                mCursor.moveToFirst();

                adapterListener.notifyCursorChanged(mCursor);



                View view = baz.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                else{
                    Log.d("search","onQueryTextSubmit was null");
                }





                if(mCursor != null) {
                    return true;
                }
                else {
                    return false;
                }
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        setSupportActionBar(toolbar);

        //Sets up the drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);







        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_bazaar);


        TextView userName = (TextView) headerView.findViewById(R.id.navpane_name);
        ImageView userImage = (ImageView) headerView.findViewById(R.id.navpane_user_image);

        TextView userMajor = (TextView) headerView.findViewById(R.id.navpane_major);
        TextView userEmail = (TextView) headerView.findViewById(R.id.navpane_email);

        userName.setText(user.getFirstName() + " " + user.getLastName());
        userImage.setImageBitmap(user.getPictureThumbnailBitmap());

        userMajor.setText(user.getMajor_department());
        userEmail.setText(user.getEmail());




        //probably want a different listener than this
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewItemListFragment fragListFragment = new RecyclerViewItemListFragment();

            BuyFragment buyFragment = new BuyFragment();

            transaction.replace(R.id.buy_sell_subfragment, buyFragment);
            transaction.addToBackStack("buy_sell_subfragment");
            transaction.replace(R.id.buy_list_fragment, fragListFragment);
            transaction.addToBackStack("buy_list_fragment");
            transaction.commit();


            //ending button registration
            //categories button registration
        }

        mServiceIntent = new Intent(this, DBUpdaterService.class);
        this.startService(mServiceIntent);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != receiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(IntentKeys.REFRESH_ACTION.getString());
            this.registerReceiver(receiver, filter);
            Log.d("onResume","Registered receiver");
        }
        else{
            Log.d("onResume","Receiver is null");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (null != receiver) {
            this.unregisterReceiver(receiver);
            Log.d("onPause", "unregistered receiver");
        }
        else{
            Log.d("onPause", "Receiver is null");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

//        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.options_menu, menu);
        //  inflater.inflate(R.menu.bazaar, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


            // Handle navigation view item clicks here.
            int id = item.getItemId();

            switch (id) {

                case R.id.nav_home:

                    menuSelected(MenuKeys.HOME);
                    break;

                case R.id.nav_following:
                    menuSelected(MenuKeys.WATCH_LIST);
                    break;

                case R.id.nav_logout: {
                    IITBazaar.signOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;

                case R.id.nav_disconnect: {
                    IITBazaar.signOutAndDisconnect();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

                }
                break;


                case R.id.nav_scan_barcode:
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.initiateScan();

                    break;


                case R.id.nav_settings_refresh:

                {


                    final FragmentManager fm = getSupportFragmentManager();


                    UpdaterDialogBoxFragment dialogFragment;
                    if (null == (dialogFragment = (UpdaterDialogBoxFragment) fm.findFragmentByTag("RefreshSettings"))) {
                        dialogFragment = new UpdaterDialogBoxFragment();

                    }

                    dialogFragment.show(fm, "RefreshSettings");

                }

                break;


                case R.id.nav_settings_notifications:


                {


                    final FragmentManager fm = getSupportFragmentManager();


                    NotificationsDialogBoxFragment dialogFragment;
                    if (null == (dialogFragment = (NotificationsDialogBoxFragment) fm.findFragmentByTag("NotificationSettings"))) {
                        dialogFragment = new NotificationsDialogBoxFragment();

                    }

                    dialogFragment.show(fm, "NotificationSettings");

                }


                break;


                case R.id.nav_about:


                {


                    final FragmentManager fm = getSupportFragmentManager();


                    AboutDialogBoxFragment dialogFragment;
                    if (null == (dialogFragment = (AboutDialogBoxFragment) fm.findFragmentByTag("About"))) {
                        dialogFragment = new AboutDialogBoxFragment();

                    }

                    dialogFragment.show(fm, "About");

                }


                break;



            }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void registerRadioButtons (BuySellButtonFragment buySellButtonFragment) {
        this.buySellButtonFragment = buySellButtonFragment;
    }


    private BuyFragment launchBuyFragment(){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        BuyFragment buyFragment = new BuyFragment();

        transaction.replace(R.id.buy_sell_subfragment, buyFragment);
        transaction.addToBackStack("buyFragment");
        transaction.commit();

        return buyFragment;

    }




    public void menuSelected(MenuKeys key) {




        if (!rebalancingButtonMode) {

            Log.e("menuSelected", "menuSelected normally");

            switch (key) {

                case HOME:


                case BUY:

                //    Toast.makeText(getBaseContext(), "choice: BUY", Toast.LENGTH_SHORT).show();

                    clearedButtonState = false;


                    BuyFragment buyFragment = launchBuyFragment();


                case BUY_ENDING: {
                  //  Toast.makeText(getBaseContext(), "choice: ending",Toast.LENGTH_SHORT).show();

                    clearedButtonState = false;

                    verifyCheck(MenuKeys.BUY_ENDING);


                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    RecyclerViewItemListFragment buyEndingListFragment = new RecyclerViewItemListFragment();

                    //pass the db list to the fragment somehow

                    transaction.replace(R.id.buy_list_fragment, buyEndingListFragment);
                    transaction.addToBackStack("buyEndingListFragment");
                    transaction.commit();


                }
                break;


                case SELL:
                  //  Toast.makeText(getBaseContext(), "choice: SELLING",   Toast.LENGTH_SHORT).show();

                    clearedButtonState = false;


                {

                    SellFragment sellFragment = launchSellFragment();

                }


                case SELL_ITEM:

                {


                    clearedButtonState = false;
                    verifyCheck(MenuKeys.SELL_ITEM);


                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    SellItemInitEntryFragment sellItemInitEntryFragment = new SellItemInitEntryFragment();


                    //pass the db list to the fragment somehow
                    transaction.replace(R.id.sell_content_fragment, sellItemInitEntryFragment);
                    transaction.addToBackStack("sellItemInitEntryFragment");
                    transaction.commit();


                }

                break;


                case SELLING:

                {

                    clearedButtonState = false;

                    verifyCheck(MenuKeys.SELLING);

                    Log.d("Selling", "Launched!");

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    RecyclerViewSellingListFragment sellListFragment = new RecyclerViewSellingListFragment();

                    //pass the db list to the fragment somehow

                    transaction.replace(R.id.sell_content_fragment, sellListFragment);
                    transaction.addToBackStack("sellListFragment");
                    transaction.commit();


                }

                break;

                case BUY_CATEGORIES:
                //    Toast.makeText(getBaseContext(), "choice: cate", Toast.LENGTH_SHORT).show();

                {

                    clearedButtonState = false;
                    verifyCheck(MenuKeys.BUY_CATEGORIES);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    RecyclerViewCategoryListFragment categoryListFragment = new RecyclerViewCategoryListFragment();

                    //pass the db list to the fragment somehow

                    transaction.replace(R.id.buy_list_fragment, categoryListFragment);
                    transaction.addToBackStack("categoryListFragment");
                    transaction.commit();


                }

                break;


                case WATCH_LIST:
                  //  Toast.makeText(getBaseContext(), "choice: watchlist",  Toast.LENGTH_SHORT).show();

                {


                    launchBuyFragment();

                    final BazaarActivity baz = this;
                    IITBazaar.getDBController().syncCurrentUserWatchList(baz, baz);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    RecyclerViewWatchlistFragment watchListFragment = new RecyclerViewWatchlistFragment();

                    transaction.replace(R.id.buy_list_fragment, watchListFragment);
                    transaction.addToBackStack("watchListFragment");
                    transaction.commit();

                    clearRadioButtons();
                }

                break;


                case SEARCH:
                    //   Toast.makeText(getBaseContext(), "choice: search",
                    //         Toast.LENGTH_SHORT).show();

                {

                    clearRadioButtons();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    RecyclerViewCategoryListFragment fragcaTListFragment = new RecyclerViewCategoryListFragment();

                    //pass the db list to the fragment somehow


                    transaction.replace(R.id.buy_list_fragment, fragcaTListFragment);
                    transaction.addToBackStack("buy_list_fragment");
                    transaction.commit();

                }

                break;


            }
        } else {

            Log.e("menuSelected", "bypassing");

            //    rebalancingButtonMode = false;
        }



    }

    private SellFragment launchSellFragment() {



        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SellFragment sellFragment = new SellFragment();


        transaction.replace(R.id.buy_sell_subfragment, sellFragment);
        transaction.addToBackStack("sellFragment");
        transaction.commit();

        return sellFragment;

    }


    public void getNextSellFragment(SellNavKeys key) {


        switch (key) {


            case SELL_ITEM_DESCRIPTION_ENTRY:


            {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                ItemEntryFragment itemEntryFragment = new ItemEntryFragment();
                transaction.replace(R.id.sell_content_fragment, itemEntryFragment);
                transaction.addToBackStack("sell_content_fragment");
                transaction.commit();

            }


            break;


        }


    }


    public void itemSelected(final int itemNumber, final MenuKeys key) {


        //check if in the right activity...

        if (IITBazaar.isConnected()) {


            DBController dbController = IITBazaar.getDBController();
            dbController.syncCurrentUserWatchList(this, this);
            dbController.getItem(itemNumber, this, key);
        }
        else {
            Toast.makeText(this, "Please connect to the internet and try again.", Toast.LENGTH_SHORT).show();
            Log.e("LOGIN", "Unable to access google apps second time, peopleApi failure");
        }

    }

    public void displayItemDescription(Item item){


        IITBazaar.setCurrentItem(item);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        ItemDescriptionFragment itemDescriptionFragment = new ItemDescriptionFragment();

        //Pass an intent here to the fragment

        transaction.replace(R.id.buy_list_fragment, itemDescriptionFragment);
        transaction.addToBackStack("buy_list_fragment");
        transaction.commit();
    }



    public void displayItemDescriptionBySell(){




        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        ItemDescriptionFragment itemDescriptionFragment = new ItemDescriptionFragment();

        //Pass an intent here to the fragment

        transaction.replace(R.id.sell_content_fragment, itemDescriptionFragment);
        transaction.addToBackStack("sell_content_fragment");
        transaction.commit();
    }

    public void displayItemDescriptionBySell(Item item){

        IITBazaar.setCurrentItem(item);


        displayItemDescriptionBySell();
    }

    public void showImageError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter an image.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    public void showNonValidPriceError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter a valid price.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    public void showEnterDescriptionError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter an item description.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    public void showNonValidDateError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter a valid date.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    public void listItemRemotely(Item newItem) {


        if (IITBazaar.isConnected()){

            if (null != currentItemNumberListener) {

                DBController controller = IITBazaar.getDBController();
                controller.insertItemRemotely(newItem, currentItemNumberListener);
            //    controller.insertIntoRemoteSellTable(newItem, currentItemNumberListener);
                // Log.d("Item Listing", "Item number retrieved = " + newItem.getItemNumber());

            } else {

            }

        }
        else{
            Toast.makeText(IITBazaar.getAppContext(), "Not connected to internet. Cannot list item. Please try again.",
                    Toast.LENGTH_SHORT).show();


        }

       // controller.syncItemSQLiteMySQLDB();



    }



    public void listItemLocally(Item newItem) {


        DBController controller = IITBazaar.getDBController();
            controller.insertItemLocally(newItem);
            controller.insertSellItemLocally(newItem);


    }


    @Override
    public void notifyItemFound(final Item item, final MenuKeys key) {

        if (MenuKeys.BUY == key) {

            displayItemDescription(item);

            return;
        }

        if (MenuKeys.SELL == key) {

            displayItemDescriptionBySell(item);

            return;
        }





    }

    @Override
    public void notifyItemNotFound(final int item) {

        Toast.makeText(IITBazaar.getAppContext(), "Could not find Item #" + item,
                Toast.LENGTH_SHORT).show();

        menuSelected(MenuKeys.HOME);



    }
    @Override
    public void notifySellItemUpdated(){
        if (null!=adapterListenerSell){
            Log.d("notifySellDataChange","Sell Data adapter listener notified");

            //query local here
            Cursor mCursor = IITBazaar.getDBController().getSellingItems();
            mCursor.moveToFirst();

            //call up sell list fragment


            adapterListenerSell.notifyCursorChanged(mCursor);
        //    Toast.makeText(getBaseContext(), "Sell:Refresh Adapter Notified", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.e("Sell:notifyDataChange", "Data adapter listener is invalid");
        }

    }

    @Override
    public void notifyItemRetrievalError(final int statusCode) {



        Log.e("ItemRetrievalError", "Unable to retrieve item, status code=" + statusCode);

    }




    private PhotoListener pl;

    public void getPictureFromCamera() {




        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        File f = null;

        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }


        startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO_B);

    }

    public void registerPhotoListener(PhotoListener pl){
        this.pl =  pl;
    }

    public PhotoListener getPhotoListener(){
            return pl;
    }


    public void getPictureFromGallery() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);


    }








    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private String getAlbumName() {
        return "IITBazaar";
        // return getString(R.string.album_name);
    }


    public File getAlbumStorageDir(String albumName) {
        // TODO Auto-generated method stub
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                albumName
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    //Log.d(this.getClass().getSimpleName(),"DONE with photo !");
                    if (null != pl){
                        Log.d(this.getClass().getSimpleName(),"camera->"+mCurrentPhotoPath);
                        if (null != pl) {
                        pl.loadPhotoFromPath(mCurrentPhotoPath);
                        }
                    }
                }
                break;
            }
            case SELECT_PICTURE: {
                if (resultCode == RESULT_OK) {
                    //Log.d(this.getClass().getSimpleName(),"DONE with photo !");
                    if (null != pl){

                        Uri selectedImageUri = data.getData();


                                Log.d(this.getClass().getSimpleName(), "gallery->" + "got Something");

                       if (null != pl) {
                           pl.loadPhotoFromURI(selectedImageUri);
                       }

                    }
                }
                break;
            }
        }

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

           processScan(scanResult);

    }


    String LINKPREFIX = "iitbazaar";
    String DOMAINPREFIX = "item";
    private void processScan(IntentResult scanResult){

        if (scanResult != null) {

            String scanData = scanResult.getContents();
            String[] linky = scanData.split(LINKPREFIX);
            if (linky.length == 2) {
                String[] domain = linky[1].split(DOMAINPREFIX + ".");

                if (domain.length == 2) {
                    try {
                        int item = Integer.parseInt(domain[1]);
                        Log.d("scannedItem", "Item discovered = " + item);
                        //set as home
                         //   menuSelected(MenuKeys.HOME);



                        itemSelected(item, MenuKeys.BUY);
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                        Log.e("scannedItem", "error parsing scanned number -["+scanData+"]->["+domain[1]+"]");
                    }


                } else {
                    Log.e("scannedItem", "domain improper length -["+scanData+"]->["+ Arrays.toString(domain)+"]");
                }


            } else {
                Log.e("scannedItem", "link improper length -["+scanData+"]->["+ Arrays.toString(linky)+"]");
            }

            //      iitbazaar://item.6

        }
        else{
            Log.d("scannedItem", "not a Qr Intent");
        }


    }


    public void registerNullButtonListener(String TAG, RadioButton rb) {


        if (clearedButtonState){

            rb.toggle();

        }

        ConcurrentMap<String,RadioButton> rbs = getOrCreateNullRadioButtons();

        Log.d("clearRadio","Registering "+TAG);
        rbs.put(TAG, rb);
        Log.d("clearRadio", "Registered " + TAG);
    }


    public void clearRadioButtons(){



       Log.d("clearRadio ","clearing");

        ConcurrentMap<String,RadioButton> rbs = getOrCreateNullRadioButtons();


        Set<Map.Entry<String,RadioButton>> entrySet = rbs.entrySet();
        Iterator<Map.Entry<String,RadioButton>>  enIter = entrySet.iterator();


        if(!enIter.hasNext()){
            Log.e("clearRadio", " is empty! ");
            return;
        }

        Log.e("clearRadio", " preLoop ");
        while (enIter.hasNext()) {

            Map.Entry<String, RadioButton> rbuttonEntry = enIter.next();

            RadioButton rb = rbuttonEntry.getValue();
            String rbTag = rbuttonEntry.getKey();
            if (null != rb) {

                rb.toggle();
                Log.d("clearRadio", "selecting null for "+rbTag);

            } else {
                Log.e("clearRadio", rbTag + " is null ");
            }

        }
        clearedButtonState = true;

    }

    public void registerItemNumberListener(ItemNumberListener inl){
        this.currentItemNumberListener = inl;
    }

    public void notifyItemNumberListener(int itemNumber) {

        if (null != currentItemNumberListener) {

            currentItemNumberListener.notifyItemNumber(itemNumber);
        }

    }

    private ConcurrentMap<String,RadioButton> getOrCreateNullRadioButtons(){

        if (null == radioButtons){
            radioButtons = new ConcurrentHashMap<String,RadioButton>();
        }

        return radioButtons;

    }


    @Override
    public void notifyDataChange() {

        if (null!=adapterListener){
            Log.d("notifyDataChange","Data adapter listener notified");

            //query local here
            Cursor mCursor = IITBazaar.getDBController().getCurrentItems();
            mCursor.moveToFirst();

            adapterListener.notifyCursorChanged(mCursor);
          //  Toast.makeText(getBaseContext(), "Refresh Adapter Notified",Toast.LENGTH_SHORT).show();
             Toast.makeText(getBaseContext(), "Refreshed",Toast.LENGTH_SHORT).show();
        }
        else{
            Log.e("notifyDataChange", "Data adapter listener is invalid");
        }

    }

    @Override
    public void registerDataChangeListeners(CursorAdapter adapter) {
        Log.d("notifyDataChange","Data adapter listener registered");
        this.adapterListener = adapter;
    }

    @Override
    public void registerSellDataChangeListeners(CursorAdapter adapter){
        Log.d("SellnotifyDataChange","Sell Data adapter listener registered");
        this.adapterListenerSell = adapter;
    }

    @Override
    public void notifyWatchlistUpdated() {
        if (null!=cursorAdapterWishlist){
            Log.d("notifyWatchlistUpdated","Wishlist adapter listener notified");

            //query local here
            Cursor mCursor = IITBazaar.getDBController().getAllItemsInWatchList();
            mCursor.moveToFirst();

            cursorAdapterWishlist.notifyCursorChanged(mCursor);
         //   Toast.makeText(getBaseContext(), "Refresh Wishlist Adapter Notified", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.e("notifyWatchlistUpdated", "Adapter listener is invalid");
        }
    }

    @Override
    public void registerWatchlistChangeListeners(CursorAdapter cursorAdapter) {
        this.cursorAdapterWishlist = cursorAdapter;
    }
    @Override
    public void notifyFollowStatusUpdated() {
        if (null!=itemDescriptionButtons) {
            itemDescriptionButtons.buildFollowButton();
        }
        else{
            Log.e("FollowStatusUpdated", "Listener is invalid");
        }
    }




    public void registerFollowStatusListeners(ItemDescriptionButtonFragment itemDescriptionButtons) {
        this.itemDescriptionButtons = itemDescriptionButtons;
    }





    protected void onPostResume() {
        super.onPostResume();
       Log.d("onPostResume","Post resume");

        if (-1 == barCodeReturn){

        }

    }


    public void registerMainButtonGroup(RadioGroup rg){

        this.mainMenuRadioGroup = rg;

    }

    private void verifyCheck(MenuKeys mkey){




if (null != mainMenuRadioGroup) {
    int checkedId = mainMenuRadioGroup.getCheckedRadioButtonId();


    switch (mkey) {



        case BUY_ENDING:

            if (R.id.buy_button != checkedId) {
                rebalancingButtonMode = true;

                mainMenuRadioGroup.check(R.id.buy_button);

                rebalancingButtonMode = false;
            }



            return;



        case SELL_ITEM:

            if (R.id.sell_button != checkedId) {

                rebalancingButtonMode = true;

                mainMenuRadioGroup.check(R.id.sell_button);


                rebalancingButtonMode = false;
            }
            return;


        case SELLING:

            if (R.id.sell_button != checkedId) {

                rebalancingButtonMode = true;

                mainMenuRadioGroup.check(R.id.sell_button);


                rebalancingButtonMode = false;
            }


            return;

        case BUY_CATEGORIES:


            if (R.id.buy_button != checkedId) {

                rebalancingButtonMode = true;

                mainMenuRadioGroup.check(R.id.buy_button);


                rebalancingButtonMode = false;
            }

            return;


    }


}





    }


    public void reloadRefreshInterval() {
        this.stopService(mServiceIntent);
        this.startService(mServiceIntent);
    }

    @Override
    public void notifyCategoryListUpdated() {

        if (null!=categoryAdapter){
            Log.d("notifyCatListUpdated","Data adapter listener notified");

            //query local here
            Cursor mCursor = IITBazaar.getDBController().getCategories(-1);
            mCursor.moveToFirst();

            categoryAdapter.notifyCursorChanged(mCursor);
           // Toast.makeText(getBaseContext(), "Refresh Adapter Notified",Toast.LENGTH_SHORT).show();
        }
        else{
            Log.e("notifyCatListUpdated", "categoryAdapter is invalid");
        }

    }

    @Override
    public void registerCategoryListChangeListeners(CursorAdapter categoryAdapter) {
        this.categoryAdapter = categoryAdapter;
    }

    public void getCategoryOrItemList(int categoryNumber) {

        DBController dbController = IITBazaar.getDBController();
        Cursor cursor = dbController.getCategories(categoryNumber);

        //if curosr is not empty
        if(cursor.moveToFirst()){

            Log.d("getCategoryOrItemList","Category -"+categoryNumber+"- is a branch");
            //new subcategory
            categoryAdapter.notifyCursorChanged(cursor);

        }
        else{

            Log.d("getCategoryOrItemList", "Category -" + categoryNumber + "- is a leaf");

            this.itemCursor = dbController.getItemsByCategory(categoryNumber);



            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewCategoryItemListFragment categoryItemListFragment = new RecyclerViewCategoryItemListFragment();

            transaction.replace(R.id.buy_list_fragment, categoryItemListFragment);
            transaction.addToBackStack("categoryItemListFragment");
            transaction.commit();


            Log.d("getCategoryOrItemList", "Transaction complete");
        //    cursor = dbController.getSearchItems()

            //fragment replace of itemList

        }



    }

    public Cursor getItemCursor(){

        return itemCursor;

    }


    public void showEnterCategoryError() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please select a category.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create().show();

    }
}
