package edu.iit.cs442.team7.iitbazaar;

/**
 * Created by Swathi Shenoy on 11/27/2015.
 */
public interface SellItemUpdateListener {
    void notifySellItemUpdated();
    void registerSellDataChangeListeners(CursorAdapter cursorAdapter);
}
