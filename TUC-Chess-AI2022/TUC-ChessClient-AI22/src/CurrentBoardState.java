import java.util.ArrayList;
//import java.util.Random;

public class CurrentBoardState {
		
		private String[][] board = null;
		private int rows = 7;
		private int columns = 5;
		private int rookBlocks = 3;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
		private int totalScore = 0;
		
		
		private ArrayList<String> player_availableMoves = null;
		private int player;
		private int enemy;

		private int depth;
	
		
		public int player_score = 0;
		public int enemy_score = 0;
		
		// When updating check if mat
		public boolean king_down_flag = false;
		
//		private int myColor = 0;		
//		private ArrayList<String> enemy_availableMoves = null; 

//		private int nTurns = 0;
//		private int nBranches = 0;
//		private int noPrize = 9;
//		private String pawnName = null;
		
//		private ArrayList<String> whiteMoves = null;
		
		//	Contractor
		public CurrentBoardState(String[][]  board, ArrayList<String> availMoves, int player, int depth) {
			// Set Board
			this.board = board;
			// Players
			this.player = player;
			this.enemy = 1 - this.player;
			// AvailableMoves for the player
			this.player_availableMoves = availMoves;
			// Depth
			this.depth = depth;
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
		
		// Make move to board and return its points. If enemy king sees reversed radikia then global flag is on
		public int update_board(int player, String move) {
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
			
			int move_points = 0;
			
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
			int s_x = (int)move.charAt(0);
			int s_y = (int)move.charAt(1);
			// Set destination
			int d_x = (int)move.charAt(2);
			int d_y = (int)move.charAt(3);
			
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
				
//				return move_points;
			} // Bonus point there
			// TODO: Not sure if we take the points. Check probabilities. 
			else if (this.board[d_x][d_y].equals("P")) {
				this.board[d_x][d_y] = this.board[s_x][s_y];
				this.board[s_x][s_y] = " ";
				
				move_points = 1;
//				return move_points;
			} // Enemy pawn there
			else if (this.board[d_x][d_y].equals(enemyP)) {
				this.board[d_x][d_y] = this.board[s_x][s_y];
				this.board[s_x][s_y] = " ";
				
				move_points = 1;
//				return move_points;
			} // Enemy Rook there 
			else if (this.board[d_x][d_y].equals(enemyR)) {
				this.board[d_x][d_y] = this.board[s_x][s_y];
				this.board[s_x][s_y] = " ";
				
				move_points = 3;
//				return move_points;
			} // Enemy king there
			else if (this.board[d_x][d_y].equals(enemyK)) {
				this.board[d_x][d_y] = this.board[s_x][s_y];
				this.board[s_x][s_y] = " ";
				
				this.king_down_flag = true;
				move_points = 8;
//				return move_points;
			}
			// No need to check if new points are generated because its stochastic

			
			// TODO: Check if states such us pawn goes to last row 			
			// Is pawn and is at last row then take point and remove
			if (IsPawn && (d_x == 0 || d_x == this.rows-1)) {
				move_points ++;
				this.board[d_x][d_y] = " ";
			}
			
			return move_points;
		}
		
		
		// TODO: Make a fanction that calculates how many and what pawns each player.
		// Somehow we can manage our moves if we have more points
		
		
		// Set enemy available moves in respect of board
		private ArrayList<String> set_availableMoves()
		{	
			ArrayList<String> enemy_availableMoves = null;
			char enemy_firstLetter;
			char player_firstLetter;
			String firstLetter = "";
			String secondLetter = "";
//			String enemy_firstLetter = "";
			String move = "";

//			enemy_availableMoves = ;
			
//			Witch to look
			if (this.enemy == 0) {
				enemy_firstLetter = 'W';
				player_firstLetter = 'B';
			} else {
				enemy_firstLetter = 'B';
				player_firstLetter = 'W';
			}
			
//			For each board position
			for(int i=0; i<rows; i++)
			{
				for(int j=0; j<columns; j++)
				{
					
					firstLetter = Character.toString(board[i][j].charAt(0));
					
					// Can move only enemy parts
					if(!firstLetter.equals(enemy_firstLetter) || firstLetter.equals(" ") || firstLetter.equals("P"))
						continue;
					
					// Check the kind of the chess part
					secondLetter = Character.toString(board[i][j].charAt(1));
					
					// It is a pawn
					if(secondLetter.equals("P"))
					{
						
						// Check if it can move one vertical position ahead
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(firstLetter.equals(" ") || firstLetter.equals("P"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
							
							enemy_availableMoves.add(move);
						}
						
						// Check if enemy player can move crosswise to the left
						if(j!=0 && i!=0)
						{
							firstLetter = Character.toString(board[i-1][j-1].charAt(0));						
							// Can move only if can take point
							if(!(firstLetter.equals(enemy_firstLetter) || firstLetter.equals(" ") || firstLetter.equals("P"))) {
								move = Integer.toString(i) + Integer.toString(j) + 
										   Integer.toString(i-1) + Integer.toString(j-1);
									
								enemy_availableMoves.add(move);
							}											
						}
						
						// Check if it can move crosswise to the right
						if(j!=columns-1 && i!=0)
						{
							firstLetter = Character.toString(board[i-1][j+1].charAt(0));
							if(!(firstLetter.equals(enemy_firstLetter) || firstLetter.equals(" ") || firstLetter.equals("P"))) {
								
								move = Integer.toString(i) + Integer.toString(j) + 
										   Integer.toString(i-1) + Integer.toString(j+1);							
								enemy_availableMoves.add(move);
							}
						}
					}
					
					// It is a rook
					else if(secondLetter.equals("R"))	
					{
						// check if it can move upwards
						for(int k=0; k<rookBlocks; k++)
						{
							if((i-(k+1)) < 0)
								break;
							
							firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
							
							// If at destination there is same enemy's point then abord
							if(firstLetter.equals(enemy_firstLetter))
								break;
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-(k+1)) + Integer.toString(j);
							
							enemy_availableMoves.add(move);
							
							// prevent detouring a chesspart to attack the other
							if(firstLetter.equals(player_firstLetter) || firstLetter.equals("P"))
								break;
						}
						
						// check if it can move downwards
						for(int k=0; k<rookBlocks; k++)
						{
							if((i+(k+1)) == rows)
								break;
							
							firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
							
							if(firstLetter.equals(enemy_firstLetter))
								break;
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+(k+1)) + Integer.toString(j);
							
							enemy_availableMoves.add(move);
							
							// prevent detouring a chesspart to attack the other
							if(firstLetter.equals(player_firstLetter) || firstLetter.equals("P"))
								break;
						}
						
						// check if it can move on the left
						for(int k=0; k<rookBlocks; k++)
						{
							if((j-(k+1)) < 0)
								break;
							
							firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
							
							if(firstLetter.equals(enemy_firstLetter))
								break;
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-(k+1));
							
							enemy_availableMoves.add(move);
							
							// prevent detouring a chesspart to attack the other
							if(firstLetter.equals(player_firstLetter) || firstLetter.equals("P"))
								break;
						}
						
						// check of it can move on the right
						for(int k=0; k<rookBlocks; k++)
						{
							if((j+(k+1)) == columns)
								break;
							
							firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
							
							if(firstLetter.equals(enemy_firstLetter))
								break;
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+(k+1));
							
							enemy_availableMoves.add(move);
							
							// prevent detouring a chesspart to attack the other
							if(firstLetter.equals(player_firstLetter) || firstLetter.equals("P"))
								break;
						}
					}
					// It is the king
					else 
					{
						// check if it can move upwards
						if((i-1) >= 0)
						{
							firstLetter = Character.toString(board[i-1][j].charAt(0));
							
							if(!firstLetter.equals(enemy_firstLetter))
							{
								move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j);
									
								enemy_availableMoves.add(move);	
							}
						}
						
