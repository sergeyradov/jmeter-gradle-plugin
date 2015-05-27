package com.github.kulya.gradle.plugins.jmeter

import org.apache.commons.io.IOUtils
import org.gradle.api.GradleException
import org.gradle.api.internal.ConventionTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction

abstract class JmeterAbstractTask extends ConventionTask {

    protected final Logger log = Logging.getLogger(getClass());

    private String jmeterVersion;

    private List<String> jmeterPluginJars = pluginList();
    private File workDir;

    private File jmeterLog;

    private List<String> jmeterUserProperties;

    /**
     * Directory under which JMeter test XML files are stored.
     * <p/>
     * By default it src/test/jmeter
     */
    private File srcDir;

    private File jmeterPropertyFile;

    private boolean propertyFileChanged;

    private String maxHeapSize

    private List<File> jmeterUserPropertiesFiles;

    @TaskAction
    public void start() {
        loadJMeterVersion();

        loadPropertiesFromConvention();

        initJmeterSystemProperties();

        createJMeterDirectories()

        runTaskAction();
    }

    protected abstract void runTaskAction() throws IOException;

    def createJMeterDirectories() {
        def jmeterJUnitFolder = new File(workDir, "lib" + File.separator + "junit")
        jmeterJUnitFolder.mkdirs()

        def jmeterExtFolder = new File(workDir, "lib" + File.separator + "ext")
        jmeterExtFolder.mkdirs()
    }

    private List<String> pluginList() {
        // These come from the build.gradle
        // These come from the build.gradle
        return new ArrayList<String>
                (Arrays.asList(
                        "jmeter-plugins-1.2.1.jar",
                        "jmeter-plugins-standard-1.2.1.jar",
                        "jmeter-plugins-common-1.2.1.jar",
                        "jmeter-plugins-extras-1.2.1.jar"));
    }

    protected void loadPropertiesFromConvention() {
        jmeterPropertyFile = getJmeterPropertyFile()
        jmeterPluginJars = getJmeterPluginJars()
        jmeterUserProperties = getJmeterUserProperties()
        maxHeapSize = getMaxHeapSize()
        srcDir = getSrcDir()
    }


