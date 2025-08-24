package proceduralrails.render;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageUtils {
    static BufferedImage randomTexture(BufferedImage img, Shape shape, long seed) {
        Random rng = new Random(seed);
        Rectangle bounds = shape.getBounds();

        int sx = rng.nextInt(img.getWidth() - bounds.width);
        int sy = rng.nextInt(img.getHeight() - bounds.height);

        return img.getSubimage(sx, sy, bounds.width, bounds.height);
    }

    static void fillShape(Graphics2D g2d, Shape shape, BufferedImage texture) {
        Rectangle bounds = shape.getBounds();
        g2d.setClip(shape);
        g2d.drawImage(texture, bounds.x, bounds.y, null);
        g2d.setClip(null);
    }
}
