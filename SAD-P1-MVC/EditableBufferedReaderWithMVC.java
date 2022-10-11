package editablebufferedreaderwithmvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author entel
 */
public class EditableBufferedReaderWithMVC extends BufferedReader {
        Line line;
        Console console;
        //Valores que devuelve read() una vez se detecta alguna secuencia de escape
        public static final int RIGHT = 0;
        public static final int LEFT = 1;
        public static final int HOME =2;
        public static final int END = 3;
        public static final int INSERT = 4;
        public static final int SUPR = 5;
        public static final int ENTER= 13;        
        public static final int BACKSCAPE =127;
        
       
    public EditableBufferedReaderWithMVC(Reader in) {
        
        super(in);
        line = new Line ();
        console = new Console(line);
        
        
    }
    public void setRaw() throws IOException{
      String[] cmd = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
      Runtime.getRuntime().exec(cmd);
    }
    public void unsetRaw() throws IOException{
        String[] cmd = {"/bin/bash", "-c", "stty sane </dev/tty"};
	Runtime.getRuntime().exec(cmd);  
    }
  
    public int read() throws IOException{
      int read;
        switch(read=super.read()){
            case '\033': 
                super.read(); 
                
                switch (super.read()){
                    case 'C':
                        return RIGHT;
                    case 'D':
                        return LEFT;
                    case 'H':
                        return HOME;
                    case 'F':
                        return END;
                    case '2':
                        super.read();
                        return INSERT;
                    case '3':
                        super.read();
                        return SUPR;
                        
                }
            default: 
                return read;//si no es uno de los otros parametros se devuelve el valor leido.
       
        }
    }
       
    public String readLine() throws IOException {
    int read;  
    this.setRaw();
    while((read = this.read())!=ENTER){
        console.ConsoleOutput(read);
    }
       
    this.unsetRaw();
    return console.getConsoleLine();
    }
}