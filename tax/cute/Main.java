package tax.cute;

import java.io.*;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) throws IOException{
		// usage example
    	String host = "我的世界.games";
    	int port = 831;
    	System.out.println("Now pinging " + host + ":" + port);
        MCPing ping = MCPing.getMotd(host, port);
        System.out.println("ConnectionDelay:" + ping.getDelay() + "ms");
        System.out.println("Description:" + ping.getDescription());
        System.out.println("Version:" + ping.getVersion_name());
        System.out.println("Players:" + ping.getOnline_players() + "/" + ping.getMax_players());
        System.out.println("ModList:" + ping.getModList());
        System.out.println("ModCount:" + ping.getMod_count());
        System.out.println("Type:" + ping.getType());

        boolean saveFavicon = false; //You can choose whether to save the Favicon
        File saveSrc = new File("favicon.png");
        if (saveFavicon) ImageIO.write(Util.base64ToImage(ping.getFavicon()), "png", saveSrc);
    }
}