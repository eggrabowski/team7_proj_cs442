package edu.iit.cs442.team7.iitbazaar;



import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.net.Uri;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.iit.cs442.team7.iitbazaar.common.PhotoLibrary;
import edu.iit.cs442.team7.iitbazaar.common.RankResultCode;
import edu.iit.cs442.team7.iitbazaar.database.DBController;
import edu.iit.cs442.team7.iitbazaar.fragments.BuyFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.BypassButtonFragment;
import edu.iit.cs442.team7.iitbazaar.fragments.BypassButtonFragment_ProdFiller;
import edu.iit.cs442.team7.iitbazaar.fragments.RecyclerViewItemListFragment;
import edu.iit.cs442.team7.iitbazaar.rankrequester.RankRequesterService;
import edu.iit.cs442.team7.iitbazaar.user.User;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class LoginActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        UserRetrievalListener{

    private static final String TAG = "MainActivity";

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 1;

    private final String DEFAULT_PICTURE_CHECKSUM = "db86f87ee6a19306d8e162e23a258bb2";

    /* RequestCode for resolutions to get GET_ACCOUNTS permission on M */
    private static final int RC_PERM_GET_ACCOUNTS = 2;

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    private TextView mStatus;

    // [START resolution_variables]
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private boolean isBypass = false;



    // [END resolution_variables]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Restore from saved instance state
        // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
        // [END restore_saved_instance_state]

        // Set up button click listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
        if (savedInstanceState == null) {

        if (!BuildConfig.isProduction) {






                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                BypassButtonFragment bypassButtonFragment = new BypassButtonFragment();
                transaction.replace(R.id.bypass_button_fragment_placeholder, bypassButtonFragment);
                transaction.addToBackStack("bypass_button");
                transaction.commit();








        }
        else{



            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


            BypassButtonFragment_ProdFiller bypassButtonFragment = new BypassButtonFragment_ProdFiller();
            transaction.replace(R.id.bypass_button_fragment_placeholder, bypassButtonFragment);
            transaction.addToBackStack("bypass_button");
            transaction.commit();




        }

        }
        // Large sign-in
        ((SignInButton) findViewById(R.id.sign_in_button)).setSize(SignInButton.SIZE_WIDE);

        // Start with sign-in button disabled until sign-in either succeeds or fails
        findViewById(R.id.sign_in_button).setEnabled(false);

        // Set up view instances
        mStatus = (TextView) findViewById(R.id.status);

        // [START create_google_api_client]
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

       IITBazaar bazaarApp = (IITBazaar) this.getApplication();
        bazaarApp.registerGoogleApi(mGoogleApiClient);


        // [END create_google_api_client]
    }

   private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if (currentPerson != null) {
                // Show signed-in user's name
                String name = currentPerson.getDisplayName();
                mStatus.setText(getString(R.string.signed_in_fmt, name));

                // Show users' email address (which requires GET_ACCOUNTS permission)
                if (checkAccountsPermission()) {
                    String currentAccount = Plus.AccountApi.getAccountName(mGoogleApiClient);



                    ((TextView) findViewById(R.id.email)).setText(currentAccount);
                }
            } else {
                // If getCurrentPerson returns null there is generally some error with the
                // configuration of the application (invalid Client ID, Plus API not enabled, etc).
                Log.w(TAG, getString(R.string.error_null_person));
                mStatus.setText(getString(R.string.signed_in_err));
            }

            // Set button visibility
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            // Show signed-out message and clear email field
       //     mStatus.setText(R.string.signed_out);
         //   ((TextView) findViewById(R.id.email)).setText("");

            // Set button visibility
            findViewById(R.id.sign_in_button).setEnabled(true);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    /**
     * Check if we have the GET_ACCOUNTS permission and request it if we do not.
     * @return true if we have the permission, false if we do not.
     */
    private boolean checkAccountsPermission() {
        final String perm = Manifest.permission.GET_ACCOUNTS;

        //int permissionCheck = Context.checkSelfPermission(this, perm);
        int permissionCheck = ContextCompat.checkSelfPermission(this,perm);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // We have the permission
            return true;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
            // Need to show permission rationale, display a snackbar and then request
            // the permission again when the snackbar is dismissed.
            Snackbar.make(findViewById(R.id.main_layout),
                    R.string.contacts_permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Request the permission again.
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{perm},
                                    RC_PERM_GET_ACCOUNTS);
                        }
                    }).show();
            return false;
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{perm},
                    RC_PERM_GET_ACCOUNTS);
            return false;
        }
    }

 /*   private void showSignedInUI() {
        updateUI(true);
    }*/

   private void showSignedOutUI() {
        updateUI(false);
    }

    // [START on_start_on_stop]
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    // [END on_start_on_stop]

    // [START on_save_instance_state]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }
    // [END on_save_instance_state]

    // [START on_activity_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }
    // [END on_activity_result]

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult:" + requestCode);
        if (requestCode == RC_PERM_GET_ACCOUNTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


             //   showSignedInUI();
            } else {
                Log.d(TAG, "GET_ACCOUNTS Permission Denied.");
            }
        }
    }

    private void allFail(){

        //FIXME:comment in/out for testing
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
      }

    }

    // [START on_connected]
    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;


        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient).trim().toLowerCase();
            Log.d("LOGIN ","Login account name is = "+email);

            if (!email.contains("iit.edu")){
                //naughty...not a student
                Log.d("LOGIN ","Not an IIT Student ");

                allFail();

                Toast.makeText(this, "Sorry, this system is for the IIT community.", Toast.LENGTH_SHORT).show();

                return;
            }

            DBController dbController = IITBazaar.getDBController();

            dbController.getUser(email,this);

            return;
            }


        if (IITBazaar.isConnected()){


            Toast.makeText(this, "Unable to access google apps.", Toast.LENGTH_SHORT).show();
            Log.e("LOGIN", "Unable to access google apps, peopleApi failure");



        }
        else{

            Toast.makeText(this, "You must have an internet connection. Please try again.", Toast.LENGTH_SHORT).show();
            Log.e("LOGIN", "No internet on initial account lookup");


        }


        allFail();


        // Show the signed-in UI
      //  showSignedInUI();
    }
    // [END on_connected]

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Log.w(TAG, "onConnectionSuspended:" + i);
    }

    // [START on_connection_failed]
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            showSignedOutUI();
        }
    }
    // [END on_connection_failed]

    private void showErrorDialog(ConnectionResult connectionResult) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, RC_SIGN_IN,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                mShouldResolve = false;
                                showSignedOutUI();
                            }
                        }).show();
            } else {
                Log.w(TAG, "Google Play Services Error:" + connectionResult);
                String errorString = apiAvailability.getErrorString(resultCode);
                Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

                mShouldResolve = false;
                showSignedOutUI();
            }
        }
    }

    // [START on_click]
    @Override
    public void onClick(View v) {

        Log.d("Login-onClick","click!");

        final LoginActivity loginActivity = this;

        if (R.id.sign_in_button == v.getId()) {
            Log.d("Login-onClick","signin button clicked!");
            if (!IITBazaar.isConnected()){
                Toast.makeText(this, "Please connect to the internet and try again.", Toast.LENGTH_SHORT).show();
                Log.e("LOGIN", "Unable to access google apps second time, peopleApi failure");

            }
            else{
                onSignInClicked();
            }


            return;
        }

        if (!BuildConfig.isProduction && R.id.bypass_button == v.getId()) {

            Log.i("DefaultUser", "bypass");
            Log.d("Login-onClick", "bypass button clicked!");

            if (!IITBazaar.isConnected()){
                Toast.makeText(this, "Please connect to the internet and try again.", Toast.LENGTH_SHORT).show();
                Log.e("LOGIN", "Unable to access google apps second time, peopleApi failure");

            }else {
                isBypass = true;
                DBController controller = IITBazaar.getDBController();

                controller.getUser("default@iit.edu", loginActivity);
            }
            return;

        }

    }
    // [END on_click]

    // [START on_sign_in_clicked]
    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        mStatus.setText(R.string.signing_in);
    }

    @Override
    public void notifyUserFound(User user) {





        //convergance

        IITBazaar.setCurrentUser(user);
        Intent intent = new Intent(this, BazaarActivity.class);

        //gotta get rid of this
        if (isBypass) {
            intent.putExtra("BYPASS", true);
        }
        else{
            intent.putExtra("BYPASS", false);
        }
        startActivity(intent);

   //     allFail();


    }

    @Override
    public void notifyUserNotFound(final String email) {


        if (!isBypass ){


            //This should be done in another thread

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //your codes here

            }



            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null ) {

                final User user = new User();

                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                //      String personName = currentPerson.getDisplayName();
                Person.Name personName = currentPerson.getName();
                String firstName = "";
                String middleName = "";
                String lastName = "";


                Log.d("LOGIN ", "email " + email);
                user.setEmail(email);


                if(personName.hasGivenName()){
                    firstName = personName.getGivenName();
                }
                Log.d("LOGIN ","firstName "+firstName);
                user.setFirstName(firstName);

                if(personName.hasMiddleName()){
                    middleName = personName.getMiddleName();
                }
                Log.d("LOGIN ","middleName "+middleName);

                if(personName.hasFamilyName()){
                    lastName = personName.getFamilyName();
                }
                Log.d("LOGIN ","lastName "+lastName);
                user.setLastName(lastName);

                //    String personGooglePlusProfile = currentPerson.getUrl();

                Log.d(this.getClass().getSimpleName(), "User has image ? " + currentPerson.hasImage());


                if (currentPerson.hasImage()){
                    Log.d("LOGIN ","image - has image :)");
                        Log.d("LOGIN ","image - has image :)");
                        try {
                            String personPhoto = currentPerson.getImage().getUrl();
                            URL personPhotoUrl = new URL(personPhoto);
                            InputStream in = personPhotoUrl.openStream();
                            Bitmap userPhoto =  BitmapFactory.decodeStream(in);
                            String path = Environment.getExternalStorageDirectory().toString();
                            OutputStream fOut = null;
                            File file = new File(path, "user_"+firstName+"_"+lastName+".png"); // the File to save to
                            fOut = new FileOutputStream(file);
                            userPhoto.compress(Bitmap.CompressFormat.PNG, 100, fOut); //
                            Uri pPhoto = Uri.fromFile(file);



                    //    URL personPhotoUrl = new URL(personPhoto);
                        //Uri personPhotoUri = Uri.parse(personPhoto);


                        PhotoLibrary pl = new PhotoLibrary(this);

                        Pair<Bitmap,Bitmap> fullThumb = pl.loadPhotoPairFromURI(pPhoto);

                            if (!hasDefaultPicture(fullThumb.second)){




                        user.setPicture(fullThumb.first);
                        user.setPictureThumbnail(fullThumb.second);

                    }
                        else{


                            Toast.makeText(this, "You have no profile image in your google apps account.", Toast.LENGTH_SHORT).show();

                            Log.e("LOGIN ","image - user has no image :( - by hard test");
                            allFail();
                            return;


                        }



                        } catch (Exception e) {
                            Toast.makeText(this, "An issue occurred retrieving your profile images.", Toast.LENGTH_SHORT).show();

                            Log.e("LOGIN ","An issue occurred retrieving your profile images"+e.getLocalizedMessage());
                            allFail();
                            return;

                        }


                /*    } catch (FileNotFoundException e) {


                        if (IITBazaar.isConnected()){

                            Toast.makeText(this, "An issue was encountered creating your account. Try again later.", Toast.LENGTH_SHORT).show();
                            Log.e("LOGIN", e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                        else{


                            Toast.makeText(this, "You must have an internet connection. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("LOGIN", "No internet=" + e.getLocalizedMessage());
                            e.printStackTrace();

                        }

                        allFail();
                        return;
                    } catch (IOException e) {


                        if (IITBazaar.isConnected()){

                            Toast.makeText(this, "An issue was encountered creating your account. Try again later.", Toast.LENGTH_SHORT).show();
                            Log.e("LOGIN", e.getLocalizedMessage());
                            e.printStackTrace();


                        }
                        else{


                            Toast.makeText(this, "You must have an internet connection. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("LOGIN", "No internet="+e.getLocalizedMessage());
                            e.printStackTrace();



                        }
                        allFail();
                        return;

                    } catch (URISyntaxException e) {
                        Toast.makeText(this, "Error occurred retrieving picture URI.", Toast.LENGTH_SHORT).show();
                        Log.e("LOGIN", "URL to URI conversion problem ="+e.getLocalizedMessage());
                        e.printStackTrace();
                        allFail();
                        return;
                    }*/


                }
                else{
                    Toast.makeText(this, "You have no profile image in your google apps account.", Toast.LENGTH_SHORT).show();

                    Log.e("LOGIN ", "image - user has no image :(");
                    allFail();
                    return;
                }



                RankRequesterService rrs = new RankRequesterService();
                Pair<RankResultCode,String> result = rrs.getDepartment(firstName, lastName, email);

                if (result.first != RankResultCode.ERROR && result.first != RankResultCode.UNKNOWN ){

                    Log.d("LOGIN ","rank/level - "+result.second);

                    user.setMajor_department(result.second);

                }
                else{

                    Log.d("LOGIN ", "rank/level - not found :(");

                    Context context  = getApplicationContext();

                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.rankprompt, null);

                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                            context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final Spinner majors = (Spinner) promptsView
                            .findViewById(R.id.majorspinner);


                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Add",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {






                                            Toast.makeText(getApplicationContext(), "Selected item is "+ majors.getSelectedItemId(),
                                                    Toast.LENGTH_SHORT).show();



                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();







                }

                DBController controller = IITBazaar.getDBController();
                controller.insertUserRemotely(user, this);






            }
            else{



                if (IITBazaar.isConnected()){
                    Toast.makeText(this, "Failure on account creation. Try again later.", Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN", "Unable to access google apps second time, peopleApi failure");
                }
                else{

                    Toast.makeText(this, "You must have an internet connection. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN", "No internet accessing google apps second time");


                }


                allFail();

                return;
            }


            return;
        }

        if (isBypass){


            try {
                Log.i("DefaultUser", "Not found");
                Log.i("DefaultUser", "Creating user");
                User duser = new User();
                duser.setEmail("default@iit.edu");
                duser.setFirstName("User");
                duser.setLastName("Default");
                duser.setMajor_department("CS");
                AssetManager assetManager = getAssets();

                  String testUser = "test_user_picture.jpg";
                  String testUserFileName = "test_user_picture_thumbnail.jpg";


                  InputStream testUser_istr = null;

                testUser_istr = assetManager.open(testUser);

                InputStream testUserThumb_istr = assetManager.open(testUserFileName);

                Bitmap testUserBitmap = BitmapFactory.decodeStream(testUser_istr);
                Bitmap testUserThumbBitmap = BitmapFactory.decodeStream(testUserThumb_istr);

                duser.setPicture(testUserBitmap);
                duser.setPictureThumbnail(testUserThumbBitmap);


                DBController controller = IITBazaar.getDBController();
                controller.insertUserRemotely(duser, this);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }




    }

    @Override
    public void notifyUserRetrievalError(int statusCode) {





        if (IITBazaar.isConnected()){
            Toast.makeText(this, "A problem encountered retrieving user = "+ statusCode, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No connection. Please connect to internet and try again.", Toast.LENGTH_SHORT).show();
        }

        allFail();




    }

    @Override
    public void notifyUserInsertion(final String email) {

        Log.d("UserInsertion", "User " + email+" has been inserted");


        DBController controller = IITBazaar.getDBController();

        controller.getUser(email, this);

    }

    @Override
    public void notifyUserInsertionError(int statusCode) {


        if (IITBazaar.isConnected()){
            Toast.makeText(this, "A problem encountered inserting user = "+ statusCode, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No connection. Please connect to internet and try again.", Toast.LENGTH_SHORT).show();
        }

        allFail();


    }



    private int calculateInSampleSize(
            int inHeight, int inWidth, int reqWidth, int reqHeight) {
        // Raw height and width of image

        int inSampleSize = 1;

        if (inHeight > reqHeight || inWidth > reqWidth) {

            final int halfHeight = inHeight / 2;
            final int halfWidth = inWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private android.util.Pair<Integer,Integer> calculateScaledSize(
            int inHeight, int inWidth, int reqEdge) {
        // Raw height and width of image

        int largest_side = Math.max(inHeight,inWidth);
        double normalScaleFactor = ((double)largest_side/(double)reqEdge);

        int scaledW = (int)Math.floor(inWidth/normalScaleFactor);
        int scaledH = (int)Math.floor(inHeight/normalScaleFactor);

        return new android.util.Pair<Integer,Integer>(scaledH,scaledW);

    }


    private boolean hasDefaultPicture(final Bitmap inputBitMap) {



        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        inputBitMap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] testByte = stream.toByteArray();


        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(testByte);
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }

         //   Log.d("CHECKSUM",hexString.toString());
            boolean isDefault = DEFAULT_PICTURE_CHECKSUM.equals(hexString.toString());

            if (isDefault){

                Log.d("hasDefaultPicture"," You have a default picture");

            }


            return isDefault;


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void registerBypassButton(Button bypassButton) {

        bypassButton.setOnClickListener(this);



    }
}
