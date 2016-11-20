package nguyen.michael.projects.personal.com.myspotify;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mike on 11/20/2016.
 */

public class PlayListData implements Parcelable {

    String artist_name;
    String song_name;
    String track_url;
    String album_image_large;
    String album_image_small;


    PlayListData(String artist_name, TrackData data){
        this.artist_name = artist_name;
        song_name = data.name;
        track_url = data.track_url;
        album_image_large = data.album_image_large;
        album_image_small = data.album_image_small;
    }

    PlayListData(Parcel in){
        artist_name = in.readString();
        song_name  = in.readString();
        track_url  = in.readString();
        album_image_large  = in.readString();
        album_image_small  = in.readString();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(artist_name);
        parcel.writeString(song_name);
        parcel.writeString(track_url);
        parcel.writeString(album_image_large);
        parcel.writeString(album_image_small);
    }

    public static final Parcelable.Creator<PlayListData> CREATOR = new Parcelable.Creator<PlayListData>() {
        public PlayListData createFromParcel(Parcel in) {
            return new PlayListData(in);
        }

        public PlayListData[] newArray(int size) {
            return new PlayListData[size];
        }
    };


}
