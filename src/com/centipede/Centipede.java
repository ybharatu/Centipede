package com.centipede;

import java.util.Vector;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Centipede extends Sprite implements Commons {
    public Vector<Segment> segments = new Vector(5,2);
    int num_segments = 5;

    public Centipede(int num_segments, int x, int y) {

        initCentipede(num_segments, x, y);
    }

    public void initCentipede(int num_segments, int x, int y){
        this.num_segments = num_segments;

        segments.addElement(new Segment(x,y, "head"));
        System.out.println("Head X: " + x + " Y: " + y);
        x += SEGMENT_WIDTH;

        for(int i = 1; i < num_segments; i++){
            System.out.println("Body X: " + x + " Y: " + y);
            segments.addElement(new Segment(x,y,"body"));
            x += SEGMENT_WIDTH;
        }

    }
}
