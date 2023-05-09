package naudio.messages;

import java.io.InputStream;

public class NMessagePlayAudio extends NMessage {
    private final InputStream stream;
    private final double sourceX, sourceY, sourceZ;

    public NMessagePlayAudio(InputStream stream, double sourceX, double sourceY, double sourceZ) {
        this.stream = stream;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
    }

    public InputStream getStream() {
        return stream;
    }

    public double getSourceX() {
        return sourceX;
    }

    public double getSourceY() {
        return sourceY;
    }

    public double getSourceZ() {
        return sourceZ;
    }
}
