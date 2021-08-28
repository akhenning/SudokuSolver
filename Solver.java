import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * 
 * @author @henning
 * @version 8/26/21
 */

public class Solver {

	/**
	 * main method for the program which creates and configures the frame for the
	 * program
	 *
	 */

	public static Cell board[][];

	public static void main(String[] args) {

		String last = "last";
		String current = "";
		int c = 0;

		board = new Cell[9][9];
		int[][][]all =  {
		{{7},{6},{ },{ },{ },{2},{ },{ },{8}},
		{{ },{ },{ },{6},{ },{ },{3},{ },{ }},
		{{9},{ },{3},{ },{7},{ },{6},{ },{ }},
		{{ },{ },{4},{ },{6},{ },{ },{ },{3}},
		{{ },{1},{ },{ },{9},{ },{ },{7},{ }},
		{{3},{ },{ },{ },{4},{ },{2},{ },{ }},
		{{ },{ },{9},{ },{2},{ },{8},{ },{4}},
		{{ },{ },{7},{ },{ },{4},{ },{ },{ }},
		{{4},{ },{ },{8},{ },{ },{ },{3},{1}}};
		// {{-1},{-1},{-1},{-1},{-1},{-1},{-1},{-1},{-1}};
		for (int i=0;i<9;i+=1) {
			for (int j=0;j<9;j+=1) {
				board[i][j] = new Cell(all[i][j]);
			}
		}

		while (c < 5 && !last.equals(current)) {
			last = current;
			simple_all();
			simple_all();
			simple_all();
			//pairs_all();
			current = "";
			for (int i=0;i<9;i+=1) {
				current += "  ";
				for (int j=0;j<9;j+=1) {
					current += board[i][j].toString() + "| ";
				}
				current += "\n";
			}
			c+=1;
		}
		pairs_all();

		System.out.println(current);
		if (c>=5) {
			System.out.println("Emergency quit.");
		} else {
			System.out.println("Exited properly due to stagnation after "+c+" iterations.");
		}




		
		/**for (int i=0;i<9;i+=1) {
			System.out.print("  ");
			for (int j=0;j<9;j+=1) {
				System.out.print(board[i][j].toString() + "| ");
			}
			System.out.println();
		}*/

		check();
	}

	public static void pairs_all() {
		for (int i = 0; i < 9; i+=1) {
			for (int j = 0; j < 9; j+=1) {
				if (board[i][j].length() == 2) {
					remove_naked_pairs(i,j);
				} else if (board[i][j].length()>2) {
					remove_hidden_pairs(i,j);
				}
			}
		}
		System.out.println("Finishing pair search");
	}
	public static void remove_hidden_pairs(int i, int j) {
		int[] occurancesi = {0,0,0,0,0,0,0,0,0};
		int[] occurancesj = {0,0,0,0,0,0,0,0,0};
		int[] occurancesb = {0,0,0,0,0,0,0,0,0};
		for (int i2 = 0; i2 < 9; i2+=1) {
			for (int elm : board[i2][j].possibilities) {
				occurancesi[elm-1]+=1;
			}
			for (int elm : board[i][i2].possibilities) {
				occurancesj[elm-1]+=1;
			}
		}
		int box_i = i/3;
		int box_j = j/3;
		for (int i3 = box_i*3; i3 < box_i*3+3; i3+=1) {
			for (int j3 =  box_j*3; j3 < box_j*3+3; j3+=1) {
				for (int elm : board[i3][j3].possibilities) {
					occurancesb[elm-1]+=1;
				}
			}
		}
		int num_pairs = 0;
		for (int elm:occurancesi) {
			if (elm == 2) {
				num_pairs += 1;
			}
		}
		if (num_pairs>1) {
			System.out.println("Found some pairs along vertical at: "+i+", "+j+". "+(Arrays.toString(occurancesi)));
		}
		num_pairs = 0;
		for (int elm:occurancesj) {
			if (elm == 2) {
				num_pairs += 1;
			}
		}
		if (num_pairs>1) {
			System.out.println("Found some pairs along horizontal at: "+i+", "+j+". "+(Arrays.toString(occurancesj)));
		}
		num_pairs = 0;
		for (int elm:occurancesb) {
			if (elm == 2) {
				num_pairs += 1;
			}
		}
		if (num_pairs>1) {
			System.out.println("Found some pairs along box at: "+i+", "+j+". "+(Arrays.toString(occurancesb)));
		}
	}



