package pbo.tubes.mediaplayer;

import javafx.scene.media.AudioClip;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class AudioPlayer {

    protected String basePath;
    protected String fileName;
    protected Player player;

    protected BufferedInputStream buffer;

    protected long    position;
    protected long    length;
    protected boolean isPlaying;



    {
        basePath = "./media";
        fileName = "";
    }

    public void play(String fileName) throws IOException, JavaLayerException, UnsupportedAudioFileException, LineUnavailableException {
        buffer = new BufferedInputStream(new FileInputStream(fileName));
        player = new Player(buffer);
        setPlaying(true);
        length = buffer.available();
        player.play();
        if (player.isComplete() && isPlaying) {
            length = 0;
            buffer.close();
            setPlaying(false);
        }
    }

    public void pause() throws IOException {
        if (isPlaying) {
            position = buffer.available();
            player.close();
            setPlaying(false);
        }
    }

    public void resume() throws IOException, JavaLayerException {
        if (!isPlaying) {
            new Thread(() -> {
                try {
                    buffer = new BufferedInputStream(new FileInputStream(fileName));
                    player = new Player(buffer);
                    length = buffer.available();
                    buffer.skip(length - position);
                    player.play();
                } catch (IOException | JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
            setPlaying(true);
        }
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
