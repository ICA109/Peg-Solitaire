import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//33-peg Peg Solitaire solver done as challenge problem for COMP250. Path finding algorithm pairs BFS type traversal with
//a modified search heuristic to avoid dead-ends. Program is able to succesfully all possible configurations up to complete board.

public class pegSolitaireSolver {

	public static void main(String[] args) {

		class HiRiQ {
			// int is used to reduce storage to a minimum...
			public int config;
			public byte weight;

			// initialize to one of 5 reachable START config n=0,1,2,3,4
			HiRiQ(byte n) {
				if (n == 0) {
					config = 65536 / 2;
					weight = 1;
				} else if (n == 1) {
					config = 1626;
					weight = 6;
				} else if (n == 2) {
					config = -1140868948;
					weight = 10;
				} else if (n == 3) {
					config = -411153748;
					weight = 13;
				//For my custom test cases
				} else if (n == 4) {
					//Some of my test configs
					/*
					config = 4096;
					weight = 1;
					
					config = 131074; //2W10,5B17,16B18, (for no move after beginning!)
					weight = 2;

					config = 1276232578;
					weight = 11;

					config = -407011840;
					weight = 7;

					config = -478283824;  
					weight = 9;

					config = 1619134512;  
					weight = 7;

					config = 418604;	
					weight = 9;
					*/

					config = -411153748;
					weight = 13;
					
					
				} else {
					config = -2147450879;
					weight = 32;
				}
			}

			// Final (solved) configuration which position index should be at 15th (center). the config
			// value can be calculated by store(boolean[] B) as Config= 2^15 = 32768 and only one peg
			// left (Weight=1)
			boolean IsSolved() {
				return ((config == 65536 / 2) && (weight == 1));
			}

			// transforms the array of 33 booleans to an (int) config and a (byte) weight.
			public void store(boolean[] B) {
				int a = 1;
				config = 0;
				weight = (byte) 0;
				if (B[0]) {
					weight++;
				}
				for (int i = 1; i < 32; i++) {
					if (B[i]) {
						config = config + a;
						weight++;
					}
					a = 2 * a;
				}
				if (B[32]) { // if the 33rd bit is true (has beg), then force config to negative
					config = -config;
					weight++;
				}
			}

			// transform the int representation to an array of booleans.
			// the weight (byte) is necessary because only 32 bits are memorized
			// and so the 33rd is decided based on the fact that the config has
			// the correct weight or not.
			public boolean[] load(boolean[] B) {
				byte count = 0; // count be used to determine B[0]
				int fig = config;
				B[32] = fig < 0;
				if (B[32]) {
					fig = -fig;
					count++;
				}
				int a = 2;
				for (int i = 1; i < 32; i++) {
					B[i] = fig % a > 0;
					if (B[i]) {
						fig = fig - a / 2;
						count++;
					}
					a = 2 * a;
				}
				B[0] = count < weight;
				return (B);
			}

			// prints the int representation to an array of booleans.
			// the weight (byte) is necessary because only 32 bits are memorized
			// and so the 33rd is decided based on the fact that the config has
			// the correct weight or not.
			public void printB(boolean Z) {
				if (Z) {
					System.out.print("[ ]");
				} else {
					System.out.print("[@]");
				}
			}

			public void print() {
				byte count = 0;
				int fig = config;
				boolean next, last = fig < 0;
				if (last) {
					fig = -fig;
					count++;
				}
				int a = 2;
				for (int i = 1; i < 32; i++) {
					next = fig % a > 0;
					if (next) {
						fig = fig - a / 2;
						count++;
					}
					a = 2 * a;
				}
				next = count < weight;

				count = 0;
				fig = config;
				if (last) {
					fig = -fig;
					count++;
				}
				a = 2;

				System.out.print("      ");
				printB(next);
				for (int i = 1; i < 32; i++) {
					next = fig % a > 0;
					if (next) {
						fig = fig - a / 2;
						count++;
					}
					a = 2 * a;
					printB(next);
					if (i == 2 || i == 5 || i == 12 || i == 19 || i == 26 || i == 29) {
						System.out.println();
					}
					if (i == 2 || i == 26 || i == 29) {
						System.out.print("      ");
					}
					;
				}
				printB(last);
				System.out.println();

			}
		}

		// An array of 33 booleans represent a configuration of HiRiQ
		boolean[] B = new boolean[33];

		System.out.println("\nn=0: A solved configuration (only one peg at center)----");
		HiRiQ W = new HiRiQ((byte) 0);
		W.print();
		System.out.println(W.IsSolved());

		/////////////////////////////////////////////////
		//Test here. Set n="x" as an initial configuration (see above) 
		/////////////////////////////////////////////////
		byte n = 4;  //0-3, else as given configs; "4" is my test

		System.out.println("\n---initial test configuration (Start Board) n=" + n + " : HiRiQ X ---");
		HiRiQ X = new HiRiQ((byte) n);
		X.print();
		System.out.println("Config=" + X.config + "   Weight=" + X.weight);
		System.out.println(X.IsSolved());
		B = X.load(B); //Set 33 bits true/false by X.config & X.weight

		//Run my test method
		PlayHiQ play = new PlayHiQ();
		String returnPath = play.searchPath(B);
		System.out.println("\nreturn Path  = " + returnPath);

		// HiRiQ Y = new HiRiQ((byte) 2);
		// Y.print();
		// System.out.println(Y.IsSolved());
		// HiRiQ Z = new HiRiQ((byte) 3);
		// Z.print();
		// System.out.println(Z.IsSolved());
		// HiRiQ V = new HiRiQ((byte) 4);
		// V.print();
		// System.out.println(V.IsSolved());

	}
}

