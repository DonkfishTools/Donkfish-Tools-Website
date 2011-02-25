import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees app:info [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.a(longOpt:'appid', argName:'appId', args:1, required: false, 'CloudBees application ID')

includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees application info") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs() 
    	def appId = opt.a ? opt.a : appHelper.appId
    	
        StaxClient staxClient = staxHelper.getApiClient()
    	def result = staxClient.applicationInfo("${appId}")
    	println "ID:      ${result.id}"
        println "Status:  ${result.status}"
        println "Web URL: ${result.urls[0]}"
    }
}

target(help:"usage") {
    cli.usage()
}