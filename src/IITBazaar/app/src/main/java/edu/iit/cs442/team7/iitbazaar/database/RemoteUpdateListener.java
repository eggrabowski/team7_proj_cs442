package edu.iit.cs442.team7.iitbazaar.database;

import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;

import edu.iit.cs442.team7.iitbazaar.CursorAdapter;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public interface RemoteUpdateListener {


    void notifyDataChange();

    void registerDataChangeListeners(CursorAdapter adapter);

}
