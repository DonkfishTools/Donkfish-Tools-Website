def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees package", parser: new org.apache.commons.cli.GnuParser ());

includeTool << net.stax.sdk.tools.ModuleHelper
includeTool << net.stax.sdk.tools.AntHelper
includeTool << net.stax.sdk.tools.AppHelper
appHelper.abortIfNotProjectDir()

target(default:"bees package script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        assertExit(cmdArgs.size() == 0, "unknown arguments: ${cmdArgs}", cli, 1)
        
        def staxappxmlFile = new File(staxProjectConfig.basedir, "${staxProjectConfig.staxappxml}")
        if(staxappxmlFile.exists())
            moduleHelper.resolveModules("${staxappxmlFile}")
        
        //run the project dist target
        def antFile = new File(beesConfig.bees.project.basedir, "${staxProjectConfig.ant.file}").canonicalPath
        antHelper.ant(antfile: "${antFile}",
        		target:"dist", 
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
    println "package: package the application into a web archive"
    cli.usage()
}