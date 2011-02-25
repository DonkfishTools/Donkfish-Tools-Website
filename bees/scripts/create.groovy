import net.stax.appgen.AppGenerator;
import com.staxnet.repository.LocalRepository;

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project
def staxTemplates = new File(System.properties['user.home'], ".bees/templates").canonicalPath
if(!new File(staxTemplates).exists())
    new File(staxTemplates).mkdirs()

def cli = new CliBuilder(usage: "bees create [options] APP_DIR", parser: new org.apache.commons.cli.GnuParser ());
cli.a(longOpt:'appid', argName:'appId', args:1, 'CloudBees application Id (default is APP_DIR)')
cli.p(longOpt:'package', argName:'package', args:1, 'package name')
cli.v(longOpt:'verbose', args:0, 'verbose debugging output')

cli.t(longOpt:'template', argName:'templateId', args:1, '''application template ID
  basic  - basic J2EE web (WAR-based) 
  simple - basic J2EE web (EAR-based)
  struts - Apache Struts
  wicket - Apache Wicket
  gwt    - Google Web Toolkit
  coldfusion-core - ColdFusion 8 (minimal)
  lift   - Lift Web Framework (Scala)
  ''')   
    
target(default:"bees create script") {
    def opt = cli.parse(args)
    if(opt)
    {
        def cmdArgs = opt.getArgs()
        assertExit(cmdArgs.size() != 0, "missing APP_DIR", cli, 1)
        assertExit(cmdArgs.size() < 2, "unknown arguments: ${cmdArgs}", cli, 1)
        def appName = cmdArgs[0]
        def appId = opt.a ? opt.a : new File(appName).name
        def pkg = opt.p ? opt.p : null
        def templateId = opt.t ? opt.t : "basic"
        def verbose = opt.v ? true : false
        
        def appgenProps = [appid:appId]
        if(pkg)
            appgenProps['package'] = pkg
        appgenProps['appid'] = appId
        
        File templateFile = null;
        if(templateId.indexOf('/') == -1 && templateId.indexOf('\\') == -1)
        {
            println "loading remote template: ${templateId}"
            LocalRepository repo = new LocalRepository();
            templateFile = repo.getApplicationTemplateZip(templateId)
        }
        else
        {
            println "loading local template: " + new File(templateId).absolutePath
            templateFile = new File(templateId);
        }
        assertExit(templateFile != null && templateFile.exists(), "no such template: " + templateId,-1)
        
        println "Installing from template: ${templateFile}"
        createApp(templateFile.absolutePath, new File(appId).absolutePath, verbose, appgenProps)
    }
}
        
private void createApp(String templateDir, String outputDir, boolean verbose, Map opts)
{
    Properties props = new Properties()
    File appgenPropFile = new File(templateDir, "appgen.properties");
    if(appgenPropFile.exists())
    {
        appgenPropFile.withInputStream({ input ->
            props.load(input);
        })
    }
    opts.each({ name,value -> props.setProperty(name, value) })
    AppGenerator appgen = new AppGenerator(new File(templateDir, "appgen.xml"), props)
    appgen.verbose = verbose
    File outputDirFile = new File(outputDir)
    outputDirFile.mkdirs();
    appgen.generate(outputDirFile);
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

private void assertExit(condition, errorMessage, exitCode)
{
    if(!condition)
    {
        System.err.println "Error: ${errorMessage}";
        System.exit(exitCode);
    }
}

target(help:"usage") {
    cli.usage()
}
