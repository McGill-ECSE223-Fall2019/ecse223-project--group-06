package ca.mcgill.ecse223.quoridor.controller;

import java.io.BufferedWriter;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Move;

public class QuoridorController {
	
	/** Drop Wall 
	 * Updates game position with candidate wall move 
	 * @param game - current game
	 * @return whether or not the wall successfully dropped
	 * @author Yanis Jallouli
	 */
	public static boolean dropWall(Game game) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** Move Is Registered
	 * Query method to check if a wall move was properly registered in the game
	 * @param dir - Direction of wall to check
	 * @param row - row of wall to check (defined by northwest)
	 * @param col - column of wall to check (defined by northwest)
	 * @author Yanis Jallouli
	 */
	public static boolean moveIsRegistered(Game game, Direction dir, int row, int col) {
		throw new java.lang.UnsupportedOperationException();
	}
	//wallIsValid covered by Aidan

	
	
	/** Save Position Feature
	 * Public method to save current game into a given .txt file
	 * @return Whether the method successfully saved
	 * @param game - the game instance to be saved
	 * @param filePath - the name of the save file to write to.
	 * @author Yanis Jallouli
	 */
	public static boolean savePosition(Game currentGame, String filePath) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** Query method to check if a file is exists within the file system.
	 * @param filepath - the file to check for
	 * @return boolean - whether the file was found
	 * @author Yanis Jallouli
	 */
	public static boolean containsFile(String filepath) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** Method to check whether a save file has been updated with the current game.
	 * Uses move number and player turn to check.
	 * @param currentGame - the current game in quoridor
	 * @param filepath - the file to check for updates
	 * @return boolean - whether an error occurred
	 * @author Yanis Jallouli
	 */
	public static boolean isUpdated(Game currentGame, String filepath) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** Create a save file within the file system (initializer).
	 * @param currentGame - the current game to save in the new file
	 * @param filepath - the file to create
	 * @return boolean - whether an error occurred
	 * @author Yanis Jallouli
	 */
	public static boolean createFile(Game currentGame, String filepath) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	
	/** Private helper method encapsulating writing player moves into the save file
	 * 	Takes an initialized writer and move- writes it with predefined syntax.
	 * 	Does not account for player.  
	 *  Throws IOException in case of failure
	 * @param writer - an initialized BufferWriter to write in the file
	 * @param move - the move to write, containing round number, type, start tile, and end tile
	 * @author Yanis Jallouli
	 */
	private static void writePosition(BufferedWriter writer, Move move) {
		throw new java.lang.UnsupportedOperationException();
	}
}
