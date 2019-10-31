package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.view.QuoridorView;
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
import cucumber.api.PendingException;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {

	private QuoridorView view;
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
		tearDown();
		initQuoridorAndBoard();
		createUsersAndPlayers("user1", "user2");
	}

	@Given("^The game is running$")
	public void theGameIsRunning() {
		if(QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus() != GameStatus.Running) {
			theGameIsNotRunning();
			createAndStartGame();
		}
	}

	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		currentPlayer = player1;
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { player1, player2 };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);

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
			new WallMove(0, 1, players[playerIdx], board.getTile((wrow - 1) * 9 + wcol - 1), game, direction, wall);
			if (playerIdx == 0) {
				game.getCurrentPosition().removeWhiteWallsInStock(wall);
				game.getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				game.getCurrentPosition().removeBlackWallsInStock(wall);
				game.getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();

	}

	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		// Walls are in stock for all players
	}
	
	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************


//***********************************************
//Start a new game
// **********************************************
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@When("A new game is being initialized")
public void a_new_game_is_being_initialized(){
	QuoridorController.startGame();
	throw new cucumber.api.PendingException();
}
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@And("White player chooses a username")
public void white_player_chooses_a_username(){
     QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();	
     throw new cucumber.api.PendingException();
}
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@And("Black player chooses a username")
public void black_player_chooses_a_username(){
     QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
     throw new cucumber.api.PendingException();
}
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@Then("Total thinking time is set")
public void total_thinking_time_is_set(int inta, int intb){
     QuoridorController.setTotaltime(inta,intb);	
     throw new cucumber.api.PendingException();
}
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@Then("The game shall become ready to start")
public void the_game_shall_become_ready_to_start(){
	assertEquals(true, QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus());
	throw new cucumber.api.PendingException();
}
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@Given("The game is ready to start")
public void the_game_is_ready_to_start() {
 QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Running);
}
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@When("I start the clock")
public void I_start_the_clock() {
 QuoridorController.runwhiteclock();
}
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@Then("The game shall be running") 
public void the_game_shall_be_running(){
 Assert.assertEquals(GameStatus.Running,QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus());
}
/**
*Feature:Start a new game 
*@Author Hongshuo Zhou
*/
@And("The board shall be initialized")
public void the_board_shall_be_initialized() {
 Assert.assertEquals(QuoridorApplication.getQuoridor().setBoard(board),true);
}

