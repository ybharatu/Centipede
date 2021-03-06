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
            if(i == num_segments - 1){
                segments.get(i).setType("tail");
            }
        }

    }

     /*********************************************************************************
     * Centipede gets hit
     *********************************************************************************/
    public int got_hit(int idx){

        segments.get(idx).got_hit();
        if(segments.get(idx).getLives() != 0){
            return 0;
        }
        if(segments.get(idx).type == "tail" ){
            if(segments.get(idx - 1).type != "head"){
                segments.get(idx - 1).setType("tail");
            }
            segments.removeElementAt(idx);
            num_segments--;
        }
        else if(idx == 0 && num_segments > 1){
            if(segments.get(idx + 1).type != "head"){
                segments.get(idx + 1).setType("head");
                segments.get(idx + 1).setDirection(segments.get(idx).getDirection() * -1);
                segments.get(idx + 1).UpdateImage();
            }
            segments.removeElementAt(idx);
            num_segments--;
        }
        else if(num_segments > 1){
            if(idx + 1 < num_segments){
                segments.get(idx + 1).setType("head");
                segments.get(idx + 1).setDirection(segments.get(idx).getDirection() * -1);
                segments.get(idx + 1).UpdateImage();
            }

            if(segments.get(idx - 1).type != "head"){
                segments.get(idx - 1).setType("tail");
            }

            segments.removeElementAt(idx);
            num_segments--;
        }else{
            segments.removeElementAt(idx);
            num_segments--;
        }
        return 1;
    }


     /*********************************************************************************
     * Centipede Movement Algorithm
     *********************************************************************************/
    public void act() {
        int prev_x = 0;
        int prev_y = 0;
        for(Segment segment: segments){
            //System.out.println(segments.indexOf(segment));
             /*********************************************************************************
             * Driving Logic for head
             *********************************************************************************/
            if(segment.type.equals("head")){
                prev_x = segment.getX();
                prev_y = segment.getY();
                 /*********************************************************************************
                 * If hitting the left border, go down
                 *********************************************************************************/
                if((segment.getX() <= BORDER_LEFT && segment.direction != RIGHT) && segment.getY() <= BOARD_HEIGHT - PLAYER_AREA){
                    segment.setY(segment.getY() + SEGMENT_HEIGHT);
                    segment.setDirection(RIGHT);
                    //System.out.println("Going Down because hit left border (" + segment.getX() + ", " + segment.getY() + ")");
                }
                 /*********************************************************************************
                 * If hitting the right border, go down
                 *********************************************************************************/
                else if((segment.getX() >= BOARD_WIDTH - BORDER_RIGHT && segment.direction != LEFT) && segment.getY() <= BOARD_HEIGHT - PLAYER_AREA){
                    segment.setY(segment.getY() + SEGMENT_HEIGHT);
                    segment.setDirection(LEFT);
                    //System.out.println("Going Down because hit right border (" + segment.getX() + ", " + segment.getY() + ")");
                }
                 /*********************************************************************************
                 * One unit to the left or right every call
                 *********************************************************************************/
                else{
                    segment.setX(segment.getX() + SEGMENT_WIDTH * segment.direction);
                }

                /*********************************************************************************
                 * If hitting the right mushroom, go down
                 *********************************************************************************/
                //System.out.println("X: " + segment.getX() / GRID_UNIT + " Y: " + segment.getY() / GRID_UNIT + " Mushroom Status = " + mushroom_grid[segment.getY() / GRID_UNIT][segment.getX() / GRID_UNIT]);
                if(segment.direction == RIGHT && mushroom_grid[segment.getY() / GRID_UNIT][segment.getX() / GRID_UNIT] == 1){
                    segment.setY(segment.getY() + SEGMENT_HEIGHT);
                    segment.setDirection(LEFT);
                    segment.setX(segment.getX() + SEGMENT_WIDTH * segment.direction);
                    //System.out.println("Hit a Mushroom");
                }
                /*********************************************************************************
                 * If hitting the left mushroom, go down
                 *********************************************************************************/
                if(segment.direction == LEFT && mushroom_grid[segment.getY() / GRID_UNIT][segment.getX() / GRID_UNIT] == 1){
                    segment.setY(segment.getY() + SEGMENT_HEIGHT);
                    segment.setDirection(RIGHT);
                    segment.setX(segment.getX() + SEGMENT_WIDTH * segment.direction);
                    //System.out.println("Going Down because hit right border (" + segment.getX() + ", " + segment.getY() + ")");
                }

                 /*********************************************************************************
                 * Prevent centipede from moving down if hitting the player area
                 *********************************************************************************/
                if(segment.getY() >= BOARD_HEIGHT - PLAYER_AREA){
                    segment.setY(BOARD_HEIGHT - PLAYER_AREA - SEGMENT_HEIGHT);
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
