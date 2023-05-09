package naudio.tileentities;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import naudio.audio.NPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class NAudioTE extends TileEntity implements IHostedPeripheral {
    private NPlayer nPlayer;
    private final String[] methods = new String[]{"play", "togglePlay", "setVolume", "getVolume", "isPlaying", "help", "sendUpdate"};

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void update() {
    }

    @Override
    public String getType() {
        return "NAudio";
    }

    @Override
    public String[] getMethodNames() {
        return methods;
    }

    @Override
    public Object[] callMethod(IComputerAccess p0, int p1, Object[] p2) throws Exception {
        List<Object> data = new ArrayList<Object>();
        data.add("ok ");
        if (p1 >= 0 && p1 < methods.length) {
            switch (p1) {
                case 0: // play
                    if (p2.length != 1 || !(p2[0] instanceof String)) {
                        return new Object[]{"error", "not valid call"};
                    }
                    nPlayer.play((String) p2[0]);
                    break;
                case 1: // togglePlay
                    if (p2.length != 0) {
                        return new Object[]{"error", "not valid call"};
                    }
                    nPlayer.togglePlay();
                    break;
                case 2: // setVolume
                    if (p2.length != 1 || !(p2[0] instanceof Float)) {
                        return new Object[]{"error", "not valid call"};
                    }
                    nPlayer.setVolume((Float) p2[0]);
                    break;
                case 3: // getVolume
                    if (p2.length != 0) {
                        return new Object[]{"error", "not valid call"};
                    }
                    data.add(nPlayer.getVolume());
                    break;
                case 4: // isPlaying
                    if (p2.length != 0) {
                        return new Object[]{"error", "not valid call"};
                    }
                    data.add(nPlayer.isPlaying());
                    break;
                case 5: // help
                    return new Object[]{"play(url) - plays a specific URL (must not redirect)\n" +
                            "togglePlay() - toggles the play function (aka stop/play)\n" +
                            "setVolume(volume) - sets the output volume, must be a float between 0.0 and 1.0\n" +
                            "getVolume() - returns the output volume\n" +
                            "isPlaying() - returns whether the player is playing (aka not stopped)\n" +
                            "sendUpdate() - sends update to all nearby clients who haven't connected yet (useful to web radios, have to be called once every some seconds if you're using this function)"};
                case 6: //sendUpdate
                    if (p2.length != 0) {
                        return new Object[]{"error", "not valid call"};
                    }
                    nPlayer.sendUpdate();
                    break;
            }
        } else {
            return new Object[]{"error", "function not found"};
        }
        return data.toArray();
    }

    @Override
    public boolean canAttachToSide(int p0) {
        return true;
    }

    @Override
    public void attach(IComputerAccess p0) {
        nPlayer = new NPlayer(this.xCoord, this.yCoord, this.zCoord, worldObj.isRemote);
        System.out.println("I'm alive");
    }

    @Override
    public void detach(IComputerAccess p0) {
        nPlayer.stop();
        nPlayer = null;
    }
}
