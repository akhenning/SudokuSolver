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
	public static final int MAX_ITERATIONS = 1;

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

		while (c < MAX_ITERATIONS && !last.equals(current)) {
			last = current;
			simple_all();
			simple_all();
			simple_all();
			pairs_all();
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
		ArrayList<Cell> pairsTracker = new ArrayList<Cell>();
		ArrayList<Integer> locationTracker = new ArrayList<Integer>();
		for (int i = 0; i < 9; i+=1) {
			// 1: direction. 0=i, 1=j, -1=box.
			// 2: relevant i or j
			// 3: if box, relevant j
	
			// these would be faster as arrays
			ArrayList<Cell> pairsi = new ArrayList<Cell>();
			ArrayList<Cell> pairsj = new ArrayList<Cell>();
			for (int i2 = 0; i2 < 9; i2+=1) {
				if (board[i2][i].length() == 2) {
					//System.out.println(board[i2][j].get());
					if (pairsi.contains(board[i2][i])) {
						System.out.println("Found pair on column! "+board[i2][i].toString());
						pairsTracker.add(board[i2][i]);
						locationTracker.add(0);
						locationTracker.add(i);
						locationTracker.add(-1);
					}
					pairsi.add(board[i2][i]);
				}
				if (board[i][i2].length() == 2) {
					//System.out.println(board[i][i2].get());
					if (pairsj.contains(board[i][i2])) {
						System.out.println("Found pair on row! "+board[i][i2].toString());
						pairsTracker.add(board[i][i2]);
						locationTracker.add(1);
						locationTracker.add(i);
						locationTracker.add(-1);
					}
					pairsj.add(board[i][i2]);
					//System.out.println("Found a "+(board[i][i2].get()) + " on the j axis");
				}
			}
		}
		
		for (int box_i = 0; box_i<3;box_i+=1) {
			for (int box_j = 0; box_j<3;box_j+=1) {
				ArrayList<Cell> pairsb = new ArrayList<Cell>();
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
			}
		}
		for (int k = 0; k < pairsTracker.size();k+=1) {
			remove_values(pairsTracker.get(k),locationTracker.get(3*k),locationTracker.get(3*k+1),locationTracker.get(3*k+2));
		}

		// -------- hidden pairs now


		for (int i = 0; i < 9; i+=1) {
			int[] occurancesi = {0,0,0,0,0,0,0,0,0};
			int[] occurancesj = {0,0,0,0,0,0,0,0,0};
			for (int i2 = 0; i2 < 9; i2+=1) {
				for (int elm : board[i2][i].possibilities) {
					occurancesi[elm-1]+=1;
				}
				for (int elm : board[i][i2].possibilities) {
					occurancesj[elm-1]+=1;
				}
			}
			
			ArrayList<Integer> pairs = new ArrayList<Integer>();
			for (int k = 0; k < 9; k += 1) {
				if (occurancesi[k] == 2) {
					pairs.add(k+1);
				}
			}
			//if (pairs.size()>1) {
			//	System.out.println("Found some pairs along column: "+i+". "+pairs.toString());
			//}
			ArrayList<int[]> real_hidden_pairs = new ArrayList<int[]>();
			for (int k = 0; k < pairs.size(); k += 1) {
				for (int l = k+1; l < pairs.size(); l += 1) {
					int[] pair = {pairs.get(k),pairs.get(l)};
					int together_twice = 0;
					for (int i2 = 0; i2 < 9; i2+=1) {
						if (board[i2][i].containsPair(pair)){
							together_twice += 1;
						}
					}

					if (together_twice == 2) {
						real_hidden_pairs.add(pair);
					}
				}
			}
			
			if (real_hidden_pairs.size() > 0) {
				System.out.print("Following hiddenpairs are real for column "+i+": ");
				for (int[] pr:real_hidden_pairs) {
					System.out.print(Arrays.toString(pr));
				}
				System.out.println();
			}


			real_hidden_pairs = new ArrayList<int[]>();
			pairs = new ArrayList<Integer>();
			for (int k = 0; k < 9; k += 1) {
				if (occurancesj[k] == 2) {
					pairs.add(k+1);
				}
			}
			
			for (int k = 0; k < pairs.size(); k += 1) {
				for (int l = k+1; l < pairs.size(); l += 1) {
					int[] pair = {pairs.get(k),pairs.get(l)};
					int together_twice = 0;
					for (int i2 = 0; i2 < 9; i2+=1) {
						if (board[i][i2].containsPair(pair)){
							together_twice += 1;
						}
					}
					if (together_twice == 2) {
						real_hidden_pairs.add(pair);
					}
				}
			}
			
			if (real_hidden_pairs.size() > 0) {
				System.out.print("Following hiddenpairs are real for row "+i+": ");
				for (int[] pr:real_hidden_pairs) {
					System.out.print(Arrays.toString(pr));
				}
				System.out.println();
			}

		}
		
		for (int box_i = 0; box_i<3;box_i+=1) {
			for (int box_j = 0; box_j<3;box_j+=1) {
				int[] occurancesb = {0,0,0,0,0,0,0,0,0};
				for (int i3 = box_i*3; i3 < box_i*3+3; i3+=1) {
					for (int j3 =  box_j*3; j3 < box_j*3+3; j3+=1) {
						for (int elm : board[i3][j3].possibilities) {
							occurancesb[elm-1]+=1;
						}
					}
				}
				
				ArrayList<int[]> real_hidden_pairs = new ArrayList<int[]>();
				ArrayList<Integer> pairs = new ArrayList<Integer>();
				for (int k = 0; k < 9; k += 1) {
					if (occurancesb[k] == 2) {
						pairs.add(k+1);
					}
				}
				for (int k = 0; k < pairs.size(); k += 1) {
					for (int l = k+1; l < pairs.size(); l += 1) {
						int[] pair = {pairs.get(k),pairs.get(l)};
						int together_twice = 0;
						for (int i3 = box_i*3; i3 < box_i*3+3; i3+=1) {
							for (int j3 =  box_j*3; j3 < box_j*3+3; j3+=1) {
								if (board[i3][j3].containsPair(pair)){
									together_twice += 1;
								}
							}
						}
						if (together_twice == 2) {
							real_hidden_pairs.add(pair);
						}
					}
				}
				if (real_hidden_pairs.size() > 0) {
					System.out.print("Following hiddenpairs are real for box "+box_i+", "+box_j+": ");
					for (int[] pr:real_hidden_pairs) {
						System.out.print(Arrays.toString(pr));
					}
					System.out.println();
				}
			}
		}


		System.out.println("Finishing pair search");
	}



    public static void remove_values(Cell toRemove, int type, int i, int j) {
		if(type == -1) {
			System.out.println("Removing "+toRemove.toString()+" at box: "+i+", "+j);
			for (int i3 = i*3; i3 < i*3+3; i3+=1) {
				for (int j3 =  j*3; j3 < j*3+3; j3+=1) {
					//System.out.println("Checking: "+board[i3][j3]);
					if(!board[i3][j3].equals(toRemove) && board[i3][j3].length() > 1) {
						board[i3][j3].remove(toRemove);
					}
				}
			}
		} else if (type == 0) {
			System.out.println("Removing "+toRemove.toString()+" at column: "+i);
			for (int i2 = 0; i2 < 9; i2+=1) {
				if (!board[i2][i].equals(toRemove) && board[i2][i].length() > 1) {
					//System.out.println("Removing "+board[i2][i].toString());
					board[i2][i].remove(toRemove);
				}
			}
		} else {
			System.out.println("Removing "+toRemove.toString()+" at row: "+i);
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