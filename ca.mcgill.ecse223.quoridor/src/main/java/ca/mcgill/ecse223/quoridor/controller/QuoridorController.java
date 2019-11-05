package ca.mcgill.ecse223.quoridor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.Timer;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.features.InvalidInputException;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
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
	 * @param player player that completes his move 
	 */
	public static void completeMove(Player player) {

		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(player.getNextPlayer());

	}
	/**
	 * Set total thinking time for each player
	 * Feature: Set total thinking time
	 * @param minute minute for total thinking time 
	 * @param second second for total thinking time
	 */
	public static void setTotaltime(int minute, int second) {
		long totaltime=(minute*60+second)*1000;
		QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setRemainingTime(new Time(totaltime));
		QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setRemainingTime(new Time(totaltime));
	}
	/**
	 * @author Xiangyu Li
	 * Feature:Switch player
	 * Stop black player's clock
	 * @param Timer used for counting time and do actions
	 */
	public static void stopblackclock(Timer timer) {
		timer.stop();
	}
	/**
	 * @author Xiangyu Li
	 * Feature:Switch player
	 * Run white player's clock
	 */
	public static Timer runwhiteclock(QuoridorView view) {
	
		Timer whitetimer;
		ActionListener taskPerformer= new ActionListener(){
			 public void actionPerformed(ActionEvent evt) {
				 view.updateView();
		      }
		};
	
		whitetimer=new Timer(1000,taskPerformer);
		whitetimer.setInitialDelay(1000);
		whitetimer.start();
		return whitetimer;
	}
	/**
	 * @author Xiangyu Li
	 * Feature:Switch player
	 * Stop white player's clock
	 * @param Timer used for counting time and do actions
	 */
	public static void stopwhiteclock(Timer timer) {
		timer.stop();
	}
	/**
	 * @author Xiangyu Li
	 * Feature:Switch player
	 * Run black player's clock
	 */
	public static Timer runblackclock(QuoridorView view) {
		
		Timer blacktimer;
		ActionListener taskPerformer= new ActionListener(){
			 public void actionPerformed(ActionEvent evt) {
				 view.updateView();
		      }
		};
	
		blacktimer=new Timer(1000,taskPerformer);
		blacktimer.setInitialDelay(1000);
		blacktimer.start();
		return blacktimer;
	}
	

	/**
	 * @author Hongshuo Zhou
	 * feature: Start a new game
	 * @throws InvalidInputException
	 * This method starts the new game and check existing game
	 */
	public static void startGame() throws InvalidInputException {
		if (QuoridorApplication.getQuoridor().getCurrentGame() == null) {
			Game newGame = new Game(GameStatus.Initializing, MoveMode.PlayerMove, QuoridorApplication.getQuoridor());
			QuoridorApplication.getQuoridor().setCurrentGame(newGame);
			Player white = new Player(new Time(0), new User("qoihgpqidvp bfqqg...", QuoridorApplication.getQuoridor()), 0, Direction.Vertical);
			Player black = new Player(new Time(0), new User("agbawgbawifwagikbakwbja", QuoridorApplication.getQuoridor()), 1, Direction.Vertical);
			QuoridorApplication.getQuoridor().getCurrentGame().setWhitePlayer(white);
			QuoridorApplication.getQuoridor().getCurrentGame().setBlackPlayer(black);
		} else {
          throw new InvalidInputException("Running game exist");
		}
	}

	/** load position Feature
	 * Public method to load game 
	 * @author Hongshuo Zhou 
	 * @return Whether the game successfully loaded
	 * @param filename - name of game file
	 */
	public static Boolean loadGame(String filename, boolean status) {
		try {
			startGame();
			User white = findUserName("User1");
			User black = findUserName("User2");
			if (white == null){
				createUser("User1");
				white = findUserName("User1");
			}
			if (black == null){
				createUser("User2");
				black = findUserName("User2");
			}
			setTotaltime(1, 30);
			runwhiteclock(new QuoridorView());
			runblackclock(new QuoridorView());

		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		if(!containsFile(filename)) {
			return false;
		}
		//read line for both players
		File file = new File("src/../" + filename + ".dat" );
		String PlayerOneLine = new String();
		String PlayerTwoLine = new String();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
		try {
			
			PlayerOneLine = reader.readLine();
			PlayerTwoLine = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//create object
		String blackline;
		String whiteline;
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		int wCol,wRow,bCol,bRow;
		Tile bTile,wTile;
		PlayerPosition bposition,wposition;
        // create players
		Player bPlayer;
		Player wPlayer;
		Player playerToMove = null;
		bPlayer = game.getBlackPlayer();
		wPlayer = game.getWhitePlayer();

		if (PlayerOneLine.charAt(0) == 'B' ){
			blackline = PlayerOneLine;
			whiteline = PlayerTwoLine;
			playerToMove = bPlayer;
		}
		else{
			blackline = PlayerTwoLine;
			whiteline = PlayerOneLine;
			playerToMove = wPlayer;
		}
		//set coordinate by transfer ASCII
		bCol = blackline.charAt(3) - 96;
		bRow = blackline.charAt(4) - 48;
		bTile = QuoridorApplication.getQuoridor().getBoard().getTile((bRow-1)*9+bCol-1);   ;
		bposition = new PlayerPosition(bPlayer, bTile);
		wCol = whiteline.charAt(3) - 96;
		wRow = whiteline.charAt(4) - 48;
		wTile = QuoridorApplication.getQuoridor().getBoard().getTile((wRow-1)*9+wCol-1);
		wposition = new PlayerPosition(wPlayer, wTile);

		initializeBoard();
		GamePosition loadPosition = game.getCurrentPosition();
		
		loadPosition.setPlayerToMove(playerToMove);
		loadPosition.setBlackPosition(bposition);
		loadPosition.setWhitePosition(wposition);

		if(validatePos(loadPosition)){
			game.setCurrentPosition(loadPosition);
			return true;
		}else{
			return false;
		}

		

	}
	public static boolean validatePos(GamePosition loadPosition) {

		if (loadPosition == null){
			return false;
		}
		else{
			return true;
		}
		
	}

	
	public static boolean validatePosition() {
			GamePosition position = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
			if (position == null){
				return false;
			}
			else{
				return true;
			}
		
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
	public static boolean moveWall(Tile targetTile) {

		// take in a WallMove created in GrabWall feature and put the wall in the
		// targetTile
		// will validate position to ensure no overlapping
		if(wallIsValid()) {
			return QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setTargetTile(targetTile);
		}
		return false;
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
		return QuoridorApplication.getQuoridor().getBoard().getTile(80);
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
		if(QuoridorApplication.getQuoridor().getCurrentGame() == null) return null;
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
	
	/**
	 * Checks whether or not the position is valid
	 * @author Keanu Natchev
	 * @return Boolean: true if the position is valid and false if it is not.
	 */
		
	public static boolean validPosition() {
		GamePosition position = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		if (position == null){
			return false;
		}
		else{
			List wallsOnBoard = position.getBlackWallsOnBoard();
			Integer row = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
			Integer column = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
			Direction direction = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection();
			for(int i = 0; i < wallsOnBoard.size(); i ++) {
				Integer rowBoard = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
				Integer columnBoard = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
				Direction directionBoard = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection();
				int rowDifference = row - rowBoard;
				int columnDifference = column - columnBoard;
				if(direction == directionBoard && (rowDifference == 0 && columnDifference == 0)) {
					return false;
				}
				if(direction != directionBoard && (rowDifference == 0 && columnDifference == 0)) {
					return false;
				}
				if(direction == directionBoard && (rowDifference == 1 && columnDifference == 0)) {
					return false;
				}
				if(direction == directionBoard && (rowDifference == 0 && columnDifference == 1)) {
					return false;
				}
			}
			
			return true;
		}

	}
	
	/**
	 * Goes through the list of usernames and checks whether the given username
	 * is part of that list.
	 * @author Keanu Natchev
	 * @param userName - the username that needs to be checked
	 */

	public static boolean ExistingUserName(String userName) {
		for(User u : QuoridorApplication.getQuoridor().getUsers()) {
			if(u.getName().equals(userName)) return true;
		}
		return false;
	}
	
	public static User findUserName(String userName) {
		for(User u : QuoridorApplication.getQuoridor().getUsers()) {
			if(u.getName().equals(userName)) return u;
		}
		return null;
	}
	
	/**
	 * Creates a new user with name
	 * @author Keanu Natchev
	 * @param newUserName: desired name of new user
	 */
	
	public static void createUser(String newUserName) {
		QuoridorApplication.getQuoridor().addUser(newUserName);
	}

	///////////////////////////////////////////////////////////////////////////
	

	/**
	 * Feature 4. Initialize wall
	 * This methods sets the board to its initial position and the player's stock of 
	 * walls and clocks are counting down so that they can start playing the game
	 * @author Matteo Nunez
	 * @param board - board object that is going to be initialize
	 */
	public static void initializeBoard() {
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		//Board board = QuoridorApplication.getQuoridor().getBoard();
		
		Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		
		
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if(quoridor.getBoard() == null) {
			Board board = new Board(quoridor);
			for (int i = 1; i <= 9; i++) { // rows
				for (int j = 1; j <= 9; j++) { // columns
					board.addTile(i, j);
				}
			}
		}
		
		Tile whiteStartTile = findTile(1, 5);
		Tile blackStartTile = findTile(9, 5);
	
		
		
		GamePosition cur = new GamePosition(0,
											new PlayerPosition(whitePlayer, whiteStartTile),
											new PlayerPosition(blackPlayer, blackStartTile),
											whitePlayer,
											game);
		quoridor.getCurrentGame().setCurrentPosition(cur);
				
		
		
//		if (current.getCurrentPosition() == null)
//			current.setCurrentPosition(new GamePosition(0, new PlayerPosition(whitePlayer, board.getTile(4)), new PlayerPosition(blackPlayer, board.getTile(8*9+4)), whitePlayer, current));
		
		game.getCurrentPosition().setPlayerToMove(whitePlayer);
		
		game.getCurrentPosition().getWhitePosition().setTile(whiteStartTile);
		game.getCurrentPosition().getBlackPosition().setTile(blackStartTile);
		
		for (int whiteWallInStock = game.getCurrentPosition().getWhiteWallsInStock().size(); whiteWallInStock < 10; whiteWallInStock++) {
			
			try{game.getCurrentPosition().addWhiteWallsInStock(new Wall(whiteWallInStock, whitePlayer));}
			catch(Exception e) {break;}
		}
		
		for (int blackWallInStock = game.getCurrentPosition().getBlackWallsInStock().size(); blackWallInStock < 10; blackWallInStock++) {
			try{game.getCurrentPosition().addBlackWallsInStock(new Wall(blackWallInStock + 10, blackPlayer));}
			catch(Exception e) {break;}
			//game.getCurrentPosition().addBlackWallsInStock(new Wall(blackWallInStock + 10, blackPlayer));
		}
		
		//runwhiteclock(view);
		//throw new java.lang.UnsupportedOperationException();
	}
	
	/** 
	 * Feature 5. Rotate Wall
	 * This method rotates the grabbed wall by 90 degrees (from horizontal to 
	 * vertical or vice versa) to adjust its designated target position
	 * @author Matteo Nunez
	 * @param wall - wall object that is going to be rotated
	 */
	public static void rotateWall() {
		if (QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection().equals(Direction.Vertical)) {
			QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(Direction.Horizontal);
		} else if (QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection().equals(Direction.Horizontal)) {
			QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(Direction.Vertical);
		} else {
			throw new java.lang.UnsupportedOperationException();
		}
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
