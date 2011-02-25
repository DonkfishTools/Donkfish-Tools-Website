import net.stax.api.StaxClient

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees app:install [options]", parser: new org.apache.commons.cli.GnuParser ());
cli.a(longOpt:'appid', argName:'appId', args:1, required: false, 'CloudBees application ID')
cli.u(longOpt:'url', args:1, argName:'url', 'URL of the install descriptor')

includeTool << net.stax.sdk.tools.AppHelper
includeTool << net.stax.sdk.tools.PromptHelper
includeTool << net.stax.sdk.tools.StaxHelper

target(default:"bees install application") {
    def opt = cli.parse(args)
    if(opt)
    {
        def appId = opt.a ? opt.a : appHelper.appId
        def url = opt.u ? opt.u : promptHelper.prompt("Installation descriptor URL: ")
        
        StaxClient staxClient = staxHelper.getApiClient()
        def result = staxClient.applicationInstall("${appId}", "${url}")
        println "ID:      ${result.id}"
        println "Web URL:  ${result.url}"
    }
}

target(help:"usage") {
    cli.usage()
}