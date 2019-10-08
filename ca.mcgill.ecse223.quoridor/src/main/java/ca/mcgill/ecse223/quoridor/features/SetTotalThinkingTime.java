package ca.mcgill.ecse223.quoridor.features;
/**
* SetTotalThinkingTime is used to set think time for each player as long as the game starts
* 
* @author Xiangyu Li
* 
*/
import java.sql.Time;
import ca.mcgill.ecse223.quoridor.model.Player;

public class SetTotalThinkingTime {
	
	public SetTotalThinkingTime() {
	}
/**
 * This method is used for setting total thinking time for each players
 * @param totaltimet total thinking time for each player, input from UI 
 * @param player1, player2 players currently play this game 
 * 
 */
	public static void setTimefor2(Time totaltime, Player player1, Player player2) {
		player1.setRemainingTime(totaltime);
		player2.setRemainingTime(totaltime);
	}
	public static void setTimefor4(Time totaltime, Player player1, Player player2, Player player3, Player player4) {
		player1.setRemainingTime(totaltime);
		player2.setRemainingTime(totaltime);
		player3.setRemainingTime(totaltime);
		player4.setRemainingTime(totaltime);
	}
	
}
