package pbo.tubes.mediaplayer;


import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;

public class SfxPlayer extends AudioPlayer {

    {
        basePath += "/sfx/";
    }

    public SfxPlayer() {
        super();
    }

    public void play(SFX sfx) {
        new Thread(() -> {
            try {
                super.play(basePath + sfx.getFileName());
            } catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