//******************************My Methods Begin Here******************************
//Note: class PlayHiQ is at the level structure level as class tester
////////////////////////////////////////////////////////////////////////////////////
// To run: create PlayHiQ object, call searchPath(boolean[] toSolve) on 
// that object and store that value in the String to be printed)
// For instance (place the following 3 lines at end of main()):
// PlayHiQ play = new PlayHiQ();
// String returnPath = play.searchPath(B);
// System.out.println("\nreturn Path  = " + returnPath);
////////////////////////////////////////////////////////////////////////////////////

class PlayHiQ {

	public static String solutionPath = ""; //Used for return path (global static variable)

	public String searchPath(boolean[] initialConfig) {

		System.out.println("\nFinding a path ....\n");

		//Load input configuration as a start board
		Board initialBoard = new Board(initialConfig);
		initialBoard.config = initialBoard.gstore(initialConfig);

		if (initialBoard.config != 32768) //Solved configuration
		{
			//Create tree, with initial board as root node
			Tree root = new Tree(initialBoard);

			//Collect history of previously searched configurations
			//Avoids issue of repeating substitutions on same configs
			List<Integer> configHist = new ArrayList<Integer>();
			configHist.add(initialBoard.config);

			//Get all initial boards from a given start configuration (Doing W-Sub first)
			List<Board> initialBoards = initialBoard.checkTriplets(initialBoard.bits, configHist);

			if (initialBoards.isEmpty()) //If no possible W-Sub's found, then try B-substitution
				initialBoards = initialBoard.B_checkTriplets(initialBoard.bits, configHist);

			//For displaying the search result
			boolean pathFound = false;

			while (!pathFound) {
				//Look for each possible configuration for given initial boards with for-each loop
				for (Board nextBoard : initialBoards) {
					Tree nextNode = new Tree(nextBoard); //Prepare a tree node
					if (play(nextBoard, nextNode, configHist)) //Recursively go through every possible configuration
					{
						pathFound = true;
						root.addChild(nextNode);
						break;
					}
				}
				//If for-loop fails to find target, use a B-Sub to create a new set of possible substitutions
				System.out.println();
				initialBoards = initialBoard.B_checkTriplets(initialBoard.bits, configHist);
			}

			if (!pathFound)
				System.out.println("Path not found!");
			else {
				System.out.println();
				printSolvedPath(root);
			}
		} 
		
		else {
			System.out.println("It is solved configuration!");
		}
		System.out.println("\n\n.......Done! ");

		return solutionPath.substring(5); //Filter out null value at front
	}

