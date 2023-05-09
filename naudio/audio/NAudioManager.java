package naudio.audio;

import naudio.messages.*;
import naudio.utils.NData;
import naudio.utils.NResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

public class NAudioManager {
    private final HashMap<Integer, NPlayerThread> players;
    private final HashMap<Integer, LinkedBlockingDeque<NMessage>> channels;
    private final ConcurrentLinkedQueue<NData> data;
    private final ReentrantLock lock;
    private final Thread processor;

    private static NAudioManager instance;

    public static NAudioManager getInstance() {
        return instance;
    }

    public static void init() {
        instance = new NAudioManager();
    }

    public NAudioManager() {
        this.players = new HashMap<Integer, NPlayerThread>();
        this.channels = new HashMap<Integer, LinkedBlockingDeque<NMessage>>();
        this.data = new ConcurrentLinkedQueue<NData>();
        this.lock = new ReentrantLock();
        this.processor = new Thread(new Runnable() {
            @Override
            public void run() {
                Minecraft minecraft = Minecraft.getMinecraft();
                while (minecraft.running) {
                    EntityClientPlayerMP player = minecraft.thePlayer;
                    if (player != null) {
                        try {
                            lock.lock();
                            ArrayList<Integer> ids = new ArrayList<Integer>(8);
                            for (int id : players.keySet()) {
                                NPlayerThread playerThread = players.get(id);
                                double distance = player.getDistance(playerThread.sourceX, playerThread.sourceY, playerThread.sourceZ);
                                if (distance > 64.0 && playerThread.play) {
                                    ids.add(id);
                                }
                            }

                            for (int id : ids) {
                                unsafeStop(id);
                            }
                        } finally {
                            lock.unlock();
                        }
                    }

                    NData nData;
                    while ((nData = data.poll()) != null) {
                        try {
                            URL url = new URL(nData.getUrlString());
                            InputStream stream = new BufferedInputStream(url.openStream());
                            unsafeSendMessage(nData.getId(), new NMessagePlayAudio(stream, nData.getSourceX(), nData.getSourceY(), nData.getSourceZ()));
                        } catch (MalformedURLException ignored) {

                        } catch (IOException ignored) {
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        this.processor.start();
    }

    private void unsafeAddPlayer(int id) {
        LinkedBlockingDeque<NMessage> channel = new LinkedBlockingDeque<NMessage>();
        NPlayerThread playerThread = new NPlayerThread(channel);
        playerThread.start();
        players.put(id, playerThread);
        channels.put(id, channel);
    }

    public void addPlayer(int id) {
        try {
            lock.lock();
            if (!players.containsKey(id)) {
                unsafeAddPlayer(id);
            }
        } finally {
            lock.unlock();
        }
    }

    public void togglePlay(int id) {
        sendMessage(id, new NMessageTogglePlay());
    }

    public void setVolume(int id, float volume) {
        sendMessage(id, new NMessageVolume(volume));
    }

    public NResult play(int id, String urlString, double sourceX, double sourceY, double sourceZ) {
        try {
            lock.lock();
            // Add a player if it doesn't exist
            if (!this.channels.containsKey(id)) {
                this.unsafeAddPlayer(id);
            }

            // Add data to be processed
            data.add(new NData(id, urlString, sourceX, sourceY, sourceZ));
        } finally {
            lock.unlock();
        }

        return NResult.ok();
    }

    private void unsafeStop(int id) {
        unsafeSendMessage(id, new NMessageStop());
        this.players.remove(id);
        this.channels.remove(id);
    }

    public void stop(int id) {
        if (this.channels.containsKey(id)) unsafeStop(id);
    }

    public void setState(int id, String urlString, double sourceX, double sourceY, double sourceZ, float volume, boolean playing) {
        this.play(id, urlString, sourceX, sourceY, sourceZ);
        this.setVolume(id, volume);
        if (!playing) this.togglePlay(id);
    }

    private void unsafeSendMessage(int id, NMessage message) {
        try {
            lock.lock();
            channels.get(id).add(message);
        } finally {
            lock.unlock();
        }
    }

    private void sendMessage(int id, NMessage message) {
        if (channels.containsKey(id)) {
            unsafeSendMessage(id, message);
        }
    }

    private void unsafeBroadcastMessage(List<Integer> ids, NMessage message) {
        for (int id : ids) {
            unsafeSendMessage(id, message);
        }
    }

    private void broadcastMessage(List<Integer> ids, NMessage message) {
        for (int id : ids) {
            sendMessage(id, message);
        }
    }

    private void broadcastAllMessage(NMessage message) {
        for (int id : this.channels.keySet()) {
            unsafeSendMessage(id, message);
        }
    }
}
