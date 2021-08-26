/**
 * Class that contains the main method for the program and creates the frame
 * containing the component.
 * 
 * @author @henning
 * @version 8/7/20
 */

public class Solver {

	/**
	 * main method for the program which creates and configures the frame for the
	 * program
	 *
	 */

	public static Cell board[][];

	public static void main(String[] args) {

		board = new Cell[9][9];
		int[][][]all =  {
		{{ },{7},{ },{ },{8},{5},{ },{6},{ }},
		{{ },{4},{9},{ },{6},{1},{ },{7},{3}},
		{{ },{ },{8},{ },{ },{ },{ },{ },{5}},
		{{ },{6},{ },{ },{5},{ },{ },{ },{ }},
		{{ },{5},{1},{ },{9},{ },{4},{2},{ }},
		{{ },{ },{ },{ },{3},{ },{ },{5},{ }},
		{{4},{ },{ },{ },{ },{ },{2},{ },{ }},
		{{7},{1},{ },{3},{2},{ },{6},{8},{ }},
		{{ },{3},{ },{8},{4},{ },{ },{1},{ }}};
		// {{-1},{-1},{-1},{-1},{-1},{-1},{-1},{-1},{-1}};
		for (int i=0;i<9;i+=1) {
			for (int j=0;j<9;j+=1) {
				board[i][j] = new Cell(all[i][j]);
			}
		}

		simple_all();
		
		System.out.println("Done.");
		for (int i=0;i<9;i+=1) {
			System.out.print("  ");
			for (int j=0;j<9;j+=1) {
				System.out.print(board[i][j].toString() + "| ");
			}
			System.out.println();
		}

		simple_all();
		simple_all();
		simple_all();
		simple_all();
		simple_all();

		
		simple_all();
		for (int i=0;i<9;i+=1) {
			System.out.print("  ");
			for (int j=0;j<9;j+=1) {
				System.out.print(board[i][j].toString() + "| ");
			}
			System.out.println();
		}

		check();
	}

    public static void simple_all() {
		for (int i = 0; i < 9; i+=1) {
			for (int j = 0; j < 9; j+=1) {
				if (board[i][j].length() != 1) {
					simple_deduction(i,j);
				} //else {
					//System.out.println("Not pursuing ");
				//}
			}
		}
	}

	public static void simple_deduction(int i, int j) {
		System.out.println("Checking: "+i+", "+j);
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
		System.out.println("Result: "+board[i][j].toString());
	}

	public static boolean check() {
		for (int i = 0; i < 9; i+=1) {
			for (int j = 0; j < 9; j+=1) {
				if (!checkThis(i,j)) {
					return false;
				}
			}
		}
		System.out.println("All correct. I think.");
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



/**
 * 
		int[][]r1 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
		int[][]r2 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
		int[][]r3 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
		int[][]r4 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
		int[][]r5 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
		int[][]r6 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
		int[][]r7 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
		int[][]r8 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
		int[][]r9 = {{ },{ },{ },{ },{ },{ },{ },{ },{ }};
 */

 // https://www.websudoku.com/?level=1&set_id=7876016013