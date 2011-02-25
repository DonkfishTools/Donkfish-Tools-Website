package net.stax.sdk.tools
import org.codehaus.gant.GantBinding;
import com.staxnet.appserver.config.AppConfig;
import com.staxnet.appserver.config.AppConfigHelper;

public class AppHelper
{
    private static String appId
    private GantBinding binding
    AppHelper(GantBinding binding)
    {
        this.binding = binding
    }
    
    public String getAppId(environments=[])
    {
        if(!appId)
        {
            if(binding.beesConfig.bees.project.app.id != [:])
            {
                appId = binding.beesConfig.bees.project.app.id;
            }
            else if(binding.beesConfig.bees.project.webroot != [:])
            {
                def webroot = binding.beesConfig.bees.project.webroot
                def webrootDir = new File("${binding.beesConfig.bees.project.basedir}", webroot)
                def configFile = new File(webrootDir, "WEB-INF/cloudbees-web.xml");
                if(!configFile.exists())
                  configFile = new File(webrootDir, "WEB-INF/stax-web.xml");
                if(configFile.exists())
                  appId = getAppConfig(configFile, (String[])environments, new String[0]).applicationId
            }
        }
        
        if(appId == null || appId == "")
            appId = new PromptHelper(binding).prompt("CloudBees application id: ")
        if(appId.split("/").length < 2 && binding.beesConfig.bees.project.app.domain != [:])
            appId = binding.beesConfig.bees.project.app.domain + "/" + appId
            
        return appId;
    }
    
    public boolean abortIfNotProjectDir()
    {
        if(binding.beesConfig.bees.project.basedir == [:])
        {
            throw new Exception("This is not a valid project dir")
        }
    }
    
    private AppConfig getAppConfig(File configFile, final String[] environments,
            final String[] implicitEnvironments) throws IOException {
        AppConfig appConfig = new AppConfig();
        
        configFile.withInputStream { input ->
            AppConfigHelper.load(appConfig, input, configFile.absolutePath, environments,
                    implicitEnvironments);
        }
        return appConfig;
    }
}