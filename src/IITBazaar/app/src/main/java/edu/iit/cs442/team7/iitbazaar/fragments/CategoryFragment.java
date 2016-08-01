package edu.iit.cs442.team7.iitbazaar.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.iit.cs442.team7.iitbazaar.R;

/**
 * Created by Swathi Shenoy on 10/28/2015.
 */
public class CategoryFragment extends Fragment {

    /*to keep track of all possible categories
    String [] listItems = {"Furniture","Appliances","Sports Goods","Books","Notes and previous year papers","Mentoring","Home Decor","Laptops","Mobiles","Collectibles","cds/dvds","Clothes","Tools","Video Games","Jewellery and Accessories","Music Instruments"};
    boolean [] listImages ={true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};*/
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        //inflate the layout for this fragment
        return inflater.inflate(R.layout.buy_category_fragment,container,false);
    }
}
