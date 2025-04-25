package org.example;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;


/*
* Creates the JFrame that holds our form as well as sets values for that frame.
*   This file probably doesn't need to be edited further.
*
*   Usage:  So far when you run the program you will see an input field for 'IP Address'.
*           You can enter an IP address, or you can enter the URL for a webpage (to test).
*           When you click the button it currently makes a GET request to the IP Address/URL
*           and should return the page's HTML. If the GUI locks up or freezes it is probably
*           because it is trying to write a lot of text to the display. We could fix this, but
*           for what we will be using it for that doesn't seem necessary.
* */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatCarbonIJTheme());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        JFrame jFrame = new JFrame("Mission Control");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(700,500));
        Form form = new Form();
        jFrame.add(form, BorderLayout.PAGE_START);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}