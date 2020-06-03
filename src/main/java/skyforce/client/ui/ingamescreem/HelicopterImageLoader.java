package skyforce.client.ui.ingamescreem;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HelicopterImageLoader {
    public static ArrayList<BufferedImage> frames;

    private static int curFrame;

    public static void init() {
        frames = new ArrayList<>(3);
        frames.add(0, LoadImage.imageLoader("/me-1.png"));
        frames.add(1, LoadImage.imageLoader("/me-2.png"));
        frames.add(2, LoadImage.imageLoader("/me-3.png"));
        curFrame = 0;
    }

    public static BufferedImage getPlaneFrame() {
        BufferedImage frame = frames.get(curFrame);
        if (curFrame == 2) {
            curFrame = 0;
        } else {
            curFrame++;
        }
        return frame;
    }
}
