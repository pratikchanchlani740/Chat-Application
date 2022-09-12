/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Demo.MessageDemo;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

/**
 *
 * @author Pratik
 */
public class FileDownloader extends Thread {

    
    
    MessageDemo msg;
   
            
    public FileDownloader(MessageDemo msg)
    {
        this.msg=msg;
     
        
        
    }
    
    public void run()
    {
        
        try{  
            
               System.out.println("Hello");
              Vector V= msg.getData();
                //File F=new File(getClass().getResource("download").getPath());
                
                //
               
                
                File currDir = new File(".");
               String  path = currDir.getAbsolutePath();
               
               path=path.substring(0,path.length()-1);
               
               path=path.replace("\\", "/");
               System.out.println(path);
               System.out.println(V.get(0));
               String filename=(String)V.get(0);
                System.out.println(filename);
               path=path+"src/download/"+filename;
                System.out.println(path);
                
                
                
               FileOutputStream cout= new FileOutputStream(path);
               
               byte b[]=(byte[])V.get(1);
               
               cout.write(b);
               
               cout.close();
              
               
               //s.downloadSuccessfully();
               
        }
        catch(Exception ee)
        {
            System.out.println("FileDownloader Class Error :"+ee);
            
        }
    }
    
}
