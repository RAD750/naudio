package naudio.messages;

public class NMessageVolume extends NMessage {
    private final float volume;

    public NMessageVolume(float volume) {
        this.volume = volume;
    }

    public float getVolume() {
        return volume;
    }
}