	public static void remove_naked_pairs(int i, int j) {
		ArrayList<Cell> pairsTracker = new ArrayList<Cell>();
		ArrayList<Integer> locationTracker = new ArrayList<Integer>();
		// 1: direction. 0=i, 1=j, -1=box.
		// 2: relevant i or j
		// 3: if box, relevant j

		// these would be faster as arrays
		ArrayList<Cell> pairsi = new ArrayList<Cell>();
		ArrayList<Cell> pairsj = new ArrayList<Cell>();
		for (int i2 = 0; i2 < 9; i2+=1) {
			if (board[i2][j].length() == 2) {
				//System.out.println(board[i2][j].get());
				if (pairsi.contains(board[i2][j])) {
					//System.out.println("Found pair on i! "+board[i2][j].toString());
					pairsTracker.add(board[i2][j]);
					locationTracker.add(0);
					locationTracker.add(j);
					locationTracker.add(-1);
				}
				pairsi.add(board[i2][j]);
			}
			if (board[i][i2].length() == 2) {
				//System.out.println(board[i][i2].get());
				if (pairsj.contains(board[i][i2])) {
					//System.out.println("Found pair on j! "+board[i][i2].toString());
					pairsTracker.add(board[i][i2]);
					locationTracker.add(1);
					locationTracker.add(i);
					locationTracker.add(-1);
				}
				pairsj.add(board[i][i2]);
				//System.out.println("Found a "+(board[i][i2].get()) + " on the j axis");
			}
		}
		ArrayList<Cell> pairsb = new ArrayList<Cell>();
		int box_i = i/3;
		int box_j = j/3;
		for (int i3 = box_i*3; i3 < box_i*3+3; i3+=1) {
			for (int j3 =  box_j*3; j3 < box_j*3+3; j3+=1) {
				//System.out.println(i3+", "+j3);
				if (board[i3][j3].length() == 2) {
					if (pairsb.contains(board[i3][j3])) {
						System.out.println("Found pair in box! "+board[i3][j3].toString());
						pairsTracker.add(board[i3][j3]);
						locationTracker.add(-1);
						locationTracker.add(box_i);
						locationTracker.add(box_j);
					}
					pairsb.add(board[i3][j3]);
				}
			}
		}
		for (int k = 0; k < pairsTracker.size();k+=1) {
			remove_values(pairsTracker.get(k),locationTracker.get(3*k),locationTracker.get(3*k+1),locationTracker.get(3*k+2));
		}
	}

    public static void remove_values(Cell toRemove, int type, int i, int j) {
		if(type == -1) {
			//System.out.println("Removing "+toRemove.toString()+" at box: "+i+", "+j);
			for (int i3 = i*3; i3 < i*3+3; i3+=1) {
				for (int j3 =  j*3; j3 < j*3+3; j3+=1) {
					if(!board[i3][j3].equals(toRemove) && board[i3][j3].length() > 1) {
						board[i3][j3].remove(toRemove);
					}
				}
			}
		} else if (type == 0) {
			//System.out.println("Removing "+toRemove.toString()+" at i location: "+i);
			for (int i2 = 0; i2 < 9; i2+=1) {
				if (!board[i2][i].equals(toRemove) && board[i2][i].length() > 1) {
					//System.out.println("Removing "+board[i2][i].toString());
					board[i2][i].remove(toRemove);
				}
			}
		} else {
			//System.out.println("Removing "+toRemove.toString()+" at j location: "+i);
			for (int i2 = 0; i2 < 9; i2+=1) {
				if (!board[i][i2].equals(toRemove) && board[i][i2].length() > 1) {
					board[i][i2].remove(toRemove);
				}
			}
		}
	}

    public static void simple_all() {
		for (int i = 0; i < 9; i+=1) {
			for (int j = 0; j < 9; j+=1) {
				if (board[i][j].length() != 1) {
					simple_deduction(i,j);
				}
			}
		}
	}

