/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Demo.MessageDemo;
import java.io.File;
import java.io.FileInputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JOptionPane;


/**
 *
 * @author Pratik
 */
public class ClientDemo implements Runnable {
    
    
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    ClientSetup frame;
    Thread t;
    
//    public void confirmation(String path)
//    {
//        try{
//            
//            writeMessage(new MessageDemo("server","","confirmation","",null));
//            
//        }catch(Exception ee)
//        {
//            System.out.println("Confirmation Error"+ee);
//        }
//    }
    
    public void sendFileToServer(String path)
    {
        try{
            
            File F= new File(path);
            String name=F.getName();
            FileInputStream in =new FileInputStream(path);
            
            byte b[]=new byte[in.available()];
            in.read(b);
            
            in.close();
            
            Vector V= new Vector();
            V.add(name);
            V.add(b);
            System.out.println(V);
            writeMessage(new MessageDemo("client","server","sendfileserver","",V));
           
            
        }catch(Exception ee)
        {
            System.out.println("Exception In SendFileToServer"+ee);
        }
    }
    
    
    public ClientDemo(ClientSetup fr)
    {
        try{
        frame=fr;
        socket=new Socket("127.0.0.1",8001);
        
        String ip= socket.getRemoteSocketAddress().toString();
        frame.lblname.setText(ip);
        setStream();
        t=new Thread(this);
        t.start();
        
        }catch(Exception ee)
        {
            System.out.println("Client Demo Constructor Error :"+ee);
        }
    }
    
    public void setStream()
    {
        try{
            
            if(socket!=null)
            {
                 out= new ObjectOutputStream(socket.getOutputStream());
                in=new ObjectInputStream(socket.getInputStream());
               
            }
        }catch(Exception ee)
        {
            System.out.println("setStream Error: "+ee);
        }
    }
    
    public void writeMessage(MessageDemo msg)
    {
        try{
            //System.out.println("WriteMessage"+msg);
            //System.out.println(""+out);
           
            out.flush();
        out.writeObject(msg);
        
        }catch(Exception ee)
        {
            System.out.println("WriteMessage Error: "+ee);
        }
    }
    
    public void sendMessage(String msg)
    {
        try{
        out.writeObject(new MessageDemo(frame.lblname.getText(),"server","",msg,null));
        out.flush();
        }
        catch(Exception ee)
        {
            System.out.println("Send MEssage Error: "+ee);
        }
    }
    public void sendFileToClient(String path,String ip)
    {
        try{
            
              File F= new File(path);
            String name=F.getName();
            FileInputStream in =new FileInputStream(path);
            
            byte b[]=new byte[in.available()];
            in.read(b);
            
            in.close();
            
            Vector V= new Vector();
            V.add(name);
            V.add(b);
           //V.add(ip);
            
            System.out.println(V.get(0));
            System.out.println(V.get(1));
            out.writeObject(new MessageDemo(frame.lblname.getText(),ip,"sendfileclient","",V));
           
            
        }
        catch(Exception ee)
        {
            System.out.println("Exception in SendFileToClient"+ee);
        }
    }
    
     public void sendMessageToClient(String msg,String ip)
    {
        try{
            
        out.writeObject(new MessageDemo(frame.lblname.getText(),ip,"sendtoclient",msg,null));
        
        }
        catch(Exception ee)
        {
            System.out.println("Send MEssage Error: "+ee);
        }
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
    public void run()
    {
        try{
            while(true)
            {
                
            Demo.MessageDemo m=(Demo.MessageDemo)in.readObject();
            System.out.println("Message is"+m.getMessage());
            
            if(m.getMessage().equalsIgnoreCase("newuser"))
            {
                frame.txtmain.append("Server Says :"+m.getContent()+"\n");
                frame.lblname.setText(m.getReceiver());
            }
            else if(m.getMessage().equalsIgnoreCase("ip"))
            {
                Vector V= m.getData();
                frame.clientlist.clear();
                for(Object obj:V)
                {
                    String ip=(String)obj;
                    
                    if(frame.lblname.getText().equalsIgnoreCase(ip)==false)
                    {
                        frame.clientlist.add(ip);
                    }
                }
                
            
            }
             else if(m.getMessage().equalsIgnoreCase("download"))
            {   
                frame.txtmain.append("Sender : "+m.getSender() +"Says : File Download Successfully");
                
            }
             else if(m.getMessage().equalsIgnoreCase("notdownload"))
             {
                 JOptionPane.showMessageDialog(frame,m.getContent());
             }
            else if(m.getMessage().equalsIgnoreCase("sendtoclient"))
            {
                frame.txtmain.append(m.getSender()+" Says :"+m.getContent()+"\n");
            }
              else if(m.getMessage().equalsIgnoreCase("sendfileclient"))
            {
                
                if(JOptionPane.showConfirmDialog(frame,"R u Really wnat to Download")==JOptionPane.OK_OPTION)
               {   
                    FileDownloader downloader;
                   downloader=new FileDownloader(m);
                   downloader.start();
                  
                   
                  
                       //String ip=m.getSender();
                       //System.out.println("Client"+ip);
                       writeMessage(new MessageDemo(m.getReceiver(),m.getSender(),"download","File Successfully Downlaod",null));
                  
                       
                  
                   
                   
               }
                 else{
                
                   
                      String ip=m.getSender();
                      System.out.println(ip);
                        writeMessage(new MessageDemo(frame.lblname.getText(),ip,"notdownload","Server Not Downloaded File",null));
                       
                   }
                       
            
                 // JOptionPane.showMessageDialog(frame,"Not Downloading");
               
            }
          
          
            /*
            {
                String arr[]=frame.clientlist.getItems();
                 Vector v=new Vector();
                 for(String s:arr)
                 {
                     v.add(s);
                 }
                 
                 
                 sendToIp(v);
            }
            
            */
           
            
            else if(m.getMessage().equalsIgnoreCase("sendtomany"))
            {
                frame.txtmain.append(m.getSender()+" Says :"+m.getContent()+"\n");
            }
            else if(m.getSender().equalsIgnoreCase("server"))
            {
                frame.txtmain.append("Server Says :"+m.getContent()+"\n");
            }
            
            }
            
            
        }catch(Exception err)
        {
            System.out.println("Run Method Error In Client"+err);
        }
    }

    
    public void sendToManyClient(String msg,ArrayList lst) {
       try{
           Vector V=new Vector();
             for(Object obj:lst)
             {
                 String ip=(String)obj;
               
               V.add(ip);
               //System.out.println(ip);
              // System.out.println(V);
                // frame.clientlist.getItem(0);
                 //Client c=list.get(ip);
                //v.get(ip);
                
                
                 
                 
                
             }
              
            out.writeObject(new MessageDemo(frame.lblname.getText(),null,"sendtomany",msg,V));
                
            
       }catch(Exception ee)
       {
           System.out.println("Send to Many Client Error :"+ee);
       }
    } 
     
}
