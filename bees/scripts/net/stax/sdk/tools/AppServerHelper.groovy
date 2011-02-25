package net.stax.sdk.tools
import org.codehaus.gant.GantBinding;
import com.staxnet.ant.ModuleDownloader;

public class AppServerHelper
{
    private GantBinding binding;
    AppServerHelper(GantBinding binding)
    {
        this.binding = binding;
    }
    
    public void runWar(Map opts = [:])
    {
        def webroot = opts.web
        def dir = opts.workingDir
        def maxMemory = opts.maxMemory ? opts.maxMemory : 256
        def debugPort = opts.debugPort
        def port = opts.port ? opts.port : 8080;
        def env = opts.env ? opts.env : null;
        
        URLClassLoader cl = createAppServerClassLoader()
        
        //execute the appserver's main method
        def appServerClass = cl.loadClass("com.staxnet.appserver.StaxSdkAppServer")
        def main = appServerClass.getMethod("main", String[].class)
        String[] mainArgs = (String[])constructAppServerArgs("${webroot}", "${dir}", port, env)
        main.invoke(null, (Object[])[mainArgs])
    }

    public void forkWar(Map opts = [:])
    {
        def webroot = opts.web
        def dir = opts.workingDir
        def maxMemory = opts.maxMemory ? opts.maxMemory : 256
        def debugPort = opts.debugPort
        def port = opts.port ? opts.port : 8080;
        def env = opts.env ? opts.env : null;
        
        def serverArgs = constructAppServerArgs("${webroot}", "${dir}", port, env)
        
        binding.ant.java(classname: "com.staxnet.appserver.StaxSdkAppServer", fork:true, spawn:false)
        {
            if(debugPort)
            {
                println "Running server debug on port ${debugPort}"
                jvmarg(value: "-Xdebug")
                jvmarg(value: "-Xnoagent")
                jvmarg(value: "-Djava.compiler=NONE")
                jvmarg(value: "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=${debugPort}")
            }
            
            jvmarg(value: "-Xmx${maxMemory}M")
            
            serverArgs.each({
                arg(value: it)
            })

            classpath{
                fileset(dir: "${System.getProperty('bees.home')}/lib", includes: "*.jar")
            }
        }
    }
    
    def constructAppServerArgs(webroot, workingDir, port, env)
    {
        def args = []
        
        if(webroot)
            args += ["-web", "${webroot}"]
        if(workingDir)
            args += ["-dir", "${workingDir}"]
        if(port)
            args += ["-port", port]
        if(env)
            args += ["-env", env]
        return args
    }
    
    /**
     * Creates a clean classloader that only includes the core appserver jars and the System JVM classes.
     * Note: This prevents the Groovy and SDK classes from contaminating the appserver (which could cause locally run
     * apps to behave differently than the deployed app, which would be very uncool)
     */
    private ClassLoader createAppServerClassLoader()
    {
        def clUrls = [];
        def libdir = new File(System.getProperty("bees.home"), 'lib')
        libdir.eachFileMatch(~/.*\.jar$/){
            clUrls.add(it.toURL())
        }
        
        //get the very root system class loader so that groovy classes are not included
        def classLoader = ClassLoader.systemClassLoader
        while (classLoader.parent) {
            classLoader = classLoader.parent
        }
        
        URLClassLoader cl = new URLClassLoader(clUrls.toArray(new URL[0]), classLoader)
        return cl
    }
}