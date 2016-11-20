package nguyen.michael.projects.personal.com.myspotify;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {

    private ArrayList<PlayListData> list;
    private ListView list_view;
    private PlayListAdapter adapter;
    private static final String TAG = PlayListActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        list = getIntent().getParcelableArrayListExtra("playlist");
        Log.d(TAG, "onCreate: " + list.size());
        list_view = (ListView) findViewById(R.id.playlist_list_view);
        adapter = new PlayListAdapter(list);
        list_view.setAdapter(adapter);
    }


    public class PlayListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ArrayList<PlayListData> arr_list;
        Context context;;

        PlayListAdapter(ArrayList<PlayListData> arr){
            arr_list = arr;
            context = PlayListActivity.this;
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
                row = inflater.inflate(R.layout.playlist_list_item, viewGroup, false);
            }


            ImageView imageView = (ImageView) row.findViewById(R.id.playlist_image_view);
            PlayListData data = (PlayListData) getItem(i);
            String url = data.album_image_small;
            Picasso.with(row.getContext()).load(url).placeholder(R.drawable.placeholder).error(R.drawable.error).into(imageView);

            TextView artist_name = (TextView) row.findViewById(R.id.playlist_artist_name);
            TextView song_name = (TextView) row.findViewById(R.id.playlist_song_name);
            artist_name.setText(data.artist_name);
            song_name.setText(data.song_name);


            return row;
        }
    }
}
