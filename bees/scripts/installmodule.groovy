import groovy.xml.dom.DOMCategory

def staxApiConfig = beesConfig.bees.api
def staxAppConfig = beesConfig.bees.project.app
def staxProjectConfig = beesConfig.bees.project

def cli = new CliBuilder(usage: "bees install-module [options] INSTALL_DIR", parser: new org.apache.commons.cli.GnuParser ());
cli.o(longOpt:'org', argName:'organization', args: 1, 'organization (group) name')
cli.m(longOpt:'module', argName:'module', args: 1, 'module (artifact) name')
cli.v(longOpt:'version', argName:'version', args: 1, 'revision id')

includeTool << gant.tools.Ivy
includeTool << net.stax.sdk.tools.PromptHelper

target(default:"bees install script") {
	def opt = cli.parse(args)
    if(opt)
    {
	    //def group='org.hibernate';
	    //def module='hibernate-annotations'
	    //def version='3.4.0.GA'
	    
	
	    //def group='commons-lang'
	    //def module='commons-lang'
	    //def version='2+'
	    def cmdArgs = opt.getArgs()
    
	    def installPath = cmdArgs ? cmdArgs[0] : promptHelper.prompt("Install path (default is current dir): ", [defaultValue: "."])
	    def group = opt.o ? opt.o : promptHelper.prompt("organization (group) id: ")
	    def module = opt.m ? opt.m : promptHelper.prompt("module (artifact) id: ")
	    def version = opt.v ? opt.v : promptHelper.prompt("version: (default attempts: latest.milestone)", [defaultValue: "latest.milestone"])
	    
	    File defaultIvyConfig = new File(System.properties['bees.home'], "ivy-settings.xml")
        ivy.settings(file:"${defaultIvyConfig.canonicalPath}")
	    
        ivy.resolve (
                organisation: "${group}",
                module:"${module}",
                revision:"${version}",
                inline:"true"
        )
        //use a tmp dir to hold the files while we retrieve the jars 
        def installtmpdir = "${installPath}/tmp"
        for(int i=0; new File(installtmpdir).exists(); i++){
            installtmpdir = "${installPath}/tmp_${i}"
        }
	    try{    
	        ant.mkdir(dir: installtmpdir)
	        
	        ivy.retrieve (
	            pattern: "${installtmpdir}/[artifact]-[revision]_.[ext]",
	            organisation: "${group}",
	            module:"${module}",
	            revision:"${version}",
	            inline:"true"
	        )    
	    
	        ant.move(todir:"${installPath}")
	        {
	            fileset(dir:"${installtmpdir}")
	        }
	        
	        //update the ivy.xml file
	        File ivyFile = new File(installPath, "ivy.xml");    
	        addDependency(ivyFile, group, module, version)
	    }
	    finally
	    {
	        ant.delete(dir: "${installtmpdir}")
	    }
    }
}

def addDependency(File ivyFile, String group, String module, String version)
{
    def builder = groovy.xml.DOMBuilder.newInstance()
    def doc 
    if(ivyFile.exists())
    {
        doc = groovy.xml.DOMBuilder.parse(new FileReader(ivyFile))
    }
    else
    {
        builder = groovy.xml.DOMBuilder.newInstance()
        doc = builder.createDocument();
        def orgName = "example"
        def moduleName = "example"
        if(beesConfig.bees.project.app.id != [:])
        {
            def idList = "${beesConfig.bees.project.app.id}".split('/');
            orgName = idList[0]
            moduleName = idList.length > 1 ? idList[1] : orgName
        }
        def ivyModule = doc.createElement("ivy-module")
        ivyModule.setAttribute("version", "2.0")
        def ivyInfo = doc.createElement("info")
        ivyInfo.setAttribute("organisation", "net.stax.${orgName}")
        ivyInfo.setAttribute("module", "${moduleName}")
        ivyModule.appendChild(ivyInfo)
        def docElement = doc.appendChild(ivyModule)        
    }
    
    boolean ivyModuleUpdated = false;
    def ivyModule = doc.documentElement    
    use(DOMCategory)
    {
        def dependencies = ivyModule.'dependencies'
        if(dependencies.size() == 0)
        {
            dependencies = [ivyModule.appendChild(doc.createElement("dependencies"))]
        }
        
        def existingDependencies = dependencies.'dependency'.findAll{            
            return it.nodeName=="dependency" &&
            it.'@org'==group &&
            it.'@name'==module &&
            it.'@revision'==version            
        }        
        if(existingDependencies.size() == 0)
        {
            def dependency = dependencies[0].appendNode('dependency', [org:group, name:module, rev:version])
            ivyModuleUpdated = true
        }
    }
    
    if(ivyModuleUpdated)
    {
        ivyFile.withOutputStream{ out->
            groovy.xml.XmlUtil.serialize(doc.documentElement, out)
        }
    }
}

target(help:"usage") {
    println "Install modules from IVY and Maven repositories"
    cli.usage()
    println "tip: search for installable modules at http://www.mvnrepository.com/"
}
