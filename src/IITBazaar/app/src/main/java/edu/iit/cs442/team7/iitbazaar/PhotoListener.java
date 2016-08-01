package edu.iit.cs442.team7.iitbazaar;

import android.net.Uri;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public interface PhotoListener {


    public void loadPhotoFromPath(String absolutePath);

    public void loadPhotoFromURI(Uri uriPath);

}
