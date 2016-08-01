package edu.iit.cs442.team7.iitbazaar.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import edu.iit.cs442.team7.iitbazaar.BazaarActivity;
import edu.iit.cs442.team7.iitbazaar.IITBazaar;
import edu.iit.cs442.team7.iitbazaar.Item;
import edu.iit.cs442.team7.iitbazaar.R;


/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class SellItemInitEntryFragment extends Fragment {


    private View view;

    private BazaarActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.sell_item_init_entry_fragment, container, false);



        // Inflate the layout for this fragment

        final EditText editText = (EditText) view.findViewById(R.id.sell_item_title);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {


                    Item item = new Item();
                    //there should be input validation here
                    item.setTitle(editText.getText().toString());
                    IITBazaar.setCurrentItem(item);

                    parentActivity.getNextSellFragment(SellNavKeys.SELL_ITEM_DESCRIPTION_ENTRY);

                    View view = parentActivity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager inputManager = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    return true;
                }
                return false;
            }
        });




        editText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {

                    Item item = new Item();
                    //there should be input validation here
                    item.setTitle(editText.getText().toString());
                    IITBazaar.setCurrentItem(item);




                    parentActivity.getNextSellFragment(SellNavKeys.SELL_ITEM_DESCRIPTION_ENTRY);





                    return true;
                }
                return false;
            }
        });





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

    public String getEnteredTitle () {
        EditText editText = (EditText) view.findViewById(R.id.sell_item_title);
        return editText.getText().toString();
    }


}


