def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees compile", parser: new org.apache.commons.cli.GnuParser ());

includeTool << net.stax.sdk.tools.ModuleHelper
includeTool << net.stax.sdk.tools.AntHelper
includeTool << net.stax.sdk.tools.AppHelper
appHelper.abortIfNotProjectDir()

target(default:"bees compile script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        assertExit(cmdArgs.size() == 0, "unknown arguments: ${cmdArgs}", cli, 1)
        
        def staxappxmlFile = new File(staxProjectConfig.basedir, "${staxProjectConfig.staxappxml}")
        if(staxappxmlFile.exists())
        {
            println "resolving modules: ${staxappxmlFile}"
            moduleHelper.resolveModules("${staxappxmlFile}")
        }

        def antFile = new File(beesConfig.bees.project.basedir, "${staxProjectConfig.ant.file}").canonicalPath
        //run the project compile target
        antHelper.ant(antfile: "${antFile}",
                target:"compile", 
                dir: "${staxProjectConfig.basedir}",
                inheritAll:"false")
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
    println "compile: compile the web application"
    cli.usage()
}