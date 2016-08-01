package edu.iit.cs442.team7.iitbazaar.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import edu.iit.cs442.team7.iitbazaar.BazaarActivity;
import edu.iit.cs442.team7.iitbazaar.IITBazaar;
import edu.iit.cs442.team7.iitbazaar.Item;
import edu.iit.cs442.team7.iitbazaar.R;
import edu.iit.cs442.team7.iitbazaar.common.PayPalLinkContructor;
import edu.iit.cs442.team7.iitbazaar.user.User;


/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */




public class UpdaterDialogBoxFragment extends DialogFragment {

    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";

    BazaarActivity parentActivity;
    Switch switchOnOff;
    Spinner spinner;

    public UpdaterDialogBoxFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = IITBazaar.getAppContext();
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();

        View view = inflater.inflate(R.layout.updater_dialog, container);

        switchOnOff = (Switch) view.findViewById(R.id.refresh_switch);
        spinner = (Spinner) view.findViewById(R.id.refresh_spinner);

        boolean autoUpdateChecked = prefs.getBoolean(PREF_AUTO_UPDATE, false);
        switchOnOff.setChecked(autoUpdateChecked);

        if (autoUpdateChecked) {
            int updateFreq = Integer.parseInt(prefs.getString(PREF_UPDATE_FREQ, "60"));
            switch (updateFreq) {
                case 1: spinner.setSelection(0);break;
                case 5: spinner.setSelection(1);break;
                case 10: spinner.setSelection(2);break;
                case 15: spinner.setSelection(3);break;
                case 60: spinner.setSelection(4);break;
                default: break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String updatePreference = "60";
                switch (position) {
                    case 0:
                        updatePreference = "1";
                        break;
                    case 1:
                        updatePreference = "5";
                        break;
                    case 2:
                        updatePreference = "10";
                        break;
                    case 3:
                        updatePreference = "15";
                        break;
                    case 4:
                        updatePreference = "60";
                    default:
                        break;
                }
                Log.e("refSpinner", "Selected: " + updatePreference);
                editor.putString(PREF_UPDATE_FREQ, updatePreference);
                editor.commit();
                parentActivity.reloadRefreshInterval();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("refSwitch", "Checked: " + isChecked);
                spinner.setEnabled(isChecked);
                editor.putBoolean(PREF_AUTO_UPDATE, isChecked);
                editor.commit();
                parentActivity.reloadRefreshInterval();
            }
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        spinner.setEnabled(switchOnOff.isChecked());

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