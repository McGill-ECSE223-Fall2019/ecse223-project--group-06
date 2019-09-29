/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 4 "main.ump"
public class Game
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private String winner;
  private boolean gameOver;
  private boolean turn;

  //Game Associations
  private Clock clock;
  private List<Player> players;
  private Board board;
  private PositionFile positionFile;
  private Quoridor quoridor;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(String aWinner, boolean aGameOver, boolean aTurn, Clock aClock, Board aBoard, PositionFile aPositionFile, Quoridor aQuoridor)
  {
    winner = aWinner;
    gameOver = aGameOver;
    turn = aTurn;
    if (aClock == null || aClock.getGame() != null)
    {
      throw new RuntimeException("Unable to create Game due to aClock");
    }
    clock = aClock;
    players = new ArrayList<Player>();
    if (aBoard == null || aBoard.getGame() != null)
    {
      throw new RuntimeException("Unable to create Game due to aBoard");
    }
    board = aBoard;
    if (aPositionFile == null || aPositionFile.getGame() != null)
    {
      throw new RuntimeException("Unable to create Game due to aPositionFile");
    }
    positionFile = aPositionFile;
    boolean didAddQuoridor = setQuoridor(aQuoridor);
    if (!didAddQuoridor)
    {
      throw new RuntimeException("Unable to create game due to quoridor");
    }
  }

  public Game(String aWinner, boolean aGameOver, boolean aTurn, int aTimePlayer1ForClock, int aTimePlayer2ForClock, int aSizeForBoard, Quoridor aQuoridor)
  {
    winner = aWinner;
    gameOver = aGameOver;
    turn = aTurn;
    clock = new Clock(aTimePlayer1ForClock, aTimePlayer2ForClock, this);
    players = new ArrayList<Player>();
    board = new Board(aSizeForBoard, this);
    positionFile = new PositionFile(this);
    boolean didAddQuoridor = setQuoridor(aQuoridor);
    if (!didAddQuoridor)
    {
      throw new RuntimeException("Unable to create game due to quoridor");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setWinner(String aWinner)
  {
    boolean wasSet = false;
    winner = aWinner;
    wasSet = true;
    return wasSet;
  }

  public boolean setGameOver(boolean aGameOver)
  {
    boolean wasSet = false;
    gameOver = aGameOver;
    wasSet = true;
    return wasSet;
  }

  public boolean setTurn(boolean aTurn)
  {
    boolean wasSet = false;
    turn = aTurn;
    wasSet = true;
    return wasSet;
  }

  public String getWinner()
  {
    return winner;
  }

  public boolean getGameOver()
  {
    return gameOver;
  }

  public boolean getTurn()
  {
    return turn;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isGameOver()
  {
    return gameOver;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isTurn()
  {
    return turn;
  }
  /* Code from template association_GetOne */
  public Clock getClock()
  {
    return clock;
  }
  /* Code from template association_GetMany */
  public Player getPlayer(int index)
  {
    Player aPlayer = players.get(index);
    return aPlayer;
  }

  public List<Player> getPlayers()
  {
    List<Player> newPlayers = Collections.unmodifiableList(players);
    return newPlayers;
  }

  public int numberOfPlayers()
  {
    int number = players.size();
    return number;
  }

  public boolean hasPlayers()
  {
    boolean has = players.size() > 0;
    return has;
  }

  public int indexOfPlayer(Player aPlayer)
  {
    int index = players.indexOf(aPlayer);
    return index;
  }
  /* Code from template association_GetOne */
  public Board getBoard()
  {
    return board;
  }
  /* Code from template association_GetOne */
  public PositionFile getPositionFile()
  {
    return positionFile;
  }
  /* Code from template association_GetOne */
  public Quoridor getQuoridor()
  {
    return quoridor;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfPlayersValid()
  {
    boolean isValid = numberOfPlayers() >= minimumNumberOfPlayers() && numberOfPlayers() <= maximumNumberOfPlayers();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfPlayers()
  {
    return 2;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlayers()
  {
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfPlayers()
  {
    return 2;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Player addPlayer(String aUsername, Pawn aPawn)
  {
    if (numberOfPlayers() >= maximumNumberOfPlayers())
    {
      return null;
    }
    else
    {
      return new Player(aUsername, aPawn, this);
    }
  }

  public boolean addPlayer(Player aPlayer)
  {
    boolean wasAdded = false;
    if (players.contains(aPlayer)) { return false; }
    if (numberOfPlayers() >= maximumNumberOfPlayers())
    {
      return wasAdded;
    }

    Game existingGame = aPlayer.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfPlayers() <= minimumNumberOfPlayers())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aPlayer.setGame(this);
    }
    else
    {
      players.add(aPlayer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlayer(Player aPlayer)
  {
    boolean wasRemoved = false;
    //Unable to remove aPlayer, as it must always have a game
    if (this.equals(aPlayer.getGame()))
    {
      return wasRemoved;
    }

    //game already at minimum (2)
    if (numberOfPlayers() <= minimumNumberOfPlayers())
    {
      return wasRemoved;
    }
    players.remove(aPlayer);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_SetOneToMany */
  public boolean setQuoridor(Quoridor aQuoridor)
  {
    boolean wasSet = false;
    if (aQuoridor == null)
    {
      return wasSet;
    }

    Quoridor existingQuoridor = quoridor;
    quoridor = aQuoridor;
    if (existingQuoridor != null && !existingQuoridor.equals(aQuoridor))
    {
      existingQuoridor.removeGame(this);
    }
    quoridor.addGame(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Clock existingClock = clock;
    clock = null;
    if (existingClock != null)
    {
      existingClock.delete();
    }
    for(int i=players.size(); i > 0; i--)
    {
      Player aPlayer = players.get(i - 1);
      aPlayer.delete();
    }
    Board existingBoard = board;
    board = null;
    if (existingBoard != null)
    {
      existingBoard.delete();
    }
    PositionFile existingPositionFile = positionFile;
    positionFile = null;
    if (existingPositionFile != null)
    {
      existingPositionFile.delete();
    }
    Quoridor placeholderQuoridor = quoridor;
    this.quoridor = null;
    if(placeholderQuoridor != null)
    {
      placeholderQuoridor.removeGame(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "winner" + ":" + getWinner()+ "," +
            "gameOver" + ":" + getGameOver()+ "," +
            "turn" + ":" + getTurn()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "clock = "+(getClock()!=null?Integer.toHexString(System.identityHashCode(getClock())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "board = "+(getBoard()!=null?Integer.toHexString(System.identityHashCode(getBoard())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "positionFile = "+(getPositionFile()!=null?Integer.toHexString(System.identityHashCode(getPositionFile())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "quoridor = "+(getQuoridor()!=null?Integer.toHexString(System.identityHashCode(getQuoridor())):"null");
  }
}