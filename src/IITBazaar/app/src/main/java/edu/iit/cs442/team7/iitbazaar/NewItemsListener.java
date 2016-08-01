package edu.iit.cs442.team7.iitbazaar;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */
public interface NewItemsListener {

    void notifyNewItems(String arrayOfItemNumbers);

    void notifyNoNewItems();

    void notifyNewItemError(int statusCode);
}
