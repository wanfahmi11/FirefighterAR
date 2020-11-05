package com.puo.fireman_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.puo.arcore_project.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class ARActivity extends AppCompatActivity {

    private static final String TAG = ARActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private boolean shouldAddModel = true;
    private LinearLayout infoLayout;
    private TextView title;
    private TextView info;
    private AnchorNode anchorNode;
    private Node node;
    private String augmentedName = "", lang;
    private ImageButton backButton;
    private VideoView videoView;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) { return; }

        setContentView(R.layout.activity_ar);

        infoLayout = findViewById(R.id.info_layout);
        title = findViewById(R.id.title);
        info = findViewById(R.id.info);
        videoView = findViewById(R.id.videoView);
        lang = getSharedPreferences("user_pref" , Context.MODE_PRIVATE).getString("lang" , "bi");
        handler = new Handler();
        //Back button to navigate to home
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
        arFragment.getPlaneDiscoveryController().hide();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    videoView.stopPlayback();
                    videoView.setVisibility(View.GONE);
                }
                removeModel();
            }
        };

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.getVisibility() == View.VISIBLE) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                    }
                    else {
                        videoView.resume();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onUpdateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();

        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : augmentedImages) {

            //Only do something when Tracking state is as below
            if (augmentedImage.getTrackingState() == TrackingState.TRACKING && augmentedImage.getTrackingMethod() == AugmentedImage.TrackingMethod.FULL_TRACKING) {
                handler.removeCallbacks(runnable);
                //Change global variable to detected image so we can reuse the same variable on all function in this class
                if (!augmentedName.equals(augmentedImage.getName())) {
                    augmentedName = augmentedImage.getName();
                    shouldAddModel = true;
                }

                if (shouldAddModel) {

                    int raw_path = 0;
                    String[] arr = {};


                    //Consider images in switch and show info
                    switch (augmentedName) {
                        case "fire-ext.png" :
                            raw_path = R.raw.fireext;
                            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/fire_ext_video"));
                            videoView.setVisibility(View.VISIBLE);
                            videoView.start();
                            break;
                        case "fire-hose.png" :
                            raw_path = R.raw.firehose;
                            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/fire_hose_video"));
                            videoView.setVisibility(View.VISIBLE);
                            videoView.start();
                            break;
                    }

                    //Draw 3D model onto plane if the raw path is not empty
                    if(raw_path != 0) {
                        placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), raw_path);
                        shouldAddModel = false;
                    }
                }
            }
            else {
                handler.postDelayed(runnable, 2000);
            }
        }
    }

    private void removeModel() {
        if (anchorNode != null) {
            anchorNode.getAnchor().detach();
            anchorNode.setParent(null);
            anchorNode = null;
            node.setRenderable(null);
            node = null;
            augmentedName = "";
        }

        infoLayout.setVisibility(View.GONE);
    }

    private void showInfo(String name, String[] info_text) {
        title.setText(name);
        String html = generateList(info_text);
        info.setText(Html.fromHtml(html));
        infoLayout.setVisibility(View.VISIBLE);
    }

    private String generateList(String[] arr) {
        String html = "<ol>";
        for (int i = 0; i < arr.length; i++) {
            html += "<li>" + arr[i] + "</li>";
        }
        html += "</ol>";
        return html;
    }

    private void placeObject(ArFragment arFragment, Anchor anchor, int source) {
        ModelRenderable.builder().setSource(arFragment.getContext(), source).build()
                .thenAccept(modelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable))
                .exceptionally(throwable -> {Log.i(  "Model error" , throwable.getMessage()); return null;});
    }

    private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable) {
        anchorNode = new AnchorNode(anchor);
        node = new Node();
        node.setRenderable(renderable);
        node.setParent(anchorNode);

        float scale = 0.1f;
        node.setLocalScale(new Vector3(scale, scale, scale));
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }

    public boolean setupAugmentedImagesDb(Config config, Session session) {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.img);
            AugmentedImageDatabase imageDatabase = AugmentedImageDatabase.deserialize(session, inputStream);
            config.setAugmentedImageDatabase(imageDatabase);
        }
        catch (IOException e) {
            Log.e("SetupAugImgDb" , "Could not open file");
            return false;
        }

        return true;
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }

        String openGlVersionString = ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }
}
