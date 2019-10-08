package ca.mcgill.ecse223.quoridor.features;
/**
* Switchplayer is for swap turns between two players
* Clock for current player need to stop
* Clock for next player need to start
* Provides who is in this turn
* 
* @author Xiangyu Li
* 
*/
import java.util.Timer;
import java.util.TimerTask;

import ca.mcgill.ecse223.quoridor.model.Player;

public class Switchplayer {

	
	public Switchplayer() {
		
	}
	/**
	 * This method is used for swap turns and manage clock for each player 
	 * @param current is current player's clock 
	 * @param next is next player's clock
	 * @param player1, player2 players currently play this game 
	 * 
	 */	
	public static void makeTurn(Timer current, Timer next, Player currentplayer, Player nextplayer ) {
		try {
			current.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		 next.schedule(new TimerTask() {
		      public void run() {
		    	  }
		   }, nextplayer.getRemainingTime());
	}
}
