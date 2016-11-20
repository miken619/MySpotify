package nguyen.michael.projects.personal.com.myspotify;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mike on 11/18/2016.
 */

public class PlayerActivity extends AppCompatActivity{

    private static final String TAG = PlayerActivity.class.getSimpleName();

    private MediaPlayer mMediaPlayer;
    private int mCurrentTrack;
    private Button prev;
    private Button stop;
    private Button play;
    private Button next;

    private TextView artist_name;
    private TextView song_name;
    private TextView time_of_song;
    private TextView remaining_song;

    private ImageView album_image;

    private SeekBar song_progress;

    private int song_duration;
    private int song_current_position;

    private Handler myHandler = new Handler();
    private boolean bool;

    private ArrayList<TrackData> artist_top_track_list;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        prev = (Button)findViewById(R.id.prev_song_button);
        stop = (Button)findViewById(R.id.stop_song_button);
        play = (Button)findViewById(R.id.play_song_button);
        next = (Button)findViewById(R.id.next_song_button);

        artist_name = (TextView) findViewById(R.id.player_artist_name);
        song_name = (TextView) findViewById(R.id.player_song_name);
        time_of_song = (TextView) findViewById(R.id.time_of_song);
        remaining_song = (TextView) findViewById(R.id.remaining_time_of_song);

        album_image = (ImageView) findViewById(R.id.player_artist_image);

        song_progress = (SeekBar) findViewById(R.id.song_progress);
        song_progress.setClickable(false);

        Intent intent = getIntent();
        mCurrentTrack = intent.getIntExtra("position",0);
        artist_name.setText(intent.getStringExtra("artist_name"));
        artist_top_track_list = intent.getParcelableArrayListExtra("list");
        length = artist_top_track_list.size();

        mMediaPlayer = new MediaPlayer();



        bool = true;


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(mCurrentTrack);
            }

        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMediaPlayer.pause();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentTrack < length - 2){
                    myHandler.removeCallbacksAndMessages(null);
                    play(++mCurrentTrack);
                }


            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentTrack >= 1){
                    myHandler.removeCallbacksAndMessages(null);
                    play(--mCurrentTrack);
                }


            }
        });

    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            song_current_position = mMediaPlayer.getCurrentPosition();
            remaining_song.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) song_current_position),
                    TimeUnit.MILLISECONDS.toSeconds((long) song_current_position) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) song_current_position)))
            );
            song_progress.setProgress(song_current_position);
            myHandler.postDelayed(this, 100);
        }
    };


  private void play(int i){
      TrackData data = null;
      try {
          mMediaPlayer.reset();
          data = artist_top_track_list.get(i);
          mMediaPlayer.setDataSource(data.track_url);
          mMediaPlayer.prepare();
          mMediaPlayer.start();
      } catch (IOException e) {
          e.printStackTrace();
      }
      String url = data.album_image_large;
      song_name.setText(data.name);
      Picasso.with(PlayerActivity.this).load(url).placeholder(R.drawable.placeholder).error(R.drawable.error).into(album_image);


      song_duration = mMediaPlayer.getDuration();
      song_current_position = mMediaPlayer.getCurrentPosition();


      song_progress.setMax(song_duration);


      time_of_song.setText(String.format("%d min, %d sec",
              TimeUnit.MILLISECONDS.toMinutes((long) song_duration),
              TimeUnit.MILLISECONDS.toSeconds((long) song_duration) -
                      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                              song_duration)))
      );
      remaining_song.setText(String.format("%d min, %d sec",
              TimeUnit.MILLISECONDS.toMinutes((long) song_current_position),
              TimeUnit.MILLISECONDS.toSeconds((long) song_current_position) -
                      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                              song_current_position)))
      );

      song_progress.setProgress(song_current_position);
      myHandler.postDelayed(UpdateSongTime,100);
  }


}



