package net.stax.sdk.tools
import org.codehaus.gant.GantBinding;
import com.staxnet.ant.ModuleDownloader;

public class ModuleHelper
{
    private GantBinding binding;
    ModuleHelper(GantBinding binding)
    {
        this.binding = binding;
    }
    
    public void resolveModules(staxConfFile)
    {
        def appConfigFile = new File("${staxConfFile}");
        if(appConfigFile.exists())      
            new ModuleDownloader().loadApplicationModules(appConfigFile);
    }
}