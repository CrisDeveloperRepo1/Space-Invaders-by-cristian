
package Game;

import Game.GameObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Escudo extends GameObject {

    int width;
    int height;

   
    public Escudo(int xPosition, int yPosition, int width, int height, Color color) {
        super(xPosition, yPosition, color);
        this.width = width;
        this.height = height;

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(this.getXPosition(), this.getYPosition(), 90, 10);
    }


    @Override
    public Rectangle getBounds() {
        Rectangle shieldHitbox = new Rectangle(this.getXPosition(), this.getYPosition(), 90, 10);
        return shieldHitbox;
    }
}
