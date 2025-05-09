package org.example;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatCarbonIJTheme());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        JFrame jFrame = new JFrame("Mission Control");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(700, 500));
        Form form = new Form();
        jFrame.add(form, BorderLayout.PAGE_START);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}