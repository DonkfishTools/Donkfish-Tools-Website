import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees app:restart [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.a(longOpt:'appid', argName:'appId', args:1, required: false, 'CloudBees application ID')
cli.f(longOpt:'force', args:0, 'forced restart without prompting')

includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.PromptHelper
includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees restart application") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs() 
    	def appId = opt.a ? opt.a : appHelper.appId
    	if(!opt.f)
    	{
        	def answer = promptHelper.prompt("Are you sure you want to restart this application [${appId}]: (y/n) ")
        	if(answer != 'y' && answer != 'yes')
        		return;
    	}
    	
    	StaxClient staxClient = staxHelper.getApiClient()
    	def result = staxClient.applicationRestart("${appId}")
    	if(result.restarted)
    		println "application restarted - ${appId}"
    	else
    		println "application could not be restarted"
    }
}

target(help:"usage") {
    cli.usage()
}