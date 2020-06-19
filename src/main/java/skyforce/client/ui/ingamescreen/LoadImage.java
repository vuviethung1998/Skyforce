package skyforce.client.ui.ingamescreen;

import skyforce.client.Client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoadImage {
    public static BufferedImage image;
    public static BufferedImage entities;
    public static BufferedImage bullet;
    public static BufferedImage enemy, player, animated;

    public static void init() {
        image = imageLoader(Client.getIngameBackground());
        bullet = imageLoader("/bullet.png");
       entities = imageLoader("/airplane.png");
       animated = imageLoader("/plane-animated-1.png");
       enemy = entities.getSubimage(0, 0,85, 90);
       player = entities.getSubimage(85, 0, 95, 90);
    }

    public static BufferedImage imageLoader(String path) {
        try {
            return ImageIO.read(LoadImage.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
