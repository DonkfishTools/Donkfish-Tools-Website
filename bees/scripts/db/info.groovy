import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees db:info DATABASE_NAME", parser: new org.apache.commons.cli.GnuParser ());
cli.p(longOpt:'password', args:0, 'print the database password info')

includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees database info") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs() 
        def dbName = cmdArgs ? cmdArgs[0] : prompt("Database name: ")
        def fetchPassword = opt.p ? true : false;
        
        StaxClient staxClient = staxHelper.getApiClient()
        def result = staxClient.databaseInfo("${dbName}", fetchPassword)
        println "Database name: ${result.name}"
        println "Status:        ${result.status}"
        println "Master:        ${result.master}:${result.port}"
        println "Slaves:        ${result.slaves.join(',')}"
        println "JDBC URL:      jdbc:stax://${result.name}"
        println "Username:      ${result.username}"
        if(fetchPassword)
            println "Password:      ${result.password}"
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