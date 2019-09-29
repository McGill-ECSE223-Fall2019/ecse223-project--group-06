/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 14 "main.ump"
public class Clock
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Clock Attributes
  private int timePlayer1;
  private int timePlayer2;

  //Clock Associations
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Clock(int aTimePlayer1, int aTimePlayer2, Game aGame)
  {
    timePlayer1 = aTimePlayer1;
    timePlayer2 = aTimePlayer2;
    if (aGame == null || aGame.getClock() != null)
    {
      throw new RuntimeException("Unable to create Clock due to aGame");
    }
    game = aGame;
  }

  public Clock(int aTimePlayer1, int aTimePlayer2, String aWinnerForGame, boolean aGameOverForGame, boolean aTurnForGame, Board aBoardForGame, PositionFile aPositionFileForGame, Quoridor aQuoridorForGame)
  {
    timePlayer1 = aTimePlayer1;
    timePlayer2 = aTimePlayer2;
    game = new Game(aWinnerForGame, aGameOverForGame, aTurnForGame, this, aBoardForGame, aPositionFileForGame, aQuoridorForGame);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTimePlayer1(int aTimePlayer1)
  {
    boolean wasSet = false;
    timePlayer1 = aTimePlayer1;
    wasSet = true;
    return wasSet;
  }

  public boolean setTimePlayer2(int aTimePlayer2)
  {
    boolean wasSet = false;
    timePlayer2 = aTimePlayer2;
    wasSet = true;
    return wasSet;
  }

  public int getTimePlayer1()
  {
    return timePlayer1;
  }

  public int getTimePlayer2()
  {
    return timePlayer2;
  }
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


  public String toString()
  {
    return super.toString() + "["+
            "timePlayer1" + ":" + getTimePlayer1()+ "," +
            "timePlayer2" + ":" + getTimePlayer2()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }
}