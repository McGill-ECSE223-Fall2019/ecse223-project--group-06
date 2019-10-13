package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.time.Clock;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.QuoridorController.Quoridorcontroller;
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
		theGameIsNotRunning();
		createAndStartGame();
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

	/*
	 * TODO Insert your missing step definitions here
	 * 
	 * Call the methods of the controller that will manipulate the model once they
	 * are implemented
	 * 
	 */
	@After
	public void tearDown() {
		quoridor.delete();
		quoridor = null;
	}
	
	
	// After each scenario, the test model is discarded


	// ***********************************************
	// Extracted helper methods
	// ***********************************************
	

	// Place your extracted methods below
	/**
	 * Feature :Set Total thinking time
	 * take minutes and second as input to set 
	 * @param minute
	 * @param second
	 */
	@When("{int}:{int} is set as the thinking time")
	public void is_set_as_the_thinking_time(int minute,int second) {
		Quoridorcontroller.setTotaltime(minute, second);
		throw new cucumber.api.PendingException();
	}
	
	@Then("Both players shall have {int}:{int} remaining time left")
	public void both_players_shall_have_remaining_time_left(int minute,int second) {
		long remaintime=(minute*60+second)*1000;
		Assert.assertEquals(remaintime,QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime());
		Assert.assertEquals(remaintime,QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime());
		throw new cucumber.api.PendingException();
	}
	
	
	
	@Given("The player to move is {String}")
	public void Playertomove(String color) {
		if(color=="black") {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
		}
		else 
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
			throw new cucumber.api.PendingException();
	}
	
	@And("The clock of {string} is running")
	public void the_clock_of_black_is_running(String color) {
	    // Write code here that turns the phrase above into concrete actions
		if(color=="black") {
			Quoridorcontroller.runblackclock();
		}
		else
			Quoridorcontroller.runwhiteclock();
		throw new cucumber.api.PendingException();
	}

	@And("The clock of {string} is stopped")
	public void the_clock_of_white_is_stopped(String color) {
	    // Write code here that turns the phrase above into concrete actions
		if(color=="white") {
			Quoridorcontroller.stopwhiteclock();
		}
		else
			Quoridorcontroller.stopblackclock();
		
		throw new cucumber.api.PendingException();
	}

	@When("Player {string} completes his move")
	public void player_blackplayer_completes_his_move(String color) {
		if(color=="black") {
		Quoridorcontroller.completeMove(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
		}
		else {
		Quoridorcontroller.completeMove(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());	
		}
		throw new cucumber.api.PendingException();
	}
	
	@Then("The user interface is showing it is {string}'s turn")
	public void the_user_interface_is_showing_it_is_white_s_turn(String color) {
	    // Write code here that turns the phrase above into concrete actions
		if(color=="black") {
			Assert.assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(),currentPlayer);
		}
		if(color=="white") {
			Assert.assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(),currentPlayer);
		}
		throw new cucumber.api.PendingException();
	}

	@And ("The clock of {string} is stopped")
	public void the_clock_of_black_is_stopped(String color)throws InterruptedException {
		if(color=="black") {
			Quoridorcontroller.stopblackclock();
		}
		else
			Quoridorcontroller.stopwhiteclock();
		throw new cucumber.api.PendingException();
	}

	@And ("The clock of {string} is running")
	public void the_clock_of_white_is_running(String color) {
		if(color=="white") {
			Quoridorcontroller.runwhiteclock();
		}
		else
			Quoridorcontroller.runblackclock();
		throw new cucumber.api.PendingException();
	}

	@And("The next Player to move shall be {string}")
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
		throw new cucumber.api.PendingException();
	}

	
	
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
