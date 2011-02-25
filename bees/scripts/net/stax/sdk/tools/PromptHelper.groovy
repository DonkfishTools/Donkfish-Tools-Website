package net.stax.sdk.tools
import org.codehaus.gant.GantBinding;

public class PromptHelper
{
    private GantBinding binding;
    PromptHelper(GantBinding binding)
    {
        this.binding = binding;
    }
    
    def prompt(message, Map opts = [:])
    {
        def force = opts.force ? opts.force : true;
        def defaultValue = opts.defaultValue ? opts.defaultValue : null;
        def readInput = System.in.newReader().&readLine
        print message
        def result = readInput();
        //sleep for just a bit so the process can abort if the user presses Ctrl+C
        Thread.sleep(50)
        while(result == "" && force && !defaultValue)
        {
            print message
            result = readInput();
            Thread.sleep(50)
        }
        
        if(defaultValue && result == "")
            return defaultValue
        else
            return result
    }
    
    //attempts to mask the prompt to hide the password
    def promptPassword(message)
    {
        print message
        boolean maskInput = true;
        def maskchar = ' '
        Thread.start{ while (maskInput) { System.out.print("\010" + maskchar) } }
        try{            
            def password = System.in.newReader().readLine()
            //sleep for just a bit so the process can abort if the user presses Ctrl+C
            Thread.sleep(50)
            
            return password
        }
        finally
        {            
            maskInput = false;
        }        
    }
}