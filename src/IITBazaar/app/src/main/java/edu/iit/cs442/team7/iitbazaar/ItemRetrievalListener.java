package edu.iit.cs442.team7.iitbazaar;



/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public interface ItemRetrievalListener {

    void notifyItemFound(Item item, MenuKeys key);

    void notifyItemNotFound(int item);

    void notifyItemRetrievalError(int statusCode);

}
