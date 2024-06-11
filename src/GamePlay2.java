
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Siddh
 */
class GamePlay2 extends JPanel implements KeyListener, ActionListener  {
	private boolean play = true;
	private int score = 0;
	
	private int totalBricks = 30;
	
	private final Timer timer;
	private final int delay;
	
	private int playerX = 310;
	
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
          private BufferedImage background;
          private BufferedImage ballimage;
       
         	private MapGenerator2 map;
  
	

	public GamePlay2() {
          try {
                background = ImageIO.read(getClass().getResource("ground2.jpg"));
                
                ballimage = ImageIO.read(getClass().getResource("ball.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
        this.delay = 1;
		map = new MapGenerator2(3,10);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
        
        @Override
	public void paint(Graphics g) {
		
	
            //background image
             g.drawImage(background, 1, 1, 692, 592, this);

          
                    
		map.draw((Graphics2D)g);
		
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		g.setColor(Color.blue);
		g.fillRect(playerX, 550, 100, 12);
		
                //drwas ball
                g.drawImage(ballimage, ballposX,ballposY,20,20,  this);

		//Score Text
		g.setColor(Color.black);
		g.setFont(new Font("MV Boli", Font.BOLD, 25));
		g.drawString("Score: " + score, 520, 30);
		
		
		if (totalBricks <= 0) { // if all bricks are destroyed then you win
		
		play = false;
            ballXdir = 0;
            ballYdir = 0;
             g.setColor(Color.red);
             g.setFont(new Font("serif", Font.BOLD, 50));
              g.drawString("You Won, Score: "+score, 190, 300);
        
              g.setFont(new Font("serif", Font.BOLD, 20));
              g.drawString("Press Enter to Restart ", 230, 350);
              
              
               g.setFont(new Font("serif", Font.BOLD, 20));
              g.drawString("Press N to Select Mode", 230, 400);
              
                  g.setFont(new Font("serif", Font.BOLD, 20));
              g.drawString("Press H to Home", 230, 450);
		
		
	
	
		}
		
		if(ballposY > 570) { // if ball goes below the paddle then you lose 
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.BLACK);
			g.setFont(new Font("MV Boli", Font.BOLD, 30));
			g.drawString("Game Over, Score: " + score, 190, 300);
			
			g.setFont(new Font("MV Boli", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
				
		} 
		g.dispose();
	}
      

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
            
		timer.start();
		if(play) {
			// Ball - Pedal  interaction 
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 98, 8))) {
				ballYdir = -ballYdir;
                           
                               
			}
                       
                      
                      

			for( int i = 0; i<map.map.length; i++) { // Ball - Brick interaction
				for(int j = 0; j<map.map[0].length; j++) {  // map.map[0].length is the number of columns
					if(map.map[i][j] > 0) {
						int brickX = j*map.brickWidth + 80;
						int brickY = i*map.brickHeight + 50;
						int brickWidth= map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20,20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect) ) {
							map.setBrickValue(0, i, j);
							totalBricks--;
                                                 
							score+=5;
							
							if(ballposX + 19 <= brickRect.x || ballposX +1 >= brickRect.x + brickRect.width) 
								ballXdir = -ballXdir;
							 else {
								ballYdir = -ballYdir;
							}
						}
						
					}
					
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0) { // if ball hits the left wall then it bounces back
				ballXdir = -ballXdir;
			}
			if(ballposY < 0) {  // if ball hits the top wall then it bounces back
				ballYdir = -ballYdir;
			}
			if(ballposX > 670) { // if ball hits the right wall then it bounces back
				ballXdir = -ballXdir;  
			
			}
			
		}
		
		
		repaint();

	}
	

	@Override
	public void keyTyped(KeyEvent arg0) {
            
		
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) { // if right arrow key is pressed then paddle moves right
			if(playerX >= 600) {
				playerX = 600;
			} else {
				moveRight();
					
			}
		}
		if(arg0.getKeyCode() == KeyEvent.VK_LEFT) { // if left arrow key is pressed then paddle moves left
			if(playerX < 10) {
				playerX = 10;
			} else {
				moveLeft();
					
			}
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER) { // if enter key is pressed then game restarts
			if(!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				score = 0;
				totalBricks = 30;
				map = new MapGenerator2(3,10);
				
				repaint();
			}
		}
                if(arg0.getKeyCode() == KeyEvent.VK_N)
                {
                   new SelectMode().setVisible(true);
                  
                }
                if(arg0.getKeyCode()==KeyEvent.VK_H){
                    new HomeScreen().setVisible(true);
                }
                
		
	}	
		public void moveRight() { // paddle moves right by 50 pixels
			play = true;
			playerX += 50;
		}
		public void moveLeft() { // paddle moves left by 50 pixels
			play = true;
			playerX -= 50;
		}
		
	

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}


}
class MapGenerator2 {
	
	public int map [][];
	public int brickWidth;
	public int brickHeight;
        
	public MapGenerator2(int row, int col) {
		map = new int [row][col];
		for (int i = 0; i < map.length; i++) { 
			for (int j=0; j< map[0].length;j++) {
				map[i][j] = 1;
			}
		}
		
		brickWidth = 540/col;
		brickHeight = 150/row;
	}
	
	// this draws the bricks
	public void draw(Graphics2D g) {
          
		for (int i = 0; i < map.length; i++) {
			for (int j=0; j< map[0].length;j++) {
				if(map[i][j] > 0) {
					g.setColor(new Color(0X808080)); // brick color
					g.fillRect(j*brickWidth + 80, i*brickHeight + 50, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke(4));
					g.setColor(Color.BLACK);
					g.drawRect(j*brickWidth + 80, i*brickHeight + 50, brickWidth, brickHeight);
                               
				}
			}
			
		}
	}
	
	// this sets the value of brick to 0 if it is hit by the ball
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}

}