    protected void loadJMeterVersion() {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("jmeter-plugin.properties")
            Properties pluginProps = new Properties()
            pluginProps.load(is)
            jmeterVersion = pluginProps.getProperty("jmeter.version", null)
            if (jmeterVersion == null) {
                throw new GradleException("You should set correct jmeter.version at jmeter-plugin.properies file")
            }
        } catch (Exception e) {
            log.error("Can't load JMeter version, build will stop", e)
            throw new GradleException("Can't load JMeter version, build will stop", e)
        }
    }

    protected void initJmeterSystemProperties() throws IOException {
        workDir = new File(getProject().getBuildDir(), "/jmeter")
        workDir.mkdirs()


        jmeterLog = new File(workDir, "jmeter.log")
        try {
            System.setProperty("log_file", jmeterLog.getCanonicalPath())
        } catch (IOException e) {
            throw new GradleException("Can't get canonical path for log file", e)
        }

        System.setProperty("jmeter.home", workDir.getCanonicalPath())
        log.info("jmeter home is set ot " + System.getProperty("jmeter.home"))
        initTempProperties()
        resolveJmeterSearchPath()

    }

    protected void resolveJmeterSearchPath() {
        StringBuilder cp = new StringBuilder()
        URL[] classPath = ((URLClassLoader) this.getClass().getClassLoader()).getURLs()
        String jmeterVersionPattern = getJmeterVersion().replaceAll("[.]", "[.]")
        String pathSeparator = /**System.getProperty("path.separator")*/
                ";"
        for (URL dep : classPath) {
            if (dep.getPath().matches("^.*org[./]apache[./]jmeter[/]ApacheJMeter.*" +
                    jmeterVersionPattern + ".jar\$")) {
                cp.append(dep.getPath())
                cp.append(pathSeparator)
            } else if (dep.getPath().matches("^.*bsh.*[.]jar\$")) {
                cp.append(dep.getPath())
                cp.append(pathSeparator)
            } else if (jmeterPluginJars != null) {
                for (String plugin : jmeterPluginJars) {
                    if (dep.getPath().matches("^.*" + plugin + "\$")) {
                        cp.append(dep.getPath())
                        cp.append(pathSeparator)
                    }
                }
            }
        }
        cp.append(new File(workDir, "lib" + File.separator + "ext").getCanonicalPath())
        System.setProperty("search_paths", cp.toString());
        log.debug("Search path is set to " + System.getProperty("search_paths"))
    }

    protected void initTempProperties() throws IOException {
        List<File> tempProperties = new ArrayList<File>();

        File saveServiceProperties = new File(workDir, "saveservice.properties");
        System.setProperty("saveservice_properties", "/" + saveServiceProperties.getName());
        tempProperties.add(saveServiceProperties);
        log.debug("saveservice_properties location is " + System.getProperty("saveservice_properties"))

        File upgradeProperties = new File(workDir, "upgrade.properties");
        System.setProperty("upgrade_properties", "/" + upgradeProperties.getName());
        tempProperties.add(upgradeProperties);

        for (File f : tempProperties) {
            try {
                FileWriter writer = new FileWriter(f);
                IOUtils.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream(f.getName()), writer);
                writer.flush();
                writer.close();
            } catch (IOException ioe) {
                throw new GradleException("Couldn't create temporary property file " + f.getName() + " in directory " + workDir.getPath(), ioe);
            }

        }
    }

    protected void initUserProperties(List<String> jmeterArgs) {
        if (jmeterUserProperties != null) {
            jmeterUserProperties.each { property -> jmeterArgs.add("-J" + property) }
        }
    }

    String getJmeterVersion() {
        return jmeterVersion
    }

    void setJmeterVersion(String jmeterVersion) {
        this.jmeterVersion = jmeterVersion
    }

    List<String> getJmeterPluginJars() {
        return jmeterPluginJars
    }

    void setJmeterPluginJars(List<String> jmeterPluginJars) {
        this.jmeterPluginJars = pluginList();
        this.jmeterPluginJars.addAll(jmeterPluginJars);
    }

    File getWorkDir() {
        return workDir
    }

    void setWorkDir(File workDir) {
        this.workDir = workDir
    }

    File getJmeterLog() {
        return jmeterLog
    }

    void setJmeterLog(File jmeterLog) {
        this.jmeterLog = jmeterLog
    }

    List<String> getJmeterUserProperties() {
        return jmeterUserProperties
    }

    void setJmeterUserProperties(List<String> jmeterUserProperties) {
        this.jmeterUserProperties = jmeterUserProperties
    }

    File getSrcDir() {
        return srcDir
    }

    void setSrcDir(File srcDir) {
        this.srcDir = srcDir
        if (!propertyFileChanged) {
            setJmeterPropertyFile(new File(srcDir, JmeterPluginConvention.JMETER_DEFAULT_PROPERTY_NAME));
        }
    }

    File getJmeterPropertyFile() {
        this.jmeterPropertyFile
    }

    void setJmeterPropertyFile(File jmeterPropertyFile) {
        this.jmeterPropertyFile = jmeterPropertyFile
        propertyFileChanged = true
    }

    String getMaxHeapSize() {
        return maxHeapSize
    }

    void setMaxHeapSize(String maxHeapSize) {
        this.maxHeapSize = maxHeapSize
    }

    public List<File> getJmeterUserPropertiesFiles() {
        return jmeterUserPropertiesFiles;
    }

    public void setJmeterUserPropertiesFiles(List<File> jmeterUserPropertiesFiles) {
        this.jmeterUserPropertiesFiles = jmeterUserPropertiesFiles;
    }
}
