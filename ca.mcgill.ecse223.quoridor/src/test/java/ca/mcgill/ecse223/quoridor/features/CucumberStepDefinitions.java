package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import ca.mcgill.ecse223.quoridor.Controller.*;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import cucumber.api.PendingException;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {

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
			// Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
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
			new WallMove(0, 1, players[playerIdx], quoridor.getBoard().getTile((wrow - 1) * 9 + wcol - 1),
					quoridor.getCurrentGame(), direction, wall);
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
	public void iDoNotHaveAWallInMyHand() {
		// GUI-related feature -- TODO for later
	}

	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// GUI-related feature -- TODO for later
	}

	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	/*
	 * TODO Insert your missing step definitions here
	 * 
	 * Call the methods of the controller that will manipulate the model once they
	 * are implemented
	 * 
	 */

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
		Quoridorcontroller.grabWall(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getPlayerToMove().getWall(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
						.getPlayerToMove().getWalls().size() - 1));
		throw new cucumber.api.PendingException();
	}

	@Then("A wall move candidate shall be created at initial position")
	public void aWallMoveCandidateShallBeCreatedAtInitialPosition() {
		WallMove aWallMove = QuoridorController.grabWall(QuoridorApplication.getQuoridor().getCurrentGame()
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
	@Given("A wall move candidate exists with \"<dir>\" at position (<row>, <col>)")
	public void aWallMoveCandidateExistsWith(String dir, int row, int col) {
		if (dir.equals("vertical"))
			aWallMove.setWallDirection(Direction.Vertical);
		else
			aWallMove.setWallDirection(Direction.Horizontal);
		QuoridorController.moveWall(aWallMove, QuoridorController.findTile(row, col));
		throw new cucumber.api.PendingException();
	}

	@And("The wall candidate is not at the \"<side>\" edge of the board")
	public void notAtTheSide(String side) {
		Assert.assertFalse(QuoridorController.isSide(aWallMove));
		throw new cucumber.api.PendingException();
	}

	@When("I try to move the wall \"<side>\"")
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

	@Then("The wall shall be moved over the board to position (<nrow>, <ncol>)")
	public void validatePosition(int nrow, int ncol) {
		Assert.assertTrue(aWallMove.getTargetTile().getRow() == nrow && aWallMove.getTargetTile().getColumn() == ncol);
		throw new cucumber.api.PendingException();
	}

	@And("A wall move candidate shall exist with \"<dir>\" at position (<nrow>, <ncol>)")
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

	@And("The wall candidate is at the \"<side>\" edge of the board")
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

	// ***********************************************
	// Start a new game
	// **********************************************
	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@When("A new game is being initialized")
	public void a_new_game_is_being_initialized() {
		Quoridorcontroller.startGame();
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@And("White player chooses a username")
	public void white_player_chooses_a_username() {
		QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@And("Black player chooses a username")
	public void black_player_chooses_a_username() {
		QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Then("Total thinking time is set")
	public void total_thinking_time_is_set(int inta, int intb) {
		Quoridorcontroller.setTotaltime(inta, intb);
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Then("The game shall become ready to start")
	public void the_game_shall_become_ready_to_start() {
		assertEquals(true, QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus());
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Given("The game is ready to start")
	public void the_game_is_ready_to_start() {
		QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Running);
	}

	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@When("I start the clock")
	public void I_start_the_clock() {
		Quoridorcontroller.runwhiteclock();
	}

	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Then("the game shall be running")
	public void the_game_shall_be_running() {
		Assert.assertEquals(GameStatus.Running, QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus());
	}

	/**
	 * Feature:Start a new game
	 * 
	 * @Author Hongshuo Zhou
	 */
	@And("The board shall be initialized")
	public void the_board_shall_be_initialized() {
		Assert.assertEquals(QuoridorApplication.getQuoridor().setBoard(board), true);
	}

	// ***********************************************
	// Load Position
	// **********************************************
	/**
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
	 */
	@When("I initiate to load a saved game {string}")
	public void i_initiate_to_load_a_saved_game(String string) {
		Quoridorcontroller.loadGame(string);
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
	 */
	@And("The position to load is valid")
	public void the_position_to_load_is_valid() {
		assertEquals(true, Quoridorcontroller.validatePosition());
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Then("It shall be {string}'s turn")
	public void it_shall_be_s_turn(String string) {
		String currentcolor;
		if (QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
				.hasGameAsBlack()) {
			currentcolor = "black";
		} else {
			currentcolor = "white";
		}
		assertEquals(string, currentcolor);
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Then("{string} shall be at {int}:{int}")
	public void shall_be_at(String string, Integer intx, Integer inty) {
		Integer row;
		Integer col;
		if (string == "black") {
			row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile()
					.getRow();
			col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile()
					.getColumn();
		} else {
			row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile()
					.getRow();
			col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile()
					.getColumn();
		}
		assertEquals(row, intx);
		assertEquals(col, inty);
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Then("{string} shall have a vertical wall at {int}:{int}")
	public void shall_have_a_vertical_wall_at(String string, Integer intx, Integer inty) {
		Integer col;
		Integer row;
		Direction wallDirection;
		if (string == "black") {
			wallDirection = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getWall(0).getMove()
					.getWallDirection();
			col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(0)
					.getMove().getTargetTile().getColumn();
			row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(0)
					.getMove().getTargetTile().getRow();
		} else {
			wallDirection = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getWall(0).getMove()
					.getWallDirection();
			col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(0)
					.getMove().getTargetTile().getColumn();
			row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(0)
					.getMove().getTargetTile().getRow();
		}
		assertEquals(Direction.Vertical, wallDirection);
		assertEquals(row, intx);
		assertEquals(col, inty);
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Then("{string} shall have a horizontal wall at {int}:{int}")
	public void shall_have_a_horizontal_wall_at(String string, Integer intx, Integer inty) {
		Integer col;
		Integer row;
		Direction wallDirection;
		if (string == "black") {
			wallDirection = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getWall(0).getMove()
					.getWallDirection();
			col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(0)
					.getMove().getTargetTile().getColumn();
			row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(0)
					.getMove().getTargetTile().getRow();
		} else {
			wallDirection = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getWall(0).getMove()
					.getWallDirection();
			col = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(0)
					.getMove().getTargetTile().getColumn();
			row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(0)
					.getMove().getTargetTile().getRow();
		}
		assertEquals(Direction.Horizontal, wallDirection);
		assertEquals(row, intx);
		assertEquals(col, inty);
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
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
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
	 */
	@When("The position to load is invalid")
	public void the_position_to_load_is_invalid() {
		assertEquals(false, Quoridorcontroller.validatePosition());
		throw new cucumber.api.PendingException();
	}

	/**
	 * Feature: Load Position
	 * 
	 * @Author Hongshuo Zhou
	 */
	@Then("The load shall return an error")
	public void the_load_shall_return_an_error() {
		assertEquals("Failed loading game", Quoridorcontroller.getLoadResult());
		throw new cucumber.api.PendingException();
	}

	// ***********************************************
	// Set total thinking time
	// **********************************************
	/**
	 * Feature :Set Total thinking time
	 * 
	 * @Author Xiangyu Li
	 * @param minute
	 * @param second
	 */

	@When("{int}:{int} is set as the thinking time")
	public void is_set_as_the_thinking_time(int minute, int second) {
		Quoridorcontroller.setTotaltime(minute, second);
	}

	/**
	 * Feature :Set Total thinking time
	 * 
	 * @Author Xiangyu Li
	 * @param minute
	 * @param second
	 */

	@Then("Both players shall have {int}:{int} remaining time left")
	public void both_players_shall_have_remaining_time_left(int minute, int second) {
		long remaintime = (minute * 60 + second) * 1000;
		Assert.assertEquals(remaintime,
				QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime());
		Assert.assertEquals(remaintime,
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime());
	}

	// *************************************************
	// Switch Player
	// *************************************************

	/**
	 * Feature :Switch current player Xiangyu Li
	 * 
	 * @param color
	 */
	@Given("The player to move is {String}")
	public void Playertomove(String color) {
		if (color == "black") {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
					.setPlayerToMove(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
		} else
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
					.setPlayerToMove(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
	}

	/**
	 * Feature :Switch current player Xiangyu Li
	 * 
	 * @param color
	 */
	@And("The clock of {string} is running")
	public void the_clock_of_black_is_running(String color) {
		// Write code here that turns the phrase above into concrete actions
		if (color == "black") {
			Quoridorcontroller.runblackclock();
		} else
			Quoridorcontroller.runwhiteclock();
	}

	/**
	 * Feature :Switch current player Xiangyu Li
	 * 
	 * @param color
	 */
	@And("The clock of {string} is stopped")
	public void the_clock_of_white_is_stopped(String color) {
		// Write code here that turns the phrase above into concrete actions
		if (color == "white") {
			Quoridorcontroller.stopwhiteclock();
		} else
			Quoridorcontroller.stopblackclock();
	}

	/**
	 * Feature :Switch current player Xiangyu Li
	 * 
	 * @param color
	 */
	@When("Player {string} completes his move")
	public void player_blackplayer_completes_his_move(String color) {
		if (color == "black") {
			Quoridorcontroller.completeMove(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
		} else {
			Quoridorcontroller.completeMove(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
		}
	}

	/**
	 * Feature :Switch current player Xiangyu Li
	 * 
	 * @param color
	 */
	@Then("The user interface is showing it is {string}'s turn")
	public void the_user_interface_is_showing_it_is_white_s_turn(String color) {
		// Write code here that turns the phrase above into concrete actions
		if (color == "black") {
			Assert.assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(), currentPlayer);
		}
		if (color == "white") {
			Assert.assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(), currentPlayer);
		}
	}

	/**
	 * Feature :Switch current player Xiangyu Li
	 * 
	 * @param color
	 */
	@And("The clock of {string} is stopped")
	public void the_clock_of_black_is_stopped(String color) {
		if (color == "black") {
			Quoridorcontroller.stopblackclock();
		} else
			Quoridorcontroller.stopwhiteclock();
	}

	/**
	 * Feature :Switch current player Xiangyu Li
	 * 
	 * @param color
	 */
	@And("The clock of {string} is running")
	public void the_clock_of_white_is_running(String color) {
		if (color == "white") {
			Quoridorcontroller.runwhiteclock();
		} else
			Quoridorcontroller.runblackclock();
	}

	/**
	 * Feature :Switch current player Xiangyu Li
	 * 
	 * @param color
	 */
	@And("The next Player to move shall be {string}")
	public void the_player_to_move_is_secondplayer(String color) {
		Player player;
		if (color == "white") {
			player = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			Assert.assertEquals(
					QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove(), player);
		}
		if (color == "black") {
			player = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			Assert.assertEquals(
					QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove(), player);
		}
	}

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
		for (int i = 0; i < 20; i++) {
			Wall wall = Wall.getWithId(i);
			if (wall != null) {
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
		// @formatter:off
		/*
		 * __________ | | | | |x-> <-x| | | |__________|
		 * 
		 */
		// @formatter:on
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
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
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);

		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, players.get(0), players.get(1), quoridor);

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(),
				player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(),
				player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

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
