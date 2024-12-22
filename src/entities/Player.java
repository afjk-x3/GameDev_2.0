package entities;

import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Player extends Entity{
	
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 15;
	private int playerAction = IDLE;
	private int playerDir = -1;
	private boolean moving = false, attacking = false, sungkit = false, launch = false, blocking = false;
	private boolean jumping = false, crouching = false, lookLeft = false, lookRight = false;
	private boolean left, up, right, down;
	private float playerSpeed = 2.07f;
	
	 int width;
	 int height;
	 float x;
	 float y;
	 private boolean isBlocking = false;
		
	 private int health = 100;
	 private int stamina = 100;
	 private int attackDamage = 20;
	 private int block;
	 //private int blockStaminaCost = 10;
	 private int attackRange = 50; // Attack range in pixels
	 
	//platform collision
	private Platform platform;
	private float gravity = 0.4f;  // Gravity force pulling the player down
	private float yVelocity = 9f;   // Vertical velocity
	private boolean isOnGround = false; // Whether the player is standing on the platform
	
	
	public Player(float x, float y, Platform platform) {
		super(x,y);
		this.x = x;
		this.y = y;
		this.width = 50;  
        this.height = 100;
        this.platform = platform; // Assign the platform for collision checks
		loadAnimations();
	}
	
	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
	}
	
	 public boolean checkAttackCollision(PlayerTwo opponent) {
	        if (attacking || sungkit || launch) {
	            // Define attack hitbox based on the player's exact rendering
	            int attackHitboxX = (int) (x + 35); // Align with the player's rectangle position
	            int attackHitboxY = (int) y;
	            int attackHitboxWidth = (int) width ; // Use the player's width for the attack hitbox
	           int attackHitboxHeight = (int) height; // Use the player's height foddddadasdr the attack hitbox

	            // Define opponent's hurtbox
	            int opponentHurtboxX = (int) opponent.getX();
	            int opponentHurtboxY = (int) opponent.getY();
	            int opponentHurtboxWidth = (int) opponent.getWidth();
	            int opponentHurtboxHeight = (int) opponent.getHeight();

	            // Check collision
	            boolean collision = attackHitboxX < opponentHurtboxX + opponentHurtboxWidth &&
	                                attackHitboxX + attackHitboxWidth > opponentHurtboxX &&
	                                attackHitboxY < opponentHurtboxY + opponentHurtboxHeight &&
	                                attackHitboxY + attackHitboxHeight > opponentHurtboxY;

	             if (collision) {
	                    if (opponent.isBlocking()) {
	                    // Reduce opponent's stamina for blocking
	                     int blockStaminaCost = 10; // Example stamina cost for blocking
	                     opponent.reduceStamina(blockStaminaCost);
	                     // Apply chip damage (a small portion of the attack damage)
	                    
	                     } else {
	                     // Apply full damage if not blocking
	                     opponent.applyDamage(attackDamage);
	                                    }
	                                    return true;
	                                }
	        }
	        return false;
	    }
    
   
 // Debugging: Render the attack hitbox
    public void renderAttackHitbox(Graphics g) {
        if (attacking || sungkit || launch) {
            int attackHitboxX = (int) x ;
            int attackHitboxY = (int) y;
            int attackHitboxWidth = (int) attackRange;
            int attackHitboxHeight = (int) height;

            // Draw the attack hitbox for debugging
            g.setColor(Color.RED);
            g.drawRect( attackHitboxX + 135 , attackHitboxY + 40, attackHitboxWidth - 35, attackHitboxHeight);
        }
    }
    
    
	public void render(Graphics g) {
		// Render player sprite (placeholder rectangle)
        g.setColor(Color.BLUE);
        g.drawRect((int) x + 35 , (int) y + 40, width, height);

        // Debugging: Render attack hitbox
        renderAttackHitbox(g);

        // Debugging: Render health and stamina
        g.setColor(Color.RED);
        g.drawString("Health: " + health, (int) x + 35, (int) y + 10);
       
        
		g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, 150, 150, null);
	}
	
	
	private void loadAnimations() {
		InputStream RedSprite = getClass().getResourceAsStream("/RED_SPRITESHEET.png");
		//InputStream BlueSprite = getClass().getResourceAsStream("/BLUE_SPRITESHEET.png");
		
		try {
			BufferedImage img = ImageIO.read(RedSprite);
			animations = new BufferedImage[20][12]; //[r][c]
			
			for(int j = 0; j < animations.length; j++) { //rows
				for(int i = 0; i < animations[j].length; i++) { //column
					animations[j][i] = img.getSubimage(i * 64, j * 64, 64, 64);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				RedSprite.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void updateAnimationTick() {

		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmounts(playerAction)) {
				aniIndex = 0;
				attacking = false;
				sungkit = false;
				launch = false;
				jumping = false;
			}
			
		}
		
	}
	
	private void setAnimation() {
		int startAni = playerAction;
		
		if(moving) {
			playerAction = RUNNING;
		} else
			playerAction = IDLE;
		
		if(attacking) {
			playerAction = ATTACK_1;
		}
		
		if(sungkit) {
			playerAction = DOWN_ATTK;
		}
		
		if(launch) {
			playerAction = JUMP_ATTK;
		}
		
		if(blocking) {
			playerAction = DOWN_BLOCK;
		}
		
		if(jumping) {
			playerAction = JUMP;
		}
		
		if(crouching) {
			playerAction = CROUCH;
		}
		
		if(startAni != playerAction) {
			resetAniTick();
		}
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		
		if(left && !right) {
			x -= playerSpeed;
			moving = true;
		} else if(right && !left){
			x += playerSpeed;
			moving = true;
		}else {
			moving = false;
		}
		
		 // Apply gravity if not on the ground
        if (!isOnGround) {
            yVelocity += gravity;  // Increase velocity due to gravity
        }

        float nextX = x;
        float nextY = y + yVelocity; // Add vertical velocity to position

        
        // Check collision with the left platform
        if (nextX + 64 > platform.getLeftX() && nextX < platform.getLeftX() + platform.getLeftWidth()) {
            if (nextY + 64 > platform.getLeftY() && y + 64 <= platform.getLeftY()) {
                // Player is falling onto the platform
                nextY = platform.getLeftY() - 95; // Position player on top of the platform
                yVelocity = 0;                // Stop vertical velocity
                isOnGround = true;            // Mark player as on the ground
            }
        }         
        
        // Check collision with the center platform
        else if (nextX + 64 > platform.getX() && nextX < platform.getX() + platform.getWidth()) {
            if (nextY + 64 > platform.getY() && y + 64 <= platform.getY()) {
                // Player is falling onto the platform
                nextY = platform.getY() - 105; // Position player on top of the platform
                yVelocity = 0;                // Stop vertical velocity
                isOnGround = true;            // Mark player as on the ground
            }
        }
        
        
        // Check collision with the right platform
        else if (nextX + 64 > platform.getRightX() && nextX < platform.getRightX() + platform.getRightWidth()) {
            if (nextY + 64 > platform.getRightY() && y + 64 <= platform.getRightY()) {
                // Player is falling onto the platform
                nextY = platform.getRightY() - 95; // Position player on top of the platform
                yVelocity = 0;                // Stop vertical velocity
                isOnGround = true;            // Mark player as on the ground
            }
        }
        else {
            isOnGround = false; // Player is not on any platform
        }

        // Update position
        x = nextX;
        y = nextY;
		
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
	}
	 
   
	
 // Method to apply damage to the player
    public void applyDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0; // Ensure health doesn't go negative
        }
    }
    
    public void reduceStamina(int amount) {
        stamina -= amount; // Reduce stamina by the given amount
        if (stamina < 0) {
            stamina = 0; // Prevent stamina from going below zero
        }
    }
    
	public void setAttack(boolean attacking) {
		this.attacking = attacking;
	}
	public boolean isAttacking() {
        return attacking;
    }
	
	public void setSungkit(boolean sungkit) {
		this.sungkit = sungkit;
	}
	
	public void setLaunch(boolean launch) {
		this.launch = launch;
	}
	//public void setBlock(boolean blocking) {
	//	this.blocking = blocking;
	//}
	
	public void block() {
    	isBlocking = true;
    }
    
    public boolean isBlocking() {
        return blocking;
    }
    public void setBlocking(boolean blocking) {
    	this.blocking = blocking;
    }

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJump(boolean jumping) {
		this.jumping = jumping;
		if (jumping && isOnGround) {
            yVelocity = -15;  // Apply an upward velocity to simulate jump
            isOnGround = false;
        }
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isCrouching() {
        return crouching;
    }
    public void setCrouch(boolean crouch) {
    	this.crouching = crouch;
    }
	
	
	//PLATFORM COLLSION
	 public float getX() {
	        return x;
	    }
	    public void setX(float x) {
	        this.x = x;
	    }

	    public float getY() {
	        return y;
	    }
	    public void setY(float y) {
	        this.y = y;
	    }
	    
	    public int getWidth() {
	        return width;
	    }
	    public void setWidth(int width) {
	        this.width = width;
	    }

	    public int getHeight() {
	        return height;
	    }
	    public void setHeight(int height) {
	        this.height = height;
	    }
	

}
