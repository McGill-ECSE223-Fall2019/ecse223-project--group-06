/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 28 "main.ump"
public class Wall
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wall Attributes
  private String wallPosition;

  //Wall Associations
  private Player player;
  private Board board;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wall(String aWallPosition, Player aPlayer, Board aBoard)
  {
    wallPosition = aWallPosition;
    boolean didAddPlayer = setPlayer(aPlayer);
    if (!didAddPlayer)
    {
      throw new RuntimeException("Unable to create wall due to player");
    }
    boolean didAddBoard = setBoard(aBoard);
    if (!didAddBoard)
    {
      throw new RuntimeException("Unable to create wall due to board");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setWallPosition(String aWallPosition)
  {
    boolean wasSet = false;
    wallPosition = aWallPosition;
    wasSet = true;
    return wasSet;
  }

  public String getWallPosition()
  {
    return wallPosition;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }
  /* Code from template association_GetOne */
  public Board getBoard()
  {
    return board;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setPlayer(Player aPlayer)
  {
    boolean wasSet = false;
    //Must provide player to wall
    if (aPlayer == null)
    {
      return wasSet;
    }

    //player already at maximum (10)
    if (aPlayer.numberOfWalls() >= Player.maximumNumberOfWalls())
    {
      return wasSet;
    }
    
    Player existingPlayer = player;
    player = aPlayer;
    if (existingPlayer != null && !existingPlayer.equals(aPlayer))
    {
      boolean didRemove = existingPlayer.removeWall(this);
      if (!didRemove)
      {
        player = existingPlayer;
        return wasSet;
      }
    }
    player.addWall(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setBoard(Board aBoard)
  {
    boolean wasSet = false;
    //Must provide board to wall
    if (aBoard == null)
    {
      return wasSet;
    }

    //board already at maximum (20)
    if (aBoard.numberOfWalls() >= Board.maximumNumberOfWalls())
    {
      return wasSet;
    }
    
    Board existingBoard = board;
    board = aBoard;
    if (existingBoard != null && !existingBoard.equals(aBoard))
    {
      boolean didRemove = existingBoard.removeWall(this);
      if (!didRemove)
      {
        board = existingBoard;
        return wasSet;
      }
    }
    board.addWall(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Player placeholderPlayer = player;
    this.player = null;
    if(placeholderPlayer != null)
    {
      placeholderPlayer.removeWall(this);
    }
    Board placeholderBoard = board;
    this.board = null;
    if(placeholderBoard != null)
    {
      placeholderBoard.removeWall(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "wallPosition" + ":" + getWallPosition()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "board = "+(getBoard()!=null?Integer.toHexString(System.identityHashCode(getBoard())):"null");
  }
}