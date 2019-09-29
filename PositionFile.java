/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 40 "main.ump"
public class PositionFile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PositionFile Associations
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PositionFile(Game aGame)
  {
    if (aGame == null || aGame.getPositionFile() != null)
    {
      throw new RuntimeException("Unable to create PositionFile due to aGame");
    }
    game = aGame;
  }

  public PositionFile(String aWinnerForGame, boolean aGameOverForGame, boolean aTurnForGame, Clock aClockForGame, Board aBoardForGame, Quoridor aQuoridorForGame)
  {
    game = new Game(aWinnerForGame, aGameOverForGame, aTurnForGame, aClockForGame, aBoardForGame, this, aQuoridorForGame);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }

  public void delete()
  {
    Game existingGame = game;
    game = null;
    if (existingGame != null)
    {
      existingGame.delete();
    }
  }

}