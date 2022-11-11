import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MapaConnexions {
    Map<String, MySocket> clients_map;
    Lock l;

    public MapaConnexions() {
        clients_map = new HashMap<>();
        l = new ReentrantLock();
    }

    public String addParticipant(String nickname_original, MySocket sc) {
        String nickname = null;
        l.lock();
        try { 
            //S'afegeixen 3 nums random darrere si ja existeix el nickname
            if(clients_map.containsKey(nickname_original)){
                do {
                    nickname = nickname_original + "_"
                            + (int) (Math.random() * 10)
                            + (int) (Math.random() * 10)
                            + (int) (Math.random() * 10);
                } while (clients_map.containsKey(nickname));            
            }
            else{
                nickname = nickname_original;
            }  
            clients_map.put(nickname, sc);                      
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
        return nickname;
    }

    public void broadcast(String content, String nick) { //
        l.lock();
        try {
            for (String nickname : clients_map.keySet()) {
                if (!nickname.equals(nick)) {
                    MySocket sc = clients_map.get(nickname);
                    sc.write(nick + ": " + content);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
    }

    public void removeParticipant(String meu_nickname) {
        l.lock();
        try {
            clients_map.remove(meu_nickname);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
    }
}