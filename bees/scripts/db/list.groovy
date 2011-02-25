import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees db:list", parser: new org.apache.commons.cli.GnuParser ());

includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees database list") {
    def opt = cli.parse(args)
    if(opt)
    {
    	StaxClient staxClient = staxHelper.getApiClient()
    	def result = staxClient.databaseList()
    	println "CloudBees Databases:"
    	result.databases.each({dbInfo -> println "  ${dbInfo.name}" });
    }
}
	
def prompt(message)
{
	def readInput = System.in.newReader().&readLine
	print message
	return readInput();
}

target(help:"usage") {
    cli.usage()
}