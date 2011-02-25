def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees call [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.k(longOpt:'key', argName:'key', args:1, 'CloudBees API key')
cli.s(longOpt:'secret', argName:'secret', args:1, 'CloudBees secret')

includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees call script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        def key = opt.k ? opt.k : null
        def secret = opt.s ? opt.s : null

        def staxClient = staxHelper.getApiClient(key: key, secret: secret)
        staxClient.mainCall(cmdArgs);
    }
}