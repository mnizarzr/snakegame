package pbo.tubes.mediaplayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer extends AudioPlayer {

    AudioInputStream audioInputStream;
    Clip             clip;

    {
        basePath += "/music/";
    }

    @Override
    public void play(String fileName) {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(basePath + fileName).getAbsoluteFile());
            clip             = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        clip.stop();
        clip.close();
    }

}
