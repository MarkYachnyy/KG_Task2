package ru.vsu.cs.yachnyy_m_a;

import ru.vsu.cs.yachnyy_m_a.graphics.PixelWriter;
import ru.vsu.cs.yachnyy_m_a.graphics.Rasterization;
import ru.vsu.cs.yachnyy_m_a.gui.FormMain;
import ru.vsu.cs.yachnyy_m_a.util.SwingUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        SwingUtils.setDefaultFont("Calibri", 20);
        EventQueue.invokeLater(() -> {
            new FormMain().setVisible(true);
        });
    }
}
