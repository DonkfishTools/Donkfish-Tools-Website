import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.StaxHelper

def cli = new CliBuilder(usage: "bees app:deploy [options] WAR_FILE", parser: new org.apache.commons.cli.GnuParser ());
cli.a(longOpt:'appid', argName:'appId', args:1, required: false, 'CloudBees application ID')
cli.k(longOpt:'key', argName:'key', args:1, 'CloudBees API key')
cli.s(longOpt:'secret', argName:'secret', args:1, 'CloudBees API secret')
cli.m(longOpt:'message', argName:'message', args:1, required: false, 'message describing the deployment')
cli.e(longOpt:'environment', argName:'env', args:1, required: false, 'environment configurations to deploy')
cli.d(longOpt:'delta', argName:'true|false', args:1, required: false, 'true to enable, false to disable delta upload (optional)')

target(default:"bees app deploy script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        assertExit(cmdArgs.size() < 2, "unknown arguments: ${cmdArgs}", cli, 1)
        assertExit(cmdArgs.size() == 1, "missing required argument WAR_FILE", cli, 1)
        def archiveFile = cmdArgs[0]
        assertExit(new File(archiveFile).exists(), "file does not exist ${archiveFile}", cli, 2)
        
        def appId = opt.a ? opt.a : appHelper.appId
        assertExit(appId != null, "appid could not be determined, use -a", cli, 3)
        def key = opt.k ? opt.k : null
        def secret = opt.s ? opt.s : null
        def delta = opt.d ? opt.d : "true"

        def srcFile = null
        def env = opt.e ? opt.e : null
        def description = opt.m ? opt.m : null
        def archiveType = archiveFile.endsWith(".war") ? "war" : "ear"
        
        StaxClient staxClient = staxHelper.getApiClient(key: key, secret: secret)
        def result = staxClient.applicationDeployArchive(appId, env, description, archiveFile, srcFile, archiveType, delta.toBoolean(), new net.stax.api.HashWriteProgress())
        println "Application ${result.id} deployed: ${result.url}"
    }
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
    println "app:deploy: deploy an application archive"
    cli.usage()
}