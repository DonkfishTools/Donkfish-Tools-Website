import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees app:list", parser: new org.apache.commons.cli.GnuParser ());

includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees application list") {
    def opt = cli.parse(args)
    if(opt)
    {
    	StaxClient staxClient = staxHelper.getApiClient()
    	def result = staxClient.applicationList()
    	result.applications.each({appInfo -> println appInfo.id });
    }
}

target(help:"usage") {
    cli.usage()
}