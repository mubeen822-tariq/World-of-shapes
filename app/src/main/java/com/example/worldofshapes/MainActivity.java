package com.example.worldofshapes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.worldofshapes.Helper.CenterZoomLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView shapesRecycler;
    private List<ImageItem> imageItemList;
    private ShapesAdapter adapter;
    ImageView home;

    private CenterZoomLayoutManager centerZoomLayoutManager;

//    private AdView adView;

    private Button previous, play, next;
    private int counter = 0;

    private MediaPlayer mediaPlayer;
    private int[] sounds;

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
                                finish();
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

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter--;
                shapesRecycler.smoothScrollToPosition(counter);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter++;
                shapesRecycler.smoothScrollToPosition(counter);
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
}