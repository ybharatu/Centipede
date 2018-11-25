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
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
    public int invincible = 0;
    public String display;
    public int score = 0;


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
                try{
                    String soundName = "src/images/Laser_Shoot2.wav";
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                }catch(Exception e){
                    System.out.println(e);
                }

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

    public synchronized void drawShot(Graphics g) {

        synchronized (shots) {
            for (Shot shot : shots) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
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
            drawDisplay(g);
            //drawGrid(g);

        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void drawDisplay(Graphics g){
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(display, 10, 10);
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
        timer.cancel();
        timer.purge();
    }

    public class CentipedeTimer extends TimerTask implements Commons{
        public void run(){
            centipede.act();
        }
    }

    public class SpiderTimer extends TimerTask implements Commons{
        public void run() { spider.act(); }
    }

    public class HitTimer extends TimerTask implements Commons{
        public void run() { invincible = 0; }
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

        if (player.getLives() == 0) {

            ingame = false;
            message = "Game Over! Score: " + score;
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
                        score += 1;
                        if(m.getLives() == 0){
                            mushrooms_to_delete.addElement(m);
                            //mushrooms.remove(m);
                            score += 4;
                            mushroom_grid[shot.getY() / GRID_UNIT][shot.getX() / GRID_UNIT] = 0;
                        }
                    }

                     /*********************************************************************************
                     * Detect Centipede Collision
                     *********************************************************************************/
                    for(Segment s: centipede.segments){
                        if(s.getX() / GRID_UNIT == shot.getX() / GRID_UNIT && s.getY() / GRID_UNIT == shot.getY() / GRID_UNIT){
                            shots_to_delete.addElement(shot);
                            if(centipede.got_hit(centipede.segments.indexOf(s)) == 0){
                                score += 2;
                            }else{
                                score += 5;
                            }

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
                        score += 100;
                        if(spider.getLives() == 0){
                            spider = new Spider(BOARD_WIDTH / 2, SPIDER_INIT_Y);
                            score += 500;
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
            score += 600;
        }

         /*********************************************************************************
         * Check Player Collisions
         *********************************************************************************/

         /*********************************************************************************
         * Detect Centipede Collision
         *********************************************************************************/
        for(Segment s: centipede.segments){
            if(s.type == "head"){
                if((s.getX() / GRID_UNIT == player.getX() / GRID_UNIT && s.getY() / GRID_UNIT == player.getY() / GRID_UNIT)
                && invincible == 0){
                    player.got_hit();
                    invincible = 1;
                    timer.schedule(new HitTimer(), 3000);
                    resetGame();
                    break;
                }
            }
        }

         /*********************************************************************************
         * Detect Spider Collision
         *********************************************************************************/
        if(     ((player.getX() <= spider.getX() + SPIDER_WIDTH )
                && (player.getX() >= spider.getX() - SPIDER_WIDTH ))
                && ((player.getY() <= spider.getY() + SPIDER_HEIGHT )
                && (player.getY()  >= spider.getY() - SPIDER_HEIGHT )) ){
            player.got_hit();
            resetGame();
        }
    }

    public void resetGame(){
        //player = new Player();
        //centipede = new Centipede(NUMBER_SEGMENTS, CENTIPEDE_INIT_X, CENTIPEDE_INIT_Y );
        spider = new Spider(BOARD_WIDTH / 2, SPIDER_INIT_Y);

        for(Mushroom m: mushrooms){
            if(m.lives != 3){
                score += 10;
            }
            m.lives = 3;
            m.updateImage();
        }
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
            display = "Score " + score + " Lives: " + player.getLives();
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