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
	public int maxDepth = 9;
	
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
		
		if(this.myColor ==0) {
			System.out.println("I am a smart guy");
			return this.selectRandomAction(); 
		}
		else {
			System.out.println("I am a dumb guy");
			return this.selectMinMax();
		}
	}
	
	@SuppressWarnings("unused")
	private String selectRandomAction()
	{		
		Random ran = new Random();
		int x = ran.nextInt(availableMoves.size());
		
		return availableMoves.get(x);
	}
	
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
	
	class SearchResult {
		public int score;
		public String move;
	}
	
	// Returns the next move for the current player
	private String selectMinMax() {

		SearchResult result = findMinMax(0,0,-50000, 50000);		

		String nextMove = result.move;
			
		System.out.println("Player " + this.myColor + " played " + nextMove + " with score " + result.score);
		
		return nextMove;
	}
	
	// returns the max profit subtree, and the move leading to it
	private SearchResult findMinMax(int currentDepth, int previousScore, int alpha, int beta)
	{		
		// initializations	
		SearchResult result = new SearchResult(),tempResult = new SearchResult(),maxResult = new SearchResult(), minResult = new SearchResult();
		maxResult.score = -50000; minResult.score= 50000;
		int newScore;
		
		// store current state(score, board,player,availableMoves
		String[][] tempBoard =  new String[this.rows][this.columns];
		tempBoard = saveBoard();  // save the current board
		ArrayList<String> tempAvailMoves =  saveAvailableMoves(); // this.availableMoves;
		
		// if we are at leaf or max depth
		if(currentDepth>=this.maxDepth  || gameHasEnded()) {  // TODO: or game has ended!
			result.score = previousScore;  //getPoints(this.board, this.myColor);  // calculate how good or bad the current state of the game is			
			return result;			
		}	
		 
		// Find the "children" of the current node
		this.availableMoves = new ArrayList<String>();  //empty the available moves list
		int otherColor;
		
		if(currentDepth%2==0) {
			getAvailableMoves(this.myColor);
		}
		else if(currentDepth%2==1){
			if(this.myColor==1)
				otherColor=0;
			else
				otherColor=1;
			
			getAvailableMoves(otherColor);
		}			
		
		ArrayList<Integer> childrenResults = new ArrayList<Integer>();  // DEBUG
		
		// for each available move(children)
		for (int i=0; i<this.availableMoves.size(); i++) {
			// make a simulated move	
			newScore = update_board(this.availableMoves.get(i));  // now the new score is updated on the class variable player_Score
			
			// get the move before the findMinMax() makes any changes to the availableMoves arraylist!
			maxResult.move = minResult.move = this.availableMoves.get(i);  // this is the move that lead to the terminal state
						
			if(currentDepth%2==0) {
				tempResult.score = findMinMax(currentDepth+1, previousScore+newScore, alpha, beta).score;  // my player's score is added
				if(tempResult.score>maxResult.score)
					maxResult.score=tempResult.score;
				
				childrenResults.add(maxResult.score);  // DEBUG
				
				//max evaluation between current score and alpha
				alpha = Math.max(alpha, tempResult.score);
			}
			else {
				tempResult.score = findMinMax(currentDepth+1, previousScore-newScore, alpha, beta).score;  // the enemy players score is subtracted
				if(tempResult.score<minResult.score)
					minResult.score=tempResult.score;
				
				childrenResults.add(minResult.score);  // DEBUG
				
				beta = Math.min(beta, tempResult.score);
			}
			
			// alpha-beta pruning of the probability tree
			if (beta<= alpha)
				break;
			
			
			
			//restore old state(go to to previous node after trying one move)
			setBoard(tempBoard);
			setAvailableMoves(tempAvailMoves);//this.availableMoves = new ArrayList<String>(tempAvailMoves);
		}
		
		for (int i=0; i<childrenResults.size(); i++) {  // DEBUG loop
			System.out.print("["+childrenResults.get(i)+"]");
		}
		
		// return result
		if(currentDepth%2==0) {
			System.out.println("-->[max]="+maxResult.score);
			return maxResult;
		}
		else {
			System.out.println("-->[min]="+minResult.score);
			return minResult;
		}
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
	public void getAvailableMoves(int playerColor) {
		if(playerColor == 1)
			blackMoves();
		else
			whiteMoves();
	}

	// takes two boards as an argument, and compares the point difference between the two

	public ArrayList<String> saveAvailableMoves(){
		ArrayList<String> tempArrayList = new ArrayList<String>();
		for (int k =0; k < this.availableMoves.size(); k++)
			tempArrayList.add(this.availableMoves.get(k));
		
		return tempArrayList;
	}
	
	public void setAvailableMoves(ArrayList<String> tempArrayList) {
		this.availableMoves.clear();
		for (int k =0; k<tempArrayList.size(); k++)
			this.availableMoves.add(tempArrayList.get(k));
		return;
	}

	public boolean gameHasEnded() {
		boolean hasOtherPawns = false;
		int howManyKings = 0;
		
		for(int row = 0; row<this.rows;row++) {
			for(int col = 0; col<this.columns;col++) {
				// check if its a (any color) king
				if (this.board[row][col].equals("WK")  || this.board[row][col].equals("BK") ) {
					howManyKings++;
				}  // check if its a (any color) pawn(WP,BP)
				else if(!this.board[row][col].equals("P") && !this.board[row][col].equals(" ")) {
					hasOtherPawns = true;
				}
			}
		}
		// essentially means "if its a draw or somebody already won"
		if ((howManyKings==2 && !hasOtherPawns)||(howManyKings==1))
			return true;  // its a draw

		return false;
	}
}
