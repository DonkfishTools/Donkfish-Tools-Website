import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees db:delete [options] DATABASE_NAME", parser: new org.apache.commons.cli.GnuParser ());
cli.f(longOpt:'force', args:0, 'forced delete without prompting')

includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees db:delete script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs() 
    	def dbName = cmdArgs ? cmdArgs[0] : prompt("Database name: ")
    	if(!opt.f)
    	{
    	    def answer = prompt("Are you sure you want to delete this database [${dbName}]: (y/n) ")
    	    if(answer != 'y' && answer != 'yes')
    	        return;
    	}
    		
    	StaxClient staxClient = staxHelper.getApiClient()
    	def result = staxClient.databaseDelete("${dbName}")
    	if(result.deleted)
    		println "database deleted - ${dbName}"
    	else
    		println "database could not be deleted"
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