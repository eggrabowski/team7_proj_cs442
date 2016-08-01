package edu.iit.cs442.team7.iitbazaar;

import android.database.Cursor;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public interface CursorAdapter {
    void notifyCursorChanged(Cursor cursor);
}
