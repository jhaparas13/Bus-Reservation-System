package com.paras.BusReservation;

import com.paras.BusReservation.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
