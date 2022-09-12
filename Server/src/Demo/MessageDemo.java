/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Demo;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author Pratik
 */
public class MessageDemo implements Serializable{

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the sender
     */
 
    private String content;
    
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


   private String sender;
   private String receiver;
           
    private String msg;

Vector data=new Vector();


public MessageDemo(String sender,String receiver,String m,String content,Vector v)
{
    msg=m;
    data=v;
    this.sender=sender;
    this.receiver=receiver;
    this.content=content;
    
}
    
public String getMessage()
{
    return msg;
}

public Vector getData()
{
    return data;
}
    
}
