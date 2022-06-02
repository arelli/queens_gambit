import java.util.ArrayList;

public class MinMaxPlayer {
	World simWorld;
	
	public void updateWorld(int playerColor, int playerMaxDepth) {
		 simWorld= new World();
		 simWorld.player = simWorld.myColor = playerColor;
		 simWorld.maxDepth = playerMaxDepth;
	}
	
	
	class SearchResult {
		public int score;
		public String move;
	}
	
	// Returns the next move for the current player
	public String selectMinMax(String[][] board) {
		//Initialization of the simulation world
		setBoard(board);  // to get the current state of the game
		

		SearchResult result = findMinMax(0,0,-50000, 50000);		

		String nextMove = result.move;
			
		System.out.println("Player " + simWorld.myColor + " played " + nextMove + " with score " + result.score);
		
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
		String[][] tempBoard =  new String[simWorld.rows][simWorld.columns];
		tempBoard = saveBoard();  // save the current board
		ArrayList<String> tempAvailMoves =  saveAvailableMoves(); // simWorld.availableMoves;
		
		// if we are at leaf or max depth
		if(currentDepth>=simWorld.maxDepth  || gameHasEnded()) {  // TODO: or game has ended!
			result.score = previousScore;  //getPoints(simWorld.board, simWorld.myColor);  // calculate how good or bad the current state of the game is			
			return result;			
		}	
		 
		// Find the "children" of the current node
		simWorld.availableMoves = new ArrayList<String>();  //empty the available moves list
		int otherColor;
		
		if(currentDepth%2==0) {
			getAvailableMoves(simWorld.myColor);
		}
		else if(currentDepth%2==1){
			if(simWorld.myColor==1)
				otherColor=0;
			else
				otherColor=1;
			
			getAvailableMoves(otherColor);
		}			
		
		ArrayList<Integer> childrenResults = new ArrayList<Integer>();  // DEBUG
		
		// for each available move(children)
		for (int i=0; i<simWorld.availableMoves.size(); i++) {
			// make a simulated move	
			newScore = update_board(simWorld.availableMoves.get(i));  // now the new score is updated on the class variable player_Score
			
			// get the move before the findMinMax() makes any changes to the availableMoves arraylist!
			maxResult.move = minResult.move = simWorld.availableMoves.get(i);  // simWorld is the move that lead to the terminal state
						
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
			setAvailableMoves(tempAvailMoves);//simWorld.availableMoves = new ArrayList<String>(tempAvailMoves);
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
		
		for(int i=0; i<simWorld.rows; i++)
		{
			for(int j=0; j<simWorld.columns; j++)
			{
				chessPart = simWorld.board[i][j];
				
				
				if (chessPart.equals(" ")){
					// Is Empty Space
					System.out.print("--");
				} else {
					System.out.print(chessPart);
					
				}
				
				// Print Horizontal definers
				if (j < simWorld.columns-1) {
					System.out.print("|");
				}
			}
			// New line
			System.out.println("");
		}
		
	}
	
		
	// save the current simWorld.board to a temporary string
	public String[][] saveBoard() {
		String[][] tempString = new String[simWorld.rows][simWorld.columns];
		for(int i = 0; i<simWorld.rows; i++) {
			for (int k =0; k<simWorld.columns; k++)
				tempString[i][k] = simWorld.board[i][k];
		}
		return tempString;
	}
	
	
	// set the current simWorld.board to a temporary string
	public void setBoard(String[][] newBoard) {
		for(int i = 0; i<simWorld.rows; i++) {
			for (int k =0; k<simWorld.columns; k++)
				simWorld.board[i][k] = newBoard[i][k];
		}
		return;
	}
		

	// get the available moves based on the current board, and the current player
	public void getAvailableMoves(int playerColor) {
		if(playerColor == 1)
			simWorld.blackMoves();
		else
			simWorld.whiteMoves();
	}

	// takes two boards as an argument, and compares the point difference between the two

	public ArrayList<String> saveAvailableMoves(){
		ArrayList<String> tempArrayList = new ArrayList<String>();
		for (int k =0; k < simWorld.availableMoves.size(); k++)
			tempArrayList.add(simWorld.availableMoves.get(k));
		
		return tempArrayList;
	}
	
	public void setAvailableMoves(ArrayList<String> tempArrayList) {
		simWorld.availableMoves.clear();
		for (int k =0; k<tempArrayList.size(); k++)
			simWorld.availableMoves.add(tempArrayList.get(k));
		return;
	}

	public boolean gameHasEnded() {
		boolean hasOtherPawns = false;
		int howManyKings = 0;
		
		for(int row = 0; row<simWorld.rows;row++) {
			for(int col = 0; col<simWorld.columns;col++) {
				// check if its a (any color) king
				if (simWorld.board[row][col].equals("WK")  || simWorld.board[row][col].equals("BK") ) {
					howManyKings++;
				}  // check if its a (any color) pawn(WP,BP)
				else if(!simWorld.board[row][col].equals("P") && !simWorld.board[row][col].equals(" ")) {
					hasOtherPawns = true;
				}
			}
		}
		// essentially means "if its a draw or somebody already won"
		if ((howManyKings==2 && !hasOtherPawns)||(howManyKings==1))
			return true;  // its a draw

		return false;
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
			
//				int move_points = 0;
			
			// Set enemy players pawn
			if (simWorld.player==0) {
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
			if (s_x < 0 || s_x > simWorld.rows || s_y < 0 || s_y > simWorld.columns) {
				System.out.println("Error update_board. Invalid 'source' coordinates");
			}
			if (d_x < 0 || d_x > simWorld.rows || d_y < 0 || d_y > simWorld.columns) {
				System.out.println("Error update_board. Invalid 'destination' coordinates");
			}
			
			// To check if its pawn
			if (simWorld.board[s_x][s_y].equals(playerP)) {
				IsPawn = true;
			}
			
		
			// Make move
			// None there
			if (simWorld.board[d_x][d_y].equals(" ")) {
				simWorld.board[d_x][d_y] = simWorld.board[s_x][s_y];
				simWorld.board[s_x][s_y] = " ";
				
//					return move_points;
			} // Bonus point there
			// TODO: Not sure if we take the points. Check probabilities. 
			else if (simWorld.board[d_x][d_y].equals("P")) {
				simWorld.board[d_x][d_y] = simWorld.board[s_x][s_y];
				simWorld.board[s_x][s_y] = " ";
				
				playerScore = 1;
//					return move_points;
			} // Enemy pawn there
			else if (simWorld.board[d_x][d_y].equals(enemyP)) {
				simWorld.board[d_x][d_y] = simWorld.board[s_x][s_y];
				simWorld.board[s_x][s_y] = " ";
				
				playerScore = 1;
//					return move_points;
			} // Enemy Rook there 
			else if (simWorld.board[d_x][d_y].equals(enemyR)) {
				simWorld.board[d_x][d_y] = simWorld.board[s_x][s_y];
				simWorld.board[s_x][s_y] = " ";
				
				playerScore = 3;
//					return move_points;
			} // Enemy king there
			else if (simWorld.board[d_x][d_y].equals(enemyK)) {
				simWorld.board[d_x][d_y] = simWorld.board[s_x][s_y];
				simWorld.board[s_x][s_y] = " ";
				
				playerScore = 8;
//					return move_points;
			}
			
			// TODO: Check if states such us pawn goes to last row 			
			// Is pawn and is at last row then take point and remove
			if (IsPawn && (d_x == 0 || d_x == simWorld.rows-1)) {
				playerScore ++;
				simWorld.board[d_x][d_y] = " ";
			}
			
			return playerScore;
		}

}
