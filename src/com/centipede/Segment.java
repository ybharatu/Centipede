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
     * Sets type of segment
     *********************************************************************************/
    public void setType(String type){
        this.type = type;
    }

     /*********************************************************************************
     * Initializes Segment Values
     *********************************************************************************/
    private void initSegment(int x, int y, String type) {

        this.x = x;
        this.y = y;
        this.type = type;

        ImageIcon ii;

        if(type == "body" || type == "tail"){
            ii = new ImageIcon(SegmentImg);
            Image newImage = ii.getImage().getScaledInstance(SEGMENT_WIDTH, SEGMENT_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }
        else if(type == "head"){
            ii = new ImageIcon(HeadImg);
            Image newImage = ii.getImage().getScaledInstance(SEGMENT_WIDTH, SEGMENT_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }
        getImageDimensions();
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    public int getDirection(){
        return this.direction;
    }

    public void UpdateImage(){
        ImageIcon ii;
        if(type == "body" || type == "tail"){
            ii = new ImageIcon(SegmentImg);
            Image newImage = ii.getImage().getScaledInstance(SEGMENT_WIDTH, SEGMENT_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }
        else if(type == "head"){
            ii = new ImageIcon(HeadImg);
            Image newImage = ii.getImage().getScaledInstance(SEGMENT_WIDTH, SEGMENT_HEIGHT, Image.SCALE_DEFAULT);
            setImage(newImage);
        }
    }

}
