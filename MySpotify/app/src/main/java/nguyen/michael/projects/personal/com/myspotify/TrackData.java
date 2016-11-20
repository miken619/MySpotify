package nguyen.michael.projects.personal.com.myspotify;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Mike on 11/18/2016.
 */

public class TrackData implements Parcelable {
    String name;
    String track_url;
    String album_image_small;
    String album_image_large;
    TrackData(Track t){
        name = t.name;
        track_url = t.preview_url;

        for(Image albumImage: t.album.images){
            if (albumImage.width >= 150 && albumImage.width <= 300)
                album_image_small = albumImage.url;
            if(albumImage.width >= 500)
                album_image_large = albumImage.url;
        }

    }
    TrackData(Parcel in){
        name = in.readString();
        track_url = in.readString();
        album_image_small = in.readString();
        album_image_large = in.readString();
    }
    public static final Parcelable.Creator<TrackData> CREATOR = new Parcelable.Creator<TrackData>() {
        public TrackData createFromParcel(Parcel in) {
            return new TrackData(in);
        }

        public TrackData[] newArray(int size) {
            return new TrackData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(track_url);
        parcel.writeString(album_image_small);
        parcel.writeString(album_image_large);

    }
}
