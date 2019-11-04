package ca.mcgill.ecse223.quoridor.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import ca.mcgill.ecse223.quoridor.view.QuoridorView;


public class QuoridorController {
	QuoridorView view;
	
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
		if(wallIsValid()) {
			return curMove.setTargetTile(targetTile);
		}
		return false;
	}

	public static boolean wallIsValid() {
		// loop through wall moves to see if any interfere with desired move to be made
		// check to see if wall to be moved overlaps with players or is out of bounds
		for(int move=0; move < QuoridorApplication.getQuoridor().getCurrentGame().numberOfMoves(); move++) {
			if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn() == QuoridorApplication.getQuoridor().getCurrentGame().getMove(move).getTargetTile().getColumn() && 
					QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow() == QuoridorApplication.getQuoridor().getCurrentGame().getMove(move).getTargetTile().getRow()) {
				return false;
			}
		}
		if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn() == QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn() && 
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow() == QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow()) {
			return false;
		}
		if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn() == QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn() && 
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow() == QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow()) {
			return false;
		}
		return true;
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
		if(r <= 0)
			r = 1;
		if(r > 9)
			r = 9;
		if(c <= 0)
			c = 1;
		if(c > 9)
			c = 9;
		return QuoridorApplication.getQuoridor().getBoard().getTile((r-1)*9+c-1);
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

	public static boolean grabWall() {
		// will take in a wall and create a wall move object with some default values
		WallMove newMove;
		QuoridorApplication.getQuoridor().getCurrentGame().setMoveMode(MoveMode.WallMove);
		int walls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().numberOfWalls();
		
		if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasWalls()) {
			newMove = new WallMove(QuoridorApplication.getQuoridor().getCurrentGame().getMoves().size()+1, 
											QuoridorApplication.getQuoridor().getCurrentGame().getMoves().size()/2+1, 
											QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove(), 
											defaultTile(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()), 
											QuoridorApplication.getQuoridor().getCurrentGame(), 
											Direction.Vertical, 
											QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getWall(walls-1));
			QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(newMove);
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
			.removeWall(newMove.getWallPlaced());
			
			return true;
		}
		return false;
		//throw new java.lang.UnsupportedOperationException();
	}
	
	/**
	 * defaultTile helper method assigns default starting tile for white and black player
	 * @param curPlayer
	 * @return defaultTile to start a wall move candidate
	 */
	public static Tile defaultTile(Player curPlayer) {
		if(curPlayer.hasGameAsBlack())
			return QuoridorApplication.getQuoridor().getBoard().getTile(0);
		return QuoridorApplication.getQuoridor().getBoard().getTile(81);
	}

	/**
	 * isSide helper method Checks whether wallMove targetTile is on side of board
	 * 
	 * @param aWallMove
	 * @return boolean
	 */
	public static boolean isSide(WallMove aWallMove) {
		if(aWallMove.getTargetTile().getColumn() == 1 || 
			aWallMove.getTargetTile().getColumn() == 9 || 
			aWallMove.getTargetTile().getRow() == 1 || 
			aWallMove.getTargetTile().getRow() == 9)
			return true;
		return false;
	}
	////////////////////////////////////////////////////////////////
  
    /** Drop Wall 
	 * Updates game position with candidate wall move 
	 * @return whether or not the wall successfully dropped
	 * @author Yanis Jallouli
	 */
	public static boolean dropWall() {
		//TODO: You forgot to set Move variables, like previous move or next move
		//I set the move variables, though it woul be better if this were done intializing the move
		//TODO: Check if you used move/round number. You probably used it wrong
		Game current = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition curPos = current.getCurrentPosition();
		
		Wall curWall = current.getWallMoveCandidate().getWallPlaced();
		
		List<WallMove> moveList = new ArrayList<WallMove>();
		for(Move move : current.getMoves()) {
			if(move instanceof WallMove) moveList.add((WallMove) move);
		}
		
		
		//If the wallmove candidate is valid
		if(wallIsValid())	 {
			
			//TODO: Create a .equals method for player <- This
			//If White
			if(curPos.getPlayerToMove().equals(current.getWhitePlayer())) {
				//Move the wall from the player stock to the board
				curPos.removeWhiteWallsInStock(curWall);
				curPos.addWhiteWallsOnBoard(curWall);

			//If Black
			} else {
				curPos.removeBlackWallsInStock(curWall);
				curPos.addBlackWallsOnBoard(curWall);
			}
			
			//TODO: Add GUI update steps
			//Add the move to game list
			current.getWallMoveCandidate().setPrevMove(current.getMove(current.getMoves().size() - 1));
			current.addMove(current.getWallMoveCandidate());
			
			
			//TODO: See if switch does this
			//Updating the position- see if needed (might be done in switch)
			
			current.addPosition(curPos);  //Switch player will need to make a new curPos
			GamePosition newPos;
			if(curPos.getPlayerToMove().equals(current.getWhitePlayer())) {
				newPos = new GamePosition(curPos.getId() + 1, 
										  curPos.getWhitePosition(), curPos.getBlackPosition(), 
							   		      current.getWhitePlayer(), 
										  QuoridorApplication.getQuoridor().getCurrentGame() );
			} else {
				newPos = new GamePosition(curPos.getId() + 1, 
						  curPos.getWhitePosition(), curPos.getBlackPosition(), 
			   		      current.getWhitePlayer(), 
						  QuoridorApplication.getQuoridor().getCurrentGame() );
			}
			current.setCurrentPosition(newPos);
			
			
			
			//Alright complete needs to set MoveMode, PlayertoMove
			current.setWallMoveCandidate(null);
			
			completeMove(curPos.getPlayerToMove());
			return true;
			
		} else {
			//TODO: Add GUI notification steps
			return false;
		}
	}
	
	/** Move Is Registered
	 * Query method to check if a wall move was properly registered in the game
	 * @param dir - Direction of wall to check
	 * @param row - row of wall to check (defined by northwest)
	 * @param col - column of wall to check (defined by northwest)
	 * @author Yanis Jallouli
	 */
	public static boolean moveIsRegistered(Direction dir, int row, int col) {

		Game current = QuoridorApplication.getQuoridor().getCurrentGame();
		Move lastMove = current.getMoves().get(current.getMoves().size() - 1);
		
		if(lastMove instanceof WallMove) {
			
			WallMove lastWallMove = (WallMove) lastMove;
			
			//If we completed a move: Check if the last move is the one desired
			if(current.getWallMoveCandidate() == null) {
				if(findTile(row, col).equals(lastWallMove.getTargetTile()) &&
				   dir == lastWallMove.getWallDirection()                ) {
						return true;
					}
			} 
		}
		
		//If 1)move!=completed, 2) LastMove!=wallMove, 3) LastWallMove!=desired
		//Then it wasn't registered
		return false;
	}
	
	
	
	/** Save Position Feature
	 * Public method to save current game into a given .txt file
	 * @return Whether the method successfully saved
	 * @param filePath - the name of the save file to write to.
	 * @author Yanis Jallouli
	 */
	public static boolean savePosition(String filePath) {
		File tmp = new File(filePath);
		
		//No easy way to write certain lines of file, so I just remake it every time
		if(!containsFile(filePath)) {
			createFile(filePath);
		} else {
			deleteFile(filePath);
			createFile(filePath);
		}
		File fil = new File(filePath);
		
		
		/* 1) Writes white position (playerPos & walls) in file
		 * 2) Writes black position the same way on new line
		 * 3) Go down two lines and start writing white/black moves of each round
		 */	
		
		try {
			List<Move> moves = QuoridorApplication.getQuoridor().getCurrentGame().getMoves();
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fil)));
	
			writer.print("W: ");
			
			Tile whitePos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
			
			char col = (char) ((whitePos.getColumn() -1) - 'a');
			writer.print(col);
			writer.print(whitePos.getRow());
			writer.print(", ");
			
			
			for(int i = 0; i < moves.size(); i++) {
				Move move = moves.get(i);
				//Assumes White Player moves first
				if(i % 2 == 0 && move instanceof WallMove) {
					writeWall((WallMove) move, writer);
					writer.print(", ");
				}
					
			}
			writer.println();
			
			writer.print("B: ");
			
			Tile blackPos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile();
			
			col = (char) ((blackPos.getColumn() -1) - 'a');
			writer.print(col);
			writer.print(blackPos.getRow());
			writer.print(", ");
			
			
			for(int i = 0; i < moves.size(); i++) {
				Move move = moves.get(i);
				//Assumes Black Player moves second
				if(i % 2 == 1 && move instanceof WallMove) {
					writeWall((WallMove) move, writer);
					writer.print(", ");
				}	
			}
			
			writer.println();
			writer.println();
			
			
			
			
			//Ok this looks massive but it's simple. For each round, it prints the target tile of white&black's move
			// The conditionals are all just type of move made and move number within the round
			for(int i = 0; i < moves.size(); i++) {
				Move move = moves.get(i);
				
				//If even,we're starting a new line
				if(i % 2 == 0) {
					writer.print(move.getRoundNumber() + ". ");
					
					//WALLMOVE- print e3h or a4v, or any other such wall placement 
					if(move instanceof WallMove) {
						writeWall((WallMove) move, writer);
						writer.print(" ");
						
					//PLAYERMOVE - print b5 or h7 or whatever other move was made
					} else { 
						writePlayer(move, writer);
						writer.print(" ");
					}	
					
				//Basically same as above, but ending current line	
				} else {				
					if(move instanceof WallMove) {
						writeWall((WallMove) move, writer);
						writer.println();
					} else { 
						writePlayer(move, writer);
						writer.println();
					}			
				}	
			}
			if(writer.checkError() ) throw new IOException();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//TODO: Make Javadoc
	private static void writeWall(WallMove move, PrintWriter writer) throws IOException {
		char col = (char) ((move.getTargetTile().getColumn() -1) - 'a');
		writer.print(col);
		writer.print(move.getTargetTile().getRow());
		if(move.getWallDirection() == Direction.Horizontal) writer.print("h");
		else writer.print("v");
	}
	
	private static void writePlayer(Move move, PrintWriter writer) throws IOException {
		char col = (char) ((move.getTargetTile().getColumn() -1) - 'a');
		writer.print(col);
		writer.print(move.getTargetTile().getRow());
	}
	
	
	
	
	/** Query method to check if a file is exists within the file system.
	 * @param filepath - the file to check for
	 * @return boolean - whether the file was found
	 * @author Yanis Jallouli
	 */
	public static boolean containsFile(String filepath) {
		//String workDirectory = System.getProperty("user.dir");
		File file = new File(filepath);
		return file.exists();
	}
	
	/** Method to check whether a save file has been updated with the current game.
	 * Uses move number and player turn to check.
	 * @param filepath - the file to check for updates
	 * @return boolean - whether an error occurred
	 * @author Yanis Jallouli
	 */
	public static boolean isUpdated(String filepath) {
		if(!containsFile(filepath)) return false;
		
		
		File fil = new File(filepath);
		int moveNumber;
		
		try {
			//Goes to the last line of the scanner
			Scanner scan = new Scanner(fil);
			scan.useDelimiter(Pattern.compile("."));
			//I have two empty lines. One in the middle and one at the end
			while(scan.hasNextLine() && !scan.nextLine().isEmpty()) {
				scan.nextLine();
			}
			scan.nextLine();
			while(scan.hasNextLine() && !scan.nextLine().isEmpty()) {
				scan.nextLine();
			}
			
			if(scan.hasNextInt()) {
				moveNumber = scan.nextInt();
			} else {
				moveNumber = 0;
			}
			
			scan.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		int realMoveNum = QuoridorApplication.getQuoridor().getCurrentGame().getMoves().size();
		if(realMoveNum == moveNumber) {
			return true;
		} else {
			return false;
		}

	}
	
	/** Create a save file within the file system (initializer).
	 * @param filepath - the file to create
	 * @return boolean - whether an error occurred
	 * @author Yanis Jallouli
	 */
	public static boolean createFile(String filepath) {
		//String workDirectory = System.getProperty("user.dir");
		File file = new File(filepath);
		if(!file.exists()) {
			try {return file.createNewFile();}
			catch (IOException e) {e.printStackTrace();}
		}
		return false;
	}
	
	//TODO: Make Javadoc
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) return false;
		return file.delete();
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
