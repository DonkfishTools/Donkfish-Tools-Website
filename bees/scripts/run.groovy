def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees run [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.p(longOpt:'port', argName:'port', args:1, 'server listen port (default: 8080)')
cli.e(longOpt:'env', argName:'environment', args:1, 'environment configurations to load (default: run)')

includeTool << net.stax.sdk.tools.ModuleHelper
includeTool << net.stax.sdk.tools.AntHelper
includeTool << net.stax.sdk.tools.AppHelper
appHelper.abortIfNotProjectDir()

target(default:"bees run script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        assertExit(cmdArgs.size() == 0, "unknown arguments: ${cmdArgs}", cli, 1)
        def port = opt.p ? opt.p : 8080
        def env = opt.e ? opt.e : null
        
        def staxappxmlFile = new File(staxProjectConfig.basedir, "${staxProjectConfig.staxappxml}")
        if(staxappxmlFile.exists())
            moduleHelper.resolveModules("${staxappxmlFile}")
        
    	//run the project run target
    	def antFile = new File(beesConfig.bees.project.basedir, "${staxProjectConfig.ant.file}")
    	antHelper.ant(antfile: "${antFile}",
    			target:"run", 
    			dir: "${staxProjectConfig.basedir}",
    			inheritAll:"false")
    	{
            property(name: "run.port", value: port)
            property(name: "run.environment", value: env ? env : "")
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
    println "run: launch the web application in a local test environment"
    cli.usage()
}
