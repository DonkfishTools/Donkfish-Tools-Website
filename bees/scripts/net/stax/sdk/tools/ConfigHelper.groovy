package net.stax.sdk.tools
import org.codehaus.gant.GantBinding;

public class ConfigHelper
{
    private GantBinding binding
    ConfigHelper(GantBinding binding)
    {
        this.binding = binding
    }
    
    public ConfigObject load(String configName)
    {
        if("${binding.beesConfig.bees.project.basedir}" == "")
            throw new Exception("Not in a project context")
        File srcFile = new File(binding.beesConfig.bees.project.basedir, configName + ".config")
        if(srcFile.exists())
            return new ConfigSlurper().parse(srcFile.toURL())
        else
            return new ConfigObject()
    }
    
    public void save(String configName, ConfigObject config)
    {
        File srcFile = new File(binding.beesConfig.bees.project.basedir, configName + ".config")
        srcFile.withWriter { writer ->
            config.writeTo(writer)
        }
    }
}
