package ca.mcgill.ecse223.quoridor.features;

import java.io.BufferedWriter;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Move;


/** Save Position 
 *  A class made solely of methods in order to save the current Game Position to a .txt file.
 *  To be used when a player hits the save button
 * @author Yanis Jallouli
 */
public class SavePosition {
	
	/** Save Position Feature
	 * Public method to save current game into a given .txt file
	 * @return Whether the method successfully saved
	 * @param game - the game instance to be saved
	 * @param filePath - the location of the save file to write to.
	 */
	public static boolean savePosition(Game currentGame, String filePath) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** Query method to check if a file is exists within the file system
	 * @param filepath - the file (including the full path) to check for
	 * @return boolean - whether the file was found
	 */
	public static boolean containsFile(String filepath) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** Modifier method to create a save file within the file system
	 * @param filepath - the file (including the full path) to create
	 * @return boolean - whether an error occurred
	 */
	public static boolean createFile(String filepath) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	
	/** Private helper method encapsulating writing player moves into the save file
	 * 	Takes an initialized writer and with a move- writes it with specified syntax
	 * 	Does not account for player.  
	 *  Throws IOException in case of failure
	 * @param writer - an initialized BufferWriter to write in the file
	 * @param move - the move to write, containing round number, type, start tile, and end tile
	 */
	private static void writePosition(BufferedWriter writer, Move move) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	
	
	
	
}
