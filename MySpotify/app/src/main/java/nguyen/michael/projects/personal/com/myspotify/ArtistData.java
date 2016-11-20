package nguyen.michael.projects.personal.com.myspotify;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Mike on 11/13/2016.
 */

public class ArtistData {
    String name = "";
    String id;
    String image;

    ArtistData(Artist artist){
        name = artist.name;
        id = artist.id;
        for (Image artistImage : artist.images) {
            if (artistImage.width >= 150 && artistImage.width <= 300) {
                image = artistImage.url;
                break;
            }
        }

    }
}
