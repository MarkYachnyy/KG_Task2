package ru.vsu.cs.yachnyy_m_a;

import ru.vsu.cs.yachnyy_m_a.graphics.PixelWriter;
import ru.vsu.cs.yachnyy_m_a.graphics.Rasterization;
import ru.vsu.cs.yachnyy_m_a.graphics.SimplePixelWriter;
import ru.vsu.cs.yachnyy_m_a.gui.FormMain;
import ru.vsu.cs.yachnyy_m_a.util.SwingUtils;

import javax.print.DocFlavor;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        SwingUtils.setDefaultFont("Calibri", 20);
//        BufferedImage bufferedImage = new BufferedImage(1920, 1280, BufferedImage.TYPE_INT_RGB);
//        PixelWriter pixelWriter = new SimplePixelWriter(bufferedImage);
//        long t0 = System.currentTimeMillis();
//        for (int i = 0; i < 2500; i++) {
//            Rasterization.fillTriangle(pixelWriter, 10, 10, 20, 15, 15, 30, Color.RED, Color.GREEN, Color.BLUE);
//        }
//        System.out.println(System.currentTimeMillis() - t0);
        EventQueue.invokeLater(() -> {
            new FormMain().setVisible(true);
        });
    }
}
