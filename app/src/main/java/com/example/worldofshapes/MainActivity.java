package com.example.worldofshapes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.worldofshapes.AdsPackage.AdsHandler;
import com.example.worldofshapes.AdsPackage.AdsInterface;
import com.example.worldofshapes.Helper.CenterZoomLayoutManager;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdsInterface {
    private RecyclerView shapesRecycler;
    private List<ImageItem> imageItemList;
    private ShapesAdapter adapter;
    ImageView home;
     int count=0;

    private CenterZoomLayoutManager centerZoomLayoutManager;

//    private AdView adView;

    private Button previous, play, next;
    private int counter = 0;

    private MediaPlayer mediaPlayer;
    private int[] sounds;
    ProgressDialog progressDialog;
    AdsHandler adsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting up the activity for full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sounds = new int[]{R.raw.circle, R.raw.triangle, R.raw.sphere, R.raw.square, R.raw.rectangle, R.raw.hexagon,
                R.raw.pentagon, R.raw.cylinder, R.raw.cube, R.raw.pyramid, R.raw.cone};

        /*adView = (AdView) findViewById(R.id.shapes_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build();
        adView.loadAd(adRequest);*/
        home = (ImageView) findViewById(R.id.logout);
        // initialized Adshandler constructor
        adsHandler = new AdsHandler(getApplicationContext(), MainActivity.this, MainActivity.this);

        home.bringToFront();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.UpdateDialogStyle));
                /*  builder.setTitle(R.string.app_name);*/

                builder.setMessage("Do you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                boolean t = adsHandler.checkInternetConenction();
//                                if (t == true) {
//                                    progressDialog.show();
//                                    adsHandler = new AdsHandler(getApplicationContext(), MainActivity.this, MainActivity.this);
//                                    adsHandler.InterstitialAdLoad(getApplicationContext());
//                                }else {
                                    finish();
                                //}
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        imageItemList = new ArrayList<>();
        initList();
        adapter = new ShapesAdapter(this, imageItemList);
        shapesRecycler = (RecyclerView) findViewById(R.id.recycler_shapes);
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        shapesRecycler.setLayoutManager(centerZoomLayoutManager);
        shapesRecycler.setItemAnimator(new DefaultItemAnimator());
        shapesRecycler.setAdapter(adapter);

        previous = (Button) findViewById(R.id.previous_shapes);
        play = (Button) findViewById(R.id.play_shapes);
        next = (Button) findViewById(R.id.next_shapes);

        counter = Integer.MAX_VALUE / 2;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        final Random myRandom = new Random();
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                int random = myRandom.nextInt(imageItemList.size());
                Log.d("random", "" + random);
                if (count == imageItemList.size()) {
                    count = 0;
                }
                Log.d("Counter", "" + count);
                // check network state
                Boolean True = adsHandler.checkInternetConenction();
                if (count == random && True) {
                    progressDialog.show();
                    adsHandler.InterstitialAdLoad(getApplicationContext());
                } else {
                    counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                    counter--;
                    shapesRecycler.smoothScrollToPosition(counter);

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int random = myRandom.nextInt(imageItemList.size());
                Log.d("random", "" + random);
                if (count == imageItemList.size()) {
                    count = 0;
                }
                Log.d("Counter", "" + count);
                // check network state
                Boolean True = adsHandler.checkInternetConenction();
                if (count == random && True) {
                    progressDialog.show();
                    adsHandler.InterstitialAdLoad(getApplicationContext());
                } else {
                    counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                    counter++;
                    shapesRecycler.smoothScrollToPosition(counter);
                }
            }
        });

        shapesRecycler.scrollToPosition(counter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(shapesRecycler);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                int pos = counter % imageItemList.size();
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds[pos]);
                mediaPlayer.start();
            }
        });
    }

    private void initList() {
        imageItemList.add(new ImageItem("Circle", R.drawable.circle));
        imageItemList.add(new ImageItem("Triangle", R.drawable.triangle));
        imageItemList.add(new ImageItem("Sphere", R.drawable.sphere));
        imageItemList.add(new ImageItem("Square", R.drawable.square));
        imageItemList.add(new ImageItem("Rectangle", R.drawable.rectangle));
        imageItemList.add(new ImageItem("Hexagon", R.drawable.hexagon));
        imageItemList.add(new ImageItem("Pentagon", R.drawable.pentagon));
        imageItemList.add(new ImageItem("Cylinder", R.drawable.cylinder));
        imageItemList.add(new ImageItem("Cube", R.drawable.cube));
        imageItemList.add(new ImageItem("Pyramid", R.drawable.pyramid));
        imageItemList.add(new ImageItem("Cone", R.drawable.cone));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void OnLoadAd(InterstitialAd interstitialAd, Activity activity) {
        progressDialog.hide();
        if (interstitialAd != null) {
            interstitialAd.show(activity);
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void OnError(LoadAdError error, Activity activity) {
        progressDialog.hide();
        Toast.makeText(activity, ""+error, Toast.LENGTH_LONG).show();
        Log.d("Error",""+error);


    }
}