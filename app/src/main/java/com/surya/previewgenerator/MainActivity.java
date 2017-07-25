package com.surya.previewgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        UrlPreview urlPreview = (UrlPreview) findViewById(R.id.preview);

        urlPreview.processUrl("https://android.jlelse.eu/");
        new Thread(new Runnable() {
            @Override
            public void run() {

                Document doc = null;
                try {
                    doc = Jsoup.connect("http://square.github.io/picasso/").get();
                    Log.e(TAG, "onCreate: " + doc.title() );

                    Elements elements = doc.select("meta[property=og:description]");
                    Log.e(TAG, "run: " + elements);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
