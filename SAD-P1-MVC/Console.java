package editablebufferedreaderwithmvc;

public class Console {
        private Line line;
        public static final int RIGHT = 0;
        public static final int LEFT = 1;
        public static final int HOME =2;
        public static final int END = 3;
        public static final int INSERT = 4;
        public static final int SUPR = 5;
        public static final int ENTER= 13;        
        public static final int BACKSCAPE =127;
       
    public Console(Line line){
        this.line = line;
    }
    
    public void ConsoleOutput(int read){
        int fin;
        switch (read) {
                case RIGHT:
                    if (line.right()) {
                        System.out.print("\033[C");
                    }
                    break;
                case LEFT:
                    if (line.left()) {
                        System.out.print("\033[D");
                    }
                    break;
                case HOME:
                    System.out.print("\033[" + line.home() + "D"); 
                    break;
                case END:
                    fin = line.end();
                    if(fin > 0){
                    System.out.print("\033[" + fin + "C");
                    }
                    break;
                case INSERT:
                    
                    line.insert();
                    break;
                case SUPR:
                    if (line.supr()) {
                        System.out.print("\033[P");
                    }
                    break;
                case BACKSCAPE:
                    if (line.bksp()) {
                        System.out.print("\033[D"); //Movemos cursor a dcha.
                        System.out.print("\033[P"); //Insertamos espacio en blanco
                    }
                    break;
                default:
                    if (line.setLine((char) read)) { //Casteamos el int para convertirlo a char
                        System.out.print("\033[@");
                        System.out.print((char) read);
                    } else {
                        System.out.print((char) read);
                    }

            } 
    }
    public String getConsoleLine(){
        String consoleLine = line.getLine();
        return consoleLine;
    }
}