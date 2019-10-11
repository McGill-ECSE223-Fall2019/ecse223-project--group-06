package ca.mcgill.ecse223.quoridor.features;

import java.util.List;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.WallMove;


/** Drop Wall
 * A class made of methods to drop a Quoridor wall onto the board
 * 
 * @author Yanis Jallouli
 */
public class DropWall {
	
	/** Drop Wall Feature
	 * Will fail if position is not valid
	 * Updates game position with candidate wall move 
	 * @param game - current game
	 * @return whether or not the wall successfully dropped
	 */
	public static boolean dropWall(Game game) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** Register Move
	 * Updates the game to include a new wall move with given specifications
	 * @param dir - Direction of placed wall
	 * @param row - row wall was placed at (defined by northwest)
	 * @param col - column wall was placed at (defined by northwest)
	 */
	public static void registerMove(Game game, Direction dir, int row, int col) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** End Move
	 * A clean up method to call after registering a move
	 * Resets things like wallMoveCandidate, MoveMode
	 * @param game - the game of which the current players move is ended
	 */
	public static void endMove(Game game) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	
	/** Check Wall Position Validity
	 *  Checks whether a candidate wall intersects with a placed wall or cuts off the player entirely
	 * @param checkMove - the WallMove to validate
	 * @param gameMoves - a list of all the moves made in the game (position of the walls)
	 * @return whether the wall is in a valid position
	 */
	public static boolean wallIsValid(WallMove checkMove, List<Move> gameMoves) {
		//In here I'll loop through the games moves and if they're walls
		//Determine wether they intersect with check move or... 
		//Somehow wether checkmove still leaves a path open
		throw new java.lang.UnsupportedOperationException();	
	}
}
