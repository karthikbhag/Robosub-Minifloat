package org.example;

import javax.swing.*;
import java.awt.*;


/*
* This class is a JPanel that sets up the form
*   that the user inputs the information for the device in.
*   Use the Form's addComponent to add components that are needed in order to see output.
*
* */
public class Form extends JPanel implements  OutputDisplay{
    JTextField addressField;
    JButton submitButton;
    JTextArea outputField;

    public Form()
    {
        setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS) );
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel title = new JLabel("Mission Control");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton = new JButton("GET Request");
        /*
        TODO:
         Have separate buttons for however we want to get/send data to and from the ESP32?
         Like a button labeled Start that sends a post request to the esp's IP address with /start
         and then /data to get the data from it? Button could be labelled "Retrieve Data" or something
        */

        JLabel outputLabel = new JLabel("Output:");
        outputField = new JTextArea(16,32);
        outputField.setEditable(false);
        outputField.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(outputField);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        submitButton.addActionListener((event)->submitForm()); // Tells button to call the 'submitForm' method when it is clicked


        add(title);
        addressField = (JTextField) addComponent("IP Address:", new JTextField(12));

        add(submitButton);

        add(outputLabel);
        add(scroll);
    }

    // Used to have more use for this. Keep around just incase we need more fields
    Component addComponent(String label, Component component)
    {
        JPanel jPanel = new JPanel();
        JLabel jLabel = new JLabel(label);
        jPanel.add(jLabel);
        jPanel.add(component);
        add(jPanel);
        return component;
    }

    //  Everything here occurs when the form is submitted (or when the current submit button is pressed)
    private void submitForm()
    {
        // Add more validation logic. RegEx for valid IP? I tried using an inet thing to validate, but it said 123 was a valid IP (maybe it is)
        ConnectionManager connectionManager =  new ConnectionManager(this);
        connectionManager.makeConnection(addressField.getText());
    }

    // This is used to display data in the output text field
    @Override
    public void display(String data) {
        outputField.append(data + "\n");
    }
}
