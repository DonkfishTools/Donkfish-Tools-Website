package net.stax.sdk.tools
import org.codehaus.gant.GantBinding;

public class AntHelper
{
    private GantBinding binding
    AntHelper(GantBinding binding)
    {
        this.binding = binding
    }
    
    public void ant(Map args)
    {
        ant(args, null)
    }
    
    public void ant(Map args, c)
    {
        def antProperties = binding.beesConfig.toProperties("beesConfig")
        binding.ant.ant(args)
        {
            antProperties.each{ entry->
                property(name: entry.key, value: entry.value)
            }
            if(c)
                c();
        }
    }
}