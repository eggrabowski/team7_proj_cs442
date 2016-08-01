package edu.iit.cs442.team7.iitbazaar.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import edu.iit.cs442.team7.iitbazaar.BazaarActivity;
import edu.iit.cs442.team7.iitbazaar.MenuKeys;
import edu.iit.cs442.team7.iitbazaar.R;


/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class BuyMenuFragment extends Fragment {



    private View view;

    private BazaarActivity parentActivity;

    private RadioGroup buyMenuRadioGroup;

    private final String TAG = "BuyMenuFragment";

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.buymenufragment, container, false);
        buyMenuRadioGroup = (RadioGroup) view.findViewById(R.id.buy_menu_radio_group);


/*

        buyMenuRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.buy_ending_button) {

                    parentActivity.menuSelected(MenuKeys.BUY_ENDING);

                } else if (checkedId == R.id.buy_categories_button) {
                    parentActivity.menuSelected(MenuKeys.BUY_CATEGORIES);
                }
            }

        });
*/



        RadioButton buyEndingButton = (RadioButton) buyMenuRadioGroup.findViewById(R.id.buy_ending_button);

        buyEndingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parentActivity.menuSelected(MenuKeys.BUY_ENDING);

            }
        });

        RadioButton buyCategoriesButton = (RadioButton) buyMenuRadioGroup.findViewById(R.id.buy_categories_button);


        buyCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parentActivity.menuSelected(MenuKeys.BUY_CATEGORIES);

            }
        });



        RadioButton nullButton =   (RadioButton) view.findViewById(R.id.buy_null_button);

        parentActivity.registerNullButtonListener(TAG,nullButton);

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


