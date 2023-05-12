package naudio.messages;

public class NMessageAmpl extends NMessage {
    private final float ampl;

    public NMessageAmpl(float ampl) {
        this.ampl = ampl;
    }

    public float getAmpl() {
        return ampl;
    }
}
