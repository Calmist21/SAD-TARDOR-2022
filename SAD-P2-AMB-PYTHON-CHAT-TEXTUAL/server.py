import socket
import threading


class Server:
   

    def __init__(self):
        self.ss = socket.socket(socket.AF_INET, socket.SOCK_STREAM)   #definim el socket amb el que ens ve per default a la llibreria de socket (af_inet i SOCK_STREAM constants)
        self.ss.bind(('127.0.0.1', 8080))
        self.ss.listen()
        self.dicc = dict()          #creació d'un diccionari buit (clau-valor) on les claus serán els username i el valor els socket
        print("\n Servidor inicialitzat correctament ")

    def broadcast(self, message, c):  #Enviar missatge a tots els socket menys el seu
        for valor in self.dicc.values():
            if (valor != c):
                valor.send(message)

    def handle(self, username):
        c = self.dicc.get(username)
        while True:
            try:
                message = c.recv(1024)   #guardem a message les dades rebudes amb buffer 1024 al socket c
                if(message.decode('ascii') == 'CLOSE'):
                    print(username+" S'ha desconnectat")
                    c.send('CLOSE'.encode('ascii'))
                    c.close()
                else:
                    self.broadcast(message, c) #envia per broadcast
            except:
                c.close()
                self.broadcast('\n {}  ha marxat!\n'.format(username).encode('ascii'), c)  # {} fa referencia a .format(username), i a la funció broadcast li passem el socket com sempre
                self.dicc.pop(username)  #BORRA del diccionari
                break

    def rebre_missatge(self):
        while True:
            c, a = self.ss.accept()
            print("Connexio nova amb "+str(a)) #a -> Ret_Address pasat a String
            c.send('USERNAME'.encode('ascii'))
            username = c.recv(1024).decode('ascii')
            self.dicc[username] = c
            print("Usuari:  {}".format(username)) #substitueix {} per el username en String
            self.broadcast("{} s'ha unit al chat!\n".format(username).encode('ascii'), c) #igual que adalt però ara ho passa a ascii
            thread = threading.Thread(target=self.handle, args=(username, ))
            thread.start()

serv=Server()
serv.rebre_missatge()

#al executar el server.py l'inicialitzem i després cridant a rebre_missatge() s'accepta el socket , s'afegeix al dicc de sockets amb clau 'username'
#allá tmb es crea un thread Handle amb el username