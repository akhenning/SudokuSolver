import java.util.ArrayList;
//import java.util.Arrays;

public class Cell {
    public ArrayList<Integer> possibilities;
    public Cell(int[] start) {
        possibilities = new ArrayList<Integer>();
        for (int i:start) {
            possibilities.add(i);
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
        if (s.length() > 12) {
            s=s.substring(0,12)+"-";
        }
        if (s.length() != 7) {
            s+="\t";
        }
        return s;
    }

    public void set(int[] start) {
        possibilities = new ArrayList<Integer>();
        for (int i:start) {
            possibilities.add(i);
        }
    }

    public boolean equals(Cell c) {
        return possibilities.equals(c.possibilities);
    }
    public boolean equals(Object o) {
        return (equals((Cell)o));
    }

    // For all elements of C, remove them from possibilities
    public void remove(Cell c) {
        //System.out.println("Removing: "+c.possibilities.toString()+" from "+possibilities.toString());
        for (Integer i : c.possibilities) {
            //System.out.println("Removing number " +i);
            int index = possibilities.indexOf(i);
            //System.out.println("index: " +index);
            if (index != -1) {
                possibilities.remove(index);
            }
        }
        //System.out.println("Result: "+possibilities.toString());
    }

    public boolean containsPair(int[] check) {
        if (check.length != 2) {
            System.out.println("Error: ContainsPair undefined usage");
            return false;
        }
        //System.out.println("Comparing "+Arrays.toString(check)+" and "+possibilities.toString());
        //System.out.println("Result: "+possibilities.contains(check[0])+" and "+ possibilities.contains(check[1]));
        return possibilities.contains(check[0]) && possibilities.contains(check[1]);
    }
}