						// check if it can move downwards
						if((i+1) < rows)
						{
							firstLetter = Character.toString(board[i+1][j].charAt(0));
							
							if(!firstLetter.equals(enemy_firstLetter))
							{
								move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j);
									
								enemy_availableMoves.add(move);	
							}
						}
						
						// check if it can move on the left
						if((j-1) >= 0)
						{
							firstLetter = Character.toString(board[i][j-1].charAt(0));
							
							if(!firstLetter.equals(enemy_firstLetter))
							{
								move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i) + Integer.toString(j-1);
									
								enemy_availableMoves.add(move);	
							}
						}
						
						// check if it can move on the right
						if((j+1) < columns)
						{
							firstLetter = Character.toString(board[i][j+1].charAt(0));
							
							if(!firstLetter.equals(enemy_firstLetter))
							{
								move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i) + Integer.toString(j+1);
									
								enemy_availableMoves.add(move);	
							}
						}
					}			
				}	
			}
			return enemy_availableMoves;
		}
		
		public static void Main() { 
			System.out.println("Peos");
			
			// Create a new board
			int rows = 7;
			int columns = 5;
			String[][] board = new  String[rows][columns];
			
			// initialization of the board
			for(int i=0; i<rows; i++)
				for(int j=0; j<columns; j++)
					board[i][j] = " ";
			
			
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
			
			
			
			
			System.out.println("Peos");
			
			CurrentBoardState curBoard = new CurrentBoardState(board, null, 0,0);
			
			curBoard.view_board();

		}

}
