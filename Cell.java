import java.util.ArrayList;

public class Cell {
    private ArrayList<Integer> possibilities;
    public Cell(int[] start) {
        possibilities = new ArrayList<Integer>();
        if (start.length != 0) {
            possibilities.add(start[0]);
        }
    }
    public int length() {
        return possibilities.size();
    }
    public void add(int i) {
        possibilities.add(i);
    }
    public int get() {
        if (possibilities.size() != 1) {
            System.out.println("Sanity check failed: Get was called on something of size !=1.");
        }
        return possibilities.get(0);
    }
    public void clear() {
        possibilities.clear();
    }
    public String toString() {
        String s = possibilities.toString();
        if (s.length() < 7) {
            s+="\t";
        }
        if (s.length() > 10) {
            s=s.substring(0,10)+"...";
        }
        if (s.length() != 7) {
            s+="\t";
        }
        return s;
    }
}
