import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {
    public static String my_nickname;
    public static void main(String[] args){        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Introdueix username: ");

        try{
            my_nickname = in.readLine();
            System.out.println("Hello " + my_nickname);
        }catch(Exception e){
            System.out.println("Error al llegir missatge");
        }

        MySocket sc = new MySocket(my_nickname);        
        
        Thread socketThread = new Thread(() -> {
            try{
                String line;
                while ((line = in.readLine()) != null){ //enunciat pràctica
                    sc.write(line);                                        
                }
            }catch (Exception e){
                System.out.println("Error al llegir missatge");
            }
        });
        socketThread.start();

        Thread keyboardThread =new Thread(() -> {
            String line;
            while((line = sc.read())!= null){ //enunciat pràctica
                System.out.println(line);
            }
        });  
        keyboardThread.start();
    }
}