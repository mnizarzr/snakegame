package pbo.tubes.mediaplayer;

public enum SFX {
    Eat("snake_eat.mp3"),
    Collided("snake_collided.mp3");
    private final String fileName;

    SFX(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
