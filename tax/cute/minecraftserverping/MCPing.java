package tax.cute.minecraftserverping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MCPing {
    private String version_name;
    private String version_protocol;
    private int max_players;
    private int online_players;
    private String description;
    private String favicon;
    private String type;
    private int mod_count;
    private int delay;
    private List<String> modList;

    // this constructor don't need to public
    private MCPing(
            String version_name,
            String version_protocol,
            int max_players,
            int online_players,
            String description,
            String favicon,
            String type,
            int mod_count,
            int delay,
            List<String> modList
    ) {
        this.version_name = version_name;
        this.version_protocol = version_protocol;
        this.max_players = max_players;
        this.online_players = online_players;
        this.description = description;
        this.favicon = favicon;
        this.type = type;
        this.mod_count = mod_count;
        this.modList = modList;
        this.delay = delay;
    }

    public static MCPing getMotd(String host, int port) throws IOException {
        return MCPing.getMotd(host, port, 5000);
    }

    public static MCPing getMotd(String host, int port, int timeout) throws IOException {
        // initialization
        List<String> modList = new ArrayList<>();
        JSONObject version_json;
        String description = "";
        String version_protocol = "null";
        String version_name = "null";
        String favicon = "null";
        String type = "null";
        JSONObject players_json;
        int max_players = -1;
        int online_players = -1;
        int mod_count = 0;

        // connection
        Socket socket = new Socket();

        String _encode_host = Punycode.encodeURL(host);
        long start = System.currentTimeMillis();
        socket.connect(new InetSocketAddress(_encode_host, port), timeout);
        int delay = Math.toIntExact(System.currentTimeMillis() - start);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(byteArray);

        handshake.writeByte(0x00);
        Util.writeVarInt(handshake, 4);
        Util.writeVarInt(handshake, host.length());
        handshake.writeBytes(host);
        handshake.writeShort(port);
        Util.writeVarInt(handshake, 1);

        Util.writeVarInt(out, byteArray.size());

        out.write(byteArray.toByteArray());
        out.writeByte(0x01);
        out.writeByte(0x00);

        Util.readVarInt(in);
        Util.readVarInt(in);

        int len = Util.readVarInt(in);
        byte[] bytes = new byte[len];
        in.readFully(bytes);

        // close
        out.flush();
        out.close();
        in.close();
        socket.close();

        String data = new String(bytes, "UTF-8"); // get json
        // json parsing
        JSONObject data_json = JSONObject.parseObject(data);

        if (data_json.containsKey("version")) {
            version_json = data_json.getJSONObject("version");
            if (version_json.get("protocol") instanceof Integer) {
                version_protocol = version_json.getString("protocol");
            }
            if (version_json.get("name") instanceof String) {
                version_name = version_json.getString("name");
            }
        }

        if (data_json.containsKey("players")) {
            if (data_json.get("players") instanceof JSONObject) {
                players_json = data_json.getJSONObject("players");
                if (players_json.get("max") instanceof Integer) {
                    max_players = players_json.getInteger("max");
                }

                if (players_json.get("online") instanceof Integer) {
                    online_players = players_json.getIntValue("online");
                }
            }
        }

        if (data_json.containsKey("favicon")) {
            if (data_json.get("favicon") instanceof String) {
                favicon = data_json.getString("favicon").split(",")[1];
            }
        }

        if (data_json.containsKey("modinfo")) {
            if (data_json.get("modinfo") instanceof JSONObject) {
                JSONObject modinfo_json = data_json.getJSONObject("modinfo");

                if (modinfo_json.get("type") instanceof String) {
                    type = modinfo_json.getString("type");
                }

                if (modinfo_json.get("modList") instanceof JSONArray) {
                    JSONArray modList_json = modinfo_json.getJSONArray("modList");
                    mod_count = modList_json.size();
                    for (int i = 0; i < modList_json.size(); i++) {
                        modList.add(modList_json.getJSONObject(i).getString("modid"));
                    }
                }
            }
        }

        try {
            if (data_json.containsKey("description")) {
                if (data_json.get("description") instanceof String) {
                    description = data_json.getString("description");
                } else if (data_json.get("description") instanceof JSONObject) {
                    JSONObject description_json = data_json.getJSONObject("description");
                    if (description_json.containsKey("text")) {
                        description = description_json.getString("text");
                    }
                    if (description_json.containsKey("extra")) {
                        JSONArray extra_array = description_json.getJSONArray("extra");
                        JSONObject text_json;
                        for (int i = 0; i < extra_array.size(); i++) {
                            text_json = extra_array.getJSONObject(i);
                            if (text_json.containsKey("text")) {
                                description += text_json.getString("text");
                            }
                        }
                    }

                }
            }
        } catch (NullPointerException e) {
            description = "null";
        }

        return new MCPing(
                version_name,
                version_protocol,
                max_players,
                online_players,
                description,
                favicon,
                type,
                mod_count,
                delay,
                modList
        );

    }

    public String getVersion_name() {
        return this.version_name;
    }

    public String getVersion_protocol() {
        return this.version_protocol;
    }

    public int getMax_players() {
        return this.max_players;
    }

    public int getOnline_players() {
        return this.online_players;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public String getType() {
        return this.type;
    }

    public int getMod_count() {
        return this.mod_count;
    }

    public List<String> getModList() {
        return this.modList;
    }

    public int getDelay() {
        return this.delay;
    }
}
