
package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;


public class Enemigos extends MovingGameObject {

    ImageIcon alien1 = new ImageIcon("resources/alien1Skin.gif");
    ImageIcon alien2 = new ImageIcon("resources/alien2Skin.gif");
    ImageIcon alien3 = new ImageIcon("resources/alien3Skin.gif");
    ImageIcon alienBoss = new ImageIcon("resources/boss1.gif");
    ImageIcon alienBoss2 = new ImageIcon("resources/boss2.gif");
    ImageIcon alienBoss3 = new ImageIcon("resources/boss3.gif");
    
    private int enemytype, width, height;

    
    // Constuctor para el enemigo
    public Enemigos(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height) {
        super(xPosition, yPosition, xVelocity, yVelocity, color);
        this.enemytype = enemyType;
        this.width = width;
        this.height = height;
    }
    
    @Override
    // Draws alien
    public void draw(Graphics g) {
        // Varient 1
        if (this.enemytype % 3 == 0) {
            alien1.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Varient 2
        } else if (this.enemytype % 3 == 1 && this.enemytype != 100) {
            alien2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Varient 3
        } else if (this.enemytype % 3 == 2) {
            alien3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Boss Enemigos
        } if (this.enemytype == 100)
        {
            if(GamePanel.getBossHealth()>20){
                alienBoss.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
            else if(GamePanel.getBossHealth()>10){
                alienBoss2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
            else if(GamePanel.getBossHealth()>0){
                alienBoss3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
        }
    }

   
    @Override
    public Rectangle getBounds() {
        Rectangle enemyHitBox = new Rectangle(this.getXPosition(), this.getYPosition(), width, height);
        return enemyHitBox;
    }

   
    @Override
    public void move() {
        xPos += xVel;
    }

}
