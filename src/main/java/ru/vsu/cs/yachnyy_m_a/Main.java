package ru.vsu.cs.yachnyy_m_a;

import ru.vsu.cs.yachnyy_m_a.gui.FormMain;
import ru.vsu.cs.yachnyy_m_a.util.SwingUtils;

public class Main {
    public static void main(String[] args) {
        SwingUtils.setDefaultFont("Comic Sans MS", 20);
        java.awt.EventQueue.invokeLater(() -> {
            new FormMain().setVisible(true);
        });
    }
}
