package tax.cute;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

import javax.imageio.ImageIO;

public class Util {
    public static int readVarInt(DataInputStream in) throws IOException {
        int a = 0;
        int b = 0;
        while (true) {
            int c = in.readByte();

            a |= (c & 0x7F) << b++ * 7;

            if (b > 5)
                throw new RuntimeException("VarInt too big");

            if ((c & 0x80) != 128)
                break;
        }
        return a;
    }
    
    // updated by: MrXiaoM
    public static BufferedImage base64ToImage(String base64) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(in);
        in.close();
        return image;
    }

    public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }

            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }
}
