package edu.iit.cs442.team7.iitbazaar;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public interface CategoryUpdateListener {
    void notifyCategoryListUpdated();
    void registerCategoryListChangeListeners(CursorAdapter cursorAdapter);
}