	//Explore all possible configurations via recursion from current entry node (as parent)
	public boolean play(Board bd, Tree parent, List<Integer> configHist) {
		//Check if this board is the solved board, if so stop searching and return true
		//else go on and check other boards
		if (bd.finalBoard(32768))
			return true;

		//Get possible next configurations for the board
		List<Board> nextBoards = bd.checkTriplets(bd.bits, configHist);

		if (nextBoards.size() == 0)
			return false; //If no such possible Boards with given Board bd
		else
			System.out.print("*"); //For testing only

		boolean found = false;

		//If the board has nextBoard(s), for each move do:
		for (Board nextBoard : nextBoards) {
			Tree nextNode = new Tree(nextBoard);
			if (play(nextBoard, nextNode, configHist)) { // go recurse!!!
				found = true;
				parent.addChild(nextNode);	//add path into a return tree
				//break;
			}
		}
		//Until all nextBoard, nextnextBoard, ...., done
		return found;
	}

	//Print the nodes on the way up the tree (child to parent) and then remove it
	public void printSolvedPath(Tree parent) {
		System.out.print(parent.getPath() + ", ");
		solutionPath  = solutionPath + parent.getPath() + ", ";
		if (parent.numChildren() > 0) {
			Tree nextNode = parent.getFirstChild();
			printSolvedPath(nextNode);
			if (nextNode.numChildren() == 0)
				parent.removeFirstChild();
		}
	}
}

//Class for implementing the tree for saving searched nodes
class Tree {
	
	Board bd;
	ArrayList<Tree> node = new ArrayList<Tree>();

	public Tree(Board bd) {
		this.bd = bd;
	}

	public void addChild(Tree child) {
		node.add(child);
	}

	public String getPath() {
		return bd.Path;
	}

	public Tree getFirstChild() {
		return node.get(0);
	}

	public void removeFirstChild() {
		node.remove(0);
	}

	public int numChildren() {
		return node.size();
	}
}

//Class for board configuration 
class Board {

	boolean[] bits = new boolean[33];
	int config;
	byte weight;
	String Path;

	//Create an initial board from start array
	public Board(boolean[] bits) {
		this.bits = bits;
	}

	//Copy constructor (when jump to the now node?)
	public Board(Board that) {
		for (int i = 0; i < 33; ++i)
			bits[i] = that.bits[i];
	}

	// transforms the array of 33 booleans to an (int) config and a (byte) weight.
	// Copied from given tester code (had originally wrote program as a separate file) 
	public int gstore(boolean[] B) {
		int a = 1;
		config = 0;
		weight = (byte) 0;
		if (B[0]) {
			weight++;
		}
		for (int i = 1; i < 32; i++) {
			if (B[i]) {
				config = config + a;
				weight++;
			}
			a = 2 * a;
		}
		if (B[32]) { // if the 33rd bit is true (has beg), then force config to negative
			config = -config;
			weight++;
		}
		return config;
	}

