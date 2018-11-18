package com.centipede;

import java.util.Vector;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Segment extends Sprite implements Commons{
    public String type = "body";
    private final String SegmentImg = "src/images/centipede_body_left_16x8.png";
    public String HeadImg = "src/images/centipede_head_left_16x8.png";
    public int direction = LEFT;

     /*********************************************************************************
     * Segment Constructor
     *********************************************************************************/
    public Segment(int x, int y, String type) {

        initSegment(x, y, type);
    }

     /*********************************************************************************
     * Initializes Segment Values
     *********************************************************************************/
    private void initSegment(int x, int y, String type) {

        this.x = x;
        this.y = y;
        this.type = type;

        ImageIcon ii;

        if(type == "body"){
            ii = new ImageIcon(SegmentImg);
            setImage(ii.getImage());
        }
        else if(type == "head"){
            ii = new ImageIcon(HeadImg);
            setImage(ii.getImage());
        }
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    public int getDirection(){
        return this.direction;
    }

}
