package naudio.audio;

import naudio.network.PacketHandler;
import naudio.utils.NResult;

public class NPlayer {
    private static int ID = 0;

    private final int id;
    private float volume;
    private final double sourceX, sourceY, sourceZ;
    private boolean play;
    private final boolean isServer;

    public NPlayer(double sourceX, double sourceY, double sourceZ, boolean isRemote) {
        this.id = ID++;
        this.volume = 1.0f;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
        this.play = false;
        this.isServer = !isRemote;
    }

    public void play(String urlString) {
        if (isServer) {
            if (this.play) togglePlay();
            NResult result = PacketHandler.sendPlayAudio(this.id, urlString, sourceX, sourceY, sourceZ);
            if (result.isError())
                System.out.println(result.getError());
            play = true;
        }
    }

    public void togglePlay() {
        if (isServer) {
            NResult result = PacketHandler.sendTogglePlay(id, sourceX, sourceY, sourceZ);
            if (result.isError()) {
                System.out.println(result.getError());
            }
            this.play = !this.play;
        }
    }

    public void setVolume(float volume) {
        if (isServer) {
            NResult result = PacketHandler.sendVolume(id, volume, sourceX, sourceY, sourceZ);
            if (result.isError()) {
                System.out.println(result.getError());
            }
            this.volume = volume;
        }
    }

    public void stop() {
        if (isServer) {
            NResult result = PacketHandler.sendStop(id, sourceX, sourceY, sourceZ);
            if (result.isError()) {
                System.out.println(result.getError());
            }
            return;
        }

        if (this.play) {
            togglePlay();
        }
        NAudioManager.getInstance().stop(id);
    }

    public float getVolume() {
        return this.volume;
    }

    public boolean isPlaying() {
        return this.play;
    }
}
