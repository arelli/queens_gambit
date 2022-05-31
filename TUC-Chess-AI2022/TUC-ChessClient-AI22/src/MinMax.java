import java.util.ArrayList;

public class MinMax {
	private String[][] initialBoard = null;
	private int initialScore;
	private int depth;
	public int maxDepth;
	private int player;   // 0 or 1
	private ArrayList<String> availableMoves = null;
	private int currentScore;
	
	
	CurrentBoardState board;
	
	public MinMax(int player, ArrayList<String> availableMoves, String[][] initialBoard, int maxDepth) {
		this.maxDepth = maxDepth;  // e estw
		this.player = player;
		this.availableMoves = availableMoves;		
		this.initialBoard = initialBoard;
		this.board = new CurrentBoardState(initialBoard, availableMoves, player, 0);
	}
	
	class SearchResult {
		public int score;
		public String move;
	}
	

	public String nextMove(String [][] currentBoard, int currentScore) { 
		
		// Save everything
		this.initialScore = currentScore;
		String result =  findMax(currentBoard,0, currentScore).move;
		// return everything to initial state
		return result;
	}
	
	
	
	public SearchResult findMax(String[][] board,int score, int currentDepth) {
		SearchResult result = new SearchResult();
		// save the current state of the board
		String[][] currentBoard = board;
		
		
		this.initialBoard = board;
		
		// if children leaf, or at max depth-1
		
		
		// search all children
		for(int i=0; i < this.availableMoves.size() ; i++) {
			
		}
		
		
		
		findMin(board,score,currentDepth+1);
		
		// restore previous state
		this.initialBoard = currentBoard;
		
		return result;
	}
	
	
	
	
	
	public SearchResult findMin(String[][] board,int score, int currentDepth) {
		SearchResult result = new SearchResult();
		
		
		return result;
	}

}
