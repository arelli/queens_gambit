import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class World
{
	public String[][] board = null;
	public String[][] cur_board = null;
	private int rows = 7;
	private int columns = 5;
	private int myColor = 0;
	private ArrayList<String> availableMoves = null;
	private int rookBlocks = 3;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private int nBranches = 0;
	private int noPrize = 9;
	
	public int player_score = 0;
	public boolean king_down_flag = false;
	public int player;
	
	// the max depth of a minmax search
	public int maxDepth = 1;
	
	public World()
	{
		board = new String[rows][columns];
		
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		
		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";
		
		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
		availableMoves = new ArrayList<String>();
	}
	
	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
	}

	
	private void whiteMoves()

	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a white chess part in this position then keep on searching
				if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-1) + Integer.toString(j);
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j-1].charAt(0));						
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j-1);
								
							availableMoves.add(move);
						}											
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j+1].charAt(0));
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j+1);							
							availableMoves.add(move);
						}
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
	}
	
	private void blackMoves()
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a black chess part in this position then keep on searching
				if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i+1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+1) + Integer.toString(j);
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j-1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j-1);
								
							availableMoves.add(move);
						}																	
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j+1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j+1);
								
							availableMoves.add(move);
						}
							
						
						
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
	}
	
	// saves the current score at the class variable player_score
	public void update_board(String move) {
		if (move.length()!=4) {
			System.out.println("Error update_board. Length(move) !=4");
		}
		// To see at the board and calculate points
		String enemyK;
		String enemyR;
		String enemyP;
		String playerP;
		
		// Pawns removed at last/first row
		boolean IsPawn = false;
		
//		int move_points = 0;
		
		// Set enemy players pawn
		if (player==0) {
			enemyK = "BK";
			enemyR = "BR";
			enemyP = "BP";
			
			playerP = "WP";
		}else {
			enemyK = "WK";
			enemyR = "WR";
			enemyP = "WP";
			
			playerP = "BP";
		}
		
		//x -> rows
		//y -> columns
		// Set source
		int s_x = Character.getNumericValue(move.charAt(0));
		int s_y = Character.getNumericValue(move.charAt(1));
		// Set destination
		int d_x = Character.getNumericValue(move.charAt(2));
		int d_y = Character.getNumericValue(move.charAt(3));
		
		// Check Coordinates
		if (s_x < 0 || s_x > this.rows || s_y < 0 || s_y > this.columns) {
			System.out.println("Error update_board. Invalid 'source' coordinates");
		}
		if (d_x < 0 || d_x > this.rows || d_y < 0 || d_y > this.columns) {
			System.out.println("Error update_board. Invalid 'destination' coordinates");
		}
		
		// To check if its pawn
		if (this.board[s_x][s_y].equals(playerP)) {
			IsPawn = true;
		}
		
	
		// Make move
		// None there
		if (this.board[d_x][d_y].equals(" ")) {
			this.board[d_x][d_y] = this.board[s_x][s_y];
			this.board[s_x][s_y] = " ";
			
//			return move_points;
		} // Bonus point there
		// TODO: Not sure if we take the points. Check probabilities. 
		else if (this.board[d_x][d_y].equals("P")) {
			this.board[d_x][d_y] = this.board[s_x][s_y];
			this.board[s_x][s_y] = " ";
			
			player_score += 1;
//			return move_points;
		} // Enemy pawn there
		else if (this.board[d_x][d_y].equals(enemyP)) {
			this.board[d_x][d_y] = this.board[s_x][s_y];
			this.board[s_x][s_y] = " ";
			
			player_score += 1;
//			return move_points;
		} // Enemy Rook there 
		else if (this.board[d_x][d_y].equals(enemyR)) {
			this.board[d_x][d_y] = this.board[s_x][s_y];
			this.board[s_x][s_y] = " ";
			
			player_score += 3;
//			return move_points;
		} // Enemy king there
		else if (this.board[d_x][d_y].equals(enemyK)) {
			this.board[d_x][d_y] = this.board[s_x][s_y];
			this.board[s_x][s_y] = " ";
			
			this.king_down_flag = true;
			player_score += 8;
//			return move_points;
		}
		
		// TODO: Check if states such us pawn goes to last row 			
		// Is pawn and is at last row then take point and remove
		if (IsPawn && (d_x == 0 || d_x == this.rows-1)) {
			player_score ++;
			this.board[d_x][d_y] = " ";
		}
	}
	
	public String selectAction()
	{
		availableMoves = new ArrayList<String>();
				
		if(myColor == 0)		// I am the white player
			this.whiteMoves();
		else					// I am the black player
			this.blackMoves();
		
		// keeping track of the branch factor
		nTurns++;
		nBranches += availableMoves.size();
		
		return this.selectMinMax();  // this.selectRandomAction();
	}
	
	@SuppressWarnings("unused")
	private String selectRandomAction()
	{		
		Random ran = new Random();
		int x = ran.nextInt(availableMoves.size());
		
		return availableMoves.get(x);
	}
	
	
	class SearchResult {
		public int score;
		public String move;
	}
	
	
	public boolean gameHasEnded() {
		// check if somebody won or there is a draw
		
		//TODO: fill it up
		return false;
	}

	// Returns the next move for the current player
	private String selectMinMax() {
//		String[][] tempBoard =  new String[this.rows][this.columns];
//		copyBoard(this.board,tempBoard);
//		view_board();
		
		String tempString = findMinMax(0).move;		
		
//		copyBoard(tempBoard, this.board);
//		view_board();
		return tempString;
	}
	
	// returns the max profit subtree, and the move leading to it
	private SearchResult findMinMax(int currentDepth)
	{		
		// initializations
		view_board();
		SearchResult result = new SearchResult();
		ArrayList<SearchResult> childrenResults = new ArrayList<SearchResult>();
		ArrayList<Integer> childrenScores = new ArrayList<Integer>();
		//System.out.println("[MinMax]depth=" + currentDepth + ",current_score="+this.player_score );
		
		// store current state(score, board,player,availableMoves
		//int tempScore = this.player_score;
		//String[][] tempBoard = this.board;
		
		String[][] tempBoard = new String[this.rows][this.columns];
		for(int i = 0; i < this.board.length; i++)
			tempBoard[i] = this.board[i].clone();
		
		
		
		int tempPlayer = this.player;
		ArrayList<String> tempAvailMoves = this.availableMoves; 
		
		
		// if we are at leaf or max depth
		if(currentDepth>=this.maxDepth  || gameHasEnded()) {  // TODO: or game has ended!
			// return current score
			result.score = this.player_score;
			System.out.println("Returning result " + result.score );
			return result;			
		}	
		
		// for each available move(children)
		for (int i=0; i<this.availableMoves.size(); i++) {
			System.out.println("Investigating action " + this.availableMoves.get(i) + " of " + this.availableMoves.size());
			// makeMove()
			update_board(this.availableMoves.get(i));  // now the new score is updated on the class variable player_Score
			// blackMoves() or whiteMoves() depending on depth
			if (currentDepth%2 == 0)
				if(this.player == 0)
					blackMoves();
				else
					whiteMoves();
			else 
				if(this.player == 1 )
					whiteMoves();
				else
					blackMoves();
			// call selectMin(depth+1,) & store return values in a array
			SearchResult tempResult = new SearchResult();
			tempResult = findMinMax(currentDepth+1);  // this only contains a score!
			tempResult.move = this.availableMoves.get(i);  // this is the move that lead to the terminal state
			childrenResults.add(tempResult);
			
			//restore old state(go to to previous node after trying one move)
			//this.player_score = tempScore;;
			//this.board = tempBoard;
			for(int k = 0; i < tempBoard.length; i++)
				this.board[k] = tempBoard[k].clone();
			
			this.player = tempPlayer;
			this.availableMoves = new ArrayList<String>(tempAvailMoves); 
		}
		
		
		// find max(array) and put it into Result
		for(int i=0; i<childrenResults.size(); i++) {
			childrenScores.add(childrenResults.get(i).score);			
		}
		
		
		if (currentDepth%2 == 0)
			result = childrenResults.get(childrenScores.indexOf(Collections.max(childrenScores)));
		else
			result = childrenResults.get(childrenScores.indexOf(Collections.min(childrenScores)));
		
		// return result
		System.out.println("Returning move " + result.move + ", " + result.score );
		return result;
	}
	
	
	/*
	// returns the min profit subtree, and the move leading to it
	private SearchResult findMin(int currentDepth)
	{		
		// initializations
		SearchResult result = new SearchResult();
		ArrayList<SearchResult> childrenResults = new ArrayList<SearchResult>();
		ArrayList<Integer> childrenScores = new ArrayList<Integer>();
		System.out.println("[min]depth=" + currentDepth + ",current_score="+this.player_score );
		
		// store current state(score, board,player,availableMoves
		int tempScore = this.player_score;
		String[][] tempBoard = this.board;
		int tempPlayer = this.player;
		ArrayList<String> tempAvailMoves = this.availableMoves; 
		
		// if we are at leaf or max depth
		if(currentDepth>=this.maxDepth  || gameHasEnded()) {  // TODO: or game has ended!
			// return current score
			result.score = this.player_score;
			return result;			
		}	
		
		// for each available move(children)
		for (int i=0; i<this.availableMoves.size(); i++) {
			System.out.println("Investigating action " + this.availableMoves.get(i) + " of " + this.availableMoves.size());
			// makeMove()
			update_board(this.availableMoves.get(i));  // now the new score is updated on the class variable player_Score
			// blackMoves() or whiteMoves() depending on depth
			if (currentDepth%2 == 0) blackMoves(); else whiteMoves();
			// call selectMin(depth+1,) & store return values in a array
			childrenResults.add(findMax(currentDepth+1));
			
			//restore old state(go to to previous node after trying one move)
			this.player_score = tempScore;;
			this.board = tempBoard;
			this.player = tempPlayer;
			this.availableMoves = new ArrayList<String>(tempAvailMoves); 
		}
		
		
		// find max(array) and put it into Result
		for(int i=0; i<childrenResults.size(); i++) {
			childrenScores.add(childrenResults.get(i).score);			
		}
		
		result = childrenResults.get(childrenScores.indexOf(Collections.min(childrenScores)));
		
		// return result
		return result;
	}
	
	*/
		
		
	public double getAvgBFactor()
	{
		return nBranches / (double) nTurns;
	}
	
	public void makeMove(int x1, int y1, int x2, int y2, int prizeX, int prizeY)
	{
		String chesspart = Character.toString(board[x1][y1].charAt(1));
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[x2][y2] = " ";	// in a case an opponent's chess part has just been captured
				board[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[x2][y2] = board[x1][y1];
			board[x1][y1] = " ";
		}
		
		// check if a prize has been added in the game
		if(prizeX != noPrize)
			board[prizeX][prizeY] = "P";
	}
	
	
	public void view_board(){
		
		String chessPart;

		
		System.out.println("Board at current state");
		System.out.println("======================");
		
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				chessPart = board[i][j];
				
				
				if (chessPart.equals(" ")){
					// Is Empty Space
					System.out.print("--");
				} else {
					System.out.print(chessPart);
					
				}
				
				// Print Horizontal definers
				if (j < columns-1) {
					System.out.print("|");
				}
			}
			// New line
			System.out.println("");
		}
		
	}
	
	
	public void copyBoard(String[][] oldBoard, String[][] newBoard) {
		for(int i = 0; i<this.rows; i++) {
			for (int k =0; k<this.columns; k++)
				oldBoard[i][k] = newBoard[i][k];
		}
		return;
	}
	
}
