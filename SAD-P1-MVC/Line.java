package editablebufferedreaderwithmvc;

import java.util.ArrayList;

public class Line {
    private int cursor; 
    private boolean insert; 
    private ArrayList<Character> line;

    

    public Line() {
        this.cursor = 0;
        this.insert = true; 
        this.line = new ArrayList<>();
    }

    public boolean setLine(char c) {

        if (insert) {  
            line.add(cursor, c);  
            cursor++;
            return true; 
        } else { 

            if (cursor >= line.size() - 1) {
                line.add(cursor, c);  
            } else {
                line.set(cursor, c);  
            }
            cursor++;
            return false;
        }
    }

    public String getLine() { 
        String l = "";

        for(Character c : line) { 
            l += c;
        }

        return l;
    }

    public boolean right() {
        if (cursor < line.size()) {
            cursor++;
            return true;
        } else {
            return false;
        }
    }

    public boolean left() {
        if (cursor > 0) {
            cursor--;
            return true;
        } else {
            return false;
        }
    }

    public int home() {
        int cursorActual = cursor;
        this.cursor = 0;
        return cursorActual; 
    }

    public int end() {
        int cursorActual = cursor;
        this.cursor = line.size();
        return (line.size() - cursorActual); 
    }

    public void insert() {
        this.insert = !insert;
    }

    public boolean supr() {
        if (cursor < line.size()) { 
            line.remove(cursor);
            return true;
        } else {
            return false;
        }
    }

    public boolean bksp() {
        if (cursor > 0) {
            line.remove(cursor - 1); 
            cursor--; 
            return true;
        } else {
            return false;
        }
    }
}