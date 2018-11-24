package com.centipede;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.TimerTask;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable, Commons {

    private Dimension d;
    //private ArrayList<Alien> aliens;

    //private Shot shot;
    private Vector<Shot> shots = new Vector(5,2);
    //private int [][] mushroom_grid = new int [BOARD_HEIGHT / GRID_UNIT][BOARD_WIDTH / GRID_UNIT];
    private Vector<Mushroom> mushrooms = new Vector(5, 2);

    private final int ALIEN_INIT_X = 150;
    private final int ALIEN_INIT_Y = 5;
    private int direction = -1;
    private int deaths = 0;

    private boolean ingame = true;
    private final String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    /*********************************************************************************
     * Objects
     *********************************************************************************/
    private Player player;
    private Centipede centipede;
    private Spider spider;
    public Timer timer = new Timer();


    private Thread animator;

    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        addMouseListener(new java.awt.event.MouseAdapter() {
             /*********************************************************************************
             * Enables shooting by clicking
             *********************************************************************************/
            public void mousePressed(java.awt.event.MouseEvent evt){
                Shot shot = new Shot(player.getX(), player.getY());
                shots.addElement(shot);
            }

        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
             /*********************************************************************************
             * Enables Player control using the mouse
             *********************************************************************************/
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                //formMouseMoved(evt);
                player.setX(evt.getX());
                player.setY(evt.getY());
            }

        });

        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        setupMushrooms();
        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {

        super.addNotify();
        gameInit();
    }

    public void gameInit() {

//        aliens = new ArrayList<>();
//
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 6; j++) {
//
//                Alien alien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i);
//                aliens.add(alien);
//            }
//        }

        player = new Player();
        centipede = new Centipede(NUMBER_SEGMENTS, CENTIPEDE_INIT_X, CENTIPEDE_INIT_Y );
        //spider = new Spider(SPIDER_INIT_X, SPIDER_INIT_Y);
        spider = new Spider(BOARD_WIDTH / 2, SPIDER_INIT_Y);

        if (animator == null || !ingame) {

            animator = new Thread(this);
            animator.start();
        }
    }

    public void setupMushrooms(){

        Random generator = new Random();

        for(int i = CENTIPEDE_INIT_Y + GRID_UNIT; i < BOARD_HEIGHT - PLAYER_AREA - GRID_UNIT; i += GRID_UNIT){
            for(int j = GRID_UNIT + GRID_UNIT; j < BOARD_WIDTH - GRID_UNIT - 20; j += GRID_UNIT){
                //mushroom_grid[j / GRID_UNIT][i / GRID_UNIT] = 2;
                int rand_int = generator.nextInt(MUCHROOM_CHANCE) + 1;

                if(rand_int == 1 && mushroom_grid[(i / GRID_UNIT) - 1][(j / GRID_UNIT) - 1] == 0 && mushroom_grid[(i / GRID_UNIT) - 1][(j / GRID_UNIT) + 1] == 0){
                    mushroom_grid[i / GRID_UNIT][j / GRID_UNIT] = 1;
                    mushrooms.addElement(new Mushroom(j,i));
                }
            }
        }

//        for(int i = 0; i < mushroom_grid.length; i++){
//            printRow(mushroom_grid[i]);
//        }
        //System.out.println("Mushroom grid length " + mushroom_grid.length);
    }

    public static void printRow(int[] row) {
        for (int i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }

    public void drawCentipede(Graphics g) {
        for(Segment segment: centipede.segments){
            g.drawImage(segment.getImage(), segment.getX(), segment.getY(), this);
        }
    }

    public void drawMushrooms(Graphics g) {
        for(Mushroom mushroom: mushrooms){
            g.drawImage(mushroom.getImage(), mushroom.getX(), mushroom.getY(), this);
        }
    }

//    public void drawAliens(Graphics g) {
//
//        Iterator it = aliens.iterator();
//
//        for (Alien alien: aliens) {
//
//            if (alien.isVisible()) {
//
//                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
//            }
//
//            if (alien.isDying()) {
//
//                alien.die();
//            }
//        }
//    }

    public void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            ingame = false;
        }
    }

    public void drawSpider(Graphics g){
        g.drawImage(spider.getImage(), spider.getX(), spider.getY(), this);
    }

    public void drawShot(Graphics g) {

        for(Shot shot: shots){
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
//        if (shot.isVisible()) {
//
//            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
//        }
    }

//    public void drawBombing(Graphics g) {
//
//        for (Alien a : aliens) {
//
//            Alien.Bomb b = a.getBomb();
//
//            if (!b.isDestroyed()) {
//
//                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
//            }
//        }
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {

            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            drawPlayer(g);
            drawShot(g);
            drawCentipede(g);
            drawMushrooms(g);
            drawSpider(g);
            //drawGrid(g);

        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {

        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

    public class CentipedeTimer extends TimerTask implements Commons{
        public void run(){
            centipede.act();
        }
    }

    public class SpiderTimer extends TimerTask implements Commons{
        public void run() { spider.act(); }
    }

    public Mushroom getMushroom(int x, int y){
        for(Mushroom mushroom: mushrooms){
            if(mushroom.getX() / GRID_UNIT == x && mushroom.getY() / GRID_UNIT == y){
                Mushroom m = mushroom;
                return m;
            }
        }
        return null;
    }

    public void animationCycle() {

        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {

            ingame = false;
            message = "Game won!";
        }

         /*********************************************************************************
         * Player Act
         *********************************************************************************/
        player.act();


         /*********************************************************************************
         * Vectors that contain elements to be removed
         *********************************************************************************/
        Vector <Shot> shots_to_delete = new Vector(5,2);
        Vector <Mushroom> mushrooms_to_delete = new Vector(5, 2);
        synchronized (shots){
            for(Shot shot: shots){
                if(shot.isVisible()){
                    int y = shot.getY();
                    y -= 4;

                    /*********************************************************************************
                     * Add shot to be removed if shot is < 0 or > BOARD_HEIGHT. Or move shot forward
                     *********************************************************************************/
                    if (y < 0 || y > BOARD_HEIGHT) {
                        //shot.die();
                        shots_to_delete.addElement(shot);
                    } else {
                        shot.setY(y);
                    }

                     /*********************************************************************************
                     * Detect Mushroom Collision
                     *********************************************************************************/
                    if(mushroom_grid[shot.getY() / GRID_UNIT][shot.getX() / GRID_UNIT] == 1){
                        Mushroom m = getMushroom(shot.getX() / GRID_UNIT, shot.getY() / GRID_UNIT );
                        m.got_hit();
                        shots_to_delete.addElement(shot);
                        if(m.getLives() == 0){
                            mushrooms.remove(m);
                            mushroom_grid[shot.getY() / GRID_UNIT][shot.getX() / GRID_UNIT] = 0;
                        }
                    }

                     /*********************************************************************************
                     * Detect Centipede Collision
                     *********************************************************************************/
                    for(Segment s: centipede.segments){
                        if(s.getX() / GRID_UNIT == shot.getX() / GRID_UNIT && s.getY() / GRID_UNIT == shot.getY() / GRID_UNIT){
                            shots_to_delete.addElement(shot);
                            centipede.got_hit(centipede.segments.indexOf(s));
                            break;
                        }
                    }

                     /*********************************************************************************
                     * Detect Spider Collision
                     *********************************************************************************/
                    if(     ((shot.getX() <= spider.getX() + SPIDER_WIDTH )
                            && (shot.getX() >= spider.getX() - SPIDER_WIDTH ))
                            && ((shot.getY() <= spider.getY() + SPIDER_HEIGHT )
                            && (shot.getY()  >= spider.getY() - SPIDER_HEIGHT )) ){
                        shots_to_delete.addElement(shot);
                        spider.got_hit();
                        if(spider.getLives() == 0){
                            spider = new Spider(BOARD_WIDTH / 2, SPIDER_INIT_Y);
                        }
                    }
                }

            }
        }


         /*********************************************************************************
         * Remove shot from shot list
         *********************************************************************************/
        for(Shot shot: shots_to_delete){
            shots.remove(shot);
        }

        /*********************************************************************************
         * Remove Mushroom from Mushroom list
         *********************************************************************************/
        for(Mushroom mushroom: mushrooms_to_delete){
            mushrooms.remove(mushroom);
        }

         /*********************************************************************************
         * Recreate Centipede if dead
         *********************************************************************************/
        if(centipede.num_segments == 0){
            centipede = new Centipede(NUMBER_SEGMENTS, CENTIPEDE_INIT_X, CENTIPEDE_INIT_Y );
        }
        // aliens

//        for (Alien alien: aliens) {
//
//            int x = alien.getX();
//
//            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
//
//                direction = -1;
//                Iterator i1 = aliens.iterator();
//
//                while (i1.hasNext()) {
//
//                    Alien a2 = (Alien) i1.next();
//                    a2.setY(a2.getY() + GO_DOWN);
//                }
//            }
//
//            if (x <= BORDER_LEFT && direction != 1) {
//
//                direction = 1;
//
//                Iterator i2 = aliens.iterator();
//
//                while (i2.hasNext()) {
//
//                    Alien a = (Alien) i2.next();
//                    a.setY(a.getY() + GO_DOWN);
//                }
//            }
//        }

//        Iterator it = aliens.iterator();
//
//        while (it.hasNext()) {
//
//            Alien alien = (Alien) it.next();
//
//            if (alien.isVisible()) {
//
//                int y = alien.getY();
//
//                if (y > GROUND - ALIEN_HEIGHT) {
//                    ingame = false;
//                    message = "Invasion!";
//                }
//
//                alien.act(direction);
//            }
//        }

        // bombs
//        Random generator = new Random();
//
//        for (Alien alien: aliens) {
//
//            int shot = generator.nextInt(15);
//            Alien.Bomb b = alien.getBomb();
//
//            if (shot == CHANCE && alien.isVisible() && b.isDestroyed()) {
//
//                b.setDestroyed(false);
//                b.setX(alien.getX());
//                b.setY(alien.getY());
//            }
//
//            int bombX = b.getX();
//            int bombY = b.getY();
//            int playerX = player.getX();
//            int playerY = player.getY();
//
//            if (player.isVisible() && !b.isDestroyed()) {
//
//                if (bombX >= (playerX)
//                        && bombX <= (playerX + PLAYER_WIDTH)
//                        && bombY >= (playerY)
//                        && bombY <= (playerY + PLAYER_HEIGHT)) {
//                    ImageIcon ii
//                            = new ImageIcon(explImg);
//                    player.setImage(ii.getImage());
//                    player.setDying(true);
//                    b.setDestroyed(true);
//                }
//            }
//
//            if (!b.isDestroyed()) {
//
//                b.setY(b.getY() + 1);
//
//                if (b.getY() >= GROUND - BOMB_HEIGHT) {
//                    b.setDestroyed(true);
//                }
//            }
//        }
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

         /*********************************************************************************
         * Scheduled Timer Task that calls Centipede Act() function
         *********************************************************************************/
        timer.scheduleAtFixedRate(new CentipedeTimer(), INIT_TIME, CENTIPEDE_SPEED);
        timer.scheduleAtFixedRate(new SpiderTimer(), INIT_TIME, SPIDER_SPEED);

        while (ingame) {

            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }

        gameOver();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (ingame) {
                    Shot shot = new Shot(x, y);
                    shots.addElement(shot);
                }
            }
        }
    }
}