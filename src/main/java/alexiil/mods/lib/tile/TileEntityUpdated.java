package alexiil.mods.lib.tile;

import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import alexiil.mods.lib.AlexIILMod;
import alexiil.mods.lib.net.INetworkTile;
import alexiil.mods.lib.net.MessageUpdate;

/** @param <M>
 *            The message type that is used for this tile entity */
public abstract class TileEntityUpdated<M extends MessageUpdate<?, ?>> extends TileEntityBasic implements INetworkTile<M> {
    private int updated;
    public final AlexIILMod mod;
    private TargetPoint point = null;

    public TileEntityUpdated(AlexIILMod mod) {
        this.mod = mod;
    }

    @Override
    public void onTick() {
        updated++;
        if (updated >= mod.netRate.getInt()) {
            updated = 0;
            if (needsNetworkUpdate())
                sendUpdatePacket();
        }
    }

    protected void resetTargetPoint() {
        point = null;
    }

    private TargetPoint targetPoint() {
        if (point == null)
            point = new TargetPoint(worldObj.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), mod.netDistance.getInt());
        return point;
    }

    public void sendUpdatePacket() {
        mod.provider.get().sendToAllAround(getCustomUpdateMessage(), targetPoint());
    }

    public boolean needsNetworkUpdate() {
        return true;
    }
}
