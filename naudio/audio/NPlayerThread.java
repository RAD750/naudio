package naudio.audio;

import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import naudio.messages.*;
import naudio.utils.NMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class NPlayerThread extends Thread {
    private final LinkedBlockingDeque<NMessage> channel;
    private float volume;
    private float ampl;
    private Bitstream stream;
    private AudioDevice audioDevice;
    private Decoder decoder;
    private EntityClientPlayerMP player;
    protected double sourceX, sourceY, sourceZ;
    protected boolean play;
    private final Minecraft minecraft;
    private boolean stop;
    public boolean started;

    public NPlayerThread(LinkedBlockingDeque<NMessage> channel) {
        this.channel = channel;
        this.volume = 1.0f;
        this.ampl = 1.0f;
        stream = null;
        play = false;
        minecraft = Minecraft.getMinecraft();
        stop = false;
        started = false;
        initAudio();
    }

    private void initAudio() {
        decoder = new Decoder();
        FactoryRegistry registry = FactoryRegistry.systemRegistry();
        try {
            audioDevice = registry.createAudioDevice();
            audioDevice.open(decoder);
        } catch (JavaLayerException e) {
            audioDevice = null;
            System.out.println("Can't create audio device");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                processMessages();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (stop) {
                break;
            }
            if (play && stream != null) {
                try {
                    Header header = stream.readFrame();
                    if (header != null) {
                        SampleBuffer buffer = (SampleBuffer) decoder.decodeFrame(header, stream);
                        processAudio(buffer.getBuffer(), buffer.getBufferLength());
                        audioDevice.write(buffer.getBuffer(), 0, buffer.getBufferLength());
                        stream.closeFrame();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processMessages() throws LineUnavailableException, InterruptedException {
        NMessage message;
        while ((message = channel.poll(10, TimeUnit.MILLISECONDS)) != null) {
            if (message instanceof NMessagePlayAudio) {
                if (stream != null) {
                    try {
                        stream.close();
                        audioDevice.flush();
                        audioDevice.close();
                        audioDevice = null;
                        initAudio();
                    } catch (BitstreamException e) {
                        e.printStackTrace();
                    }
                }

                if (player == null) {
                    player = minecraft.thePlayer;
                }

                stream = new Bitstream(((NMessagePlayAudio) message).getStream());
                sourceX = ((NMessagePlayAudio) message).getSourceX();
                sourceY = ((NMessagePlayAudio) message).getSourceY();
                sourceZ = ((NMessagePlayAudio) message).getSourceZ();
                play = true;
                started = true;
            } else if (message instanceof NMessageTogglePlay) {
                play = !play;
            } else if (message instanceof NMessageVolume) {
                volume = ((NMessageVolume) message).getVolume();
            } else if (message instanceof NMessageStop) {
                stop = true;
            } else if (message instanceof NMessageAmpl) {
                ampl = ((NMessageAmpl) message).getAmpl();
            }
        }
    }

    private void processAudio(short[] buffer, int len) {
        // TODO: add spatial audio
        // Player position
        double playerX = player.posX;
        double playerY = player.posY;
        double playerZ = player.posZ;
        double volume = minecraft.gameSettings.soundVolume;

        for (int i = 0; i < len; i++) {
            // distanced audio
            double distance = Math.pow(playerX - sourceX, 2) + Math.pow(playerY - sourceY, 2) + Math.pow(playerZ - sourceZ, 2);
            // max distance: 20 blocks (20^2 = 400)
            // min distance: 2 blocks (2^2 = 4)
            double lerpedDistance = NMath.inverseLerp(400 * this.ampl, 4 * this.ampl, distance);

            buffer[i] *= NMath.clamp(0, 1, this.volume * volume * lerpedDistance);
        }
    }
}
