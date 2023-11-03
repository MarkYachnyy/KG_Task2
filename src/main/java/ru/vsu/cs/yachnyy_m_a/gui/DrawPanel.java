package ru.vsu.cs.yachnyy_m_a.gui;

import ru.vsu.cs.yachnyy_m_a.graphics.PixelWriter;
import ru.vsu.cs.yachnyy_m_a.graphics.Rasterization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class DrawPanel extends JPanel {

    private BufferedImage canvas;
    private ArrayList<Point> points;
    private HashMap<Integer, Color> point_colors;

    private static final int PROXIMITY = 10;

    private int selected_point = -1;

    private Consumer<Color> selectPointCallback;

    public DrawPanel(Consumer<Color> pointSelected) {
        this.addComponentListener(sizeChangeListener);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseMotionListener);
        this.selectPointCallback = pointSelected;
        points = new ArrayList<>();
        point_colors = new HashMap<>();
        addPoint(1, 1, Color.RED);
        addPoint(400, 250, Color.GREEN);
        addPoint(200, 400, Color.BLUE);
    }

    public void addPoint(int x, int y, Color color) {
        points.add(new Point(x, y));
        point_colors.put(points.size() - 1, color);
    }

    public int getPointAt(int x, int y) {
        for (int i = 0; i < points.size(); i++) {
            Point point2D = points.get(i);
            if (x > point2D.getX() - PROXIMITY && x < point2D.getX() + PROXIMITY &&
                    y > point2D.getY() - PROXIMITY && y < point2D.getY() + PROXIMITY) {
                return i;
            }
        }
        return -1;
    }

    public void acceptColor(Color color) {
        if (selected_point >= 0) {
            point_colors.put(selected_point, color);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvas != null && this.getWidth() > 0 && this.getHeight() > 0) {
            fillCanvas(Color.WHITE);
            drawTheTriangle();
            ((Graphics2D) g).drawImage(canvas, null, null);
        }
    }


    private PixelWriter canvasPixelWriter = new PixelWriter() {
        @Override
        public void setRGB(int x, int y, Color color) {
            canvas.setRGB(x, y, color.getRGB());
        }
    };

    private ComponentAdapter sizeChangeListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            canvas = new BufferedImage(DrawPanel.this.getWidth(), DrawPanel.this.getHeight(), BufferedImage.TYPE_INT_RGB);
            fillCanvas(Color.WHITE);
            drawTheTriangle();
        }
    };

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int id = getPointAt(e.getX(), e.getY());
            selected_point = id;
            if (id >= 0) {
                selectPointCallback.accept(point_colors.get(id));
            } else {
                selectPointCallback.accept(null);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            selected_point = getPointAt(e.getX(), e.getY());
            selectPointCallback.accept(null);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            selected_point = -1;
            selectPointCallback.accept(null);
        }
    };

    private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (selected_point >= 0) {
                fillCanvas(Color.WHITE);
                int new_x = e.getX();
                int new_y = e.getY();
                if (new_x < canvas.getWidth() && new_x > 0) points.get(selected_point).x = e.getX();
                if (new_y < canvas.getHeight() && new_y > 0) points.get(selected_point).y = e.getY();
                DrawPanel.this.repaint();
            }
        }
    };

    private void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
    }

    private void drawTheTriangle() {
        int x1 = points.get(0).x;
        int x2 = points.get(1).x;
        int x3 = points.get(2).x;
        int y1 = points.get(0).y;
        int y2 = points.get(1).y;
        int y3 = points.get(2).y;
        Rasterization.fillTriangle(canvasPixelWriter, x1, y1, x2, y2, x3, y3, point_colors.get(0), point_colors.get(1), point_colors.get(2));
    }
}