	public static void simple_deduction(int i, int j) {
		//System.out.println("Checking: "+i+", "+j);
		boolean[] flags = {true,true,true,true,true,true,true,true,true};
		for (int i2 = 0; i2 < 9; i2+=1) {
			if (board[i2][j].length() == 1) {
				//System.out.println(board[i2][j].get());
				flags[board[i2][j].get()-1] = false;
				//System.out.println("Found a "+(board[i2][j].get()) + " on the i axis");
			}
			if (board[i][i2].length() == 1) {
				//System.out.println(board[i][i2].get());
				flags[board[i][i2].get()-1] = false;
				//System.out.println("Found a "+(board[i][i2].get()) + " on the j axis");
			}
		}
		int box_i = i/3;
		int box_j = j/3;
		for (int i3 = box_i*3; i3 < box_i*3+3; i3+=1) {
			for (int j3 =  box_j*3; j3 < box_j*3+3; j3+=1) {
				//System.out.println(i3+", "+j3);
				if (board[i3][j3].length() == 1) {
					//System.out.println(board[i3][j3].get());
					//System.out.println("Found a "+(board[i3][j3].get()) + "on the board");
					flags[board[i3][j3].get()-1] = false;
				}
			}
		}
		board[i][j].clear();
		for (int i4 = 0; i4 < 9; i4+=1) {
			if(flags[i4]) {
				//System.out.println("Possible thing determined: "+i4);
				board[i][j].add(i4+1);
			}
		}
		//System.out.println("Result: "+board[i][j].toString());
	}

	public static boolean check() {
		for (int i = 0; i < 9; i+=1) {
			for (int j = 0; j < 9; j+=1) {
				if (!checkThis(i,j)) {
					return false;
				}
			}
		}
		System.out.println("CONGRATULATIONS! All correct! I think.");
		return true;

	}
	public static boolean checkThis(int i, int j) {
		boolean[] flagsi = {true,true,true,true,true,true,true,true,true};
		boolean[] flagsj = {true,true,true,true,true,true,true,true,true};
		boolean[] flagsb = {true,true,true,true,true,true,true,true,true};
		for (int i2 = 0; i2 < 9; i2+=1) {
			if (board[i2][j].length() != 1) {
				System.out.println("Some number is not finialized");
				return false;
			}
			if (flagsi[board[i2][j].get()-1] == false) {
				System.out.println("Number is repeated");
				return false;
			}
			flagsi[board[i2][j].get()-1] = false;
			if (board[i][i2].length() != 1) {
				System.out.println("Some number is not finialized");
				return false;
			}
			if (flagsj[board[i][i2].get()-1] == false) {
				System.out.println("Number is repeated");
				return false;
			}
			flagsj[board[i][i2].get()-1] = false;
		}
		for (boolean b : flagsi) {
			if (b) {
				System.out.println("I column missing number");
				return false;
			}
		}
		for (boolean b : flagsj) {
			if (b) {
				System.out.println("J column missing number");
				return false;
			}
		}

        int box_i = i/3;
		int box_j = j/3;
		for (int i3 = box_i*3; i3 < box_i*3+3; i3+=1) {
			for (int j3 =  box_j*3; j3 < box_j*3+3; j3+=1) {
				//System.out.println(i3+", "+j3);
				if (board[i3][j3].length() != 1) {
					System.out.println("Some number is not finialized");
					return false;
				}
				if (flagsb[board[i3][j3].get()-1] == false) {
					System.out.println("Number is repeated in box");
					return false;
				}
				flagsb[board[i3][j3].get()-1] = false;
			}
		}
		for (boolean b : flagsb) {
			if (b) {
				System.out.println("Box missing number");
				return false;
			}
		}
		return true;

	}
}



/**  https://www.websudoku.com/?level=2&set_id=688120864
 * 
 * 
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }},
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }},
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }},
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }},
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }},
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }},
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }},
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }},
		{{ },{ },{ },{ },{ },{ },{ },{ },{ }}};
 * 
		{{ },{7},{ },{ },{8},{5},{ },{6},{ }},
		{{ },{4},{9},{ },{6},{1},{ },{7},{3}},
		{{ },{ },{8},{ },{ },{ },{ },{ },{5}},
		{{ },{6},{ },{ },{5},{ },{ },{ },{ }},
		{{ },{5},{1},{ },{9},{ },{4},{2},{ }},
		{{ },{ },{ },{ },{3},{ },{ },{5},{ }},
		{{4},{ },{ },{ },{ },{ },{2},{ },{ }},
		{{7},{1},{ },{3},{2},{ },{6},{8},{ }},
		{{ },{3},{ },{8},{4},{ },{ },{1},{ }}};
 */

 // https://www.websudoku.com/?level=1&set_id=7876016013