/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Demo.MessageDemo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Pratik
 */


class Client extends Thread
{
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    String ip;
    ServerDemo server;
    FileDownloader downloader;
    
    public Client(Socket sc,ServerDemo server)
    {
        socket=sc;
       this.server=server;
        setStream();
    }
    
    
    public void run()
    {
        
        try{
            
            while(true)
            {
            MessageDemo m= (MessageDemo) in.readObject();
            
            //System.out.println(""+m.getReceiver());
            //System.out.println(""+m.getMessage());
            
            if(m.getMessage().equalsIgnoreCase("sendtoclient"))
            {
                System.out.println("Hello1");
               Client c= server.getReceiver(m.getReceiver());
               c.writeMessage(new MessageDemo(m.getSender(),null,"sendtoclient",m.getContent(),null));
            }
            else if(m.getMessage().equalsIgnoreCase("sendtomany"))
            { 
                System.out.println("HelloM");
                
                 Vector v=m.getData();
                
                for(Object obj:v)
                {   
                   // System.out.println(ip);
                    String rip=(String)obj;
                   // System.out.println("Hello"+rip);
                   Client c= server.getReceiver(rip);
                   // System.out.println(rip+""+c);
                   c.writeMessage(new MessageDemo(m.getSender(),null,"sendtomany",m.getContent(),null));
                    
                }
            }
           
            else if(m.getMessage().equalsIgnoreCase("sendfileserver"))
            {
                
                
                
               if(JOptionPane.showConfirmDialog(server.frame, "R U Realy Download The File")==JOptionPane.OK_OPTION)
               {
                
                   downloader=new FileDownloader(m,this);
                   downloader.start();
                   
                   
               }
               else{
                 
                  writeMessage(new MessageDemo("server","","notdownload","Server Not Downloaded File",null));
                 // JOptionPane.showMessageDialog(frame,"Not Downloading");
               }
               
               
                
            }
            else if(m.getMessage().equalsIgnoreCase("sendfileclient"))
                
            {
                Vector V=m.getData();
                System.out.println(""+m.getReceiver());
                Client c=server.getReceiver(m.getReceiver());
                
                 c.writeMessage(new MessageDemo(m.getSender(),m.getReceiver(),"sendfileclient","",V));
                
               
            }
            else if(m.getMessage().equalsIgnoreCase("download"))
            {
                 Client c=server.getReceiver(m.getReceiver());
                c.writeMessage(new MessageDemo(m.getSender(),m.getReceiver(),"download",m.getContent(),null));
            }
            else if(m.getMessage().equalsIgnoreCase("notdownload"))
            {
                System.out.println(""+m.getReceiver());
                out.writeObject(new MessageDemo(m.getSender(),m.getReceiver(),"notdownload",m.getContent(),null));
            }
            
              else if(m.getReceiver().equalsIgnoreCase("server"))
                
            {
               System.out.println("Server");
                server.frame.txtmain.append(m.getSender()+" : Says =>"+m.getContent()+"\n");
            }
                     
            
            } 
            
            
        }catch(Exception ee)
        {
            System.out.println("Client Class Error:"+ee);
        }
    }
    
    
    public void setStream()
    {
        try{
            
            if(socket!=null)
            {
                in=new ObjectInputStream(socket.getInputStream());
                out= new ObjectOutputStream(socket.getOutputStream());
            }
        }catch(Exception ee)
        {
            System.out.println("setStream Error: "+ee);
        }
    }
    
    public void writeMessage(MessageDemo msg)
    {
        try{
            //System.out.println("WriteMessage in ServerClient");
        out.writeObject(msg);
        }catch(Exception ee)
        {
            System.out.println("WriteMessage Error: "+ee);
        }
    }
    
    public void downloadSuccessfully()
    {
        JOptionPane.showMessageDialog(server.frame,"File Successfully Download");
    }
    
    public void readMessage()
    {
        
        try{
              MessageDemo d=(MessageDemo)in.readObject();
              
        }catch(Exception ee)
        {
            System.out.println("ReadMessage Error: "+ee);
        }
    }
    
    public void sendMessage(MessageDemo msg)
    {
        try{
        out.writeObject(msg);
        }catch(Exception ee)
        {
            System.out.println("SendMessage Error "+ee);
        }
    }
    
    
}
    

public class ServerDemo implements Runnable {
    
    Hashtable<String, Client> list= new Hashtable<String,Client>();
    
    //ArrayList<Client> list= new ArrayList();
   
    
    ServerSocket server;
    Socket socket;
    Thread t;
    public ServerSetup frame;
    
   
 
    
    public Client getReceiver(String ip)
    {
        return list.get(ip);
    }
   
    public ServerDemo(ServerSetup fr)
    {
        try{
            
            frame=fr;
            server=new ServerSocket(8001);
            
            t=new Thread(this);
            t.start();
            
        }catch(Exception ee)
        {
            System.out.println("Server Constructor : "+ee);
        }
    }
    
    
    public void sendToAllClient(String msg)
    {
    
       Enumeration<Client> e=list.elements();
       
      while(e.hasMoreElements())
      {
          Client c= e.nextElement();
          
          c.writeMessage(new MessageDemo("server",null,"",msg,null));
      }
        
        
        
    }
    
    
    public void sendToSingleClient(String msg,ArrayList lst)
    {
        try{
            
            for(Object obj:lst)
            {
                String ip=(String)obj;
                Client c=list.get(ip);
                System.out.println(ip+""+c);
                
                c.writeMessage(new MessageDemo("client",null,"",msg,null));
            }
            
        }catch(Exception ee)
        {
            System.out.println("Error In SendToSingleClient"+ee);
        }
        
    }
    
    public void sendToIp(Vector v)
    {
        try{
       
            Enumeration<Client> e=list.elements();
       
      while(e.hasMoreElements())
      {
          Client c= e.nextElement();
          
          c.writeMessage(new MessageDemo("server",null,"ip",null,v));
      }
       
            
        }catch(Exception ee)
        {
            System.out.println("send To Ip Error :"+ee);
        }
    }
    public void run()
    {
        try{
        
        while(true)
        {
            Socket sc=server.accept();
            String ip= sc.getRemoteSocketAddress().toString();
            Client c= new Client(sc,this);
            
            list.put(ip, c);
            c.start();
            
            frame.txtmain.append("Connected : "+ip);
            
            frame.clientlist.add(ip);
            
            MessageDemo d=new MessageDemo("server",ip,"newuser","Welcome To Chat Window",null);
            c.writeMessage(d);
            
            if(list.size()>1)
            {
                String arr[]=frame.clientlist.getItems();
                 Vector v=new Vector();
                 for(String s:arr)
                 {
                     v.add(s);
                 }
                 
                 
                 sendToIp(v);
            }
            //MessageDemo d1=(MessageDemo)in.readObject();
            
            
      }
        }catch(Exception ee)
        {
            System.out.println("Server run Method Error : "+ee);
        }
        
    }
}
