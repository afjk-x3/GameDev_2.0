package LABANAN;

import java.awt.Graphics;

import entities.Player;
import entities.PlayerTwo;
import entities.Platform;

public class Game implements Runnable{

    private GamePanel GP;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private Platform platform;
    private Player player;
    private PlayerTwo player2;
    
    public Game() {
        initClasses();
        
        GP = new GamePanel(this);
        new GameWindow(GP);
        GP.requestFocus();
        startGameLoop();
    }
    
    private void initClasses() {
        platform = new Platform(700, 650, 503, 0, 250, 503, 288, 0, 1350, 503, 288, 0, 0, 5000, 10000, 10); // x, y, length, height, L/R, fall 
        player = new Player(745, 200, platform);
        player2 = new PlayerTwo(1045,200, platform);
    }
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void update() {
        player.update();
        
        if(platform.isPlayerFalling(this)) {
            platform.respawnPlayer(this); //Respawn the player
        }
        
        player2.update();
        if(platform.isPlayerFalling2(this)) {
            platform.respawnPlayer2(this); //Respawn the player2
        }
        
     // Check collision for attacks between Player 1 and Player 2
        if (player.checkAttackCollision(player2)) {
            System.out.println("Player 1 landed an attack on Player 2!");
        }
        if (player2.checkAttackCollision(player)) {
            System.out.println("Player 2 landed an attack on Player 1!");
        }
        
    }

    public void render(Graphics g) {
        player.render(g);
        player2.render(g);
        platform.render(g); // Render the platform
    }

    @Override
    public void run() {
        
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        
        long previousTime = System.nanoTime();
        
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        
        double deltaU = 0;
        double deltaF = 0;
        
        while(true) {
            
            long currentTime = System.nanoTime();
            
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            
             while (deltaU >= 1) {
                    GP.updateGame(); // Update game only if not paused
                    updates++;
                    deltaU--;
                }

                // Render the game (FPS)
                if (deltaF >= 1) {
                    GP.repaint();
                    frames++;
                    deltaF--;
                }

                // Output debug info every second
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;
                }
        }
        
    }
    public void windowFocusLost() {
        player.resetDirBooleans();
        player2.resetDirBooleans();
    }
    public Player getPlayer() {
        return player;
    }
    
    public PlayerTwo getPlayer2() {
        return player2;
    }
}