//***********************************************
// Load Position
// **********************************************
	/**
	*Feature: Load Position
	*@Author Hongshuo Zhou
	*/
	@When("I initiate to load a saved game {string}")
	public void i_initiate_to_load_a_saved_game(String string) {
		QuoridorController.loadGame(string);
	    throw new cucumber.api.PendingException();
	}
	/**
	*Feature: Load Position
	*@Author Hongshuo Zhou
	*/
	@And("The position to load is valid")
	public void the_position_to_load_is_valid() {
	    assertEquals(true, QuoridorController.validatePosition());
	    throw new cucumber.api.PendingException();
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
	    throw new cucumber.api.PendingException();
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
	    throw new cucumber.api.PendingException();
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
	    throw new cucumber.api.PendingException();
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
	    throw new cucumber.api.PendingException();
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
	    throw new cucumber.api.PendingException();
	}
	/**
	*Feature: Load Position
	*@Author Hongshuo Zhou
	*/
	@When("The position to load is invalid")
	public void the_position_to_load_is_invalid() {
		assertEquals(false, QuoridorController.validatePosition());
	    throw new cucumber.api.PendingException();
	}
	/**
	*Feature: Load Position
	*@Author Hongshuo Zhou
	*/
	@Then("The load shall return an error") 
	public void the_load_shall_return_an_error() {
	    assertEquals("Failed loading game", QuoridorController.getLoadResult());
	    throw new cucumber.api.PendingException();
	}
	//***********************************************
	//Set total thinking time
	// **********************************************
	/**
	 * Feature :Set Total thinking time
	 * @Author Xiangyu Li
	 * @param minute
	 * @param second
	 */
  
	@When("{int}:{int} is set as the thinking time")
	public void is_set_as_the_thinking_time(int minute,int second) {
		QuoridorController.setTotaltime(minute, second);
	}
	
	/**
	 * Feature :Set Total thinking time
	 * @Author Xiangyu Li
	 * @param minute
	 * @param second
	 */
	
	@Then("Both players shall have {int}:{int} remaining time left")
	public void both_players_shall_have_remaining_time_left(int minute,int second) {
		long remaintime=(minute*60+second)*1000;
		Assert.assertEquals(remaintime,QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime());
		Assert.assertEquals(remaintime,QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime());
	}
	
	//*************************************************
	//Switch Player
	//*************************************************
	
	/**
	 * Feature :Switch current player
	 * Xiangyu Li
	 * @param color
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
	 * @param color
	 */
	@And("The clock of {string} is running")
	public void the_clock_of_black_is_running(String color) {
	    // Write code here that turns the phrase above into concrete actions
		if(color=="black") {
			QuoridorController.runblackclock();
		}
		else
			QuoridorController.runwhiteclock();
	}
	/**
	 * Feature :Switch current player
	 * Xiangyu Li
	 * @param color
	 */
	@And("The clock of {string} is stopped")
	public void the_clock_of_white_is_stopped(String color) {
	    // Write code here that turns the phrase above into concrete actions
		if(color=="white") {
			QuoridorController.stopwhiteclock();
		}
		else
			QuoridorController.stopblackclock();
	}
	/**
	 * Feature :Switch current player
	 * Xiangyu Li
	 * @param color
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
	 * @param color
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
	 * @param color
	 */
	@And("The clock of {string} shall be stopped")
	public void the_clock_of_black_shall_be_stopped(String color) {
		if(color=="black") {
			QuoridorController.stopblackclock();
		}
		else
			QuoridorController.stopwhiteclock();
	}
	
	/**
	 * Feature :Switch current player
	 * Xiangyu Li
	 * @param color
	 */
	@And("The clock of {string} shall be running")
	public void the_clock_of_white_shall_be_running(String color) {
		if(color=="white") {
			QuoridorController.runwhiteclock();
		}
		else
			QuoridorController.runblackclock();
	}
	

	/**
	 * Feature :Switch current player
	 * Xiangyu Li
	 * @param color
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
		throw new cucumber.api.PendingException();
	}

	@When("I try to grab a wall from my stock")
	public void iTryToGrabAWallFromMyStock() {
		QuoridorController.grabWall(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getPlayerToMove().getWall(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
						.getPlayerToMove().getWalls().size() - 1));
		throw new cucumber.api.PendingException();
	}

	@Then("A wall move candidate shall be created at initial position")
	public void aWallMoveCandidateShallBeCreatedAtInitialPosition() {
		aWallMove = QuoridorController.grabWall(QuoridorApplication.getQuoridor().getCurrentGame()
				.getCurrentPosition().getPlayerToMove().getWall(QuoridorApplication.getQuoridor().getCurrentGame()
						.getCurrentPosition().getPlayerToMove().getWalls().size() - 1));
		throw new cucumber.api.PendingException();
	}

	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		// GUI-related feature -- TODO for later
		throw new cucumber.api.PendingException();
	}

	@And("The wall in my hand shall disappear from my stock")
	public void theWallInMyHandShallDisappearFromMyStock() {
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
				.removeWall(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
						.getWall(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
								.getPlayerToMove().getWalls().size() - 1));
		throw new cucumber.api.PendingException();
	}

	// Scenario 2
	@Given("I have no more walls on stock")
	public void iHaveNoMoreWallsOnStock() {
		for (Wall wall : QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
				.getWalls())
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().removeWall(wall);
		throw new cucumber.api.PendingException();
	}

	@Then("I shall be notified that I have no more walls")
	public void iShallBeNotifiedThatIHaveNoMoreWalls() {
		// GUI-related feature -- TODO for later
		throw new cucumber.api.PendingException();
	}

	@And("I shall have no walls in my hand")
	public void iShallHaveNoWallsInMyHand() {
		Assert.assertFalse(
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasWalls());
		throw new cucumber.api.PendingException();
	}

	// Scenario Outline: Move Wall over the board
	@Given("A wall move candidate exists with {string} at position {int}, {int}")
	public void aWallMoveCandidateExistsWith(String dir, int row, int col) {
		if (dir.equals("vertical"))
			aWallMove.setWallDirection(Direction.Vertical);
		else
			aWallMove.setWallDirection(Direction.Horizontal);
		QuoridorController.moveWall(aWallMove, QuoridorController.findTile(row, col));
		throw new cucumber.api.PendingException();
	}

	@And("The wall candidate is not at the {string} edge of the board")
	public void notAtTheSide(String side) {
		Assert.assertFalse(QuoridorController.isSide(aWallMove));
		throw new cucumber.api.PendingException();
	}

	@When("I try to move the wall {string}")
	public void tryToMoveWall(String side) {
		if (side.equals("left"))
			QuoridorController.moveWall(aWallMove, QuoridorController.findTile(aWallMove.getTargetTile().getRow(),
					aWallMove.getTargetTile().getColumn() - 1));
		if (side.equals("right"))
			QuoridorController.moveWall(aWallMove, QuoridorController.findTile(aWallMove.getTargetTile().getRow(),
					aWallMove.getTargetTile().getColumn() + 1));
		if (side.equals("up"))
			QuoridorController.moveWall(aWallMove, QuoridorController.findTile(aWallMove.getTargetTile().getRow() - 1,
					aWallMove.getTargetTile().getColumn()));
		if (side.equals("down"))
			QuoridorController.moveWall(aWallMove, QuoridorController.findTile(aWallMove.getTargetTile().getRow() + 1,
					aWallMove.getTargetTile().getColumn()));
		throw new cucumber.api.PendingException();
	}

	@Then("The wall shall be moved over the board to position {int}, {int}")
	public void validatePosition(int nrow, int ncol) {
		Assert.assertTrue(aWallMove.getTargetTile().getRow() == nrow && aWallMove.getTargetTile().getColumn() == ncol);
		throw new cucumber.api.PendingException();
	}

	@And("A wall move candidate shall exist with {string} at position {int}, {int}")
	public void validateCandidate(String dir, int nrow, int ncol) {
		if (dir.equals("vertical"))
			Assert.assertTrue(aWallMove.getWallDirection().equals(Direction.Vertical)
					&& aWallMove.getTargetTile().equals(QuoridorController.findTile(nrow, ncol)));
		if (dir.equals("horizontal"))
			Assert.assertTrue(aWallMove.getWallDirection().equals(Direction.Horizontal)
					&& aWallMove.getTargetTile().equals(QuoridorController.findTile(nrow, ncol)));
		throw new cucumber.api.PendingException();
	}

	// Scenario Outline: Move wall at the edge of the board

	@And("The wall candidate is at the {string} edge of the board")
	public void atTheSide(String side) {
		Assert.assertTrue(QuoridorController.isSide(aWallMove));
		throw new cucumber.api.PendingException();
	}

	@Then("I shall be notified that my move is illegal")
	public void moveIsIllegal() {
		// GUI-related feature -- TODO for later
		throw new cucumber.api.PendingException();
	}

	////////////////////////////////////////////////////////////////////////////

  
    /** Drop Wall Step Definition File
	 * @author Yanis Jallouli
	 */
	// ***********************************************
	// Drop Wall definitions
	// ***********************************************
	
	@And("I have a wall in my hand over the board")
	public void iHaveAWallInHandOverBoard() {
		if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() == null) {
			QuoridorController.grabWall(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getWall(0));
		}
	}
	
	//Scenario 1
	@Given("The wall move candidate with {string} at position {int}, {int} is valid")
	public void theWallMoveCandidateWithDirAtPosIsValid(String dir, int row, int col) throws InvalidInputException {
		//Get a string- make a direction
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		//Quoridorcontroller.rotate(QuoridorApplication.getQuoridor().getCurrentGame(), direction);
		QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(direction);
		QuoridorController.moveWall(  QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() , QuoridorController.findTile(row, col));
		//Fail if invalid wall given
		List<WallMove> moveList = new ArrayList<WallMove>();
		for(Move move : QuoridorApplication.getQuoridor().getCurrentGame().getMoves()) {
			if(move instanceof WallMove) moveList.add((WallMove) move);
		}
		
		if(!QuoridorController.wallIsValid(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate(), 
				moveList, 
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition(),
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition())) {
			Assert.fail();
		}
	}
	
	@When("I release the wall in my hand") 
	public void iReleaseTheWallInMyHand(){
		QuoridorController.dropWall();
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
		//TODO: GUI step?
		throw new PendingException(); //I'm assuming this is a User confirming move
	}
	
	@And("It shall not be my turn to move")
	public void itIsNotMyTurnToMove() {
		//Or maybe I'm supposed to change it last step and check it this one? 
		QuoridorController.completeMove(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
		Assert.assertTrue( !QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())     );
	}
	//SCENARIO 2
	@Given("The wall move candidate with {string} at position {int}, {int} is invalid")
	public void theWallMoveCandidateWithDirAtPosIsInvalid(String dir, int row, int col) throws InvalidInputException {
		//Background ensures I have a wall in hand
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(direction);
		QuoridorController.moveWall(  QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() , QuoridorController.findTile(row, col));
		//Check
		List<WallMove> moveList = new ArrayList<WallMove>();
		for(Move move : QuoridorApplication.getQuoridor().getCurrentGame().getMoves()) {
			if(move instanceof WallMove) moveList.add((WallMove) move);
		}
		
		if(QuoridorController.wallIsValid(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate(), 
				moveList, 
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition(),
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition())) {
			Assert.fail(); //If you reached here, the parameters being passed in are wrong
		}
	}
	
	
	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
		throw new PendingException(); //GUI stuff
	}
	/*//Taken care of in grab/move wall
	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().hasWallMoveCandidate());
	}
	*/
	
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
	//Scenario 1
	@Given("No file {string} exists in the filesystem")
	public void noFileExistsInTheSystem(String fileName) {
		//I can't find anything on using givens as control flow
		Assert.assertFalse(QuoridorController.containsFile(fileName));
	}
	
	@When("The user initiates to save the game with name {string}")
	public void theUserInitiatesToSaveTheGameWithName(String fileName) {
		//TODO: GUI Step
		throw new PendingException();
	}
	
	@Then("A file with {string} shall be created in the filesystem")
	public void aFileWithNameShallBeCreated(String fileName) {
		QuoridorController.createFile(fileName); 
		Assert.assertTrue(QuoridorController.containsFile(fileName));
	}
	//Scenario 2
	@Given("File {string} exists in the filesystem")
	public void fileNameExistsInSystem(String fileName) {
		//This is confusing me. We have one for it doesn't exist so it
		//seems like an if thing. Should I assert it's true? or make it true somehow?
		Assert.assertTrue(QuoridorController.containsFile(fileName));
	}
	
	@And("The user confirms to overwrite existing file")
	public void theUserConfirmsToOverwrite() {
		//TODO: GUI step
		throw new PendingException();
	}
	@Then("File with {string} shall be updated in the filesystem")
	public void fileWithNameShallBeUpdatedInSystem(String fileName) {
		QuoridorController.savePosition(fileName);
		Assert.assertTrue(QuoridorController.isUpdated(fileName));
	}

	@And("The user cancels to overwrite existing file")
	public void theUserCancelsToOverwrite() {
		//TODO: GUI Step
		throw new PendingException();
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
		QuoridorController.initializeBoard(QuoridorApplication.getQuoridor().getBoard());
	    throw new cucumber.api.PendingException();
	}
	
	@Then("It shall be white player to move")
	public void itShallBeWhitePlayerToMove() {
		assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite());
	    throw new cucumber.api.PendingException();
	}

	@Then("White's pawn shall be in its initial position")
	public void whitesPawnShallBeInItsInitialPosition() {
		assertEquals(9, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow());
		assertEquals(4, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn());
		throw new cucumber.api.PendingException();
	}

	@Then("Black's pawn shall be in its initial position")
	public void blacksPawnShallBeInItsInitialPosition() {
		assertEquals(0, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow());
		assertEquals(4, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn());
	    throw new cucumber.api.PendingException();
	}

	@Then("All of White's walls shall be in stock")
	public void allOfWhitesWallsShallBeInStock() {
		assertEquals(10,QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
	    throw new cucumber.api.PendingException();
	}

	@Then("All of Black's walls shall be in stock")
	public void allOfBlacksWallsShallBeInStock() {
		assertEquals(10,QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
	    throw new cucumber.api.PendingException();
	}

	@Then("White's clock shall be counting down")
	public void whitesClockShallBeCountingDown() throws InterruptedException {
		Time time1 = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getRemainingTime();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getRemainingTime().wait(1000);
		Time time2 = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getRemainingTime();
		assertFalse(time1.compareTo(time2) == 0);
	    throw new cucumber.api.PendingException();
	}

	@Then("It shall be shown that this is White's turn")
	public void itShallBeShownThatThisIsWhitesTurn() {
		// GUI-related feature -- TODO for later
	    throw new cucumber.api.PendingException();
	}
	
	/**
	 * Feature 5. Rotate Wall step definitions
	 * 
	 * @author Matteo Nunez
	 *
	 */
//	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
//	public void aWallMoveCandidateExistsWith(String string, int row, int col) {
//		Direction direction = string.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
//		QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(direction);
//		Quoridorcontroller.moveWall(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate(), QuoridorController.findTile(row, col));
//		throw new cucumber.api.PendingException();
//	}

	@When("I try to flip the wall")
	public void iTryToFlipTheWall() {
		currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Wall wall = currentPlayer.getWall(currentPlayer.getWalls().size() - 1);
		QuoridorController.rotateWall(wall);
	    throw new cucumber.api.PendingException();
	}

	@Then("The wall shall be rotated over the board to {string}")
	public void theWallShallBeRotatedOverTheBoardTo(String dir) {
		Direction newDir = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		assertEquals(newDir, aWallMove.getWallDirection()); 
	    throw new cucumber.api.PendingException();
	}

	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateShallExistWithAtPosition(String string, int row, int col) {
		Direction direction = string.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		assertEquals(direction, QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection());
		assertEquals(QuoridorController.findTile(row, col), QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile());
	    throw new cucumber.api.PendingException();
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Feature: #2 Provide or select user name
	//Name: Keanu, Natchev
	//ID#: 260804586

	@Given("A new game is initializing")
	public void aNewGameIsInitializing() {
		QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus();
		assertEquals(true, GameStatus.Initializing);
		throw new cucumber.api.PendingException();
	}

	@Given("Next player to set user name is {string}")
	public void nextPlayerToSetUserNameIs(String string) {
		if(!(string == "black") && !(string == "white")) {
			throw new IllegalArgumentException();
		}
		else {
			if(string == "black") {
				QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			}
			if(string == "white") {
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			}
		}

		throw new cucumber.api.PendingException();
	}

	@Given("There is existing user {string}")
	public void thereIsExistingUser(String string) {
		List<User> existingUsers = QuoridorApplication.getQuoridor().getUsers();
		for(int i = 0; i < existingUsers.size(); i++) {
			assertEquals(string, existingUsers.get(i).getName());
		}
		throw new cucumber.api.PendingException();
	}

	@When("The player selects existing {string}")
	public void thePlayerSelectsExisting(String string) {
		assertEquals(true, QuoridorController.ExistingUserName(string));
		throw new cucumber.api.PendingException();
	}

	@Then("The name of player {string} in the new game shall be {string}")
	public void theNameOfPlayerInTheNewGameShallBe(String string, String string2) {
		if(string == "black") {
			QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().setName(string2);

		}
		if(string == "white") {
			QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().setName(string2);
		}

		throw new cucumber.api.PendingException();
	}

	@Given("There is no existing user {string}")
	public void thereIsNoExistingUser(String string) {
		assertEquals(false, QuoridorController.ExistingUserName(string));
		throw new cucumber.api.PendingException();
	}

	@When("The player provides new user name: {string}")
	public void thePlayerProvidesNewUserName(String string) {
		QuoridorApplication.getQuoridor().addUser(string);
		throw new cucumber.api.PendingException();
	}

	@Then("The player shall be warned that {string} already exists")
	public void thePlayerShallBeWarnedThatAlreadyExists(String string) {
		if(QuoridorController.ExistingUserName(string)) {
			System.out.println("The user with the name: " + string + "already exists.");
		}
		throw new cucumber.api.PendingException();
	}

	@Then("Next player to set user name shall be {string}")
	public void nextPlayerToSetUserNameShallBe(String string) {
		if(!string.equals("black") && !string.equals("white")) {
			throw new IllegalArgumentException();
		}
		else {
			if(string.equals("black")) {
				QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			}
			if(string.equals("white")) {
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			}
		}
		throw new cucumber.api.PendingException();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Feature: #11 Validate position
	//Name: Keanu, Natchev
	//ID#: 260804586

	@Given("A game position is supplied with pawn coordinate {int}:{int}")
	public void aGamePositionIsSuppliedWithPawnCoordinate(Integer int1, Integer int2) {
		if(int1 < 1 || int1 > 9 || int2 < 1 || int2 > 9) {
			System.out.println("Invalid coordinates given. Values must be between 1 and 9.");
		}
		else {			
			Integer row;
			Integer column;
			if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack()) {
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
				column = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
				assertEquals(row, int1);
				assertEquals(column, int2);
			}
			if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
				column = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
				assertEquals(row, int1);
				assertEquals(column, int2);
			}
		}


		throw new cucumber.api.PendingException();
	}

	@When("Validation of the position is initiated")
	public void validationOfThePositionIsInitiated() {
		QuoridorController.validatePosition();
		throw new cucumber.api.PendingException();
	}

	@Then("The position shall be {string}")
	public void thePositionShallBe(String string) {
		if(QuoridorController.validatePosition()) {
			string = "ok";
		}
		else {
			string = "error";
		}
		throw new cucumber.api.PendingException();
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


		throw new cucumber.api.PendingException();
	}

	@Then("The position shall be valid")
	public void thePositionShallBeValid() {
		assertEquals(true, QuoridorController.validPosition());
		throw new cucumber.api.PendingException();
	}

	@Then("The position shall be invalid")
	public void thePositionShallBeInvalid() {
		assertEquals(false, QuoridorController.validPosition());
		throw new cucumber.api.PendingException();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	

	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded




	// ***********************************************
	// Extracted helper methods
	// ***********************************************
	
	@After
	public void tearDown() {
		quoridor.delete();
		quoridor = null;
  }
	// Place your extracted methods below

	
	
	private void initQuoridorAndBoard() {
		
		quoridor = QuoridorApplication.getQuoridor();
		board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}

	private void createUsersAndPlayers(String userName1, String userName2) {
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
		player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}
	}

	private void createAndStartGame() {
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = board.getTile(36);
		Tile player2StartPos = board.getTile(44);

		PlayerPosition player1Position = new PlayerPosition(player1, player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(player2, player2StartPos);

		game = new Game(GameStatus.Running, MoveMode.PlayerMove, player1, player2, quoridor);
		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, player1, game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}

		game.setCurrentPosition(gamePosition);
	}
	
	

}
