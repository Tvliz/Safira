package cn.nukkit.network.player;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.DataPacket;

public abstract class PacketHandler<T extends DataPacket> {

    public abstract boolean handlePacket(Player player, T packet);

}
