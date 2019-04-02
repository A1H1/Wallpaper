package in.devco.creativesuperheroes4kartworks;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import in.devco.creativesuperheroes4kartworks.func.DataUrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import in.devco.creativesuperheroes4kartworks.R;
import in.devco.creativesuperheroes4kartworks.config.admob;
import in.devco.creativesuperheroes4kartworks.func.Button;
import in.devco.creativesuperheroes4kartworks.func.CustomTextView;

/**
 * Created by kingdov on 17/01/2017.
 */

public class WallpaperDetail extends AppCompatActivity {

    public static DataUrl sData;
    ImageView arrow_back, wallstuffimage;
    CustomTextView wallstufftitle, wallstuffsite, wallstufflink,
            wallstufflicense, wallstufftype;
    Button setwallpaper ,download, preview;
    String directory, extension, fileplace, fulldescPerm;
    View apnaview;
    final int writeStorageRequest = 0;
    FloatingActionButton wallstuffshare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);
        admob.initialInterstitial(this);
        LinearLayout linearlayout = findViewById(R.id.unitads);
        admob.admobBannerCall(this, linearlayout);
        findStuff();
        setCopyrightText();
        setData();
        downloadWall();
        setArrowBack();
        setPreview();
        setWallButton();
        setShareButton();
    }

    private void findStuff() {
//        wallstufftitle = findViewById(R.id.wallstufftitle);
//        wallstufftype = findViewById(R.id.wallpaperType);
//        wallstuffsite = findViewById(R.id.wallstuffsite);
//        wallstufflink = findViewById(R.id.wallstufflink);
        wallstuffimage = findViewById(R.id.wallstuffimage);
        arrow_back = findViewById(R.id.arrow_back);
//        wallstufflicense = findViewById(R.id.wallstufflicense);
        download = findViewById(R.id.download);
        preview = findViewById(R.id.preview);
        setwallpaper = findViewById(R.id.setwallpaper);
        wallstuffshare = findViewById(R.id.wallstuffshare);
    }

    private void setData() {
//        wallstufftitle.setText(sData.title);
//        wallstuffsite.setText(sData.title);
//        wallstufflink.setText(sData.url);
//        switch (sData.url.toLowerCase().substring(sData.url.length() - 3)){
//            case "jpg" :
//                wallstufftype.setText("Format jpg");
//                extension=".jpg";
//                break;
//            case "peg" :
//                wallstufftype.setText("Format jpeg");
//                extension=".jpeg";
//                break;
//            case "png" :
//                wallstufftype.setText("Format png");
//                extension=".png";
//                break;
//            case "gif" :
//                wallstufftype.setText("Format gif");
//                extension=".gif";
//                break;
//            default :
//                wallstufftype.setText("Unknown");
//                extension=".jpg";
//                break;
//        }

//        if (sData.title.matches("Pinimg") || sData.title.matches("Imgur")) {
//            wallstufflicense.setText(R.string.public_domain);
//        } else {
//            wallstufflicense.setText(R.string.creative_commons);
//        }


        GlideApp.with(this)
                .load(sData.url)
                .error(R.drawable.ic_placeholder_wallpaper_head)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder_wallpaper_head)
                .into(wallstuffimage);

    }

    private void setCopyrightText() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.licence_text));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperDetail.this, DiscActivity.class);
                startActivity(intent);
                admob.showInterstitial(true);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        };

        ss.setSpan(clickableSpan, 335, 339, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void setDownload(String uRl) {

        directory = "/Pictures/" + getResources().getString(R.string.app_name) + "/";
        fileplace = Environment.getExternalStorageDirectory() + directory + sData.title;


        File direct = new File(Environment.getExternalStorageDirectory()
                + directory);
        Toast.makeText(this,"Downloading...",Toast.LENGTH_SHORT).show();
        if (!direct.exists()) {
            direct.mkdir();
        }

        GlideApp.with(this)
                .asBitmap()
                .load(sData.url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, Transition<? super Bitmap> transition) {
                        new AsyncTask<Void, Void, Long>() {

                            @Override
                            protected Long doInBackground(Void... voids) {
                                File file = new File(fileplace);
                                long contentlength = resource.getByteCount();

                                try {
                                    OutputStream os = new FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.PNG, 100, os);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                return contentlength;
                            }

                            @Override
                            protected void onPostExecute(Long contentlength) {
                                super.onPostExecute(contentlength);

                                // Add downloaded wallie to completed downloads, show notif and all
                                DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                manager.addCompletedDownload(
                                        sData.title,
                                        "By" + sData.title,
                                        true,
                                        "image/png",
                                        fileplace,
                                        contentlength,
                                        true
                                );

                                // Show snackbar too
                                Snackbar.make(apnaview, "Download Finished", Snackbar.LENGTH_LONG)

                                        .setAction("Open", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(Build.VERSION.SDK_INT>=24){
                                                    try{
                                                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                                        m.invoke(null);
                                                    }catch(Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                                Intent intent = new Intent();
                                                intent.setAction(Intent.ACTION_VIEW);
                                                intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory() + directory + "/" + sData.title), "image/*");
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }
                        }.execute();
                    }
                });
    }

    private void downloadWall() {
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apnaview = view;
                if (isPermissionGranted()) {
                    setDownload(sData.url);
                }
                admob.showInterstitial(true);
            }
        });
    }

    private boolean isPermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                // Explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    fulldescPerm = getResources().getString(R.string.storage_explanation_1)
                            + " "
                            + getResources().getString(R.string.app_name)
                            + " "
                            + getResources().getString(R.string.storage_explanation_2);

                    AlertDialog.Builder builder = new AlertDialog.Builder(WallpaperDetail.this);
                    builder.setMessage(fulldescPerm)
                            .setTitle(R.string.storage_required_title)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(WallpaperDetail.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            writeStorageRequest);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return false;

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            writeStorageRequest);
                    return false;
                }
            }
        } else {
            // Auto grant if pre-MM device
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case writeStorageRequest: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.received_permission, Toast.LENGTH_SHORT).show();
                    setDownload(sData.url);
                } else {
                    Toast.makeText(this, R.string.no_permission, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setArrowBack() {
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
    }

    private void setPreview() {
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperDetail.this, WallpaperPreview.class);
                startActivity(intent);
                admob.showInterstitial(true);
            }
        });
    }

    private void setWallButton() {

        setwallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View ourView) {
                GlideApp.with(getApplicationContext())
                        .asBitmap()
                        .load(sData.url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                WallpaperSelectDialog wallSelectDialog = new WallpaperSelectDialog(WallpaperDetail.this, resource, ourView);
                                wallSelectDialog.show(getFragmentManager(), "wallselectdialog");
                            }
                        });
            }
        });

        admob.showInterstitial(true);
    }

    private void setShareButton() {
        
        final String shareText = getString(R.string.share_text) + " "
                + getString(R.string.app_name) + " app by "
                + getString(R.string.dev) + ": \n\n"
                + sData.title + "\n"
                + sData.url;

        wallstuffshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(sendIntent, "Share wallpaper via:"));
            }
        });

    }

}
