package naudio.handler;

import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.IPeripheralHandler;
import naudio.tileentities.NAudioTE;
import net.minecraft.tileentity.TileEntity;

public class NAudioHandler implements IPeripheralHandler {
    @Override
    public IHostedPeripheral getPeripheral(TileEntity p0) {
        if (p0 instanceof NAudioTE) {
            return (IHostedPeripheral) p0;
        }
        return null;
    }
}
