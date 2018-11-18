package com.centipede;

import javax.swing.*;
import java.awt.*;

public class CentipedeGame extends JFrame implements Commons {

    public CentipedeGame() {

        initUI();
    }

    private void initUI() {

        add(new Board());
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            CentipedeGame ex = new CentipedeGame();
            ex.setVisible(true);
        });
    }
}