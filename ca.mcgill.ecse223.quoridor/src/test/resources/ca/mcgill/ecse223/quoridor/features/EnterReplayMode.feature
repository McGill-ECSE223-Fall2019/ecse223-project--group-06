Feature: Enter Replay Mode
As a player, I wish to review a past game in replay mode to walk through the moves.

	Scenario: Entering replay mode 
		Given The game is not running
		When I initiate replay mode
		Then The game shall be in replay mode 
			
	Scenario Outline: Continue an unfinished game  
		Given The game is replay mode
    Given The following moves have been played in game:
      | mv | rnd | move |
      |  1 |   1 | e8   |
      |  2 |   1 | e2   |
      |  1 |   2 | e7   |
      |  2 |   2 | e3   |
      |  1 |   3 | e3h  |
      |  2 |   3 | e8h  |		
		And The game does not have a final result
		And The next move is <movno>.<rndno> 
		When I initiate to continue game
		Then The game shall be running
		And The remaining moves of the game shall be removed
    And The next player to move shall be "<player>" 
	
		Examples: 
      | movno | rndno | player |
      |     2 |     1 | black  | 
			|     1 |     2 | white  | 
			|     2 |     2 | black  | 
			|     1 |     3 | white  | 
			
	Scenario Outline: Continue a finished game  
		Given The game is replay mode
    Given The following moves have been played in game:
      | mv | rnd | move |
      |  1 |   1 | e8   |
      |  2 |   1 | e2   |
      |  1 |   2 | e7   |
      |  2 |   2 | e3   |
      |  1 |   3 | e3h  |
      |  2 |   3 | e8h  |		
      |  1 |   4 | 0-1  |		
		And The game has a final result
		And The next move is <movno>.<rndno> 
		When I initiate to continue game
		Then The game shall be in replay mode 
		And I shall be notified that finished games cannot be continued 
	
		Examples: 
      | movno | rndno | 
      |     2 |     1 |  
			|     1 |     2 |  
			|     2 |     2 |  
			|     1 |     4 |  
				