import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees app:delete [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.f(longOpt:'force', args:0, 'forced delete without prompting')
cli.a(longOpt:'appid', argName:'appId', args:1, required: false, 'CloudBees application ID')

includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.PromptHelper
includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees delete application") {
    def opt = cli.parse(args)
    if(opt)
    {
        def appId = opt.a ? opt.a : appHelper.appId
    	if(!opt.f)
    	{
        	def answer = promptHelper.prompt("Are you sure you want to delete this application [${appId}]: (y/n) ")
        	if(answer != 'y' && answer != 'yes')
        		return;
    	}
    	
    	StaxClient staxClient = staxHelper.getApiClient()
    	def result = staxClient.applicationDelete("${appId}")
    	if(result.deleted)
    		println "application deleted - ${appId}"
    	else
    		println "application could not be deleted"
    }
}

target(help:"usage") {
    cli.usage()
}