# CHATSERVER
This server is a chat server using broadcasting , thus once you chip into the system , you can see all the messages going into
the scene

The following is the pattern how the server goes ,

#First we open up a the connection and ask the clint for a unique name
#We receive the name and give the client a conformation stating that it is accepted
#Now, we also send the the client all the previous data and send the data into the client
#Thus, the client has the complete list of data with him now , from the start of the server
#now, client has an extra option called sending a comment onto any of the message. Thus, on such an instance we take the comment 
and put it right beneath the message which is expected to be commented on. Thus, we can update this info by similar broadcasting

**Iam new to database so i have used files as a base of data and some strings and regular expressions for my convinience.
