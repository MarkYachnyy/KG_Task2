package ru.vsu.cs.yachnyy_m_a.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.function.Consumer;

public class FormMain extends JFrame {
    private JPanel PanelMain;
    private JSlider SliderRed;
    private JSlider SliderGreen;
    private JSlider SliderBlue;
    private JPanel DrawPanelContainer;
    private JLabel LabelRed;
    private JLabel LabelGreen;
    private JLabel LabelBlue;
    private JSpinner SpinnerX;
    private JSpinner SpinnerY;

    public FormMain() {
        this.setTitle("Task2");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(PanelMain);
        LabelRed.setForeground(Color.RED);
        LabelGreen.setForeground(Color.GREEN);
        LabelBlue.setForeground(Color.BLUE);
        SliderRed.setMinimum(0);
        SliderRed.setMaximum(255);
        SliderGreen.setMinimum(0);
        SliderGreen.setMaximum(255);
        SliderBlue.setMinimum(0);
        SliderBlue.setMaximum(255);
        Consumer<Color> colorConsumer = color -> {
            if (color == null) {
                LabelRed.setText("---");
                LabelGreen.setText("---");
                LabelBlue.setText("---");
                SliderRed.setEnabled(false);
                SliderGreen.setEnabled(false);
                SliderBlue.setEnabled(false);
            } else {
                SliderRed.setEnabled(true);
                SliderGreen.setEnabled(true);
                SliderBlue.setEnabled(true);
                SliderRed.setValue(color.getRed());
                SliderBlue.setValue(color.getBlue());
                SliderGreen.setValue(color.getGreen());
                LabelRed.setText(String.valueOf(SliderRed.getValue()));
                LabelGreen.setText(String.valueOf(SliderGreen.getValue()));
                LabelBlue.setText(String.valueOf(SliderBlue.getValue()));
            }
        };
        Consumer<Point> pointConsumer = point -> {
            if (point == null) {
                SpinnerX.setEnabled(false);
                SpinnerY.setEnabled(false);
            } else {
                SpinnerX.setEnabled(true);
                SpinnerY.setEnabled(true);
                SpinnerX.setValue(point.x);
                SpinnerY.setValue(point.y);
            }
        };
        DrawPanel drawPanel = new DrawPanel(colorConsumer, pointConsumer);
        DrawPanelContainer.add(drawPanel);
        colorConsumer.accept(null);
        pointConsumer.accept(null);
        ChangeListener SliderChangeListener = e -> {
            LabelRed.setText(String.valueOf(SliderRed.getValue()));
            LabelGreen.setText(String.valueOf(SliderGreen.getValue()));
            LabelBlue.setText(String.valueOf(SliderBlue.getValue()));
            drawPanel.acceptColor(new Color(SliderRed.getValue(), SliderGreen.getValue(), SliderBlue.getValue()));
        };

        ChangeListener SpinnerChangeListener = e -> {
            drawPanel.acceptPoint(new Point((Integer) SpinnerX.getValue(), (Integer) SpinnerY.getValue()));
        };
        SliderRed.addChangeListener(SliderChangeListener);
        SliderGreen.addChangeListener(SliderChangeListener);
        SliderBlue.addChangeListener(SliderChangeListener);
        SpinnerX.addChangeListener(SpinnerChangeListener);
        SpinnerY.addChangeListener(SpinnerChangeListener);

        this.pack();
    }
}
