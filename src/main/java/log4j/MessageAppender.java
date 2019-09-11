/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log4j;

import javax.swing.JTextArea;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author mar
 */
public class MessageAppender extends AppenderSkeleton {
    private final JTextArea jTextArea;

    public MessageAppender(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    @Override
    protected void append(LoggingEvent le) {
     custom();
     //if(le.getLevel().toInt()==FIXMain.LevelApp.App.intValue())
     jTextArea.append(le.getMessage().toString());
    }
       public void close() 
    {
    }
    public boolean requiresLayout() 
    {
        return false;
    }
    private void custom(){
    //jTextArea.setForeground(Color.WHITE);
    jTextArea.append("\n");
    jTextArea.setCaretPosition(jTextArea.getDocument().getLength());

    }
}