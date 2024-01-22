package cn.nukkit.network.player;

import cn.nukkit.network.player.handler.LoginPacketHandler;
import cn.nukkit.network.protocol.LoginPacket;

public class PlayerPacketHandler {

    static PacketHandler<?>[] PACKET_HANDLERS = new PacketHandler[255];

    static {
        PACKET_HANDLERS[LoginPacket.NETWORK_ID] = new LoginPacketHandler();
    }

    public static PacketHandler<?> getPacketFromPid(int pid) {
        return PACKET_HANDLERS[pid & 0xFF];
    }

}
