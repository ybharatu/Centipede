package com.centipede;

import java.util.Vector;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Centipede extends Sprite implements Commons {
    public Vector<Segment> segments = new Vector(5,2);
    int num_segments = 5;

     /*********************************************************************************
     * Centipede Constructor
     *********************************************************************************/
    public Centipede(int num_segments, int x, int y) {

        initCentipede(num_segments, x, y);
    }

     /*********************************************************************************
     * Initializes Centipede Values
     *********************************************************************************/
    public void initCentipede(int num_segments, int x, int y){
        this.num_segments = num_segments;

        segments.addElement(new Segment(x,y, "head"));
        x += SEGMENT_WIDTH;

        for(int i = 1; i < num_segments; i++){
            segments.addElement(new Segment(x,y,"body"));
            x += SEGMENT_WIDTH;
        }

    }

     /*********************************************************************************
     * Centipede Movement Algorithm
     *********************************************************************************/
    public void act() {
        int prev_x = 0;
        int prev_y = 0;
        for(Segment segment: segments){
            System.out.println(segments.indexOf(segment));
             /*********************************************************************************
             * Driving Logic for head
             *********************************************************************************/
            if(segment.type.equals("head")){
                prev_x = segment.getX();
                prev_y = segment.getY();
                 /*********************************************************************************
                 * If hitting the left border, go down
                 *********************************************************************************/
                if(segment.getX() <= BORDER_LEFT && segment.direction != RIGHT){

                    segment.setY(segment.getY() + SEGMENT_HEIGHT);
                    segment.setDirection(RIGHT);
                    //System.out.println("Going Down because hit left border (" + segment.getX() + ", " + segment.getY() + ")");
                }
                 /*********************************************************************************
                 * If hitting the right border, go down
                 *********************************************************************************/
                else if(segment.getX() >= BOARD_WIDTH - BORDER_RIGHT && segment.direction != LEFT){
                    segment.setY(segment.getY() + SEGMENT_HEIGHT);
                    segment.setDirection(LEFT);
                    //System.out.println("Going Down because hit right border (" + segment.getX() + ", " + segment.getY() + ")");
                }
                 /*********************************************************************************
                 * One unit to the left or right every call
                 *********************************************************************************/
                else{
                    segment.setX(segment.getX() + SEGMENT_WIDTH * segment.direction);
//                    if(segment.direction == LEFT){
//                        System.out.println("Going LEFT X: " + segment.getX());
//                    }
//                    else if(segment.direction == RIGHT){
//                        System.out.println("Going RIGHT X: " + segment.getX());
//                    }
//                    else{
//                        System.out.println("Going Somewhere?");
//                    }
                }

            }
            else{
                int temp_x = prev_x;
                int temp_y = prev_y;
                prev_x = segment.getX();
                prev_y = segment.getY();
                int seg_idx = segments.indexOf(segment);
                segment.setX(temp_x);
                segment.setY(temp_y);
                segment.setDirection(segments.get(seg_idx - 1).getDirection());
            }
        }

    }
}
