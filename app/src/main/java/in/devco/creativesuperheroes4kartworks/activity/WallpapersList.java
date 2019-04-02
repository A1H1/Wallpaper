package in.devco.creativesuperheroes4kartworks.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.devco.creativesuperheroes4kartworks.adapter.WallpaperAdapter;
import in.devco.creativesuperheroes4kartworks.config.Config;
import in.devco.creativesuperheroes4kartworks.config.admob;
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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.devco.creativesuperheroes4kartworks.R;

public class WallpapersList extends AppCompatActivity {

    public static String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpapers_list);

        admob.admobBannerCall(this, (LinearLayout) findViewById(R.id.wallpapers_list_ll_ad));

        String categorySlug = category.toLowerCase().replaceAll(" ", "_");

        new AsyncFetchWallpapers(categorySlug, this).execute();
    }

    private static class AsyncFetchWallpapers extends AsyncTask<String, String, String> {
        private HttpURLConnection urlConnection;
        private WeakReference<WallpapersList> activityReference;
        private String categoryName;
        private List<DataUrl> data = new ArrayList<>();

        AsyncFetchWallpapers(String category, WallpapersList context) {
            categoryName = category;
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(Config.WALLPAPERS_URL);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                HashMap<String, String> hm = new HashMap<>();
                hm.put("app_name", Config.APP_NAME);
                hm.put("category", categoryName);

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
            WallpapersList activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

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

                RecyclerView recyclerView = activity.findViewById(R.id.wallpapers_list_rv_list);
                TextView noCategory = activity.findViewById(R.id.wallpapers_list_tv_no_category);

                if(data.size()>0)
                    noCategory.setVisibility(View.GONE);
                else
                    noCategory.setVisibility(View.VISIBLE);

                WallpaperAdapter recyclerAdapter = new WallpaperAdapter(activity, data);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
            } catch (JSONException e) {
                Log.e("Url", e.toString());
            }
        }

        private String buffToString(Reader ourReader, boolean save) {
            WallpapersList activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return null;

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
                        writeJsonToFile(result.toString(), activity);
                    }
                }

                return (result.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        private void writeJsonToFile(String data, Context context) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(categoryName + ".json", Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
                Log.i("io", "Wrote file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}