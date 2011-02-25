package net.stax.sdk.tools
import org.codehaus.gant.GantBinding;
import com.staxnet.ant.ModuleDownloader;

public class StaxHelper
{
    private GantBinding binding;
    private PromptHelper promptHelper;
    StaxHelper(GantBinding binding)
    {
        this.binding = binding;
    }
    
    public net.stax.api.StaxClient getApiClient(Map opts = [:])
    {
        def apiConfig = binding.beesConfig.bees.api;
        def apiVersion = "1.0" 
        def apiKey = opts.apikey ? opts.apikey : apiConfig.key 
        def apiSecret = opts.secret ? opts.secret : apiConfig.secret        
        
        if(apiKey == null || apiKey == [:])
        {
            if(opts.key)
                apiConfig.key = opts.key
            if(opts.secret)
                apiConfig.secret = opts.secret
            
            if(apiConfig.key == [:])
                apiConfig.key = prompt("CloudBees API key: ")
            if(apiConfig.secret == [:])
                apiConfig.secret = prompt("CloudBees secret: ")
            apiKey = apiConfig.key
            apiSecret = apiConfig.secret
            apiVersion = "1.0"
        }
        
        net.stax.api.StaxClient staxClient = new net.stax.api.StaxClient("${apiConfig.url}", "${apiKey}", "${apiSecret}", "xml", "${apiVersion}")
    }
    
    def prompt(message)
    {
        if(promptHelper == null)
            promptHelper = new PromptHelper(binding)
        return promptHelper.prompt(message)
    }
}