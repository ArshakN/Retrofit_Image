package com.example.retrofit_image;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    String url = "https://api.androidhive.info";
    ProgressDialog pd;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.RetrofitImage);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setVisibility(View.GONE);
                progresDialog(true);
                getRetrofitImage();
            }
        });
    }

    void getRetrofitImage() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitImageAPI service = retrofit.create(RetrofitImageAPI.class);

        Call<ResponseBody> call = service.getImageDetails();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

                try {
                    Log.d("onResponse", "Response came from server");
                    boolean FileDownloaded = DownloadImage(response.body());
                    Log.d("onResponse", "Image is downloaded and saved ? " + FileDownloaded);
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private boolean DownloadImage(ResponseBody body) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(getExternalFilesDir(null) + File.separator + "Android.jpg");
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } catch (IOException e) {
                Log.d("DownloadImage", e.toString());
                return false;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            int width, height;
            progresDialog(false);
            ImageView image = (ImageView) findViewById(R.id.imageViewId);
            Bitmap bMap = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + "Android.jpg");
            width = bMap.getWidth();
            height =  bMap.getHeight();
            Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
            image.setImageBitmap(bMap2);
            return true;

        } catch (IOException e) {
            Log.d("DownloadImage", e.toString());
            return false;
        }
    }

    public void progresDialog(boolean b)
    {
        if (b)
        {
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Wait");
            pd.setMessage("Downloading image");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setIndeterminate(true);
            pd.show();
        } else {
            pd.setIndeterminate(false);
            pd.dismiss();
        }

    }
}