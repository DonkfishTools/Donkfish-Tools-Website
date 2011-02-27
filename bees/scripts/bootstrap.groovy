import java.io.*
import java.util.Properties
import org.codehaus.gant.GantBinding

public class Bootstrap
{
    public static String version = "0.5.0"
    public String staxHome
    public File userConfigFile = new File(getUserHome(), ".bees/bees.config")
    public File baseDir
    
    public static void main(String[] args)
    {
        Bootstrap b = new Bootstrap([homeDir:System.properties['bees.home'], baseDir: ".", checkVersion: true])
        if(!args || (args[0] == "help" && args.size()==1))
        {
            println '''usage: bees subcommand [options] [arguments]
Bees command-line client, version ''' + version + '''
Type 'bees help <subcommand>' for help on a specific subcommand.

Project subcommands:
    clean
    compile
    create
    deploy
    getapp
    run

App subcommands:
    app:delete
    app:deploy
    app:info
    app:list
    app:restart
    app:tail

DB subcommands:
    db:create
    db:delete
    db:info
    db:list'''
        return
        }
        b.run(args)
        System.exit(0)
    }
    
    public Bootstrap(config)
    {
        staxHome = config.homeDir ?  "${config.homeDir}" : System.properties['bees.home']
        baseDir = new File("${config.baseDir}" ?  config.baseDir : ".").canonicalFile
        if(!System.properties['bees.repository'])
            System.properties['bees.repository'] = new File(getUserHome(), ".bees").absolutePath

        def loader = this.class.classLoader.rootLoader
        def dir = new File(staxHome, 'lib')
        dir.eachFileMatch(~/.*\.jar$/){
            //println "adding ${it}"
            loader.addURL(it.toURL())
        }
        loader.addURL(new File(staxHome, 'scripts').toURL())
        
        def repository = loader.loadClass("com.staxnet.repository.LocalRepository").newInstance()
        //repository.sdkConfigUrl = sdkConfigUrl
        
        if(config.checkVersion)
            checkVersion(version, repository)
    }
    
    public void run(String[] args)
    {
        def showErrorDetails = System.getenv("BEES_TRACE") == "true";
    
        def subcommand = args[0];               
        def subargs = args.toList()
        subargs.remove(0)
    
    //    init the default Gant script global variables
    //    load the default bees config
        ConfigObject config = new ConfigObject();
        config.bees.api.url = "https://api.cloudbees.com/api"

    //    load the user stax.config file
        if(userConfigFile.exists())
        {
            def fis = new FileInputStream(userConfigFile)
            def props = new Properties()
            props.load(fis)
            fis.close()
            ConfigObject userConfig = new ConfigSlurper().parse(props)
//            ConfigObject userConfig = new ConfigSlurper().parse(userConfigFile.toURL())
            config = config.merge(userConfig);
        }
        else
        {
            def read = System.in.newReader().&readLine 
            println "You have not created a CloudBees configuration profile yet, let's create one now..."
            println "Go to https://grandcentral.cloudbees.com/user/keys to retrieve your API key"

            print "Enter your CloudBees API key: "
            config.bees.api.key = read()
            
            print "Enter your CloudBees secret: "
            config.bees.api.secret = read()
            
            print "Enter your default CloudBees account name: "
            config.bees.project.app.domain = read()
            
            if(!userConfigFile.parentFile.exists())
                userConfigFile.parentFile.mkdirs();

            def fos = new FileOutputStream(userConfigFile)
            def props = config.toProperties()
            props.store(fos, "CloudBees SDK config")
            fos.close()
/*
            userConfigFile.withWriter { writer ->
                config.writeTo(writer)
            }
*/
        }
    
    //    load the project bees.config file
        File dir = baseDir
        File projectConfigFile;
        while(dir != null && !dir.path.equals(getUserHome()) && !projectConfigFile)
        {
            File projDir = new File(dir, ".bees");

            // For compatibility with old stax projects
            if(!projDir.exists())
              projDir = new File(dir, ".stax");

            if(projDir.exists())
            {
                projectConfigFile = new File(projDir, "bees.config");

                // For compatibility with old stax projects
                if(!projectConfigFile.exists()) {
                  projectConfigFile = new File(projDir, "stax.config");
                }

                if(projectConfigFile.exists())
                {
                    def slurper = new ConfigSlurper()
                    slurper.binding = [configContextDir:dir.absolutePath]
                    def projConfig = slurper.parse(projectConfigFile.toURL())

                    // If projectConfigFile is an old stax one, add the tag "bees"
                    def stax = projConfig.get("stax")
                    if (stax != null) {
                      projConfig.setProperty("bees", stax)
                    }

                    config = config.merge(projConfig);
                }
            }
            dir = dir.parentFile
        }
        //println "bees project: " + config.bees.project.basedir
        //println "basedir: " + config.basedir
        // println "config:" + config

        def scriptDirs = [new File(getUserHome(), ".bees/scripts"), new File(staxHome, "scripts")]

        // For backward compatibility with stax groovy scripts
        if(config.stax.project.basedir != [:])
        {
            scriptDirs.add(0, new File(config.stax.project.basedir, ".stax/scripts"))
        }

        if(config.bees.project.basedir != [:])
        {
            scriptDirs.add(0, new File(config.bees.project.basedir, ".bees/scripts"))
        } else {
          config.bees.project.basedir = "."
          if(new File("stax-build.xml").exists())
          {
              //HACK to support really old Stax beta projects
              config.bees.project.basedir = new File(".").canonicalPath
              config.bees.project.ant.file = "stax-build.xml"
              config.bees.project.staxappxml= "conf/stax-application.xml"
              println "WARNING: This project was created with a previous version of the Stax SDK and needs to be upgraded."
          }
        }

        try{
            def target = null;
            if(subcommand == "help")
            {
                target = "help"
                subcommand = subargs[0]
                subargs.remove(0)
            }

            runScript(subcommand, scriptDirs, config, subargs, target)
        }
        catch(Exception e)
        {
            println "ERROR: " + e.message
            if(showErrorDetails)
            {
                def currError = e;
                while(currError != null)
                {
                    currError.printStackTrace();
                    def nextError = e.cause
                    currError = currError != nextError ? nextError : null;
                }
            }
        }
    }
    
