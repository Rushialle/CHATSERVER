/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashSet;

/**
 *
 * @author rushikesh1
 */
public class ChatServer {

    /**
     * @param args the command line arguments
     */
    private static final int PORT = 9001;
    private static InetAddress Ipaddr = null ;//= InetAddress.getByName("192.177.24.12");
    
    private static HashSet<String> client_names = new HashSet<String>();

    private static HashSet<PrintWriter> client_writers = new HashSet<PrintWriter>();
    private static File save_file = null;
    private static FileInputStream in_file = null;
    private static PrintWriter out_file = null;
    private static int message_count = 0;
    private static int comment_count= 0;
    private static String now_message = "";
    private static boolean firsttime = true;
    
    public static void main(String[] args) throws IOException
    {
        // TODO code application logic here
        save_file = new File("save.txt");
        if(save_file.exists())
        {
            
            System.out.print("Iam in this ??");
        }
        else
        {
            save_file.createNewFile();
             
            //in_file = new FileInputStream(save_file);
             System.out.print("Iam ??");
        }
       // Ipaddr = InetAddress.getByName("12.17.241.120");
        System.out.println("The server is started !");
        
    ServerSocket listen = new ServerSocket (PORT );
        System.out.println("IP : " + listen.getInetAddress().getHostAddress());
    try
        {
            while(true)
            {
                new Handler (listen.accept()).start();
            }
    }
        finally
        {
            listen.close();
    }
    }
    
    private static void comment_handler (String input) throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        System.out.println("the comments !!!");
        //assume that the message coming to us is of the type --> 
        //sender_name COMMENT[:]/****message(original)****/[:]/**message now*/ 
        //String now_input = input ;
        String[] parts = input.split("\\:");
       
       
      
        //so we need to find out the part[2] and add an extra line after it
        BufferedReader rd = null;
    BufferedWriter wt = null;

    try {
        rd = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("save.txt"), "UTF-8")
                );

        wt = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                "newfile.txt"), "UTF-8")
                );

        int count = 0;
        for (String line; (line = rd.readLine()) != null;) {
           
           System.out.println("chudu :" + line);
           String[] poot = line.split("\\:");
            wt.write(line);
            wt.newLine();
            if (poot[2].compareTo(parts[2]) == 0 ) {
                System.out.println("Yes buddy iam the one to comment!");
               wt.write("COMMENT:"+parts[3]);
               wt.newLine();
            }
        }
    } finally {
        wt.close();
        rd.close();
        //delete the old file and update the name of newfile.txt to save.txt
    File oldfile =new File("newfile.txt");
		File newfile =new File("save.txt");
		if(oldfile.renameTo(newfile)){
			System.out.println("Rename succesful");
		}else{
			System.out.println("Rename failed");
		}     
    }
   }
    
    private static class Handler extends Thread 
    {

        private static String name ;
        private Socket socket ;
    private BufferedReader in;
        private PrintWriter out;
        

        public Handler(Socket sock)
    {
            socket  = sock;
    }

    public void run()
        {
            System.out.println("Iam in this babe ");
            try
            {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);
        while(true)
        {
                    
                    out.println("GIMMENAME");
                    //System.out.println("question sent !! ");
                    name = in.readLine();
                    //System.out.println("receiving thing part !! !! ");
                   
                    
                    if(name ==null)
                    {
                        System.out.println("Strill did not enter anything man ? ");
                        return ;
                        
                    }
                   
                    synchronized(client_names)
                    {
                        if(!client_names.contains(name))
                        {
                            //new client!!
                            client_names.add(name);
                            //this shows that its  a new file so ... once we need to send him the complete details of the file
                            
                            break;
                        }
                    }
                }
                
        if(client_names.size() != 0)
                {
                    
                //send him the complete data!!!
                    try(BufferedReader br = new BufferedReader(new FileReader("save.txt"))) 
                    {
                      for(String line; (line = br.readLine()) != null || line !="" ; ) 
                      {
                        
                // process the line.
                          if(line !=null)
                          {
                         out.println(line);
                          }
                          else
                          {
                              break;
                          }
                      }
    // line is not visible here.
                    }
                    
                }
                out.println("ACCEPTED");
                System.out.println("Hey sent the accepted part !!");
                
        client_writers.add(out);
        while(true)
        {
                    String input = in.readLine();
                    if(input == null)
                    {
            return;
                        
                    }
                   else if(input.contains("COMMENT") )
                    {
                      
                        //we need to call the method to inser the text into the file baaaaby !!!
                        comment_handler(input);
                    }
                    
                    
                    else
                    {
                        //write it down ... thus having a list of the complete data !!!
                        //assuming that the client always puts its name for the messaage traversal 
                        
                       // System.out.println("yes in the else baby ");
                       System.out.println("Iam working !!");
                       message_count++;
                        if(firsttime == true)
                       {
                        
                        out_file = new PrintWriter(new BufferedWriter(new FileWriter("save.txt", true)));
                        
                        firsttime = false;
                       }
                       else
                       {
                             out_file = new PrintWriter(new BufferedWriter(new FileWriter("save.txt", false)));
                       }
                       now_message = now_message+Integer.toString(message_count)+ ":"+input+"\n"; 
                        out_file.append(now_message);
                        //close it 
                        out_file.close();
                    }
                   
                    for(PrintWriter writer : client_writers)
                    {
                        
                        //this person in here knows the complete data !!
                        writer.println("MESSAGE" + input);
                        
                        
                    }
                }
                
            }catch(IOException e){
        System.out.println("ERROR :"+ e);
            }finally
            {
        if(name != null)
                {
                    client_names.remove(name);
        }
                if(out!= null)
                {
                    client_writers.remove(out);
                }
                try{
                    socket.close();
        }catch(IOException e)
        {
                    System.out.printf("Error : " + e);
        }
            }

        }   
    }
    
    
}
