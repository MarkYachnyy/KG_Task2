package ru.vsu.cs.yachnyy_m_a.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SimplePixelWriter implements PixelWriter {

    private BufferedImage bufferedImage;

    public SimplePixelWriter(BufferedImage image) {
        this.bufferedImage = image;
    }

    @Override
    public void setRGB(int x, int y, Color color) {
        bufferedImage.setRGB(x, y, color.getRGB());
    }
}
