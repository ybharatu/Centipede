package com.centipede;

import javax.swing.*;
import java.awt.*;

public class Mushroom extends Sprite implements Commons {
    public int lives = 3;
    public boolean is_dead = false;
    public String mushImg = "src/images/mushroom_full_8x8.png";


    public Mushroom(int x, int y) {
        initMushroom(x, y);
    }

    public void initMushroom(int x, int y){
        this.x = x;
        this.y = y;

        ImageIcon ii;
        ii = new ImageIcon(mushImg);
        Image newImage = ii.getImage().getScaledInstance(MUSHROOM_WIDTH, MUSHROOM_HEIGHT, Image.SCALE_DEFAULT);
        setImage(newImage);
        getImageDimensions();
    }
}
