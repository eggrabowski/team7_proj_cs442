package edu.iit.cs442.team7.iitbazaar;

import edu.iit.cs442.team7.iitbazaar.fragments.ItemDescriptionButtonFragment;

/**
 * Created by Ernesto on 28/11/15.
 */
public interface FollowStatusListener {
    void notifyFollowStatusUpdated();
    void registerFollowStatusListeners(ItemDescriptionButtonFragment itemDescriptionButtons);
}
