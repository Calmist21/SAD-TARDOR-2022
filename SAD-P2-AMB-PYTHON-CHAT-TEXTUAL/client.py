from email import message
import random
import socket
import threading
from tokenize import String

#sv es el Localhost sempre

class Client:
    username = str
    sc = socket
    bye = str

    def __init__(self, username):
        self.username = username
        self.sc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  #de la llibreria socket agafem les ctts per default AF_INET, SOCK_STREAM
        self.sc.connect(('127.0.0.1', 8080))
        self.bye = "Bye"




    def rebre_missatge(self):
        while True:
            try:
                missatge = self.sc.recv(1024).decode('ascii')   #rebre del seu socket amb buffer 1024
                #print(missatge)
                if missatge == 'CLOSE':
                    print("Connexió tancada")
                    self.sc.close()
                    break
                if missatge == 'USERNAME':
                    self.sc.send(self.username.encode('ascii'))
                else:
                    print(missatge)
            except Exception as e:
                print(e)
                print("Hi ha hagut algun error")
                self.sc.close()
                break

    def enviar_missatge(self):
        while True:
            #missatge = '{}: {}'.format(self.username, input(''))
            missatge = input()
            if(missatge == self.bye):
                print("\n Tancant conexió...")
                self.sc.send('CLOSE'.encode('ascii'))
                #self.sc.close()
                quit()
            else:
                missatge_f = self.username+": "+missatge
                self.sc.send(missatge_f.encode('ascii'))


username = input("\n"+"Escriu el teu nom d'usuari:")
username = username + str(random.randrange(9)) + \
    str(random.randrange(9))
    #Generem dos numeros random per a que no es repeteixin clients amb el mateix username
print("Usuari: "+username)
mysc = Client(username)

receive_thread = threading.Thread(target=mysc.rebre_missatge)
receive_thread.start()
write_thread = threading.Thread(target=mysc.enviar_missatge)
write_thread.start()
