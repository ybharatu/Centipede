package com.centipede;

import javax.swing.*;
import java.awt.*;

public class Spider extends Sprite implements Commons{
    public int lives = 2;
    public int dx = 2;
    public int dy = 2;

    public String spiderImg = "src/images/spider_16x8.png";

    public Spider(int x, int y) {
        initSpider(x, y);
    }

    public void initSpider(int x, int y){
        this.x = x;
        this.y = y;

        ImageIcon ii;
        ii = new ImageIcon(spiderImg);
        Image newImage = ii.getImage().getScaledInstance(SPIDER_WIDTH, SPIDER_HEIGHT, Image.SCALE_DEFAULT);
        setImage(newImage);
        getImageDimensions();
    }

    public void act(){
        x += dx;
        y += dy;

        if( x <= BORDER_LEFT){
            dx = 2;
            x += dx;
            //System.out.println("Spider hit left border and is going right");
        }

        if( x >= BOARD_WIDTH - BORDER_RIGHT){
            dx = -2;
            x += dx;
            //System.out.println("Spider hit right border and is going left");
        }

        if( y <= BOARD_HEIGHT - PLAYER_AREA){
            dy = 2;
            y += dy;
            //System.out.println("Spider hit top border and is going down");
        }

        if( y >= BOARD_HEIGHT - 50){
            dy = -2;
            y += dy;
            //System.out.println("Spider hit bottom border and is going up");
        }
    }

    public void got_hit(){
        lives--;
        System.out.println("Hit Spider");
    }

    public int getLives(){
        return lives;
    }
}
