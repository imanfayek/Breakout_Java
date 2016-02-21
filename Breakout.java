/*
 * File: Breakout.java
 * -------------------
 * Name: Iman Fayek
 * Section Leader: Adam Rothman
 * 
 * This file plays Brick!
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/* Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/* Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/* Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/* Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/* Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/* Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/* Separation between bricks */
	private static final int BRICK_SEP = 4;

/* Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/* Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/* Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/* Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/* Number of turns */
	private static final int NTURNS = 3;

/* Runs Brick. */
	public void run() {
		
		setUpBreakOut();
		playBreakout();
				
	}
	
	/*
	 * this method actually allows you to play the game
	 */
	private void playBreakout() {
		
		for (int n = 0; n < NTURNS; n++) {
			
			GLabel livesDisplay = new GLabel("You have " + (NTURNS - n) + " turns left.");
			livesDisplay.setColor(Color.green.darker());
			livesDisplay.setFont("Serif-BOLD-15");
			livesDisplay.setLocation(WIDTH/10, HEIGHT - (PADDLE_Y_OFFSET/4));
			add(livesDisplay);
			
			ball();
			ballVelocity();
			add(startMessage);
			waitForClick();
			remove(startMessage);
			remove(livesDisplay);
			
			while(ball.getY() <= HEIGHT) {
				
				moveBall();
				findCollisions();
				if (bricksDestroyed == (NBRICKS_PER_ROW * NBRICK_ROWS))	break;
				bounceOffWalls();
					
			}
				
			if(bricksDestroyed == (NBRICKS_PER_ROW * NBRICK_ROWS)) break;
			remove(ball);
										
		}
		
		if (bricksDestroyed == (NBRICKS_PER_ROW * NBRICK_ROWS)) {
			
			add(winnerMessage);
			
		} else {
			
			add(loserMessage);
			
		}
			
	}
	
	/*
	 * this sets up the game and applies all the graphics
	 */
	private void setUpBreakOut() {
		
		resize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		startMessage();
		loserMessage();
		winnerMessage();
		brickWall();
		paddle();
		addMouseListeners();
						
	}
			
	/*
	 *this creates the multicolored brick wall  
	 */
	private void brickWall() {
		
		int heightTwoRows = (2 * BRICK_HEIGHT) + (2 * BRICK_SEP);
		
		int yRed = BRICK_Y_OFFSET;
		centerDoubleBrickRow(yRed, Color.RED);
		
		int yOrange = yRed + heightTwoRows;
		centerDoubleBrickRow(yOrange, Color.ORANGE);
		 
		int yYellow = yOrange + heightTwoRows;
		centerDoubleBrickRow(yYellow, Color.YELLOW);
		
		int yGreen = yYellow + heightTwoRows;
		centerDoubleBrickRow(yGreen, Color.GREEN);
		
		int yCyan = yGreen + heightTwoRows;
		centerDoubleBrickRow(yCyan, Color.CYAN);
		
	}
			
	/*
	 * this creates two centered rows of bricks 
	 * that you are to set the color of
	 */
	private void centerDoubleBrickRow(double y, Color c) {
		
		double x = (WIDTH - (NBRICKS_PER_ROW * BRICK_WIDTH) - ((NBRICKS_PER_ROW - 1) * BRICK_SEP)) / 2;
		brickRow(x, y, c);
		y -= BRICK_HEIGHT + BRICK_SEP;
		brickRow(x, y, c);
				
	}
	
	/*
	 * this creates a single row of bricks
	 */
	private void brickRow(double x, double y,  Color c) {
		
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			
			drawBrick(x, y, c);
			x += BRICK_WIDTH + BRICK_SEP;
			
		}
		
	}
	
	/*
	 * this creates a single brick
	 */
	private void drawBrick(double x, double y, Color c) {
		
		brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		brick.setColor(c);
		add(brick);
		
	}
	
	/*
	 * This moves the paddle to the location of the mouse
	 * and makes sure that the paddle remains within the game boundaries
	 * 	(non-Javadoc)
	 * @see acm.program.Program#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		
		int y = HEIGHT - PADDLE_HEIGHT - PADDLE_Y_OFFSET;
		if (e.getX() > (WIDTH - (PADDLE_WIDTH / 2))) {
			
			paddle.setLocation(WIDTH - PADDLE_WIDTH, y);
			
		} else  if (e.getX() < (PADDLE_WIDTH / 2)) {
			
			paddle.setLocation(0, y);
			
		} else {
			
			paddle.setLocation(e.getX() - (PADDLE_WIDTH / 2), y);
		}
						
	}
	
	/*
	 * this draws the paddle
	 * with a set y location
	 */
	private void paddle(){
		
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		paddle.setLocation((WIDTH / 2) - (PADDLE_WIDTH / 2), (HEIGHT - PADDLE_HEIGHT - PADDLE_Y_OFFSET));
		add(paddle);
		
	}
	
	/*
	 * this creates the ball!
	 */
	private void ball() {
		
		ball = new GOval(WIDTH/2 - BALL_RADIUS, HEIGHT/2 - BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.GRAY);
		add(ball);
								
	}
	
	/*
	 * this sets the velocity of the ball
	 */
	private void ballVelocity() {
		
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
						
	}
	
	/*
	 * this moves the ball
	 */
	private void moveBall() {
		
		ball.move(vx, vy);
		pause(10);
				
	}
	
	/* 
	 * this causes the ball to bounce off the wall boundaries
	 */
	private void bounceOffWalls() {
		
		if (ball.getX() <= 0) vx = -vx;
		if (ball.getX() + (2 * BALL_RADIUS) >= WIDTH) vx = -vx;
		if (ball.getY() <= 0) vy = -vy;
		
	}
	
	/*
	 * this finds if the ball collides with anything
	 * if it hits the paddle, it will bounce upwards
	 * if it hits a brick, it will remove the brick
	 */
	public void findCollisions() {
		
		topLeft = new GPoint(ball.getX(), ball.getY());
		topRight = new GPoint(ball.getX() + (2 * BALL_RADIUS), ball.getY()); 
		bottomLeft = new GPoint(ball.getX(), ball.getY() + (2 * BALL_RADIUS));
		bottomRight = new GPoint(ball.getX() + (2 * BALL_RADIUS), ball.getY() + (2 * BALL_RADIUS));
		middleRight = new GPoint(ball.getX(), ball.getY() + BALL_RADIUS);
		middleLeft = new GPoint(ball.getX() + (2 * BALL_RADIUS), ball. getY() + BALL_RADIUS);
				
		if (getCollidingObject(bottomLeft) == paddle) {
			
			vy = - Math.abs(vy);
			
		} else if (getCollidingObject (bottomRight) == paddle) {
			
			vy = - Math.abs(vy);
			
		} else if (getCollidingObject(middleRight) == paddle) {
			
			vy = - Math.abs(vy);
			
		} else if (getCollidingObject(middleLeft) == paddle) {
			
			vy = - Math.abs(vy);
			
		} else if (getCollidingObject(topLeft) == paddle) {
			
			vy = Math.abs(vy);
			
		} else if (getCollidingObject(topRight) == paddle) {
			
			vy = Math.abs(vy);
			
		} else {
				
			if (getElementAt(topLeft) != null) {
			
				remove(getCollidingObject(topLeft));
				bricksDestroyed++;
				vy = -vy;
			
			} else if (getElementAt(topRight) != null) {
			
				remove(getCollidingObject(topRight));
				bricksDestroyed++;
				vy = -vy;
			
			} else if (getElementAt(bottomLeft) != null) {
			
				remove(getCollidingObject(bottomLeft));
				bricksDestroyed++;
				vy = -vy;
			
			} else if (getElementAt(bottomRight) != null) {
			
				remove(getCollidingObject(bottomRight));
				bricksDestroyed++;
				vy = -vy;
					
			}
			
		}
								
	}
	
	/*
	 * this finds if there is an object at the point p and returns the element
	 */
	private GObject getCollidingObject(GPoint p) {
		
		return getElementAt(p);

	}
	
	/*
	 * the next few methods are text prompters 
	 */
	private void startMessage() {
		
		startMessage = new GLabel("Click to Start!");
		startMessage.setColor(Color.GREEN.darker());
		startMessage.setFont("Serif-BOLD-30");
		startMessage.setLocation(WIDTH/2 - (startMessage.getWidth() / 2), HEIGHT/2 + (startMessage.getAscent() / 2));
		
	}
	
	private void loserMessage() {
		
		loserMessage = new GLabel("You lose.");
		loserMessage.setColor(Color.GREEN.darker());
		loserMessage.setFont("Serif-BOLD-30");
		loserMessage.setLocation(WIDTH/2 - (loserMessage.getWidth() / 2), HEIGHT/2 + (loserMessage.getAscent() /2));
		
	}
	
	private void winnerMessage() {
		
		winnerMessage = new GLabel("You win. Chea.");
		winnerMessage.setColor(Color.green.darker());
		winnerMessage.setFont("Serif-BOLD-30");
		winnerMessage.setLocation(WIDTH/2 - (winnerMessage.getWidth() / 2), HEIGHT/2 + (loserMessage.getAscent() / 2));
		
	}
	
	/*
	 * these are the global variables used in the game	
	 */
	private int bricksDestroyed = 0;
	private GLabel startMessage, loserMessage, winnerMessage;
	private GPoint topLeft, topRight, bottomLeft, bottomRight, middleRight, middleLeft;
	private GRect brick;
	private GRect paddle;
	private GOval ball;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx, vy;
			
}
