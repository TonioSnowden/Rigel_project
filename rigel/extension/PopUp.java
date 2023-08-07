package ch.epfl.rigel.extension;

import javax.swing.*;

public class PopUp extends JPopupMenu {
    JMenuItem anItem;

    public PopUp() {
        anItem = new JMenuItem("Click Me!");
        add(anItem);
    }
}