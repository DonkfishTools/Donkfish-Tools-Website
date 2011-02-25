def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees deploy [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.k(longOpt:'key', argName:'key', args:1, 'CloudBees API key')
cli.s(longOpt:'secret', argName:'secret', args:1, 'CloudBees secret')
cli.a(longOpt:'appid', argName:'appId', args:1, 'CloudBees application ID')
cli.e(longOpt:'env', argName:'environment', args:1, 'environment configurations to deploy')
cli.m(longOpt:'message', argName:'message', args:1, 'message describing the deployment')
cli.d(longOpt:'delta', argName:'true|false', args:1, 'true to enable, false to disable delta upload (optional)')

includeTool << net.stax.sdk.tools.ModuleHelper
includeTool << net.stax.sdk.tools.PromptHelper
includeTool << net.stax.sdk.tools.AntHelper
includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.StaxHelper
appHelper.abortIfNotProjectDir()

target(default:"bees deploy script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        assertExit(cmdArgs.size() == 0, "unknown arguments: ${cmdArgs}", cli, 1)
        def appId = opt.a ? opt.a : null        
        def environment = opt.e ? opt.e : null
        def message = opt.m ? opt.m : null
        def key = opt.k ? opt.k : null
        def secret = opt.s ? opt.s : null
        def delta = opt.d ? opt.d : null

        //hack: init the API client so that the user credentials are properly initialized when
        //the Stax ant deploy task is called
        def staxClient = staxHelper.getApiClient(key: key, secret: secret)
        
        def staxappxmlFile = new File(staxProjectConfig.basedir, "${staxProjectConfig.staxappxml}")
        if(staxappxmlFile.exists())
            moduleHelper.resolveModules("${staxappxmlFile}")
        
        def antFile = new File(beesConfig.bees.project.basedir, "${staxProjectConfig.ant.file}").canonicalPath
        antHelper.ant(antfile: "${antFile}",
                target: "deploy",
                dir: "${staxProjectConfig.basedir}",
                inheritAll:"false")
        {
          // Old properties, might still be used in old build.xml
            property(name: "stax.appid", value: appId ? appId : "")
            property(name: "stax.message", value: message ? message : "")
            property(name: "stax.environment", value: environment ? environment : "")


            property(name: "bees.appid", value: appId ? appId : "")
            property(name: "bees.message", value: message ? message : "")
            property(name: "bees.environment", value: environment ? environment : "")
            property(name: "bees.delta", value:  delta ? delta : "true")

            //Warning: these properties are no longer used, but we need to set them since old
            //beta projects still check that they exist in the stax-build.xml file
            property(name: "stax.username", value: key ? key : "")
            property(name: "stax.password", value:  secret ? secret : "")
        }
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
    println "deploy: deploy the web application"
    cli.usage()
}