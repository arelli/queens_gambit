import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


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
	
	public int player_score;
	public boolean king_down_flag = false;
	public int player;
	
	// the max depth of a minmax search
	public int maxDepth = 4;
	
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
		this.player_score = 0;
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
					if(i<6) {
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						if(firstLetter.equals(" ") || firstLetter.equals("P"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
							
							availableMoves.add(move);
						}
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
	public int update_board(String move) {
		if (move.length()!=4) {
			System.out.println("Error update_board. Length(move) !=4");
		}
		// To see at the board and calculate points
		String enemyK;
		String enemyR;
		String enemyP;
		String playerP;
		int playerScore=0;
		
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
			
			playerScore = 1;
//			return move_points;
		} // Enemy pawn there
		else if (this.board[d_x][d_y].equals(enemyP)) {
			this.board[d_x][d_y] = this.board[s_x][s_y];
			this.board[s_x][s_y] = " ";
			
			playerScore = 1;
//			return move_points;
		} // Enemy Rook there 
		else if (this.board[d_x][d_y].equals(enemyR)) {
			this.board[d_x][d_y] = this.board[s_x][s_y];
			this.board[s_x][s_y] = " ";
			
			playerScore = 3;
//			return move_points;
		} // Enemy king there
		else if (this.board[d_x][d_y].equals(enemyK)) {
			this.board[d_x][d_y] = this.board[s_x][s_y];
			this.board[s_x][s_y] = " ";
			
			this.king_down_flag = true;
			playerScore = 8;
//			return move_points;
		}
		
		// TODO: Check if states such us pawn goes to last row 			
		// Is pawn and is at last row then take point and remove
		if (IsPawn && (d_x == 0 || d_x == this.rows-1)) {
			playerScore ++;
			this.board[d_x][d_y] = " ";
		}
		
		return playerScore;
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
		
		if(this.myColor ==1) {
			System.out.println("I am a smart guy");
			return this.selectMinMax(); 
		}
		else {
			System.out.println("I am a dumb guy");
			return this.selectRandomAction();
		}
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
		//if(this.king_down_flag)
		//	return true;
		//TODO: fill it up
		return false;
	}

	// Returns the next move for the current player
	private String selectMinMax() {
		//String[][] tempBoard =  new String[this.rows][this.columns];
		//tempBoard = saveBoard();
    	//view_board();
		
		SearchResult result = findMinMax(saveBoard(), 0);		

		String nextMove = result.move;
			
		System.out.println("Player " + this.player + " played " + nextMove + " with weight " + result.score);
		
		return nextMove;
	}
	
	// returns the max profit subtree, and the move leading to it
	private SearchResult findMinMax(String[][] initialBoard, int currentDepth)
	{		
		// initializations	
		SearchResult result = new SearchResult(),tempResult = new SearchResult(),maxResult = new SearchResult(), minResult = new SearchResult();
		int negativeInfinity = -50000, positiveInfinity = 50000;
		maxResult.score = negativeInfinity; minResult.score= positiveInfinity;
		
		// store current state(score, board,player,availableMoves
		int tempScore = this.player_score;
		String[][] tempBoard =  new String[this.rows][this.columns];
		tempBoard = saveBoard();  // save the current board
		ArrayList<String> tempAvailMoves = this.availableMoves; 
		
		
		// if we are at leaf or max depth
		if(currentDepth>=this.maxDepth  || gameHasEnded()) {  // TODO: or game has ended!
			result.score = getPoints(initialBoard,this.board, this.myColor);  // calculate how good or bad the current state of the game si
			if(true) {
				System.out.println("Points calculated:          " + result.score);
				view_board();	
				}
			
			return result;			
		}	
		 
		// Find the "children" of the current node
		int otherColor;
		if(currentDepth%2==0) 
			if(this.myColor==1)
				otherColor=0;
			else
				otherColor=1;
		else
			if(this.myColor==1)
				otherColor=1;
			else
				otherColor=0;
			
		this.availableMoves = new ArrayList<String>();
		if(currentDepth%2==0) 
			getAvailableMoves(this.myColor, currentDepth);
		else
			getAvailableMoves(otherColor, currentDepth);
		
		// for each available move(children)
		for (int i=0; i<this.availableMoves.size(); i++) {
			// makeMove()						
			update_board(this.availableMoves.get(i));  // now the new score is updated on the class variable player_Score
			
			tempResult = findMinMax(initialBoard, currentDepth+1);  // this only contains a score!
			tempResult.move = this.availableMoves.get(i);  // this is the move that lead to the terminal state
			
			if(currentDepth%2==0) {
				if(tempResult.score>maxResult.score)
					maxResult=tempResult;
			}
			else {
				if(tempResult.score<minResult.score)
					minResult=tempResult;
			}
			
			//restore old state(go to to previous node after trying one move)
			setBoard(tempBoard);
			this.availableMoves = new ArrayList<String>(tempAvailMoves);
			this.player_score = tempScore;
		}
		
		// return result
		if(currentDepth%2==0) 
			return maxResult;
		else
			return minResult;
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
	
	// print the board at the output
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
		
	// save the current this.board to a temporary string
	public String[][] saveBoard() {
		String[][] tempString = new String[this.rows][this.columns];
		for(int i = 0; i<this.rows; i++) {
			for (int k =0; k<this.columns; k++)
				tempString[i][k] = this.board[i][k];
		}
		return tempString;
	}
	
	// set the current this.board to a temporary string
	public void setBoard(String[][] newBoard) {
		for(int i = 0; i<this.rows; i++) {
			for (int k =0; k<this.columns; k++)
				this.board[i][k] = newBoard[i][k];
		}
		return;
	}

	// get the available moves based on the current board, and the current player
	public void getAvailableMoves(int mycolor, int currentDepth) {
		if (currentDepth%2 == 0)
			if(this.myColor == 0)
				whiteMoves();
			else
				blackMoves();
		else // currentDepth%2==1
			if(this.myColor == 0)
				blackMoves();
			else
				whiteMoves();
	}

	// takes two boards as an argument, and compares the point difference between the two
 	public int getPoints(String[][] initialBoard, String[][] currentBoard, int player) {
		//player is 0 for white, and 1 for black. It can be taken  by this.myColor
		int currentPoints =0;
		String myK,myR,myP,enemyK,enemyR,enemyP;
		
		// Set enemy players pawn
		if (player==0) {
			enemyK = "BK";	enemyR = "BR";	enemyP = "BP";
			myK = "WK";	myR = "WR";	myP = "WP";
		}
		else {
			enemyK = "WK"; enemyR = "WR"; enemyP = "WP";
			myK = "BK";	myR = "BR";	myP = "BP";
		}
		
		for(int row = 0; row<this.rows;row++) {
			for(int col = 0; col<this.columns;col++) {
				//count final board "points"
				if(currentBoard[row][col].equals(myK))
					currentPoints+=8;
				else if(currentBoard[row][col].equals(myR))
					currentPoints+=3;
				else if(currentBoard[row][col].equals(myP))
					currentPoints+=1;		
				
				if(currentBoard[row][col].equals(enemyK))
					currentPoints-=8;
				else if(currentBoard[row][col].equals(enemyR))
					currentPoints-=3;
				else if(currentBoard[row][col].equals(enemyP))
					currentPoints-=1;
			}
		}
		return currentPoints;		
	}
}
