package org.example;

import javax.swing.*;
import java.awt.*;

public class Form extends JPanel implements OutputDisplay {
    private final JTextField addressField;
    private final JButton getButton;
    private final JButton postButton;
    private final JButton inflateButton;
    private final JButton deflateButton;
    private final JTextArea outputField;
    private final ConnectionManager connectionManager;

    public Form() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Mission Control");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        addressField = (JTextField) addComponent("IP Address:", new JTextField(12));

        getButton = new JButton("Send GET Request");
        postButton = new JButton("Send POST Request");
        inflateButton = new JButton("Inflate");
        deflateButton = new JButton("Deflate");

        getButton.addActionListener(e -> sendGet());
        postButton.addActionListener(e -> sendPost());
        inflateButton.addActionListener(e -> sendInflate());
        deflateButton.addActionListener(e -> sendDeflate());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(getButton);
        buttonPanel.add(postButton);
        buttonPanel.add(inflateButton);
        buttonPanel.add(deflateButton);

        JLabel outputLabel = new JLabel("Output:");
        outputField = new JTextArea(16, 32);
        outputField.setEditable(false);
        outputField.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(outputField);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(title);
        add(buttonPanel);
        add(outputLabel);
        add(scroll);

        connectionManager = new ConnectionManager(this);
    }

    private Component addComponent(String label, Component component) {
        JPanel panel = new JPanel();
        JLabel jLabel = new JLabel(label);
        panel.add(jLabel);
        panel.add(component);
        add(panel);
        return component;
    }

    private void sendGet() {
        connectionManager.makeGetConnection(addressField.getText());
    }

    private void sendPost() {
        connectionManager.makePostConnection(addressField.getText() + "/post", "{}");
    }

    private void sendInflate() {
        connectionManager.makePostConnection(addressField.getText() + "/post", "{"action": "inflate"}");
    }

    private void sendDeflate() {
        connectionManager.makePostConnection(addressField.getText() + "/post", "{"action": "deflate"}");
    }

    @Override
    public void display(String data) {
        outputField.append(data + "\n");
        outputField.setCaretPosition(outputField.getDocument().getLength());
    }
}