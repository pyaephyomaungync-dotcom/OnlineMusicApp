package com.example.onlinemusicapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.example.onlinemusicapp.adapters.AlbumAdapter;
import com.example.onlinemusicapp.models.Album;

public class MainActivity extends Activity {

    ListView albumList;
    ArrayList<Album> albums = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        albumList = findViewById(R.id.albumList);
        fetchAlbums();
    }

    private void fetchAlbums() {
        new Thread(() -> {
            try {
                String jsonUrl = "https://raw.githubusercontent.com/pyaephyomaungync-dotcom/OnlineMusicApp/main/assets/albums.json";
                URL url = new URL(jsonUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                runOnUiThread(() -> parseAlbums(sb.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void parseAlbums(String json) {
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                albums.add(new Album(
                        obj.getString("albumName"),
                        obj.getString("coverUrl")
                ));
            }
            AlbumAdapter adapter = new AlbumAdapter(this, albums);
            albumList.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
