package com.puo.fireman_app;


import android.util.Log;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class ARFragment extends ArFragment {

  @Override
  protected Config getSessionConfiguration(Session session) {
    getPlaneDiscoveryController().setInstructionView(null);
    getPlaneDiscoveryController().hide();
    Config config = new Config(session);
    config.setFocusMode(Config.FocusMode.AUTO);
    config.setUpdateMode(Config.UpdateMode.BLOCKING);
    session.configure(config);
    getArSceneView().setupSession(session);

    if ((((ARActivity) getActivity()).setupAugmentedImagesDb(config, session))) {
      Log.d("SetupAugImgDb", "Success");
    } else {
      Log.e("SetupAugImgDb","Faliure setting up db");
    }

    return config;
  }

  public ARFragment() {
    // Required empty public constructor
  }

}
