
package Game;

import Controller.KeyboardController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class GamePanel extends JPanel {
     
  
   

 
    private Timer gameTimer;
    private KeyboardController controller;

    private final int gameWidth = 800;
    private final int gameHeight = 800;
    private final int framesPerSecond = 120;


    Random r = new Random();
    private int score = 0;
    private int level = 1;
    private int numberOfLives = 3;
    private int highScore;
    private int markerX, markerY;
    private static int bossHealth = 30;
    File f = new File("Puntuacion mas alta.txt");

   
    private NaveAsalto playerShip;
    private NaveAsalto singleLife;
    private NaveAsalto bonusEnemy;
    private Enemigos enemy;
    private Escudo shield;
    private Balas bullet;
    private Beam beam, beam2, beam3;

    
    private boolean newBulletCanFire = true;
    private boolean newBeamCanFire = true;
    private boolean newBonusEnemy = true;
    private boolean hitMarker = false;

   
    private ArrayList<NaveAsalto> lifeList = new ArrayList();
    private ArrayList<NaveAsalto> bonusEnemyList = new ArrayList();
    private ArrayList<Enemigos> enemyList = new ArrayList();
    private ArrayList<Escudo> shieldList = new ArrayList();
    private ArrayList<Beam> beamList = new ArrayList();
    private ImageIcon background = new ImageIcon("resources/backgroundSkin.jpg");

  
    private File beamSound = new File("resources/alienBeam.wav");
    private File bulletSound = new File("resources/bulletSound.wav");
    private File levelUpSound = new File("resources/levelUpSound.wav");
    private File deathSound = new File("resources/deathSound.wav");
    private File hitmarkerSound = new File("resources/hitmarkerSound.wav");
    private File shieldSound = new File("resources/shieldSound.wav");
    private File bossSound = new File("resources/bossSound.wav");
    private File bonusSound = new File("resources/bonusSound.wav");
     private File damageSound = new File("resources/damageSound.wav");
    private AudioStream beamSoundAudio;
    private InputStream beamSoundInput;
    private AudioStream bulletSoundAudio;
    private InputStream bulletSoundInput;
    private AudioStream levelUpSoundAudio;
    private InputStream levelUpSoundInput;
    private AudioStream deathSoundAudio;
    private InputStream deathSoundInput;
    private AudioStream hitSoundAudio;
    private InputStream hitSoundInput;
    private AudioStream shieldSoundAudio;
    private InputStream shieldSoundInput;
    private AudioStream bossSoundAudio;
    private InputStream bossSoundInput;
    private AudioStream bonusSoundAudio;
    private InputStream bonusSoundInput;
    private AudioStream damageSoundAudio;
    private InputStream damageSoundInput;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    

    public static int getBossHealth() {
        return bossHealth;
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //

    public final void setupGame() {

        
        if (level != 3 && level != 6 && level != 9 && level != 12) {
            // Six rows
            for (int row = 0; row < 6; row++) {
                // 5 columns
                for (int column = 0; column < 5; column++) {
                    enemy = new Enemigos((20 + (row * 100)), (20 + (column * 60)), level, 0, column, null, 40, 40); // Enemigos speed will increase each level
                    enemyList.add(enemy);
                }
            }
        }
        // Sets enemy for boss levels
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            AudioPlayer.player.start(bossSoundAudio); // Plays boss roar
            enemy = new Enemigos(20, 20, 3, 0, 100, null, 150, 150);
            enemyList.add(enemy);
        }
        // Gives directions on level 1
        if (level == 1) {
           
            JOptionPane.showMessageDialog(null,"Play");
        }
        // Resets all controller movement
        controller.resetController();

        // Sets the player's ship values   
        playerShip = new NaveAsalto(375, 730, null, controller);

        // Sets the life counter Ships
        for (int column = 0; column < numberOfLives; column++) {
            singleLife = new NaveAsalto(48 + (column * 20), 10, Color.WHITE, null);
            lifeList.add(singleLife);
        }

        // Sets the values for 3 rows and 3 columns of shields
        for (int row = 0;
                row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                shield = new Escudo(100 + (column * 250), 650 - (row * 10), 70, 10, Color.RED);
                shieldList.add(shield);
            }
        }
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PAINT
    @Override
    public void paint(Graphics g) {

        // Sets background image
        background.paintIcon(null, g, 0, -150);

        // makes a string that says "+100" on enemy hit
        if (bullet != null) {
            if (hitMarker) {
                g.setColor(Color.WHITE);
                if (level != 3 && level != 6 && level != 9 && level != 12) {
                    g.drawString("+ 100", markerX + 20, markerY -= 1);
                } else {
                    g.drawString("- 1", markerX + 75, markerY += 1);
                }
            }
        }
        // Draws the player's ship
        playerShip.draw(g);

        // Draws 3 evenly-spaced shields 
        for (int index = 0; index < shieldList.size(); index++) {
            shieldList.get(index).draw(g);
        }

        //Draws 3 different kinds of aliens
        try {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).draw(g);
            }

        } catch (IndexOutOfBoundsException e) {
        }

        // Draw a bullet on space bar press
        if (controller.getKeyStatus(32)) {
            if (newBulletCanFire) {
                bullet = new Balas(playerShip.getXPosition() + 22, playerShip.getYPosition() - 20, 0, Color.RED);
                AudioPlayer.player.start(bulletSoundAudio); // Plays bullet sound
                newBulletCanFire = false;
            }
        }
        // Only attempts to draw bullet after key press
        if (bullet != null) {
            bullet.draw(g);
        }

        // Generates random beams shot from enemies
        if (level != 3 && level != 6 && level != 9 && level != 12) {
            if (newBeamCanFire) {
                for (int index = 0; index < enemyList.size(); index++) {
                    if (r.nextInt(30) == index) {
                        beam = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition(), 0, Color.YELLOW);
                        beamList.add(beam);
                        AudioPlayer.player.start(beamSoundAudio); // Plays beam sound for normal enemies
                    }
                    newBeamCanFire = false;
                }
            }
        }
        // Generates beams at a faster rate for boss
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            if (newBeamCanFire) {
                for (int index = 0; index < enemyList.size(); index++) {
                    if (r.nextInt(5) == index) {
                        beam = new Beam(enemyList.get(index).getXPosition() + 75, enemyList.get(index).getYPosition() + 140, 0, Color.YELLOW);
                        beam2 = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition() + 110, 0, Color.YELLOW);
                        beam3 = new Beam(enemyList.get(index).getXPosition() + 150, enemyList.get(index).getYPosition() + 110, 0, Color.YELLOW);
                        beamList.add(beam);
                        beamList.add(beam2);
                        beamList.add(beam3);
                        AudioPlayer.player.start(beamSoundAudio); // Plays beam sound for boss
                    }
                    newBeamCanFire = false;
                }
            }
        }
        // Draws the generated beams
        for (int index = 0; index < beamList.size(); index++) {
            beamList.get(index).draw(g);
        }
        // Generates random bonus enemy
        if (newBonusEnemy) {
            if (r.nextInt(3000) == 1500) {
                bonusEnemy = new NaveAsalto(-50, 30, Color.RED, null);
                bonusEnemyList.add(bonusEnemy);
                newBonusEnemy = false;
            }
        }
        // Draws bonus enemy
        for (int index = 0; index < bonusEnemyList.size(); index++) {
            bonusEnemyList.get(index).bonusDraw(g);
        }

        // Sets the score display
        g.setColor(Color.WHITE);
        g.drawString("Puntuacion: " + score, 260, 20);

        // Sets the life counter display
        g.setColor(Color.WHITE);
        g.drawString("Vidas:", 11, 20);
        for (int index = 0; index < lifeList.size(); index++) {
            lifeList.get(index).lifeDraw(g);
        }
        // Sets level display
        g.setColor(Color.WHITE);
        g.drawString("Nivel " + level, 750, 20);

        // Sets Highscore display
        g.setColor(Color.WHITE);
        g.drawString("Puntuacion mas alta: " + highScore, 440, 20);

        // Draws a health display for boss level
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            g.setColor(Color.WHITE);
            g.drawString("Salud del Jefe: " + bossHealth, 352, 600);
        }
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// UPDATE GAME STATE
    
    public void updateGameState(int frameNumber) {

        // Allows player to move left and right
        playerShip.move();

        // Updates highscore
        try {
            Scanner fileScan = new Scanner(f);
            while (fileScan.hasNextInt()) {
                String nextLine = fileScan.nextLine();
                Scanner lineScan = new Scanner(nextLine);
                highScore = lineScan.nextInt();
            }
        } catch (FileNotFoundException e) {
        }
        // Adds option to reset highScore
        if (controller.getKeyStatus(82)) {
            int answer = JOptionPane.showConfirmDialog(null, "Te gustaría restablecer la puntuación más alta?", ":)", 0);
            controller.resetController();
            if (answer == 0) {
                try {
                    String scoreString = Integer.toString(0);
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                    pw.write(scoreString);
                    pw.close();
                } catch (FileNotFoundException e) {
                }
            }
        }
        // Updates the high score text file if your score exceeds the previous high score
        try {
            if (score > highScore) {
                String scoreString = Integer.toString(score);
                PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                pw.write(scoreString);
                pw.close();
            }
        } catch (FileNotFoundException e) {
        }

        // Makes enemies move and change direction at borders
        if ((enemyList.get(enemyList.size() - 1).getXPosition() + enemyList.get(enemyList.size() - 1).getXVelocity()) > 760 || (enemyList.get(0).getXPosition() + enemyList.get(0).getXVelocity()) < 0) {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).setXVelocity(enemyList.get(index).getXVelocity() * -1);
                enemyList.get(index).setYPosition(enemyList.get(index).getYPosition() + 10);
            }
        } else {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).move();
            }
        }

        // Move bullet
        if (bullet != null) {
            bullet.setYPosition(bullet.getYPosition() - 15);
            if (bullet.getYPosition() < 0) {
                newBulletCanFire = true;
            }

            // Checks for collisions with normal enemies
            for (int index = 0; index < enemyList.size(); index++) {
                if (bullet.isColliding(enemyList.get(index))) {
                    AudioPlayer.player.start(hitSoundAudio); // Plays hitmarker sound if you hit an enemy
                    bullet = new Balas(0, 0, 0, null);
                    newBulletCanFire = true;
                    // Updates score for normal levels
                    if (level != 3 && level != 6 && level != 9 && level != 12) {
                        score += 100;
                        hitMarker = true;
                        markerX = enemyList.get(index).getXPosition(); // Gets positions that the "+ 100" spawns off of
                        markerY = enemyList.get(index).getYPosition();
                        enemyList.remove(index);

                    }
                    // Updates score for boss levels
                    if (level == 3 || level == 6 || level == 9 || level == 12) {
                        hitMarker = true;
                        markerX = enemyList.get(index).getXPosition(); // Gets positions that the "- 1" spawns off of
                        markerY = enemyList.get(index).getYPosition() + 165;
                        bossHealth -= 1;
                        if (bossHealth == 0) {
                            enemyList.remove(index);
                            score += 9000;// Bonus score for defeating boss
                        }
                    }
                }
            }

            // Checks for collisions with shield and bullets
            for (int index = 0; index < shieldList.size(); index++) {
                if (bullet.isColliding(shieldList.get(index))) {
                    // Each if statement changes color of the shield, indicating "strength"
                    // STRONG
                    if (shieldList.get(index).getColor() == Color.RED) {
                        shieldList.get(index).setColor(Color.ORANGE);
                        AudioPlayer.player.start(shieldSoundAudio); // Plays sound if shield takes damage
                        bullet = new Balas(0, 0, 0, null);
                        newBulletCanFire = true;
                    // GOOD
                    } else if (shieldList.get(index).getColor() == Color.ORANGE) {
                        shieldList.get(index).setColor(Color.YELLOW);
                        AudioPlayer.player.start(shieldSoundAudio);
                        bullet = new Balas(0, 0, 0, null);
                        newBulletCanFire = true;
                    // OKAY
                    } else if (shieldList.get(index).getColor() == Color.YELLOW) {
                        shieldList.get(index).setColor(Color.WHITE);
                        AudioPlayer.player.start(shieldSoundAudio);
                        bullet = new Balas(0, 0, 0, null);
                        newBulletCanFire = true;
                    // WEAK, BREAKS ON HIT
                    } else if (shieldList.get(index).getColor() == Color.WHITE) {
                        shieldList.remove(index);
                        AudioPlayer.player.start(shieldSoundAudio);
                        bullet = new Balas(0, 0, 0, null);
                        newBulletCanFire = true;
                    }
                }
            }
        }
        // Moves bonus enemy
        if (!bonusEnemyList.isEmpty()) {
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                bonusEnemyList.get(index).setXPosition(bonusEnemyList.get(index).getXPosition() + (2));
                if (bonusEnemyList.get(index).getXPosition() > 800) {
                    bonusEnemyList.remove(index);
                    newBonusEnemy = true;
                }
            }
            // bonus enemy and bullet collsion
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                if (bullet != null) {
                    if (bonusEnemyList.get(index).isColliding(bullet)) {
                        bonusEnemyList.remove(index);
                        bullet = new Balas(0, 0, 0, null);
                        newBulletCanFire = true;
                        newBonusEnemy = true;
                        AudioPlayer.player.start(bonusSoundAudio); // Plays sound if player hits a bonus enemy
                        score += 5000; // add bonus to score on hit
                    }
                }
            }
        }

        // Moves beams on normal levels
        if (level != 3 && level != 6 && level != 9 && level != 12) {
            if (beam != null) {
                for (int index = 0; index < beamList.size(); index++) {
                    beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (4));
                    if (beamList.get(index).getYPosition() > 800) {
                        beamList.remove(index);
                    }
                }
            }
        }
        // Moves beams at a faster speed for boss
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            if (beam != null) {
                for (int index = 0; index < beamList.size(); index++) {
                    beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (2 * level)); //Boss beam speed will increase each level
                    if (beamList.get(index).getYPosition() > 800) {
                        beamList.remove(index);
                    }
                }
            }
        }

        // Checks for beam and shield collisions
        try {
            for (int j = 0; j < shieldList.size(); j++) {
                for (int index = 0; index < beamList.size(); index++) {
                    if (beamList.get(index).isColliding(shieldList.get(j))) {
                        // STRONG
                        if (shieldList.get(j).getColor() == Color.RED) {
                            shieldList.get(j).setColor(Color.ORANGE);
                            AudioPlayer.player.start(shieldSoundAudio); // Plays sound if shield takes damage
                            beamList.remove(index);
                        // GOOD
                        } else if (shieldList.get(j).getColor() == Color.ORANGE) {
                            shieldList.get(j).setColor(Color.YELLOW);
                            AudioPlayer.player.start(shieldSoundAudio);
                            beamList.remove(index);
                        // OKAY
                        } else if (shieldList.get(j).getColor() == Color.YELLOW) {
                            shieldList.get(j).setColor(Color.WHITE);
                            AudioPlayer.player.start(shieldSoundAudio);
                            beamList.remove(index);
                        // WEAK, BREAKS ON HIT
                        } else if (shieldList.get(j).getColor() == Color.WHITE) {
                            shieldList.remove(j);
                            AudioPlayer.player.start(shieldSoundAudio);
                            beamList.remove(index);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }

        // Checks for beam and player collisions
        for (int index = 0; index < beamList.size(); index++) {
            if (beamList.get(index).isColliding(playerShip)) {
                beamList.remove(index);
                AudioPlayer.player.start(damageSoundAudio); // Plays damage sound
                lifeList.remove(lifeList.size() - 1); // Removes life if hit by bullet
            }
        }

        // Paces beam shooting by only allowing new beams to be fired once all old beams are off screen or have collided
        if (beamList.isEmpty()) {
            newBeamCanFire = true;
        }

        //Destroys shields if aliens collide with them
        for (int input = 0; input < enemyList.size(); input++) {
            for (int j = 0; j < shieldList.size(); j++) {
                if (enemyList.get(input).isColliding(shieldList.get(j))) {
                    shieldList.remove(j);
                }
            }
            // If aliens exceed this X Position, you reset the level and lose a life
            if (enemyList.get(input).getYPosition() + 50 >= 750) {
                enemyList.clear();
                shieldList.clear();
                lifeList.clear();
                beamList.clear();
                bossHealth = 30;
                numberOfLives -= 1;
                AudioPlayer.player.start(deathSoundAudio); // Plays death sound when enemies reach bottom
                setupGame();
            }
        }

        //Updates the life counter display 
        if (playerShip.isColliding) {
            int index = lifeList.size() - 1;
            lifeList.remove(index);
        } 
        // Ends game if player runs out of lives
        else if (lifeList.isEmpty()) {
            AudioPlayer.player.start(deathSoundAudio); // Plays death sound when you run out of lives
            // Gives the player an option to play again or exit
            int answer = JOptionPane.showConfirmDialog(null, "Te gustaria jugar de nuevo ?", "Perdiste con " + score + " puntos ", 0);
            // If they choose to play again, this resets every element in the game
            if (answer == 0) {
                lifeList.clear();
                enemyList.clear();
                shieldList.clear();
                beamList.clear();
                bonusEnemyList.clear();
                score = 0;
                level = 1;
                bossHealth = 30;
                numberOfLives = 3;
                newBulletCanFire = true;
                newBeamCanFire = true;
                newBonusEnemy = true;
                setupGame();
            }
            // If they choose not to play again, it closes the game
            if (answer == 1) {
                System.exit(0);
            }
        }

        // Goes to next level, resets all lists, sets all counters to correct values
        if (enemyList.isEmpty()) {
            beamList.clear();
            shieldList.clear();
            bonusEnemyList.clear();
            lifeList.clear();
            level += 1;
            bossHealth = 30;
            setupGame();
            AudioPlayer.player.start(levelUpSoundAudio); // Plays level up sound
        }
        
        // All streams needed for every sound in the game
        try {
            beamSoundInput = new FileInputStream(beamSound);
            beamSoundAudio = new AudioStream(beamSoundInput);
            bulletSoundInput = new FileInputStream(bulletSound);
            bulletSoundAudio = new AudioStream(bulletSoundInput);
            levelUpSoundInput = new FileInputStream(levelUpSound);
            levelUpSoundAudio = new AudioStream(levelUpSoundInput);
            deathSoundInput = new FileInputStream(deathSound);
            deathSoundAudio = new AudioStream(deathSoundInput);
            hitSoundInput = new FileInputStream(hitmarkerSound);
            hitSoundAudio = new AudioStream(hitSoundInput);
            shieldSoundInput = new FileInputStream(shieldSound);
            shieldSoundAudio = new AudioStream(shieldSoundInput);
            bossSoundInput = new FileInputStream(bossSound);
            bossSoundAudio = new AudioStream(bossSoundInput);
            bonusSoundInput = new FileInputStream(bonusSound);
            bonusSoundAudio = new AudioStream(bonusSoundInput);
            damageSoundInput = new FileInputStream(damageSound);
            damageSoundAudio = new AudioStream(damageSoundInput);
        } catch (IOException e) {
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GAME PANEL    
    
    public GamePanel() {
        // Set the size of the Panel
        this.setSize(gameWidth, gameHeight);
        this.setPreferredSize(new Dimension(gameWidth, gameHeight));
        this.setBackground(Color.BLACK);

        // Register KeyboardController as KeyListener
        controller = new KeyboardController();
        this.addKeyListener(controller);

        // Call setupGame to initialize fields
        this.setupGame();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Method to start the Timer that drives the animation for the game. It is
     * not necessary for you to modify this code unless you need to in order to
     * add some functionality.
     */
    public void start() {
        // Set up a new Timer to repeat every 20 milliseconds (50 FPS)
        gameTimer = new Timer(1000 / framesPerSecond, new ActionListener() {

            // Tracks the number of frames that have been produced.
            // May be useful for limiting action rates
            private int frameNumber = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the game's state and repaint the screen
                updateGameState(frameNumber++);
                repaint();
            }
        });
        Timer gameTimerHitMarker = new Timer(1000, new ActionListener() {

            // Tracks the number of frames that have been produced.
            // May be useful for limiting action rates
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the game's state and repaint the screen
                hitMarker = false;
            }
        });

        gameTimer.setRepeats(true);
        gameTimer.start();
        gameTimerHitMarker.setRepeats(true);
        gameTimerHitMarker.start();
    }

}
