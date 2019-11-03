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
		Game curGame = QuoridorApplication.getQuoridor().getCurrentGame();
		if(player.equals(curGame.getWhitePlayer()) ) {
			curGame.getCurrentPosition().setPlayerToMove(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
		} else {
			curGame.getCurrentPosition().setPlayerToMove(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
		}

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
	
	//TODO: A* Algorithm
	public static boolean wallIsValid() {
		// loop through wall moves to see if any interfere with desired move to be made
		// check to see if wall to be moved overlaps with players or is out of bounds
		WallMove check = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		ArrayList<WallMove> existing = new ArrayList<WallMove>();
		for(Move m : QuoridorApplication.getQuoridor().getCurrentGame().getMoves()) {
			if (m instanceof WallMove) existing.add((WallMove) m);
		}
		
		if(check.getWallDirection() == Direction.Horizontal) {
			if(check.getTargetTile().getColumn() == 9) return false;
			for(WallMove ex : existing) {
				//Horizontal check- Horizontal placed
				if(ex.getWallDirection() == Direction.Horizontal) {
					
					if(ex.getTargetTile().getRow() == check.getTargetTile().getRow()) {
						if(Math.abs(ex.getTargetTile().getColumn() - check.getTargetTile().getColumn()) < 2 ) {
							return false;
						}
					}
				//Horizontal check- Vertical Place
				} else {
					if(ex.getTargetTile().getRow() == check.getTargetTile().getRow() 
							&& ex.getTargetTile().getColumn() == check.getTargetTile().getColumn()) {
								return false;
					}
				}
			}	
			
		} else {
			if(check.getTargetTile().getRow() == 1) return false;
			for(WallMove ex : existing) {
				//Vertical check- Horizontal placed
				if(ex.getWallDirection() == Direction.Horizontal) {
					if(ex.getTargetTile().getRow() == check.getTargetTile().getRow() 
					&& ex.getTargetTile().getColumn() == check.getTargetTile().getColumn()) {
						return false;
					}
				//Vertical check- Vertical Place
				} else {
					if(ex.getTargetTile().getColumn() == check.getTargetTile().getColumn()) {
								if(Math.abs(ex.getTargetTile().getRow() - check.getTargetTile().getRow()) < 2 ) {
									return false;
								}
					}
				}
			}
		}
		return true;
		
	}
	public static WallMove invalidWall() {
		WallMove check = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		ArrayList<WallMove> existing = new ArrayList<WallMove>();
		for(Move m : QuoridorApplication.getQuoridor().getCurrentGame().getMoves()) {
			if (m instanceof WallMove) existing.add((WallMove) m);
		}
		
		if(check.getWallDirection() == Direction.Horizontal) {
			for(WallMove ex : existing) {
				//Horizontal check- Horizontal placed
				if(ex.getWallDirection() == Direction.Horizontal) {
					
					if(ex.getTargetTile().getRow() == check.getTargetTile().getRow()) {
						if(Math.abs(ex.getTargetTile().getColumn() - check.getTargetTile().getColumn()) < 2 ) {
							return ex;
						}
					}
				//Horizontal check- Vertical Place
				} else {
					if(ex.getTargetTile().getRow() == check.getTargetTile().getRow() 
							&& ex.getTargetTile().getColumn() == check.getTargetTile().getColumn()) {
								return ex;
					}
				}
			}	
			
		} else {
			for(WallMove ex : existing) {
				//Vertical check- Horizontal placed
				if(ex.getWallDirection() == Direction.Horizontal) {
					if(ex.getTargetTile().getRow() == check.getTargetTile().getRow() 
					&& ex.getTargetTile().getColumn() == check.getTargetTile().getColumn()) {
						return ex;
					}
				//Vertical check- Vertical Place
				} else {
					if(ex.getTargetTile().getColumn() == check.getTargetTile().getColumn()) {
								if(Math.abs(ex.getTargetTile().getRow() - check.getTargetTile().getRow()) < 2 ) {
									return ex;
								}
					}
				}
			}
		}
		return null;
	}

	/**
	 * findTile helper method will find a tile given coordinates row and column
	 * 
	 * @param r
	 * @param c
	 * @return tile at location
	 */
	public static Tile findTile(int r, int c) {
		// use row and col to find the tile we want. No guarantee tiles are in order
		for(Tile t : QuoridorApplication.getQuoridor().getBoard().getTiles()) {
			if(r == t.getRow() && c==t.getColumn()) return t;
		}
		return null;
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
	 * @author Yanis Jallouli
	 */
	public static void dropWall() {
		
		Game current = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition curPos = current.getCurrentPosition();
		Wall curWall = current.getWallMoveCandidate().getWallPlaced();
		
		
		//View checks if it's valid for us
		
		//Remove Walls from stock and place on board
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
		
		
		//Add the move to game list
		if(current.getMoves().size() > 0) {
			current.getWallMoveCandidate().setPrevMove(current.getMove(current.getMoves().size() - 1));
		} else {
			current.getWallMoveCandidate().setPrevMove(null);
		}
		current.addMove(current.getWallMoveCandidate());
		
		//Update currentPosition to include new wall //TODO: See if switch does it
		
		//Add current pos to game's array
		current.addPosition(curPos);
		
		//Create a new current Pos
		GamePosition newPos;
		if(curPos.getPlayerToMove().equals(current.getWhitePlayer())) {
			PlayerPosition white = new PlayerPosition(current.getWhitePlayer(), curPos.getWhitePosition().getTile());
			PlayerPosition black = new PlayerPosition(current.getBlackPlayer(), curPos.getBlackPosition().getTile());
			newPos = new GamePosition(curPos.getId() + 1, 
									  white, black, 
						   		      current.getWhitePlayer(), 
									  QuoridorApplication.getQuoridor().getCurrentGame() );
		} else {
			PlayerPosition white = new PlayerPosition(current.getWhitePlayer(), curPos.getWhitePosition().getTile());
			PlayerPosition black = new PlayerPosition(current.getBlackPlayer(), curPos.getBlackPosition().getTile());
			newPos = new GamePosition(curPos.getId() + 1, 
					  white, black, 
		   		      current.getWhitePlayer(), 
					  QuoridorApplication.getQuoridor().getCurrentGame() );
		}
		current.setCurrentPosition(newPos);
		
		
		
		//Set move type/wallmove cand. to null
		current.setWallMoveCandidate(null);
		current.setMoveMode(null);
		
		completeMove(curPos.getPlayerToMove());
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
		if(current.getMoves().size() == 0) return false;
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
	
	/**Get Walls
	 * Query method that returns an ArrayList of all wall moves
	 * registered within the current game.
	 * @return list of wall moves
	 * @author Yanis Jallouli
	 */
	public static ArrayList<WallMove> getWalls() {
		ArrayList<WallMove> walls = new ArrayList<WallMove>();
		for(Move move : QuoridorApplication.getQuoridor().getCurrentGame().getMoves()) {
			if(move instanceof WallMove) walls.add((WallMove) move);
		}
		return walls;
	}
	
	/** Save Position Feature
	 * Public method to save current game into a given .txt file
	 * @return Whether the method successfully saved
	 * @param filePath - the name of the save file to write to.
	 * @author Yanis Jallouli
	 */
	public static boolean savePosition(String filePath) {
		//No easy way to write certain lines of file, so I just remake it every time
		if(!containsFile(filePath)) {
			createFile(filePath);
		} else {
			deleteFile(filePath);
			createFile(filePath);
		}
		File fil = new File(filePath);
		
		
		/* 1) Writes white position (playerPos & walls) in file "W: e3, a4h, e8v..."
		 * 2) Writes black position the same way on new line "B: a4, c3v, f6v..."
		 * 3) Go down two lines and start writing white/black moves of each round "1. e4h a5\n2. e2 d5v\n3. ..."
		 */	
		
		try {
			List<Move> moves = QuoridorApplication.getQuoridor().getCurrentGame().getMoves();
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fil)));
	
			writer.print("W: ");
			Tile whitePos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
									.getWhitePosition().getTile();
			char col = (char) ((whitePos.getColumn() -1) + 'a');
			writer.print(col);
			writer.print(whitePos.getRow());
			
			for(WallMove move : getWalls()) {
				//Assumes White Player moves first
				if(move.getMoveNumber() % 2 == 1) {
					writer.print(", ");
					writeWall((WallMove) move, writer);
				}	
			}
			
			writer.println();
			
			writer.print("B: ");
			Tile blackPos = QuoridorApplication.getQuoridor().getCurrentGame()
									.getCurrentPosition().getBlackPosition().getTile();		
			col = (char) ((blackPos.getColumn() -1) + 'a');
			writer.print(col);
			writer.print(blackPos.getRow());
			
			
			
			for(WallMove move : getWalls()) {
				//Assumes Black Player moves second
				if(move.getMoveNumber() % 2 == 0) {
					writer.print(", ");
					writeWall((WallMove) move, writer);
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
	
	/**Private helper method that writes a wall into a file with the correct syntax
	 * Does not account for anything other than column, row, and direction
	 * @param move - WallMove to write into file
	 * @param writer - PrintWriter set to write into a file
	 * @throws IOException - thrown in case of error
	 * @author Yanis Jallouli
	 */
	private static void writeWall(WallMove move, PrintWriter writer) throws IOException {
		char col = (char) ((move.getTargetTile().getColumn() -1) + 'a');
		writer.print(col);
		writer.print(move.getTargetTile().getRow());
		if(move.getWallDirection() == Direction.Horizontal) writer.print("h");
		else writer.print("v");
	}
	
	/**Private helper method that writes a player into a file with the correct syntax
	 * Does not account for anything other than column, and row
	 * @param move - PlayerMove to write into file
	 * @param writer - PrintWriter set to write into a file
	 * @throws IOException - thrown in case of error
	 * @author Yanis Jallouli
	 */
	private static void writePlayer(Move move, PrintWriter writer) throws IOException {
		char col = (char) ((move.getTargetTile().getColumn() -1) + 'a');
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
		int moveNumber = 0;
		
		try {
			//Goes to the last line of the scanner
			Scanner scan = new Scanner(fil);
			scan.useDelimiter(Pattern.compile("."));
			//I have two empty lines. One in the middle and one at the end
			while(scan.hasNextLine()) {
				scan.nextLine();
				if(scan.hasNextInt()) {
					moveNumber = scan.nextInt();
				}
			}
			
			scan.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		int realMoveNum = QuoridorApplication.getQuoridor().getCurrentGame().getMoves().size();
		if(realMoveNum == moveNumber) {
			//This is to combat the "File is up to date but not modified('updated')"
			//Essentially I'm setting last mod to 0 when we update, or a high number when we don't
			//TODO: Remove this once we don't need Gherkin
			if(fil.lastModified() > 10000) {	
				return false;
			}
			
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
	
	/** A method to delete a file from the fileSystem
	 * @param filePath - path to the file to be deleted
	 * @return Boolean- whether the file was deleted succesfully
	 * @author Yanis Jallouli
	 */
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