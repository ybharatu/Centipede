package com.centipede;

import javax.swing.*;
import java.awt.*;

public class Mushroom extends Sprite implements Commons {
    public int lives = 3;
    public boolean is_dead = false;
    public String mushImg = "src/images/mushroom_full_8x8.png";
    public String mushImg_hit_1 = "src/images/mushroom_hit_1_8x8.png";
    public String mushImg_hit_2 = "src/images/mushroom_hit_2_8x8.png";



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

    public int getLives(){
        return this.lives;
    }

    public void got_hit(){
        lives--;
        //System.out.println("Number of lives (Mushroom): " + lives);
        if(lives == 2){
            ImageIcon ii;
            ii = new ImageIcon(mushImg_hit_1);
            Image newImage = ii.getImage().getScaledInstance(MUSHROOM_WIDTH, MUSHROOM_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }else if(lives == 1){
            ImageIcon ii;
            ii = new ImageIcon(mushImg_hit_2);
            Image newImage = ii.getImage().getScaledInstance(MUSHROOM_WIDTH, MUSHROOM_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }
    }

    public void updateImage(){
        if(lives == 2){
            ImageIcon ii;
            ii = new ImageIcon(mushImg_hit_1);
            Image newImage = ii.getImage().getScaledInstance(MUSHROOM_WIDTH, MUSHROOM_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }else if(lives == 1){
            ImageIcon ii;
            ii = new ImageIcon(mushImg_hit_2);
            Image newImage = ii.getImage().getScaledInstance(MUSHROOM_WIDTH, MUSHROOM_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }else if(lives == 3){
            ImageIcon ii;
            ii = new ImageIcon(mushImg);
            Image newImage = ii.getImage().getScaledInstance(MUSHROOM_WIDTH, MUSHROOM_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }
    }
}
