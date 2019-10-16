package ca.mcgill.ecse223.quoridor.controller;

import java.io.BufferedWriter;
import java.util.List;

import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;


public class QuoridorController {
	public QuoridorController(){		
	}
	/**
	 * Set current player to complete its move 
	 * Feature:Switch player
	 * @author Xiangyu Li
	 * @param player
	 */
	public static void completeMove(Player player) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Set total thinking time for each player
	 * Feature: Set total thinking time
	 * @param minute
	 * @param second
	 */
	public static boolean setTotaltime(int minute, int second) {
		throw new UnsupportedOperationException();
	}
	/**
	 * @author Xiangyu Li
	 * Feature:Set total thinking time
	 *Stop black player's clock
	 */
	public static void stopblackclock() {
		throw new UnsupportedOperationException();	
	}
	/**
	 * @author Xiangyu Li
	 * Feature:Set total thinking time
	 * Run white player's clock
	 */
	public static void runwhiteclock() {
		throw new UnsupportedOperationException();
	}
	/**
	 * @author Xiangyu Li
	 * Feature: Set total thinking time
	 * Stop white player's clock
	 */
	public static void stopwhiteclock() {
		throw new UnsupportedOperationException();	
	}
	/**
	 * @author Xiangyu Li
	 * Featrue:Set total thinking time
	 * Run black player's clock
	 */
	public static void runblackclock() {
		throw new UnsupportedOperationException();
	}
	/**
	 * @author Hongshuo Zhou
	 * Feature: Start a new game
	 * Stop white player's clock
	 */
	public static void startGame() {
   		throw new java.lang.UnsupportedOperationException();
	}

	/** load position Feature
	 * Public method to load game 
	 * @author Hongshuo Zhou 
	 * @return Whether the game successfully loaded
	 * @param filename - name of game file
	 */
	public static void loadGame(String filename) {
  	  	throw new java.lang.UnsupportedOperationException();
	}
    /** load position Feature
	 * @author Hongshuo Zhou 
	 * @return the load result
	 */
	public static boolean getLoadResult() {
		throw new java.lang.UnsupportedOperationException();
	}

	
	public static boolean validatePosition() {
    		throw new java.lang.UnsupportedOperationException();
	}
	//////////////////////////////////////////////////////////////
	/**
	 * Move Wall Feature
	 * 
	 * @author aidanwilliams Will fail if position is not valid Updates game
	 *         position with candidate wall move
	 * @param move       - wall move candidate
	 * @param targetTile - new tile to move to
	 * @return whether or not the wall successfully moved
	 */

	public static boolean moveWall(WallMove curMove, Tile targetTile) {

		// take in a WallMove created in GrabWall feature and put the wall in the
		// targetTile
		// will validate position to ensure no overlapping

		throw new java.lang.UnsupportedOperationException();
	}

	public static boolean wallIsValid(WallMove checkMove, List<WallMove> wallMoves, PlayerPosition position1,
			PlayerPosition position2) {
		// loop through wall moves to see if any interfere with desired move to be made
		// check to see if wall to be moved overlaps with players or is out of bounds

		throw new java.lang.UnsupportedOperationException();
	}

	/**
	 * findTile helper method will find a tile given coordinates row and column
	 * 
	 * @param r
	 * @param c
	 * @return tile at location
	 */
	public static Tile findTile(int r, int c) {
		// use row and col to find the tile we want

		throw new java.lang.UnsupportedOperationException();
	}

	/////////////////////////////////////////////////////////////
	/**
	 * Grab Wall feature
	 * 
	 * @author aidanwilliams Checks to make sure player has walls and creates
	 *         WallMove object to be passed on to move wall feature
	 * @param aWall - wall to grab
	 * @return
	 */

	public static WallMove grabWall(Wall aWall) {
		// will take in a wall and create a wall move object with some default values

		throw new java.lang.UnsupportedOperationException();
	}

	/**
	 * isSide helper method Checks whether wallMove targetTile is on side of board
	 * 
	 * @param aWallMove
	 * @return boolean
	 */
	public static boolean isSide(WallMove aWallMove) {
		throw new java.lang.UnsupportedOperationException();
	}
	////////////////////////////////////////////////////////////////
  
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
	
	///////////////////////////////////////////////////////////////////////////
	/*
	 * @author Keanu Natchev
	 * Checks whether or not the position is valid
	 */
		
	public static boolean validPosition() {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/*
	 * @author Keanu Natchev
	 * @param userName - the username that needs to be checked
	 * Checks whether or not there is a user with the same name as the input
	 */

	public static boolean ExistingUserName(String userName) {
		throw new java.lang.UnsupportedOperationException();
	}

	///////////////////////////////////////////////////////////////////////////
	

	/**
	 * Feature 4. Initialize wall
	 * This methods sets the board to its initial position and the player's stock of 
	 * walls and clocks are counting down so that they can start playing the game
	 * @author Matteo Nunez
	 * @param board - board object that is going to be initialize
	 */
	public static void initializeBoard(Board board) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** 
	 * Feature 5. Rotate Wall
	 * This method rotates the grabbed wall by 90 degrees (from horizontal to 
	 * vertical or vice versa) to adjust its designated target position
	 * @author Matteo Nunez
	 * @param wall - wall object that is going to be rotated
	 */
	public static void rotateWall(Wall wall) {
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
