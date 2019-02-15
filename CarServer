import socket, select, os 
import RPi.GPIO as GPIO     #import gpio for the motor pins
from time import sleep

PORT = 4444             #Port that the server car listens on
CONNECTION_LIST = []    #list of clients connected to the server
RECV_BUFFER = 1024      #total size of data that will be recieved every loop iteration

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 

server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_socket.bind(("0.0.0.0", PORT))   #the ip and port the server will be listening on (0.0.0.0 mean all of the ips that the device has)
server_socket.listen(10)                #listening for connections

CONNECTION_LIST.append(server_socket)   #adding clients to the connection list

GPIO.setmode(GPIO.BOARD)    #RPi gpio setup type (GPIO.BOARD or GPIO.BCM)
GPIO.setup(12, GPIO.OUT)    #Setting pin #12 and #32 as output pins
GPIO.setup(32, GPIO.OUT)

pwm12 = GPIO.PWM(32, 50)    #setting pin 32 pulse with modulation to 50 hertz
pwm18 = GPIO.PWM(12, 50)    #setting pin 12 pulse with modulation to 50 hertz

pwm12.start(0)  #setting pin 12 to 0 hertz
pwm18.start(0)  #setting pin 32 to 0 hertz

print ("Listening on port " + str(PORT))    #print the current port listen on

def constrain(n, minn, maxn):       
    return max(min(maxn, n), minn)  #setting the value "n" between the bounds of "minn" and "maxn"

def drive(Strength, Angle):
        Strength = float(Strength)  #converting the string Strength to a float
        Angle = float(Angle)-90     #converting the string angle to a float and rotating the values 90 degrees

        if(Angle > 180): Angle = -1*(Angle-360)  #sanitizing the data
        leftMotor = constrain(((Angle+90)/90)*Strength,0,100)   #contraining the left motor strength between 0 and 100
        rightMotor = constrain((-(Angle-90)/90)*Strength,0,100) #contraining the right motor strength between 0 and 100

        print("Left Motor: ",leftMotor)    
        print("Right Motor: ",rightMotor)

        pwm12.ChangeDutyCycle(leftMotor)    #updating the left motor values
        pwm18.ChangeDutyCycle(rightMotor)   #updating the right motor values
while(True):    #server loop to recieve data
        try:
        read_socket, write_socket, error_socket = select.select(CONNECTION_LIST, [], [])
        for sock in read_socket:    #for all the of clients in the read list
                if sock == server_socket:   
                        sockfd, addr = server_socket.accept()
                        CONNECTION_LIST.append(sockfd)
#                        print("Client(%s, %s) connected" % addr) #print the clients that have been recently connected
                else:
                    try:
                        data = sock.recv(RECV_BUFFER).decode()  #set the client data equal to a local value
                        Strength = ""   #initialize the values as strings
                        Angle = ""
                        if(data):
                            #print data     #print the data recieved (formatted to look like this for 50% strength and 90 degrees - 050,090)
                            for i in range (len(data)): #for each char in the data
                                    if(i < 3):
                                            Strength += data[i] #put the first half into the strength var
                                    elif(i > 3):
                                            Angle += data[i]    #put the second half into the angle var
                                #print("Strength: "+Strength+", Angle: "+Angle)  #print the values
                                drive(Strength, Angle) #use the data to update the motors
                    except:
                            broadcast_data(sock, "Client (%s, %s) is offline" % addr)   #send clients that have disconnected
                            print ("Client (%s, %s) is offline" % addr)                 #print clients that have disconnected
                            sock.close()                                                #close that socket
                            CONNECTION_LIST.remove(sock)                                #remove the client from the list of clients
                            continue                                                    #continue the loop
        except KeyboardInterrupt:   #end the loop on a ctrl+c Event
            break;
#After the looop is killed
server_socket.close()   #close the server socket                                            
pwm12.stop()            #stop the motors
pwm18.stop()
GPIO.cleanup()          #reset the gpio pins
