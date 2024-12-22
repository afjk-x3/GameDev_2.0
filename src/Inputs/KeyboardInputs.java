package Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import LABANAN.GamePanel;
import entities.Player;
import entities.PlayerTwo;

import static utilz.Constants.Directions.*;

public class KeyboardInputs implements KeyListener{
	
	private GamePanel GP;
	
	public KeyboardInputs(GamePanel GP) {
		this.GP = GP;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    Player player1 = GP.getGame().getPlayer();
	    PlayerTwo player2 = GP.getGame().getPlayer2();

	    switch (e.getKeyCode()) {
	        // Player 1 Controls
	        case KeyEvent.VK_W -> player1.setJump(true);
	        case KeyEvent.VK_A -> player1.setLeft(true);
	        //case KeyEvent.VK_S -> player1.setDown(true);
	        case KeyEvent.VK_D -> player1.setRight(true);
	        case KeyEvent.VK_C -> player1.setAttack(true);
	        case KeyEvent.VK_V -> player1.setSungkit(true);
	        case KeyEvent.VK_E -> player1.setLaunch(true);
	        case KeyEvent.VK_Q -> player1.setBlocking(true);

	        // Player 2 Controls
	        case KeyEvent.VK_UP -> player2.setJump(true);
	        case KeyEvent.VK_LEFT -> player2.setLeft(true);
	       // case KeyEvent.VK_DOWN -> player2.setDown(true);
	        case KeyEvent.VK_RIGHT -> player2.setRight(true);
	        case KeyEvent.VK_NUMPAD1 -> player2.setAttack(true);
	        case KeyEvent.VK_NUMPAD2 -> player2.setSungkit(true);
	        case KeyEvent.VK_NUMPAD3 -> player2.setLaunch(true);
	        case KeyEvent.VK_NUMPAD0 -> player2.setBlocking(true);
	    }
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    Player player1 = GP.getGame().getPlayer();
	    PlayerTwo player2 = GP.getGame().getPlayer2();

	    switch (e.getKeyCode()) {
	        // Player 1 Controls
	        case KeyEvent.VK_A -> player1.setLeft(false);
	    //    case KeyEvent.VK_S -> player1.setDown(false);
	        case KeyEvent.VK_D -> player1.setRight(false);
	        case KeyEvent.VK_Q -> player1.setBlocking(false);

	        // Player 2 Controls
	        case KeyEvent.VK_LEFT -> player2.setLeft(false);
	    //    case KeyEvent.VK_DOWN -> player2.setDown(false);
	        case KeyEvent.VK_RIGHT -> player2.setRight(false);
	        case KeyEvent.VK_NUMPAD0 -> player2.setBlocking(false);
	    }

	}
	
}
