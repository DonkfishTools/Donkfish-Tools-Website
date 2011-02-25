def staxProjectConfig = staxConfig.stax.project

includeTool << net.stax.sdk.tools.AntHelper
includeTool << net.stax.sdk.tools.AppHelper
appHelper.abortIfNotProjectDir()

target(default:"stax get:hosted script") {
    def cli = new CliBuilder(usage: "stax gwt:hosted", parser: new org.apache.commons.cli.GnuParser ());
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        assertExit(cmdArgs.size() == 0, "unknown arguments: ${cmdArgs}", cli, 1)

        def antFile = new File(staxConfig.stax.project.basedir, "${staxProjectConfig.ant.file}").canonicalPath 
        //run the project clean target
        antHelper.ant(antfile: "${antFile}",
                target:"hosted", 
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