	//Returns triplet combo that works (index value in hard-coded list)
	public List<Board> checkTriplets(boolean[] sample, List<Integer> configHist) {

		List<Board> boards = new ArrayList<Board>();
		int[][] validTrips = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 7, 8, 9 }, { 8, 9, 10 }, { 9, 10, 11 },
				{ 10, 11, 12 }, { 13, 14, 15 }, { 14, 15, 16 }, { 15, 16, 17 }, { 16, 17, 18 }, { 17, 18, 19 },
				{ 20, 21, 22 }, { 21, 22, 23 }, { 22, 23, 24 }, { 23, 24, 25 }, { 24, 25, 26 }, { 27, 28, 29 },
				{ 30, 31, 32 }, { 12, 19, 26 }, { 11, 18, 25 }, { 2, 5, 10 }, { 5, 10, 17 }, { 10, 17, 24 },
				{ 17, 24, 29 }, { 24, 29, 32 }, { 1, 4, 9 }, { 4, 9, 16 }, { 9, 16, 23 }, { 16, 23, 28 },
				{ 23, 28, 31 }, { 0, 3, 8 }, { 3, 8, 15 }, { 8, 15, 22 }, { 15, 22, 27 }, { 22, 27, 30 }, { 7, 14, 21 },
				{ 6, 13, 20 } };

		//Check all 38 triplets on sample board and add next possible moves into a child board
		for (int[] tri : validTrips) {

			boolean p1 = sample[tri[0]]; //Set bit true/false based on its index
			boolean p2 = sample[tri[1]];
			boolean p3 = sample[tri[2]];

			//Ignore cases of no peg or full peg
			if ((p1 && p2 && p3) || (!p1 && !p2 && !p3))
				continue;

			//Do a w-sub: i.e. wwb->bbw or bww->wbb
			if (p1 && p2 && !p3) { //Case of TTF
				//Create a child node
				boolean[] child = Arrays.copyOf(sample, sample.length);
				child[tri[0]] = false;
				child[tri[1]] = false;
				child[tri[2]] = true;
				if (!foundConfigs(child, configHist)) {

					Board childBoard = new Board(child);
					childBoard.config = gstore(child); //Save configuration
					childBoard.Path = tri[0] + "W" + tri[2]; //Save the path for return
					boards.add(childBoard); //Add child node into returned board
					configHist.add(childBoard.config);
				}

			} else if (!p1 && p2 && p3) { //Case of FTT
				boolean[] child = Arrays.copyOf(sample, sample.length);
				child[tri[0]] = true;
				child[tri[1]] = false;
				child[tri[2]] = false;
				if (!foundConfigs(child, configHist)) {
					Board childBoard = new Board(child);
					childBoard.config = gstore(child);
					childBoard.Path = tri[0] + "W" + tri[2];
					boards.add(childBoard);
					configHist.add(childBoard.config);
				}
			}
		}
		return boards;
	}
	
	//Is the given config the same as a previously found config (helps avoid duplicate)?
	public boolean foundConfigs(boolean[] childBits, List<Integer> configHist) {
		int childConfig = gstore(childBits);
		for (int i = 0; i < configHist.size(); i++) {
			if (configHist.get(i) == childConfig)
				return true;
		}
		return false;
	}

	public boolean finalBoard(int conf) {
		return this.config == conf;
	}

	//Only if no W-Sub(s) possible, try a B-Sub instead
	public List<Board> B_checkTriplets(boolean[] sample, List<Integer> configHist) {
		List<Board> boards = new ArrayList<Board>();
		int[][] validTrips = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 7, 8, 9 }, { 8, 9, 10 }, { 9, 10, 11 },
				{ 10, 11, 12 }, { 13, 14, 15 }, { 14, 15, 16 }, { 15, 16, 17 }, { 16, 17, 18 }, { 17, 18, 19 },
				{ 20, 21, 22 }, { 21, 22, 23 }, { 22, 23, 24 }, { 23, 24, 25 }, { 24, 25, 26 }, { 27, 28, 29 },
				{ 30, 31, 32 }, { 12, 19, 26 }, { 11, 18, 25 }, { 2, 5, 10 }, { 5, 10, 17 }, { 10, 17, 24 },
				{ 17, 24, 29 }, { 24, 29, 32 }, { 1, 4, 9 }, { 4, 9, 16 }, { 9, 16, 23 }, { 16, 23, 28 },
				{ 23, 28, 31 }, { 0, 3, 8 }, { 3, 8, 15 }, { 8, 15, 22 }, { 15, 22, 27 }, { 22, 27, 30 }, { 7, 14, 21 },
				{ 6, 13, 20 } };

		//Check all 38 triplets on sample board and add next possible moves into a child board
		for (int[] tri : validTrips) {

			boolean p1 = sample[tri[0]]; //Set bit true/false based on its index
			boolean p2 = sample[tri[1]];
			boolean p3 = sample[tri[2]];

			//Ignore no peg or full peg cases
			if ((p1 && p2 && p3) || (!p1 && !p2 && !p3))
				continue;
			
			//Do a b-sub i.e. bbw->wwb or wbb-> bww
			if (!p1 && !p2 && p3) {//Case of FFT
				boolean[] child = Arrays.copyOf(sample, sample.length);
				child[tri[0]] = true;
				child[tri[1]] = true;
				child[tri[2]] = false;
				if (!foundConfigs(child, configHist)) {
					Board childBoard = new Board(child);
					childBoard.config = gstore(child);
					childBoard.Path = tri[0] + "B" + tri[2];
					boards.add(childBoard);
					configHist.add(childBoard.config);
					break;
				}
			} else if (p1 && !p2 && !p3) { //Case of TFF
				boolean[] child = Arrays.copyOf(sample, sample.length);
				child[tri[0]] = false;
				child[tri[1]] = true;
				child[tri[2]] = true;
				if (!foundConfigs(child, configHist)) {
					Board childBoard = new Board(child);
					childBoard.config = gstore(child);
					childBoard.Path = tri[0] + "B" + tri[2];
					boards.add(childBoard);
					configHist.add(childBoard.config);
					break;
				}
			}
		}
		return boards;
	}
}