package com.surya.previewurl;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by surya on 25/7/17.
 */

public class UrlPreview extends FrameLayout {

    private static final String TAG = UrlPreview.class.getSimpleName();
    private TextView mTitle, mDescription, mWebUrl;
    private ImageView mPreviewImage;
    private LinearLayout mLinearLayout;

    public UrlPreview(@NonNull Context context) {
        super(context);
        initializeUI(context);
    }

    public UrlPreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeUI(context);
    }

    public UrlPreview(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeUI(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UrlPreview(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeUI(context);
    }

    private void initializeUI(Context context) {

        inflate(context,R.layout.preview_layout,this);

        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mTitle = findViewById(R.id.web_address);
        mPreviewImage = findViewById(R.id.web_image);
        mLinearLayout = findViewById(R.id.container);

    }

    public void processUrl(String url){

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                Log.e(TAG, "onFailure: " + e.getMessage() );

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {

                String body = response.body().string();

                Document document = Jsoup.parse(body);

                if (document.title() != null)
                    mTitle.setText(document.title());
                else
                    mTitle.setText("Not Available");

                Elements descriptionElement = document.select("meta[property=og:description]");

                if (descriptionElement.size() > 0 && descriptionElement.get(0).attr("content") != null)
                    mDescription.setText(descriptionElement.get(0).attr("content"));
                else
                    mDescription.setText("Not available");

                Elements imageElement = document.select("meta[property=og:image]");

                if (imageElement.size() > 0 && imageElement.get(0).attr("content") != null)
                    Picasso.with(getContext()).load(imageElement.get(0).attr("content")).into(mPreviewImage);
                else
                    mPreviewImage.setVisibility(GONE);



            }
        });
    }
}
