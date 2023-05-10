package naudio.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import naudio.ModInformation;
import naudio.audio.NAudioManager;
import naudio.utils.NResult;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);
        int type = reader.readInt();

        int id;
        String url;
        double sourceX;
        double sourceY;
        double sourceZ;

        switch (type) {
            case PacketTypes.PLAY:
                id = reader.readInt();
                url = reader.readUTF();
                sourceX = reader.readDouble();
                sourceY = reader.readDouble();
                sourceZ = reader.readDouble();
                NResult result = NAudioManager.getInstance().play(id, url, sourceX, sourceY, sourceZ);
                if (result.isError()) {
                    System.out.println(result.getError());
                }
                break;
            case PacketTypes.TOGGLE_PLAY:
                NAudioManager.getInstance().togglePlay(reader.readInt());
                break;
            case PacketTypes.SET_VOLUME:
                NAudioManager.getInstance().setVolume(reader.readInt(), reader.readFloat());
                break;
            case PacketTypes.SET_STATE:
                id = reader.readInt();
                url = reader.readUTF();
                sourceX = reader.readDouble();
                sourceY = reader.readDouble();
                sourceZ = reader.readDouble();
                float volume = reader.readFloat();
                boolean playing = reader.readBoolean();

                NAudioManager.getInstance().setState(id, url, sourceX, sourceY, sourceZ, volume, playing);
                break;
            case PacketTypes.ADD_PLAYER:
                NAudioManager.getInstance().addPlayer(reader.readInt());
                break;
            case PacketTypes.STOP:
                NAudioManager.getInstance().stop(reader.readInt());
                break;
        }
    }

    public static NResult sendPlayAudio(int id, String url, double sourceX, double sourceY, double sourceZ) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeInt(PacketTypes.PLAY);
            dataStream.writeInt(id);
            dataStream.writeUTF(url);
            dataStream.writeDouble(sourceX);
            dataStream.writeDouble(sourceY);
            dataStream.writeDouble(sourceZ);
        } catch (IOException e) {
            return NResult.error("Can't load play packet");
        }

        PacketDispatcher.sendPacketToAllAround(sourceX, sourceY, sourceZ, 64.0, 0, PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));
        return NResult.ok();
    }

    public static NResult sendTogglePlay(int id, double sourceX, double sourceY, double sourceZ) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeInt(PacketTypes.TOGGLE_PLAY);
            dataStream.writeInt(id);
        } catch (IOException e) {
            return NResult.error("Can't load togglePlay packet");
        }

        PacketDispatcher.sendPacketToAllAround(sourceX, sourceY, sourceZ, 64.0, 0, PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));
        return NResult.ok();
    }

    public static NResult sendVolume(int id, float volume, double sourceX, double sourceY, double sourceZ) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeInt(PacketTypes.SET_VOLUME);
            dataStream.writeInt(id);
            dataStream.writeFloat(volume);
        } catch (IOException e) {
            return NResult.error("Can't load togglePlay packet");
        }

        PacketDispatcher.sendPacketToAllAround(sourceX, sourceY, sourceZ, 64.0, 0, PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));
        return NResult.ok();
    }

    public static NResult sendStop(int id, double sourceX, double sourceY, double sourceZ) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeInt(PacketTypes.STOP);
            dataStream.writeInt(id);
        } catch (IOException e) {
            return NResult.error("Can't load togglePlay packet");
        }

        PacketDispatcher.sendPacketToAllAround(sourceX, sourceY, sourceZ, 64.0, 0, PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));
        return NResult.ok();
    }

    public static NResult sendUpdate(int id, String url, float volume, boolean playing, double sourceX, double sourceY, double sourceZ) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeInt(PacketTypes.SET_STATE);
            dataStream.writeInt(id);
            dataStream.writeUTF(url);
            dataStream.writeDouble(sourceX);
            dataStream.writeDouble(sourceY);
            dataStream.writeDouble(sourceZ);
            dataStream.writeFloat(volume);
            dataStream.writeBoolean(playing);
        } catch (IOException e) {
            return NResult.error("Can't load sendUpdate packet");
        }

        PacketDispatcher.sendPacketToAllAround(sourceX, sourceY, sourceZ, 64.0, 0, PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));


        return NResult.ok();
    }
}
