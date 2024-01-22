package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.exception.InvalidLoginPacketException;
import cn.nukkit.utils.Zlib;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by on 15-10-13.
 */
@Log4j2
public class LoginPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.LOGIN_PACKET;

    public String username;

    @Getter
    public int protocol;

    public UUID clientUUID;

    public long clientId;

    public String identityPublicKey;

    public String serverAddress;

    public Skin skin;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        var gson = new Gson();

        this.protocol = this.getInt();

        byte[] str;

        try {
            str = Zlib.inflate(this.get(this.getInt()), 64 * 1024 * 1024);
        } catch (Exception e) {
            return;
        }

        this.setBuffer(str, 0);

        try {
            decodeChainData(gson);
            decodePlayerInfo(gson);
        } catch (Exception exception) {
            throw new InvalidLoginPacketException("The received login packet is invalid", exception);
        }
    }

    @Override
    public void encode() {

    }

    private void decodeChainData(Gson gson) {
        Map<String, List<String>> map = gson.fromJson(
                this.getLIntString(),
                new TypeToken<Map<String, List<String>>>() {}.getType());

        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) return;

        List<String> chains = map.get("chain");

        for (String chainData : chains) {
            JsonObject chainObject = decodeToken(gson, chainData);

            if (chainObject == null) continue;

            if (chainObject.has("extraData")) {
                JsonObject extra = chainObject.get("extraData").getAsJsonObject();

                if (extra.has("displayName"))
                    this.username = extra.get("displayName").getAsString();

                if (extra.has("identity"))
                    this.clientUUID = UUID.fromString(extra.get("identity").getAsString());
            }

            if (chainObject.has("identityPublicKey"))
                this.identityPublicKey = chainObject.get("identityPublicKey").getAsString();
        }

        if (this.username == null || this.identityPublicKey == null) {
            throw new RuntimeException("No username or no public key found in login packet");
        }
    }

    private void decodePlayerInfo(Gson gson) {
        JsonObject playerInfo = decodeToken(gson, new String(this.get(this.getLInt())));

        if (playerInfo == null)
            throw new RuntimeException("Invalid skin token");

        if (!playerInfo.has("ClientRandomId"))
            throw new RuntimeException("There's no ClientRandomId in the player info");

        if (!playerInfo.has("ServerAddress"))
            throw new RuntimeException("The player doesn't have a ServerAddress built in the info");

        if (!playerInfo.has("SkinId"))
            throw new RuntimeException("The player does not have a skin id");

        if (!playerInfo.has("SkinData"))
            throw new RuntimeException("The player does not have skin data.");

        var skinId = playerInfo.get("SkinId").getAsString();
        var skinData = playerInfo.get("SkinData").getAsString();

        this.skin = new Skin(skinData, skinId);

        this.serverAddress = playerInfo.get("ServerAddress").getAsString();
        this.clientId = playerInfo.get("ClientRandomId").getAsLong();
    }

    private JsonObject decodeToken(Gson gson, String token) {
        String[] base = token.split("\\.");

        if (base.length < 2)
            return null;

        return gson.fromJson(new String(
                Base64.getDecoder().decode(base[1]), StandardCharsets.UTF_8),
                JsonObject.class);
    }

    @Override
    public Skin getSkin() {
        return this.skin;
    }

}
