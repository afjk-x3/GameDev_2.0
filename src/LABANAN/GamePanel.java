package LABANAN;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Inputs.KeyboardInputs;


public class GamePanel extends JPanel {

    private static final long serialVersionUID = 2233953016340222282L;
    
   
    private Game game;
    private Image bg; // Declare the image variable for the background
    private Image mainPlatform, leftPlatform, rightPlatform;

    private boolean isPaused = false; // Pause state
    private int exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight;

    public GamePanel(Game game) {
      
        this.game = game;

        setPanelSize();
        loadBackgroundImage(); // Load the image here
        loadPlatformImage();
        addKeyListener(new KeyboardInputs(this));
    
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPaused) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    if (mouseX >= exitButtonX && mouseX <= exitButtonX + exitButtonWidth &&
                        mouseY >= exitButtonY && mouseY <= exitButtonY + exitButtonHeight) {
                        System.exit(0); // Exit the game
                    }
                }
            }
        });

        // Enable focus for keyboard inputs
        setFocusable(true);
        requestFocusInWindow();

        // Initialize button dimensions
        exitButtonWidth = 200;
        exitButtonHeight = 50;
    }

    // Method to toggle the pause state
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            System.out.println("Game Paused");
        } else {
            System.out.println("Game Resumed");
        }
        repaint();
    }

     //Method to load the background image
    private void loadBackgroundImage() {
        try {
            bg = ImageIO.read(getClass().getResourceAsStream("/BACKGROUND.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading background image");
        }
    }
    
    private void loadPlatformImage() {
        try {
            mainPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM ORIGINAL.png"));
            leftPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM ORIGINAL.png"));
            rightPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM ORIGINAL.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading background image");
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1920, 1080);
        setPreferredSize(size);
    }

    public void updateGame() {
        if (!isPaused) {
            // Logic for updating game state
            game.update();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            g.drawImage(mainPlatform, 524, 345, 840, 500, null);
            g.drawImage(leftPlatform, 150, 206, 482, 500, null);
            g.drawImage(rightPlatform, 1250, 206, 482, 500, null);
        }

        if (isPaused) {
            // Render the pause menu overlay
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Paused", getWidth() / 2 - 100, getHeight() / 2 - 100);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press ESC to Resume", getWidth() / 2 - 120, getHeight() / 2);

            // Render Exit button
            exitButtonX = getWidth() / 2 - exitButtonWidth / 2;
            exitButtonY = getHeight() / 2 + 50;
            g.setColor(Color.RED);
            g.fillRect(exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Exit Game", exitButtonX + 50, exitButtonY + 30);
        } else {
            // Render the game elements
            game.render(g);
        }
    }

    public Game getGame() {
        return game;
    }
}