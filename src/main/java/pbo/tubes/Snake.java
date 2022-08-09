package pbo.tubes;

import pbo.tubes.mediaplayer.MusicPlayer;
import pbo.tubes.mediaplayer.SfxPlayer;

public class Snake {

    public static final SfxPlayer   sfxPlayer = new SfxPlayer();
    public static final MusicPlayer musicPlayer = new MusicPlayer();

    public static void main(String[] args) {
        new GameFrame();
    }

}