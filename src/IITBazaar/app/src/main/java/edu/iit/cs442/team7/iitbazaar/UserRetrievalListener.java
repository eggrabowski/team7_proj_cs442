package edu.iit.cs442.team7.iitbazaar;

import edu.iit.cs442.team7.iitbazaar.user.User;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public interface UserRetrievalListener {




    void notifyUserFound(User user);

    void notifyUserNotFound(String email);

    void notifyUserRetrievalError(int statusCode);

    void notifyUserInsertion(String email);

    void notifyUserInsertionError(int statusCode);
}
