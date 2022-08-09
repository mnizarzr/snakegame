package pbo.tubes.mediaplayer;


import javazoom.jl.decoder.JavaLayerException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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
            } catch (IOException | JavaLayerException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}
