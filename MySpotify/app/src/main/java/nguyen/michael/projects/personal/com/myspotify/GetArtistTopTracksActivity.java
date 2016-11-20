package nguyen.michael.projects.personal.com.myspotify;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class GetArtistTopTracksActivity extends AppCompatActivity {

    private ListView artist_top_track_list_view;
    private ArrayList<TrackData> artist_top_track_list;
    private ArrayList<TrackData> playlist;
    private TrackAdapter adapter;
    private Intent intent;
    private String artist_name;
    private String id;
    private CheckBox favorite;
    private HashMap<String,PlayListData> map;
    private static final String TAG = GetArtistTopTracksActivity.class.getSimpleName();

    TextView artist_name_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_artist_top_tracks);
        intent = getIntent();
        artist_name = intent.getStringExtra("artist_name");
        id = intent.getStringExtra("id");
        map = (HashMap<String,PlayListData>)intent.getSerializableExtra("map");

        artist_top_track_list_view = (ListView) findViewById(R.id.tracks_list_view);
        artist_top_track_list = new ArrayList<>();
        playlist = new ArrayList<>();
        adapter = new TrackAdapter(artist_top_track_list);
        artist_top_track_list_view.setAdapter(adapter);
        artist_name_text_view  = (TextView) findViewById(R.id.selected_artist_name);
        artist_name_text_view.setText(artist_name);

        GetTracks task = new GetTracks();
        task.execute(id);

        artist_top_track_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Intent intent = new Intent(GetArtistTopTracksActivity.this,PlayerActivity.class);
                Intent intent = new Intent(GetArtistTopTracksActivity.this,PlayListActivity.class);
                intent.putExtra("position",i);
                intent.putExtra("artist_name", artist_name);
                intent.putParcelableArrayListExtra("list",artist_top_track_list);
                intent.putParcelableArrayListExtra("playlist",new ArrayList<>(map.values()));
                startActivity(intent);
            }
        });

    }


    public class GetTracks extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

       @Override
        protected Boolean doInBackground(String... tracks) {

            try{
                SpotifyApi api = new SpotifyApi();
                api.setAccessToken(LoginActivity.token);
                SpotifyService spotify = api.getService();
                String country = "US";


                Tracks top_track = spotify.getArtistTopTrack(tracks[0],country);
                artist_top_track_list.clear();

                for(Track t: top_track.tracks){
                    TrackData data= new TrackData(t);
                    artist_top_track_list.add(data);
                }
            }catch(Exception e){e.printStackTrace();}

            return true;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("map",map);
        setResult(RESULT_OK,intent);
        finish();
    }


    public class TrackAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        ArrayList<TrackData> arr_list;
        Context context;;

        TrackAdapter(ArrayList<TrackData> arr){
            arr_list = arr;
            context = GetArtistTopTracksActivity.this;
        }

        @Override
        public int getCount() {
            return arr_list.size();
        }

        @Override
        public Object getItem(int i) {
            return arr_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = view;
            if (view == null) {
                if (inflater == null)
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.track_list_item, viewGroup, false);
            }


             ImageView imageView = (ImageView) row.findViewById(R.id.album_image);
             final TrackData data = (TrackData) getItem(i);
             String url = data.album_image_small;
             Picasso.with(row.getContext()).load(url).placeholder(R.drawable.placeholder).error(R.drawable.error).into(imageView);

             TextView name = (TextView) row.findViewById(R.id.track_result_text_view);
             name.setText(data.name);

            favorite = (CheckBox)row.findViewById(R.id.favorite_checkbox);
            favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(!b) {
                        favorite.setChecked(true);
                        map.remove(data.name);
                    }else {
                        favorite.setChecked(false);
                        map.put(data.name,new PlayListData(artist_name,data));
                        Log.d(TAG, "onCheckedChanged: " + map.size());
                    }
                }
            });
            return row;
        }
    }

}
