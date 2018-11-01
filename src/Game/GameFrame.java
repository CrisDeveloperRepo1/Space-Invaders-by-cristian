
package Game;

import java.awt.Color;
import javax.swing.JFrame;
//
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


//


public class GameFrame extends JFrame
        
{
     
    private GamePanel game;
    
    
   
    
    public GameFrame()
            
    {
        
        // Add text to title bar 
       
        
        
        super("Space Invaders - ComIT");
         
        
        // Make sure the program exits when the close button is clicked
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        // Create an instance of the Game class and turn on double buffering
        //  to ensure smooth animation
        game = new GamePanel();
        game.setDoubleBuffered(true);
        
        // Add the Breakout instance to this frame's content pane to display it
        this.getContentPane().add(game); 
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        
        // Start the game
        game.start();  
    }
    
    public static void main(String[] args) throws FileNotFoundException,
         JavaLayerException
    {
        
        
         java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
         
         //
                for(int i=1;i<1000;i++){
            Player apl = new Player(new FileInputStream(
            "resources/backgroundMusic.mp3"));           
           
            apl.play();
            
            
       
       
       
       }      
         
         //
         
         
         
         
         
         
         
         
        
    }
}
