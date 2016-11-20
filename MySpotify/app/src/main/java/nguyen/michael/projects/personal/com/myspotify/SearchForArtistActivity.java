package nguyen.michael.projects.personal.com.myspotify;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SearchForArtistActivity extends AppCompatActivity {


    private EditText search_edit_text;
    private ListView search_result_list_view;
    private ArrayList<ArtistData> artist_data_list;
    private ArrayAdapter adapter;
    private HashMap<String,PlayListData> map;
    private static final int REQUEST_CODE = 1000;
    private static final String TAG = SearchForArtistActivity.class.getSimpleName();

    SearchInBackground search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_artist);

        search_edit_text = (EditText) findViewById(R.id.search_field_edit_text);
        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(search_edit_text.length() != 0) {
                    search = new SearchInBackground();
                    search.execute(search_edit_text.getText().toString());
                }else{
                    artist_data_list.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        map = new HashMap<>();
        artist_data_list = new ArrayList<>();
        search_result_list_view = (ListView) findViewById(R.id.artist_name_list_view);
        adapter = new ArrayAdapter(artist_data_list);
        search_result_list_view.setAdapter(adapter);

        search_result_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  ArtistData data = artist_data_list.get(i);
                  String artist_name =  data.name;
                  String artist_id = data.id;
                  Intent intent = new Intent(SearchForArtistActivity.this,GetArtistTopTracksActivity.class);
                  intent.putExtra("artist_name",artist_name);
                  intent.putExtra("id",artist_id);
                  intent.putExtra("map",map);
                  startActivityForResult(intent,REQUEST_CODE);
            }
        }

        );



    }

    public class SearchInBackground extends AsyncTask<String,Void,Boolean>{

        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... artist) {

            try{
                SpotifyApi api = new SpotifyApi();
                api.setAccessToken(LoginActivity.token);
                SpotifyService spotify = api.getService();

                ArtistsPager pager = spotify.searchArtists(artist[0]);
                artist_data_list.clear();

                for(Artist a: pager.artists.items){
                    artist_data_list.add(new ArtistData(a));
                }
            }catch(Exception e){e.printStackTrace();}

            return true;
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult: Entered");
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            map = (HashMap<String,PlayListData>)intent.getSerializableExtra("map");
        }
    }

    public class ArrayAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        ArrayList<ArtistData> arr_list;
        Context context;
        ArrayAdapter(ArrayList<ArtistData> arr_list){
            this.context = SearchForArtistActivity.this;
            this.arr_list = arr_list;
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
                row = inflater.inflate(R.layout.artist_list_item, viewGroup, false);
            }

                ImageView imageView = (ImageView) row.findViewById(R.id.image_view);
                ArtistData data = (ArtistData) getItem(i);
                String url = data.image;
                Picasso.with(row.getContext()).load(url).placeholder(R.drawable.placeholder).error(R.drawable.error).into(imageView);

                TextView name = (TextView) row.findViewById(R.id.search_result_text_view);
                name.setText(data.name);


            return row;
        }
    }
}
