package edu.iit.cs442.team7.iitbazaar.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import edu.iit.cs442.team7.iitbazaar.BazaarActivity;
import edu.iit.cs442.team7.iitbazaar.IITBazaar;
import edu.iit.cs442.team7.iitbazaar.Item;
import edu.iit.cs442.team7.iitbazaar.R;
import edu.iit.cs442.team7.iitbazaar.common.PayPalLinkContructor;
import edu.iit.cs442.team7.iitbazaar.user.User;


/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */




public class BuyDialogBoxFragment extends DialogFragment {



    BazaarActivity parentActivity;

    public BuyDialogBoxFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_dialog, container);



        RelativeLayout buyByEmail = (RelativeLayout) view.findViewById(R.id.buy_dialog_by_email);

        buyByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(this.getClass().getSimpleName(), "buyByEmail");


                getDialog().dismiss();

                User user = IITBazaar.getCurrentUser();
                Item item = IITBazaar.getCurrentItem();


                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Purchase Inquiry -- IIT BAZAAR #" + item.getItemNumber() + " | " + item.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, "Hi,\nI would like to purchase:\n"+item.getItemNumber()+"\n"+item.getTitle()+"\n"+item.getItemDescription()+"\n\n"+"Is this item still available and if so, when/where would you like to meet on campus for the exchange ?\n\nThanks.\n--"+user.getFirstName()+"\n\n");
                intent.setData(Uri.parse("mailto:" + item.getListingUserEmail())); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);


            }
        });


        RelativeLayout buyByPaypal = (RelativeLayout) view.findViewById(R.id.buy_dialog_by_paypal);

        buyByPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();

                User user = IITBazaar.getCurrentUser();
                Item item = IITBazaar.getCurrentItem();

                PayPalLinkContructor pplc = new PayPalLinkContructor();

                String url = pplc.setBuyEmail(user.getEmail())
                .setBuyerFirstName(user.getFirstName())
                .setBuyerLastName(user.getLastName())
                .setSeller(item.getListingUserEmail())
                .setAmount(item.getPrice())
                .setItemName(item.getTitle())
                .setItemNumber(item.getItemNumber())
                .createLink();


                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });









        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }


    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            parentActivity=(BazaarActivity) context;
            Log.d(this.getClass().getSimpleName(), "Activity context set correctly");
        }
        else{
            Log.e(this.getClass().getSimpleName(),"Attached context of "+this.getClass().getSimpleName()+" is not of activity type.");
        }

    }



}