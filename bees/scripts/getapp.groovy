import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees getapp [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.a(longOpt:'appid', argName:'appId', args:1, 'CloudBees application ID')
cli.f(longOpt:'force', args:0, 'forced overwrite without prompting')
cli.k(longOpt:'key', argName:'key', args:1, 'CloudBees API key')
cli.s(longOpt:'secret', argName:'secret', args:1, 'CloudBees secret')

includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.PromptHelper
includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees get application") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs() 
    	assertExit(cmdArgs.size() == 0, "unknown arguments: ${cmdArgs}", cli, 1)
        def appId = opt.a ? opt.a : null
        def key = opt.k ? opt.k : null
        def secret = opt.s ? opt.s : null
        def targetDir = new File(".");
        if(!opt.f && targetDir.list().length > 0)
        {
            def result = promptHelper.prompt("WARNING: The target directory contains files that may be overwritten. \n [target directory: ${targetDir.canonicalPath}] \nDo you want to continue (Y/N)?: ")
            if(!result.equalsIgnoreCase("y"))
                return
        }

        StaxClient staxClient = staxHelper.getApiClient(key: key, secret: secret)
    	def result = staxClient.applicationGetSourceUrl("${appId}")
    	def filepath = "___" + appId.tokenize("/")[-1] + ".zip";
    	downloadToFile("${result.url}", filepath)
    	ant.unzip(src : filepath, dest: ".")
    	ant.delete( file: filepath )
    }
}

private def downloadToFile(address, filepath)
{
    def file = new FileOutputStream(filepath)
    def out = new BufferedOutputStream(file)
    out << new URL(address).openStream()
    out.close()
}

private void assertExit(condition, errorMessage, cli, exitCode)
{
    if(!condition)
    {
        System.err.println "Usage error: ${errorMessage}";
        cli.usage();
        System.exit(exitCode);
    }
}

target(help:"usage") {
    cli.usage()
}