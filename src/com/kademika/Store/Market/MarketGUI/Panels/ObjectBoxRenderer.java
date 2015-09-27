package com.kademika.Store.Market.MarketGUI.Panels;

import com.kademika.Store.Market.Objects.Animal;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dean on 8/17/15.
 */
public class ObjectBoxRenderer extends JLabel implements ListCellRenderer {

    public ObjectBoxRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        Animal anim = (Animal) value;
        setText(anim.getName());
        return this;
    }
}
