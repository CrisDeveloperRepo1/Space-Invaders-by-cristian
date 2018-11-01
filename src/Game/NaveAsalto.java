
package Game;

import Controller.KeyboardController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;


public class NaveAsalto                        
        extends ControlledGameObject {

    ImageIcon ship = new ImageIcon("resources/shipSkin.gif");
    ImageIcon bonusEnemy = new ImageIcon("resources/bonusEnemySkin.gif");
    ImageIcon lifeCounterShip = new ImageIcon("resources/shipSkinSmall.gif");

    
    public NaveAsalto(int xPosition, int yPosition, Color color, KeyboardController control) {
        super(xPosition, yPosition, color, control);
    }

  
    public void bonusDraw(Graphics g) {

        bonusEnemy.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    }

    public void lifeDraw(Graphics g) {

        lifeCounterShip.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    }

    
    @Override
    public void draw(Graphics g) {
        ship.paintIcon(null, g, this.getXPosition(), this.getYPosition());

    }

  
    @Override
    public Rectangle getBounds() {
        Rectangle shipHitbox = new Rectangle(this.getXPosition(), this.getYPosition(), 50, 50);
        return shipHitbox;
    }

    
    @Override
    public void move() {
        
        if (control.getKeyStatus(37)) {
            xPos -= 10;
        }
        
        if (control.getKeyStatus(39)) {
            xPos += 10;
        }
        
    
        if (xPos > 800) {
            xPos = -50;
        }
        if (xPos < -50) {
            xPos = 800;
        }
    }
}
