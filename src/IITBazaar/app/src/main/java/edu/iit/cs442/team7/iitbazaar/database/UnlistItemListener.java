package edu.iit.cs442.team7.iitbazaar.database;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public interface UnlistItemListener {
    void notifyItenNotUnlistedError(int itemNumber, int statusCode);

    void notifyItemNotUnlisted(int itemNumber, String insertionResult);

    void notifyItemUnlisted(int itemNumber);
}
