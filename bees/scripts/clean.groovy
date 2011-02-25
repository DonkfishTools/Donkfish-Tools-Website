def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees clean", parser: new org.apache.commons.cli.GnuParser ());
includeTool << net.stax.sdk.tools.AntHelper
includeTool << net.stax.sdk.tools.AppHelper
appHelper.abortIfNotProjectDir()

target(default:"bees clean script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        assertExit(cmdArgs.size() == 0, "unknown arguments: ${cmdArgs}", cli, 1)

        def antFile = new File(beesConfig.bees.project.basedir, "${staxProjectConfig.ant.file}").canonicalPath
        //run the project clean target
        antHelper.ant(antfile: "${antFile}",
                target:"clean", 
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
    println "clean: delete all generated files"
    cli.usage()
}