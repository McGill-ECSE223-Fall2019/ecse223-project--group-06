package ca.mcgill.ecse223.quoridor.features;
import ca.mcgill.ecse223.quoridor.model.*;

public class ProvideSelectUserName {
	public ProvideSelectUserName() {
	}
	public static void createUsername(String name, Quoridor aQuoridor) throws InvalidInputException{
		String error="";
		try {
			User user1 = new User (name,aQuoridor);
		}
		catch (RuntimeException e) {
			error = e.getMessage();
			if (error.equals("Cannot create due to duplicate name")) {
				error = "A user with this name already exists. Please use a different name.";
			}
			throw new InvalidInputException(error);
		}		
		
	}
	public static void selectUsername(User user){
			
	}
}
