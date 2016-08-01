package edu.iit.cs442.team7.iitbazaar.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.iit.cs442.team7.iitbazaar.BazaarActivity;
import edu.iit.cs442.team7.iitbazaar.IITBazaar;
import edu.iit.cs442.team7.iitbazaar.Item;
import edu.iit.cs442.team7.iitbazaar.MenuKeys;
import edu.iit.cs442.team7.iitbazaar.R;
import edu.iit.cs442.team7.iitbazaar.common.QRCodeRasterWriter;
import edu.iit.cs442.team7.iitbazaar.database.DBController;
import edu.iit.cs442.team7.iitbazaar.database.UnlistItemListener;
import edu.iit.cs442.team7.iitbazaar.user.User;


/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class ItemDescriptionButtonFragment extends Fragment implements UnlistItemListener {

    private BazaarActivity parentActivity;
    View view;
    boolean followed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (View) inflater.inflate(R.layout.item_description_button_fragment, container, false);


        final User user = IITBazaar.getCurrentUser();
        final Item item = IITBazaar.getCurrentItem();

        Button buyButton = (Button) view.findViewById(R.id.buy_button);

        Button shareButton = (Button) view.findViewById(R.id.share_button);

        final UnlistItemListener unlistItemListener = this;

        if (item.getListingUserEmail().equals(user.getEmail())) {
            buyButton.setText("Unlist");

            Button unlistButton = buyButton;


            unlistButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    DBController dbController = IITBazaar.getDBController();
                    dbController.unlistItem(item.getItemNumber(), unlistItemListener);

                }
            });

        } else {

            buyButton.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View v) {

                    final FragmentManager fm = getFragmentManager();
                    BuyDialogBoxFragment dialogFragment;
                    if (null == (dialogFragment = (BuyDialogBoxFragment) fm.findFragmentByTag("BuyDialogBoxFragment"))) {
                        dialogFragment = new BuyDialogBoxFragment();

                    }

                    dialogFragment.show(fm, "BuyDialogBoxFragment");


                }


            });


            //display dialog fragment between paypal and email.
            //Email will launch intent, paypal will launch browser with special link (use any link)


        }
        followed = IITBazaar.getDBController().isInCurrentUserWatchList(item.getItemNumber());
        buildFollowButton();


        shareButton.setOnClickListener(new Button.OnClickListener() {


            public void onClick(View v) {


                User user = IITBazaar.getCurrentUser();
                Item item = IITBazaar.getCurrentItem();

                String emailCC = user.getEmail();


                //need to "send multiple" to get more than one attachment
                final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setType("text/plain");

                emailIntent.putExtra(android.content.Intent.EXTRA_CC,
                        new String[]{emailCC});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing IIT BAZAAR #" + item.getItemNumber() + " | " + item.getTitle());


                String share =
                        "Sharing IIT BAZAAR\n\n" +
                                "Item #: " + item.getItemNumber() + "\n\n" +
                                "Title:\n" + item.getTitle() + "\n\n" +
                                "Description:\n" + item.getItemDescription() + "\n\n" +
                                "Category:\n" + item.getCategory() + "\n\n" +
                                "Price:\n" + item.getPrice() + "\n\n" +
                                "End Date:\n" + item.getListingEndDate() + "\n\n";

                emailIntent.putExtra(Intent.EXTRA_TEXT, share);

                File outputDir = IITBazaar.getAppContext().getExternalCacheDir(); // context being the Activity pointer


                ArrayList<String> filePaths = new ArrayList<String>();


                File outputFile = null;
                try {
                    outputFile = File.createTempFile("iitbazaar_" + item.getItemNumber(), ".jpg", outputDir);

                    FileOutputStream fos = new FileOutputStream(outputFile.getPath());
                    fos.write(item.getImageByte());
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                filePaths.add(outputFile.getPath());


                try {
                    outputFile = File.createTempFile("iitbazaar_barcode_" + item.getItemNumber(), ".png", outputDir);

                    FileOutputStream fos = new FileOutputStream(outputFile.getPath());


                    QRCode qrCode = item.getQRCode();
                    QRCodeRasterWriter qrw = new QRCodeRasterWriter();


                    BitMatrix qrBitMatrix = qrw.renderResult(qrCode, 1000, 1000, qrw.getQuietZoneSize(null));

                    Bitmap qrBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);


                    // int black = parentActivity.getColor(R.color.iit_red);
                    //int black = 0xCF102D;
                    //   Log.d("IITRed",black+"");
                    //  int white = parentActivity.getColor(R.color.default_background);
                    // Log.d("IITWhite",white+"");

                    //    ?android:colorBackground

                    for (int i = 0; i < 1000; i++) {//width
                        for (int j = 0; j < 1000; j++) {//height
                            //  qrBitmap.setPixel(i, j, qrBitMatrix.get(i, j) ? black : white);
                            qrBitmap.setPixel(i, j, qrBitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    byte[] imageByte = stream.toByteArray();


                    fos.write(imageByte);
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                filePaths.add(outputFile.getPath());


                ArrayList<Uri> uris = new ArrayList<Uri>();
                //convert from paths to Android friendly Parcelable Uri's
                for (String file : filePaths) {
                    File fileIn = new File(file);
                    Uri u = Uri.fromFile(fileIn);
                    uris.add(u);
                }
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.

                startActivity(emailIntent);
                //startActivityForResult(emailIntent);


            }


        });




        return view;

    }

    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            parentActivity = (BazaarActivity) context;
            parentActivity.registerFollowStatusListeners(this);
            Log.d(this.getClass().getSimpleName(), "Activity context set correctly");
        } else {
            Log.e(this.getClass().getSimpleName(), "Attached context of " + this.getClass().getSimpleName() + " is not of activity type.");
        }

    }

    private void updateFollowState() {
        followed = followed ? false : true;
    }

    public void buildFollowButton() {
        final Item item = IITBazaar.getCurrentItem();
        final Button followButton = (Button) view.findViewById(R.id.follow_button);

        if (followed) {
            followButton.setText("Unfollow");
            followButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFollowState();
                    IITBazaar.getDBController().deleteItemFromCurrentUserWatchList(item.getItemNumber(), parentActivity, parentActivity);
                }
            });
        } else {
            followButton.setText("Follow");
            followButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFollowState();
                    IITBazaar.getDBController().addItemToCurrentUserWatchList(item.getItemNumber(), parentActivity, parentActivity);
                }
            });
        }
    }


    @Override
    public void notifyItenNotUnlistedError(int itemNumber, int statusCode) {


        Log.e("unlist","Error ["+statusCode+"] encountered unlisting item number: "+itemNumber);

    }

    @Override
    public void notifyItemNotUnlisted(int itemNumber, String insertionResult) {

        Log.e("unlist","Item ["+itemNumber+"] has not been unlisted, result instead is:"+insertionResult);

    }

    @Override
    public void notifyItemUnlisted(int itemNumber) {


        parentActivity.menuSelected(MenuKeys.BUY);

    }
}


