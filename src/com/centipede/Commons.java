package com.centipede;

public interface Commons {

    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 600;
    public static final int BOMB_HEIGHT = 5;
    public static final int ALIEN_HEIGHT = 10;
    public static final int ALIEN_WIDTH = 10;
    public static final int BORDER_RIGHT = 40;
    public static final int BORDER_LEFT = 5;
    public static final int GO_DOWN = 15;
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    public static final int CHANCE = 5;
    public static final int DELAY = 17;
    public static final int PLAYER_WIDTH = 15;
    public static final int PLAYER_HEIGHT = 10;
    public static final int NUMBER_SEGMENTS = 10;
    public static final int SEGMENT_WIDTH = 20;
    public static final int SEGMENT_HEIGHT = 20;
    public static final int CENTIPEDE_INIT_X = BOARD_WIDTH / 2;
    public static final int CENTIPEDE_INIT_Y = SEGMENT_HEIGHT + 20;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final int INIT_TIME = 0;
    public static final int CENTIPEDE_SPEED = 200;
    public static final int PLAYER_AREA = 100;
    public static final int GROUND = BOARD_HEIGHT - PLAYER_AREA;
    public static final int MUSHROOM_WIDTH = 20;
    public static final int MUSHROOM_HEIGHT = 20;
    public static final int GRID_UNIT = 20;
    public static final int MUCHROOM_CHANCE = 10;
    public static final int [][] mushroom_grid = new int [BOARD_HEIGHT / GRID_UNIT][BOARD_WIDTH / GRID_UNIT];
    public static final int SPIDER_HEIGHT = 20;
    public static final int SPIDER_WIDTH = 20;
    public static final int SPIDER_INIT_X = BOARD_WIDTH - BORDER_RIGHT;
    public static final int SPIDER_INIT_Y = BOARD_HEIGHT - PLAYER_AREA;
    public static final int SPIDER_SPEED = 100;
    public static final int STARTING_LIVES = 3;
}