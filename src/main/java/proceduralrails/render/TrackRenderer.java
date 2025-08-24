package proceduralrails.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TrackRenderer {
    private BufferedImage stoneImage;
    private BufferedImage sleeperImage;
    private BufferedImage railImage;

    private Stroke sleeperBorder = new BasicStroke(2f);
    private Stroke railBorder = new BasicStroke(2f);

    public TrackRenderer() {
        try {
            sleeperImage = ImageIO.read(getClass().getResource("/WOOD.jpg"));
            railImage = ImageIO.read(getClass().getResource("/METAL.jpg"));
            stoneImage = ImageIO.read(getClass().getResource("/STONE.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderSleeper(Graphics2D g2d, Shape sleeper, long seed) {
        BufferedImage tile = ImageUtils.randomTexture(sleeperImage, sleeper, seed);
        ImageUtils.fillShape(g2d, sleeper, tile);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(sleeperBorder);
        g2d.draw(sleeper);
    }

    public void renderRail(Graphics2D g2d, Shape rail, long seed) {
        BufferedImage tile = ImageUtils.randomTexture(railImage, rail, seed);
        ImageUtils.fillShape(g2d, rail, tile);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(railBorder);
        g2d.draw(rail);
    }

    public void renderTrackbed(Graphics2D g2d, Shape trackbed, long seed) {
        BufferedImage tile = ImageUtils.randomTexture(stoneImage, trackbed, seed);
        ImageUtils.fillShape(g2d, trackbed, tile);
    }
}
