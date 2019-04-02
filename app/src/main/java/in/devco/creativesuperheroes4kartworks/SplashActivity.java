package in.devco.creativesuperheroes4kartworks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import in.devco.creativesuperheroes4kartworks.activity.MainActivity;
import in.devco.creativesuperheroes4kartworks.config.Config;
import in.devco.creativesuperheroes4kartworks.func.DataUrl;
import in.devco.creativesuperheroes4kartworks.func.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.devco.creativesuperheroes4kartworks.R;

/**
 * Created by kingdov on 17/01/2017.
 */

public class SplashActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    public static List<DataUrl> data = new ArrayList<>();
    public static List<DataUrl> allWallpapers = new ArrayList<>();
    private final String ourDataFilenameNoSlash = "category.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView tv= findViewById(R.id.tv);
        ImageView iv= findViewById(R.id.iv);
        Animation anim= AnimationUtils.loadAnimation(this, R.anim.transition_spalsh);
        tv.startAnimation(anim);
        iv.startAnimation(anim);

        new AsyncFetchCategory().execute();
        new AsyncFetchAllWallpapers().execute();
    }

    private boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                assert connectivityManager != null;
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void writeJsonToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(ourDataFilenameNoSlash, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.i("io", "Wrote file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buffToString(Reader ourReader, boolean save) {
        try {
            BufferedReader reader = new BufferedReader(ourReader);
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // write it to ourdata.json
            if (save) {
                if (!result.toString().equals(null)) {
                    writeJsonToFile(result.toString(), SplashActivity.this);
                }
            }

            return (result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private class AsyncFetchCategory extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(Config.CATEGORY_URL);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                HashMap<String, String> hm = new HashMap<>();
                hm.put("app_name", Config.APP_NAME);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(ServerConnection.getPostDataString(hm));

                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();

                return buffToString(new InputStreamReader(stream), false);
            } catch (Exception e) {
                Log.e("Url", e.toString());
            } finally {
                urlConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    DataUrl dataUrl = new DataUrl();
                    dataUrl.index = jsonData.getInt("index");
                    dataUrl.title = jsonData.getString("title");
                    dataUrl.url = jsonData.getString("url");
                    data.add(dataUrl);
                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } catch (JSONException e) {
                Log.e("Url", e.toString());
            }
        }
    }

    private class AsyncFetchAllWallpapers extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(Config.ALL_WALLPAPERS);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                HashMap<String, String> hm = new HashMap<>();
                hm.put("app_name", Config.APP_NAME);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(ServerConnection.getPostDataString(hm));

                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();

                return buffToString(new InputStreamReader(stream), true);
            } catch (Exception e) {
                Log.e("Url", e.toString());
            } finally {
                urlConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    DataUrl dataUrl = new DataUrl();
                    dataUrl.index = jsonData.getInt("index");
                    dataUrl.title = jsonData.getString("title");
                    dataUrl.url = jsonData.getString("url");
                    allWallpapers.add(dataUrl);
                }
                finish();
            } catch (JSONException e) {
                Log.e("Url", e.toString());
            }
        }
    }
}
