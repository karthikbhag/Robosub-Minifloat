package org.example;

/*
This is an interface. Basically it says that whatever implements this needs to have a way to display the output
    The Form class implements this. It just sends the data to be displayed in the TextArea.
    We use an interface here just in case we want to change how the information is displayed
 */
public interface OutputDisplay {
    void display(String data);
}
