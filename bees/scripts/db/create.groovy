import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees db:create [options] DATABASE_NAME", parser: new org.apache.commons.cli.GnuParser ());
cli.u(longOpt:'username', argName:'username', args:1, 'Database username (must be unique)')
cli.p(longOpt:'password', argName:'password', args:1, 'Database password')
cli.a(longOpt:'account', argName:'account', args:1, 'Account name')

includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees db:create script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()                             
        def dbName = cmdArgs ? cmdArgs[0] : prompt("Database name: ")       
        def dbUsername = opt.u ? opt.u : prompt("Username (must be unique): ")
        def dbPassword = opt.p ? opt.p : prompt("Password: ")
        def account = opt.a ? opt.a : beesConfig.bees.project.app.domain
        if (!account) account = prompt("Account name: ")

        StaxClient staxClient = staxHelper.getApiClient()
        def result = staxClient.databaseCreate("${account}","${dbName}", "${dbUsername}", "${dbPassword}")
        println "database created: ${result.databaseId}"
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