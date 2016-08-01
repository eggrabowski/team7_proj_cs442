package edu.iit.cs442.team7.iitbazaar.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.iit.cs442.team7.iitbazaar.R;

/**
 * Created by Ernesto on 06/11/15.
 */
public class RemovePictureFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.remove_picture_fragment, container, false);

    }
}
