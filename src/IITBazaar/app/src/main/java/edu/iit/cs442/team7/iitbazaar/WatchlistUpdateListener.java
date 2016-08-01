package edu.iit.cs442.team7.iitbazaar;

/**
 * Created by Ernesto on 27/11/15.
 */
public interface WatchlistUpdateListener {
    void notifyWatchlistUpdated();
    void registerWatchlistChangeListeners(CursorAdapter cursorAdapter);
}
