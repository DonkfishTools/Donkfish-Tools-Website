import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.StaxHelper

def cli = new CliBuilder(usage: "bees app:tail [options] LOGNAME", parser: new org.apache.commons.cli.GnuParser ());
cli.a(longOpt:'appid', argName:'appId', args:1, required: false, 'CloudBees application ID')

target(default:"bees tail script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        def appId = opt.a ? opt.a : appHelper.appId
    	def logName = cmdArgs ? cmdArgs[0] : "server"
    	
	    StaxClient staxClient = staxHelper.getApiClient()
    	staxClient.tailLog(appId, logName, System.out)    	
    }
}
	
target(help:"usage") {
    println "tail: tail a log file"
    cli.usage()
    println " LOGNAME              server, access or error"
}