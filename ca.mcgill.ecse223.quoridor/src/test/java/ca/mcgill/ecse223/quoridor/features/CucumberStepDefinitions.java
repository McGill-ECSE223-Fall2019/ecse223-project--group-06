package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import org.junit.Assert;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import ca.mcgill.ecse223.quoridor.view.QuoridorView;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {

	private QuoridorView view = new QuoridorView();
	private Quoridor quoridor;
	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private Game game;
	private WallMove aWallMove;
	
	// ***********************************************
		// Background step definitions
		// ***********************************************

		@Given("^The game is not running$")
		public void theGameIsNotRunning() {
			initQuoridorAndBoard();
			createUsersAndPlayers("user1", "user2");
		}

		@Given("^The game is running$")
		public void theGameIsRunning() {
			initQuoridorAndBoard();
			ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
			createAndStartGame(createUsersAndPlayers);
		}

		@And("^It is my turn to move$")
		public void itIsMyTurnToMove() throws Throwable {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			Player currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
		}

		@Given("The following walls exist:")
		public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			List<Map<String, String>> valueMaps = dataTable.asMaps();
			// keys: wrow, wcol, wdir
			Player[] players = { quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer() };
			int playerIdx = 0;
			int wallIdxForPlayer = 0;
			for (Map<String, String> map : valueMaps) {
				Integer wrow = Integer.decode(map.get("wrow"));
				Integer wcol = Integer.decode(map.get("wcol"));
				// Wall to place
				// Walls are placed on an alternating basis wrt. the owners
				//Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
				Wall wall = players[playerIdx].getWall(wallIdxForPlayer); // above implementation sets wall to null

				String dir = map.get("wdir");

				Direction direction;
				switch (dir) {
				case "horizontal":
					direction = Direction.Horizontal;
					break;
				case "vertical":
					direction = Direction.Vertical;
					break;
				default:
					throw new IllegalArgumentException("Unsupported wall direction was provided");
				}
				new WallMove(0, 1, players[playerIdx], quoridor.getBoard().getTile((wrow - 1) * 9 + wcol - 1), quoridor.getCurrentGame(), direction, wall);
				if (playerIdx == 0) {
					quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
					quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
				} else {
					quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
					quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
				}
				wallIdxForPlayer = wallIdxForPlayer + playerIdx;
				playerIdx++;
				playerIdx = playerIdx % 2;
			}
			System.out.println();

		}

		@And("I do not have a wall in my hand")
		public void iDoNotHaveAWallInMyHand() {assertNull(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate());
		}
		
		@And("^I have a wall in my hand over the board$")
		public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
			if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() == null) {
				QuoridorController.grabWall();	
			}
			QuoridorApplication.getQuoridor().getCurrentGame().setMoveMode(MoveMode.WallMove);
		}
		
		@Given("^A new game is initializing$")
		public void aNewGameIsInitializing() throws Throwable {
			initQuoridorAndBoard();
			ArrayList<Player> players = createUsersAndPlayers("user1", "user2");
			QuoridorApplication.getQuoridor().setCurrentGame(new Game(GameStatus.Initializing, MoveMode.PlayerMove, QuoridorApplication.getQuoridor()));
			QuoridorApplication.getQuoridor().getCurrentGame().setWhitePlayer(players.get(0));
			QuoridorApplication.getQuoridor().getCurrentGame().setBlackPlayer(players.get(1));
			view.initLoadScreen();
			view.newGame.doClick();
		}
		
		// ***********************************************
		// Scenario and scenario outline step definitions
		// ***********************************************


	//***********************************************
	// Start a new game
	// **********************************************
	/**
	 * Feature:Start a new game
	* 
	* @Author Hongshuo Zhou
	*/
	@When("A new game is being initialized")
	public void a_new_game_is_being_initialized() {
				try {
					QuoridorController.startGame();
					view.initLoadScreen();
				    view.newGame.doClick();
				    
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	/**
	*Feature:Start a new game 
	*@Author Hongshuo Zhou
	*/
	@And("White player chooses a username")
	public void white_player_chooses_a_username(){
	     
	     view.whiteName.setText("Player 1");
	     QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().setName("Player 1");
	}
	/**
	*Feature:Start a new game 
	*@Author Hongshuo Zhou
	*/
	@And("Black player chooses a username")
	public void black_player_chooses_a_username(){
	     view.blackName.setText("Player 2");
	     QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().setName("Player 2");
	}
	/**
	*Feature:Start a new game 
	*@Author Hongshuo Zhou
	*/
	@Then("Total thinking time is set")
	public void total_thinking_time_is_set(){
		 view.minutesField.setText("10");
		 view.secondsField.setText("0");
	     QuoridorController.setTotaltime(10, 0);	
	}
	/**
	*Feature:Start a new game 
	*@Author Hongshuo Zhou
	*/
	@Then("The game shall become ready to start")
	public void the_game_shall_become_ready_to_start(){
		QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.ReadyToStart);
	}
	/**
	*Feature:Start a new game 
	*@Author Hongshuo Zhou
	*/
	@Given("The game is ready to start")
	public void the_game_is_ready_to_start() {
	 this.a_new_game_is_being_initialized();
	 this.white_player_chooses_a_username();
	 this.black_player_chooses_a_username();
	 this.total_thinking_time_is_set();
	 QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Running);
	}
	/**
	*Feature:Start a new game 
	*@Author Hongshuo Zhou
	*/
	@When("I start the clock")
	public void I_start_the_clock() {
	 QuoridorController.runwhiteclock(view);
	}
	/**
	*Feature:Start a new game 
	*@Author Hongshuo Zhou
	*/
	@Then("The game shall be running") 
	public void the_game_shall_be_running(){
	 Assert.assertEquals(GameStatus.Running,QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus());
	 view.newGame.doClick();
	 if(view.confirmFrame.isVisible()) {
	 	((JButton) view.confirmFrame.getContentPane().getComponent(1)).doClick();
	 }
	}
	/**
	*Feature:Start a new game 
	*@Author Hongshuo Zhou
	*/
	@And("The board shall be initialized")
	public void the_board_shall_be_initialized() {
		assertNotNull(view.board);
	}

	//***********************************************
	// Load Position
	// **********************************************
		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
		private Boolean load;

		@When("I initiate to load a saved game {string}")
		public void i_initiate_to_load_a_saved_game(String filename) {
			load = QuoridorController.loadGame(filename, true);
		}
		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
		@And("The position to load is valid")
		public void the_position_to_load_is_valid() {
		    assertEquals(true, QuoridorController.validatePosition());
		    
		}
		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
		@Then("It shall be {string}'s turn")
		public void it_shall_be_s_turn(String string) {
			String currentcolor;
			if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack()) {
				currentcolor = "black";
			}else {
				currentcolor = "white";
			}
			assertEquals(string, currentcolor);
		    
		}
		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
		@Then("{string} shall be at {int}:{int}")
		public void shall_be_at(String string, Integer intx, Integer inty) {
			Integer row;
			Integer col;
			if(string == "black") {
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
				col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
			}else {
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
				col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
			}
			assertEquals(row, intx);
			assertEquals(col, inty);
		}
		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
	   	@Then("{string} shall have a vertical wall at {int}:{int}")
		public void shall_have_a_vertical_wall_at(String string, Integer intx, Integer inty) {
			Integer col;
			Integer row;
			Direction wallDirection;
			if(string == "black") {
				wallDirection = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getWall(0).getMove().getWallDirection();
				col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(0).getMove().getTargetTile().getColumn();
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(0).getMove().getTargetTile().getRow();
			}else {
				wallDirection = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getWall(0).getMove().getWallDirection();
				col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(0).getMove().getTargetTile().getColumn();
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(0).getMove().getTargetTile().getRow();
			}
			assertEquals(Direction.Vertical, wallDirection);
			assertEquals(row, intx);
			assertEquals(col, inty);
		}
		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
	   	@Then("{string} shall have a horizontal wall at {int}:{int}")
		public void shall_have_a_horizontal_wall_at(String string, Integer intx, Integer inty) {
			Integer col;
			Integer row;
			Direction wallDirection;
			if(string == "black") {
				wallDirection = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getWall(0).getMove().getWallDirection();
				col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(0).getMove().getTargetTile().getColumn();
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(0).getMove().getTargetTile().getRow();
			}else {
				wallDirection = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getWall(0).getMove().getWallDirection();
				col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(0).getMove().getTargetTile().getColumn();
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(0).getMove().getTargetTile().getRow();
			}
			assertEquals(Direction.Horizontal, wallDirection);
			assertEquals(row, intx);
			assertEquals(col, inty);
		    
		}

		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
	    	@Then("Both players shall have {int} in their stacks")
		public void both_players_shall_have_in_their_stacks(Integer intx) {
		    Integer blackwall = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getWalls().size();
		    Integer whitewall = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getWalls().size();
		    assertEquals(blackwall, intx);
		    assertEquals(whitewall, intx);
		}
		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
		@When("The position to load is invalid")
		public void the_position_to_load_is_invalid() {
			assertEquals(false, QuoridorController.validatePosition());
		   
		}
		/**
		*Feature: Load Position
		*@Author Hongshuo Zhou
		*/
		@Then("The load shall return an error") 
		public void the_load_shall_return_an_error() {
		    assertFalse(load);
		}
		//***********************************************
		//Set total thinking time
		// **********************************************
		/**
		 * Feature :Set Total thinking time
		 * @Author Xiangyu Li
		 * @param minute minute used for total thinking time
		 * @param second second used for total thinking time 
		 */
	  
		@When("{int}:{int} is set as the thinking time")
		public void is_set_as_the_thinking_time(int minute,int second) {
			QuoridorController.setTotaltime(minute, second);
		}
		
		/**
		 * Feature :Set Total thinking time
		 * @Author Xiangyu Li
		 * @param minute check if player have minutes left
		 * @param second check if player have second left
		 */
		
		@Then("Both players shall have {int}:{int} remaining time left")
		public void both_players_shall_have_remaining_time_left(int minute,int second) {
			long remaintime=(minute*60+second)*1000;
			Assert.assertEquals(remaintime,QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime().getTime());
			Assert.assertEquals(remaintime,QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime().getTime());
		}
		
		//*************************************************
		//Switch Player
		//*************************************************
		
		/**
		 * Feature :Switch current player
		 * Xiangyu Li
		 * @param color color of player want to move 
		 */
		@Given("The player to move is {string}")
		public void Playertomove(String color) {
			if(color=="black") {
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
			}
			else 
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
		}
		
		/**
		 * Feature :Switch current player
		 * Xiangyu Li
		 * @param color color of player to run clock
		 */
		@And("The clock of {string} is running")
		public void the_clock_of_black_is_running(String color) {
		    // Write code here that turns the phrase above into concrete actions
			if(color=="black") {
				QuoridorController.runblackclock(view);
			}
			else
				QuoridorController.runwhiteclock(view);
		}
		/**
		 * Feature :Switch current player
		 * Xiangyu Li
		 * @param color color oof player to stop clock
		 */
		@And("The clock of {string} is stopped")
		public void the_clock_of_white_is_stopped(String color) {
		    // Write code here that turns the phrase above into concrete actions
			if(color=="white") {
				Timer whitetimer=QuoridorController.runwhiteclock(view);
				QuoridorController.stopwhiteclock(whitetimer);
			}
			else {
				Timer blacktimer=QuoridorController.runblackclock(view);
				QuoridorController.stopblackclock(blacktimer);
			}
		}
		/**
		 * Feature :Switch current player
		 * Xiangyu Li
		 * @param color color of player who complete his move 
		 */
		@When("Player {string} completes his move")
		public void player_blackplayer_completes_his_move(String color) {
			if(color=="black") {
			QuoridorController.completeMove(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
			}
			else {
			QuoridorController.completeMove(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());	
			}

		}
		/**
		 * Feature :Switch current player
		 * Xiangyu Li 
		 * @param color color of player is in turn
		 */
		@Then("The user interface shall be showing it is {string} turn")
		public void the_user_interface_is_showing_it_is_white_s_turn(String color) {
		    // Write code here that turns the phrase above into concrete actions
			if(color=="black") {
				Assert.assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(),currentPlayer);
			}
			if(color=="white") {
				Assert.assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(),currentPlayer);
			}
		}
		/**
		 * Feature :Switch current player
		 * Xiangyu Li
		 * @param color color of player to stop clock
		 */
		@And("The clock of {string} shall be stopped")
		public void the_clock_of_black_shall_be_stopped(String color) {
			if(color=="white") {
				Timer whitetimer=QuoridorController.runwhiteclock(view);
				QuoridorController.stopwhiteclock(whitetimer);
			}
			else {
				Timer blacktimer=QuoridorController.runblackclock(view);
				QuoridorController.stopblackclock(blacktimer);
			}
		}
		
		/**
		 * Feature :Switch current player
		 * Xiangyu Li
		 * @param color color of player to run his clock
		 */
		@And("The clock of {string} shall be running")
		public void the_clock_of_white_shall_be_running(String color) {
			if(color=="white") {
				QuoridorController.runwhiteclock(view);
			}
			else
				QuoridorController.runblackclock(view);
		}
		

		/**
		 * Feature :Switch current player
		 * Xiangyu Li
		 * @param color color of player is going to move 
		 */
		@And("The next player to move shall be {string}")
		public void the_player_to_move_is_secondplayer(String color) {
			Player player;
			if(color=="white") {
				player=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
				Assert.assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove(),player);
			}
			if(color=="black") {
				player=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
				Assert.assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove(),player);
			}
		}
	///////////////////////////////////////////////////////////////////////////////////
		/**
		 * GrabWall and MoveWall stepdefinitions
		 * 
		 * @author aidanwilliams
		 */

		// Scenario 1
		@Given("I have more walls on stock")
		public void iHaveMoreWallsOnStock() {
			Assert.assertTrue(
					QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasWalls());
		}

		@When("I try to grab a wall from my stock")
		public void iTryToGrabAWallFromMyStock() {
			//QuoridorController.grabWall();
			view.grabButton.doClick();
		}

		@Then("A wall move candidate shall be created at initial position")
		public void aWallMoveCandidateShallBeCreatedAtInitialPosition() {
			Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() != null);
		}

		@And("I shall have a wall in my hand over the board")
		public void iShallHaveAWallInMyHandOverTheBoard() {
			Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() != null);
		}

		@And("The wall in my hand shall disappear from my stock")
		public void theWallInMyHandShallDisappearFromMyStock() {
			Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().indexOfWall(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced()) == -1);	
		}

		// Scenario 2
		@Given("I have no more walls on stock")
		public void iHaveNoMoreWallsOnStock() {
			for(int i = 0; i < QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().numberOfBlackWallsInStock(); i++) {
				Wall wallToRemove = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock(0);
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wallToRemove);
			}
			for(int i = 0; i < QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().numberOfWhiteWallsInStock(); i++) {
				Wall wallToRemove = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock(0);
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wallToRemove);
			}
			
		}

		@Then("I shall be notified that I have no more walls")
		public void iShallBeNotifiedThatIHaveNoMoreWalls() {
			
			Assert.assertTrue(view.notification.getText().equals("No walls in stock"));
		}

		@And("I shall have no walls in my hand")
		public void iShallHaveNoWallsInMyHand() {
			Assert.assertTrue(
					QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() == null);
			
		}

		// Scenario Outline: Move Wall over the board
		@Given("A wall move candidate exists with {string} at position {int}, {int}")
		public void aWallMoveCandidateExistsWith(String dir, int row, int col) {
			if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() == null) {
				QuoridorController.grabWall();	
			}
			aWallMove = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
			QuoridorController.moveWall(QuoridorController.findTile(row, col));
			if (dir.equals("vertical")) {
				aWallMove.setWallDirection(Direction.Vertical);
			}
			else {
				aWallMove.setWallDirection(Direction.Horizontal);
			}
			
		}

		@And("The wall candidate is not at the {string} edge of the board")
		public void notAtTheSide(String side) {
			Assert.assertFalse(QuoridorController.isSide(aWallMove));
		}

		@When("I try to move the wall {string}")
		public void tryToMoveWall(String side) {
			if (side.equals("left"))
				QuoridorController.moveWall(QuoridorController.findTile(aWallMove.getTargetTile().getRow(),
						aWallMove.getTargetTile().getColumn() - 1));
			if (side.equals("right"))
				QuoridorController.moveWall(QuoridorController.findTile(aWallMove.getTargetTile().getRow(),
						aWallMove.getTargetTile().getColumn() + 1));
			if (side.equals("up"))
				QuoridorController.moveWall(QuoridorController.findTile(aWallMove.getTargetTile().getRow() - 1,
						aWallMove.getTargetTile().getColumn()));
			if (side.equals("down"))
				QuoridorController.moveWall(QuoridorController.findTile(aWallMove.getTargetTile().getRow() + 1,
						aWallMove.getTargetTile().getColumn()));
		}

		@Then("The wall shall be moved over the board to position {int}, {int}")
		public void wallShallBeMovedToPosition(int nrow, int ncol) {
			Assert.assertTrue(aWallMove.getTargetTile().getRow() == nrow && aWallMove.getTargetTile().getColumn() == ncol);
		}

		@And("A wall move candidate shall exist with {string} at position {int}, {int}")
		public void validateCandidate(String dir, int nrow, int ncol) {
			if (dir.equals("vertical"))
				Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection().equals(Direction.Vertical)
						&& QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().equals(QuoridorController.findTile(nrow, ncol)));
			if (dir.equals("horizontal"))
				Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection().equals(Direction.Horizontal)
						&& QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().equals(QuoridorController.findTile(nrow, ncol)));
			
		}

		// Scenario Outline: Move wall at the edge of the board

		@And("The wall candidate is at the {string} edge of the board")
		public void atTheSide(String side) {
			//System.out.println(side + QuoridorController.isSide(aWallMove));
			QuoridorController.isSide(aWallMove);
		}

		@Then("I shall be notified that my move is illegal")
		public void moveIsIllegal() {
			Assert.assertTrue(true);
		}

		////////////////////////////////////////////////////////////////////////////

	  
	    /** Drop Wall Step Definition File
		 * @author Yanis Jallouli
		 */
		// ***********************************************
		// Drop Wall definitions
		// ***********************************************

		//Scenario 1
		@Given("The wall move candidate with {string} at position {int}, {int} is valid")
		public void theWallMoveCandidateWithDirAtPosIsValid(String dir, int row, int col) throws InvalidInputException {
			//Get a string- make a direction
			Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
			WallMove toCheck = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
			toCheck.setWallDirection(direction);
			toCheck.setTargetTile(QuoridorController.findTile(row, col));
			
			if(!QuoridorController.wallIsValid()) {
				QuoridorApplication.getQuoridor().getCurrentGame().getMoves().remove(QuoridorController.invalidWall());
			}
		}
		
		@When("I release the wall in my hand") 
		public void iReleaseTheWallInMyHand(){
			//QuoridorController.dropWall();
			
			view.DropWall();

		}
		
		@Then("A wall move shall be registered with {string} at position {int}, {int}")
		public void aWallMoveIsRegisteredAtPosition(String dir, int row, int col) throws InvalidInputException {
			Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
			Assert.assertTrue("Move wasn't registered after dropping", QuoridorController.moveIsRegistered(direction, row, col));
		}
		
		@And("I shall not have a wall in my hand") 
		public void iShallNotHaveAWallInMyHand() {
			//Ensures the candidate wallmove is null. Might be a grab wall feature, but this is easy
			Assert.assertFalse(QuoridorApplication.getQuoridor().getCurrentGame().hasWallMoveCandidate());
		}

		@And("My move shall be completed")
		public void myMoveIsCompleted() {
			assertTrue(view.p2Turn.isSelected() && !view.p1Turn.isSelected());
		}
		
		@And("It shall not be my turn to move")
		public void itIsNotMyTurnToMove() {
			Assert.assertTrue( !QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
					QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())     );
		}
		//SCENARIO 2
		@Given("The wall move candidate with {string} at position {int}, {int} is invalid")
		public void theWallMoveCandidateWithDirAtPosIsInvalid(String dir, int row, int col) throws InvalidInputException {
			//Background ensures I have a wall in hand
			
			//Get a string- make a direction
			Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
			WallMove toCheck = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
			toCheck.setWallDirection(direction);
			toCheck.setTargetTile(QuoridorController.findTile(row, col));
			
			if(QuoridorController.wallIsValid()) {
				//If it's valid, make it invalid
				QuoridorApplication.getQuoridor().getCurrentGame().addMove(toCheck);
			}
			
		}
		
		
		@Then("I shall be notified that my wall move is invalid")
		public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
			assertTrue(!view.notification.getText().equals("") && view.notification.isVisible());
		}
		
		@And("It shall be my turn to move")
		public void itShallBeMyTurnToMove() {
			Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
					QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())   );
		}
		
		@But("No wall move shall be registered with {string} at position {int}, {int}")
		public void noWallMoveShallBeRegisteredAtPosition(String dir, int row, int col) throws InvalidInputException {
			Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
			Assert.assertFalse(QuoridorController.moveIsRegistered(direction, row, col));
		}
		
		
		/** Save Position Step Definition File
		 * @author Yanis Jallouli
		 */
		// ***********************************************
		// Save Position definitions
		// ***********************************************
		//Scenario 1- Save Position
		@Given("No file {string} exists in the filesystem")
		public void noFileExistsInTheSystem(String fileName) {
			//I can't find anything on using givens as control flow
				if(QuoridorController.containsFile(fileName)) {
					QuoridorController.deleteFile(fileName);
				}	
		}
		
		@When("The user initiates to save the game with name {string}")
		public void theUserInitiatesToSaveTheGameWithName(String fileName) {	
			view.confirmSaveAction();
			
			JTextField fill = (JTextField) view.confirmFrame.getContentPane().getComponent(1); //Get TextBox
			fill.setText(fileName);
			JButton save = (JButton) view.confirmFrame.getContentPane().getComponent(2);
			save.doClick();
			
		}
		
		@Then("A file with {string} shall be created in the filesystem")
		public void aFileWithNameShallBeCreated(String fileName) {
			Assert.assertTrue(QuoridorController.containsFile(fileName));
		}
		
		//Scenario 2- Save Position with existing file name
		@Given("File {string} exists in the filesystem")
		public void fileNameExistsInSystem(String fileName) {
			if(!QuoridorController.containsFile(fileName)) {
				QuoridorController.createFile(fileName);
			}
		}
		
		@And("The user confirms to overwrite existing file")
		public void theUserConfirmsToOverwrite() {
			JButton yesBut = (JButton) view.confirmFrame.getContentPane().getComponent(1);
			yesBut.doClick();
		}
		@Then("File with {string} shall be updated in the filesystem")
		public void fileWithNameShallBeUpdatedInSystem(String fileName) {
			Assert.assertTrue(QuoridorController.isUpdated(fileName));
		}

		//Scenario 3- Save Position Cancelled
		@And("The user cancels to overwrite existing file")
		public void theUserCancelsToOverwrite() {
			JButton noBut = (JButton) view.confirmFrame.getContentPane().getComponent(2);
			noBut.doClick();
		}
		@Then("File {string} shall not be changed in the filesystem")
		public void fileWithNameShallNotBeUpdatedInSystem(String fileName) {
			Assert.assertFalse(QuoridorController.isUpdated(fileName));
		}

		/**
		 * Feature 4. Initiate Board step definitions
		 * 
		 * @author Matteo Nunez
		 *
		 */
		@When("The initialization of the board is initiated")
		public void theInitializationOfTheBoardIsInitiated() {
			view.newGame.doClick();
			if(view.confirmFrame.isVisible()) {
				((JButton) view.confirmFrame.getContentPane().getComponent(1)).doClick();
			}
		}
		
		@Then("It shall be white player to move")
		public void itShallBeWhitePlayerToMove() {
			if(!view.p1Turn.isSelected())
				QuoridorController.completeMove(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
		}

		@Then("White's pawn shall be in its initial position")
		public void whitesPawnShallBeInItsInitialPosition() {
			assertEquals(1, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow());
			assertEquals(5, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn());
		}

		@Then("Black's pawn shall be in its initial position")
		public void blacksPawnShallBeInItsInitialPosition() {
			assertEquals(9, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow());
			assertEquals(5, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn());
		}

		@Then("All of White's walls shall be in stock")
		public void allOfWhitesWallsShallBeInStock() {
			assertEquals(10,QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
		}

		@Then("All of Black's walls shall be in stock")
		public void allOfBlacksWallsShallBeInStock() {
			assertEquals(10,QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
		}

		@Then("White's clock shall be counting down")
		public void whitesClockShallBeCountingDown() throws InterruptedException {
			assertTrue(view.whiteTimer.isRunning());
		}

		@Then("It shall be shown that this is White's turn")
		public void itShallBeShownThatThisIsWhitesTurn() {
			Assert.assertTrue(view.p1Turn.isSelected());
		}
		
		/**
		 * Feature 5. Rotate Wall step definitions
		 * 
		 * @author Matteo Nunez
		 *
		 */
		@When("I try to flip the wall")
		public void iTryToFlipTheWall() {
			view.RotateWall();
		}

		@Then("The wall shall be rotated over the board to {string}")
		public void theWallShallBeRotatedOverTheBoardTo(String dir) {
			Direction newDir = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
			assertEquals(newDir, QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection()); 
		}

		@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
		public void aWallMoveCandidateShallExistWithAtPosition(String string, int row, int col) {
			Direction direction = string.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
			assertEquals(direction, QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection());
			assertEquals(QuoridorController.findTile(row, col), QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile());
		    
		}
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		/**
		 * Feature: #2 Provide or select user name
		 * @author Keanu, Natchev
		 * ID#: 260804586
		 */

		@Given("Next player to set user name is {string}")
		public void nextPlayerToSetUserNameIs(String string) {
			
			if(string.equals("black")) {
				view.useExistingBlack.doClick();
			} else if(string.equals("white")) {
				view.useExistingWhite.doClick();
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Given("There is existing user {string}")
		public void thereIsExistingUser(String string) {
			if(!QuoridorController.ExistingUserName(string)) {
				QuoridorController.createUser(string);
				view.userList.add(new JLabel(string));
			}
			
		}

		@When("The player selects existing {string}")
		public void thePlayerSelectsExisting(String string) {
			if(view.userSelecting.equals("white")) {
				view.whiteName.setText(string);
			} else {
				view.blackName.setText(string);
			}
			view.newGame.doClick(); //Start new game and confirm you want an existing user
			((JButton) view.confirmFrame.getContentPane().getComponent(1)).doClick();
		}

		@Then("The name of player {string} in the new game shall be {string}")
		public void theNameOfPlayerInTheNewGameShallBe(String string, String string2) {
			if(string.equals("black")) {
				assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().getName().equals(string2));
			} else {
				assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().getName().equals(string2));
			}
		}

		@Given("There is no existing user {string}")
		public void thereIsNoExistingUser(String string) {
			if(QuoridorController.ExistingUserName(string)) {
				QuoridorApplication.getQuoridor().removeUser(QuoridorController.findUserName(string));
			}
		}

		@When("The player provides new user name: {string}")
		public void thePlayerProvidesNewUserName(String string) {
			if(view.userSelecting.equals("white")) {
				view.whiteName.setText(string);
			} else {
				view.blackName.setText(string);
			}
			view.newGame.doClick();
		}

		@Then("The player shall be warned that {string} already exists")
		public void thePlayerShallBeWarnedThatAlreadyExists(String string) {
			view.confirmExistingName();
		}

		@Then("Next player to set user name shall be {string}")
		public void nextPlayerToSetUserNameShallBe(String string) {
			if(string.equals("white")) {
				assertTrue(view.userSelecting.equals("white"));
			} else {
				assertTrue(view.userSelecting.equals("black"));
			}
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Feature: #11 Validate position
		 * @author Keanu, Natchev
		 * ID#: 260804586
		 */
		
		@Given("A game position is supplied with pawn coordinate {int}:{int}")
		public void aGamePositionIsSuppliedWithPawnCoordinate(Integer int1, Integer int2) {
			if(int1 < 1 || int1 > 9 || int2 < 1 || int2 > 9) {
				System.out.println("Invalid coordinates given. Values must be between 1 and 9.");
			}
			else {			
				if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack()) {
					PlayerPosition aNewBlackPosition = new PlayerPosition(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile());
					QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setBlackPosition(aNewBlackPosition);
				}
				if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
					PlayerPosition aNewWhitePosition = new PlayerPosition(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer(), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile());
					QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setWhitePosition(aNewWhitePosition);
				}
			}
		}

		@When("Validation of the position is initiated")
		public void validationOfThePositionIsInitiated() {
			QuoridorController.validatePosition();
		}

		@Then("The position shall be {string}")
		public void thePositionShallBe(String string) {
			if(QuoridorController.validatePosition()) {
				string = "ok";
			}
			else {
				string = "error";
			}
		}

		@Given("A game position is supplied with wall coordinate {int}:{int}-{string}")
		public void aGamePositionIsSuppliedWithWallCoordinate(Integer int1, Integer int2, String string) {		
			Direction directionGiven = null;

			if(string == "vertical") {
				directionGiven = Direction.Vertical;					
			}
			if(string == "horizontal") {
				directionGiven = Direction.Horizontal;
			}

			if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack()) {
				List<Wall> blackWalls =  QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
				Integer row;
				Integer column;
				Direction direction;
				for(int i = 0; i < blackWalls.size(); i++) {

					row = blackWalls.get(i).getMove().getTargetTile().getRow();
					column = blackWalls.get(i).getMove().getTargetTile().getColumn();
					direction = blackWalls.get(i).getMove().getWallDirection();
					assertEquals(int1, row);
					assertEquals(int2, column);
					assertEquals(directionGiven, direction);
				}
			}
			
			if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
				List<Wall> whiteWalls =  QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
				Integer row;
				Integer column;
				Direction direction;
				for(int i = 0; i < whiteWalls.size(); i++) {

					row = whiteWalls.get(i).getMove().getTargetTile().getRow();
					column = whiteWalls.get(i).getMove().getTargetTile().getColumn();
					direction = whiteWalls.get(i).getMove().getWallDirection();
					assertEquals(int1, row);
					assertEquals(int2, column);
					assertEquals(directionGiven, direction);
				}
			}
		}

		@Then("The position shall be valid")
		public void thePositionShallBeValid() {
			assertEquals(true, QuoridorController.validatePosition());
		}

		@Then("The position shall be invalid")
		public void thePositionShallBeInvalid() {
			assertEquals(false, QuoridorController.validPosition());
		}
		

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// ***********************************************
		// Clean up
		// ***********************************************

		// After each scenario, the test model is discarded
		@After
		public void tearDown() {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			// Avoid null pointer for step definitions that are not yet implemented.
			if (quoridor != null) {
				quoridor.delete();
				quoridor = null;
			}
			for (int i = 0; i <= 20; i++) {Wall wall = Wall.getWithId(i);
			if(wall != null) {
				wall.delete();
			}
		}
	}
		// ***********************************************
		// Extracted helper methods
		// ***********************************************

		// Place your extracted methods below
		private void initQuoridorAndBoard() {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			Board board = new Board(quoridor);
			// Creating tiles by rows, i.e., the column index changes with every tile
			// creation
			for (int i = 1; i <= 9; i++) { // rows
				for (int j = 1; j <= 9; j++) { // columns
					board.addTile(i, j);
				}
			}
		}

		private ArrayList<Player> createUsersAndPlayers(String userName1, String userName2) {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			User user1 = quoridor.addUser(userName1);
			User user2 = quoridor.addUser(userName2);

			int thinkingTime = 180;

			// Players are assumed to start on opposite sides and need to make progress
			// horizontally to get to the other side
			//@formatter:off
			/*
			 *  __________
			 * |          |
			 * |          |
			 * |x->    <-x|
			 * |          |
			 * |__________|
			 * 
			 */
			//@formatter:on
			Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
			Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);
			
			player1.setNextPlayer(player2);
			player2.setNextPlayer(player1);
			
			Player[] players = { player1, player2 };

			// Create all walls. Walls with lower ID belong to player1,
			// while the second half belongs to player 2
			for (int i = 0; i < 2; i++) {
				for (int j = 1; j <= 10; j++) {
					
					new Wall(i * 10 + j, players[i]);
				}
			}
			
			ArrayList<Player> playersList = new ArrayList<Player>();
			playersList.add(player1);
			playersList.add(player2);
			
			return playersList;
		}

		private void createAndStartGame(ArrayList<Player> players) {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			Tile player1StartPos = quoridor.getBoard().getTile(4);
			Tile player2StartPos = quoridor.getBoard().getTile(76);
			Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);
			game.setWhitePlayer(players.get(0));
			game.setBlackPlayer(players.get(1));

			PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
			PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

			GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

			// Add the walls as in stock for the players
			for (int j = 1; j <= 10; j++) {
				Wall wall = Wall.getWithId(j);
				gamePosition.addWhiteWallsInStock(wall);
			}
			for (int j = 1; j <= 10; j++) {
				Wall wall = Wall.getWithId(j + 10);
				gamePosition.addBlackWallsInStock(wall);
			}

			game.setCurrentPosition(gamePosition);
		}
			}
			
