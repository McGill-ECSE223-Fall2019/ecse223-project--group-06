package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;

import org.junit.Assert;

import ca.mcgill.ecse223.quoridor.model.Quoridor;
import cucumber.api.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


/** Save Position Step Definition File
 * 
 * @author yajal
 */
public class SavePositionStepDefinition {
	Quoridor quoridor;
	
	@Given("The game is running")
	public void theGameIsRunning() {
		//USER INPUT (fill in for cucumber)
		
		ProvideSelectUserName.createUsername("User1", quoridor);
		ProvideSelectUserName.createUsername("User2", quoridor); //In user input screen
		//3600 seconds allocated per player = 1 hour
		SetTotalThinkingTime.setTimefor2(new Time(3600 * 1000),  //Set the thinking time for created players
				 quoridor.getCurrentGame().getWhitePlayer(), 
				 quoridor.getCurrentGame().getBlackPlayer());
		
		//Boot it all up
		StartNewGame.start(quoridor); //Create a quoridor game
		InitializeBoard.init(quoridor); //Create the board and display
		//One of these methods should almost certainly set the game to running
	}
	
	
	@Given("No file {string} exists in the filesystem")
	public void noFileExistsInTheSystem(String fileName) {
		Assert.assertFalse(SavePosition.containsFile(fileName));
	}
	
	@When("The user initiates to save the game with name {string}")
	public void theUserInitiatesToSaveTheGameWithName(String fileName) {
		//GUI related
		throw new PendingException();
	}
	
	@Then("A file with {string} shall be created in the filesystem")
	public void aFileWithNameShallBeCreated(String fileName) {
		SavePosition.createFile(fileName); //Will throw error if it doesn't work
	}
	
	
	
	
	
	@Given("File {string} exists in the filesystem")
	public void fileNameExistsInSystem(String fileName) {
		//This is confusing me. We have one for it doesn't exist so it
		//seems like an if thing. Should I assert it's true? or make it true?
		Assert.assertTrue(SavePosition.containsFile(fileName));
	}
	
	@And("The user confirms to overwrite existing file")
	public void theUserConfirmsToOverwrite() {
		//GUI related
		throw new PendingException();
	}
	@Then("File with {string} shall be updated in the filesystem")
	public void fileWithNameShallBeUpdatedInSystem(String fileName) {
		SavePosition.savePosition(quoridor.getCurrentGame(), fileName);
	}
	
	
	
	
	

	
	@And("The user cancels to overwrite existing file")
	public void theUserCancelsToOverwrite() {
		//GUI related
		throw new PendingException();
	}
	@Then("File {string} shall not be changed in the filesystem")
	public void fileWithNameShallNotBeUpdatedInSystem(String fileName) {
		//This is literally a step mapping to 'you will do nothing'
		//Hopefully I can do that right
	}
}