    def runScript(String scriptName, scriptDirs, config, args, target)
    {
        scriptName = scriptName.replaceAll("-", "").replace(":", "/");

        def runScript = true
        def basedir = config.basedir != [:] ? "${config.basedir}" : new File(".").canonicalPath
        scriptDirs.each{ scriptDir ->
            if(runScript)
            {
                File gantFile = new File(scriptDir, "${scriptName}.groovy")
                File antFile = new File(scriptDir, "${scriptName}.xml")
                if(gantFile.exists())
                {
                    println "running: ${gantFile.canonicalPath}"
                    runScript = false;
                    def binding = new Binding();
                    binding.setVariable("beesConfig", config)

                    // For backward compatibility with stax groovy scripts
                    if(config.stax.project.basedir != [:])
                      binding.setVariable("staxConfig", config)

                    binding.setVariable("args", args)
                    binding.setVariable("basedir", basedir)
                    def gant = this.class.classLoader.rootLoader.loadClass("gant.Gant")
                        .newInstance(new GantBinding(binding))

                    //gant.loadScript(new FileInputStream(gantFile))
                    gant.loadScript(gantFile)
                    if(target)
                        gant.processTargets(target)
                    else
                        gant.processTargets()               
                }
                else if(antFile.exists())
                {
                    println "running: ${antFile}"
                    runScript = false;
                    def antProperties = config.toProperties("beesConfig")
                    new AntBuilder().ant(antfile: "${antFile}",
                        dir: "${basedir}",
                        inheritAll:"false")
                        {
                            antProperties.each{ entry->
                                property(name: entry.key, value: entry.value)
                            }
                        }
                }
            }
        }
        if(runScript)
        {
            //the script was not found
            throw new Exception("No such plugin script: " + scriptName);
        }
    }
    
    private static boolean checkVersion(currVersion, repos)
    {       
        return true;       
    }

    private static String getUserHome()
    {       
	//println System.properties['user.home']
	String path = System.properties['user.home'];
	if(path.contains("vmware"))
		return "C:\\";
        return System.properties['user.home'];      
    }
    
    private static float strVersionToNumeric(ver)
    {
        def numbers = ver.split(/\./)
        float number = Float.parseFloat(numbers[0]) + Float.parseFloat(numbers[1])/100 + Float.parseFloat(numbers[2])/10000;
        return number
    }
}
