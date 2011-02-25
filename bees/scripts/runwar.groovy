
def cli = new CliBuilder(usage: "bees run-war [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.p(longOpt:'port', argName:'port', args:1, 'server listen port (default: 8080)')
cli.e(longOpt:'env', argName:'environment', args:1, 'environment configurations to load (default: run)')

cli.w(longOpt:'web', argName:'webdir', args:1, required: true, 'the local war directory')
cli.d(longOpt:'workingdir', argName:'dir', args:1, 'the local working directory (where temp files can be created)')
cli.f(longOpt:'fork', 'fork')

includeTool << net.stax.sdk.tools.AppServerHelper

target(default:"bees run-war script") {
	def opt = cli.parse(args)
    if(opt)
    {
		def webroot = opt.w;
		def dir = opt.d ? opt.d : "."
		def port = opt.p ? opt.p : 8080
        def env = opt.e ? opt.e : null
		
        def runArgs = [web:webroot, workingDir:dir, "port":port, env:env]
        if(opt.f)
            appServerHelper.forkWar(runArgs)
        else
            appServerHelper.runWar(runArgs)
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
    println "run-war: launch a WAR-based application in a local test server"
    cli.usage()
}
