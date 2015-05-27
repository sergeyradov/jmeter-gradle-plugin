package com.github.kulya.gradle.plugins.jmeter;

import org.gradle.api.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JmeterPluginConvention {

    /**
     * Path to a Jmeter tests XML file.
     * Relative to srcDir.
     * May be declared instead of the parameter includes.
     */
    private List<File> jmeterTestFiles;

    /**
     * Directory under which JMeter test XML files are stored.
     * <p/>
     * By default it src/test/jmeter
     */
    private File srcDir;

    /**
     * Sets the list of include patterns to use in directory scan for JMeter Test XML files.
     * Relative to srcDir.
     * May be declared instead of a single jmeterTestFile.
     * Ignored if parameter jmeterTestFile is given.
     */
    private List<String> includes;

    /**
     * Sets the list of exclude patterns to use in directory scan for Test files.
     * Relative to srcDir.
     * Ignored if parameter jmeterTestFile file is given.
     */
    private List<String> excludes;

    /**
     * Directory in which the reports are stored.
     * <p/>
     * By default build/jmeter-report"
     */
    private File reportDir;

    /**
     * Whether or not to generate reports after measurement.
     * <p/>
     * By default true
     */
    private Boolean enableReports = true;

    /**
     * Whether or not to generate extended reports after measurement.
     * <p/>
     * By default false
     */
    private Boolean enableExtendedReports = false;


    /**
     * Use remote JMeter installation to run tests
     * <p/>
     * By default false
     */
    private Boolean remote = false;

    /**
     * Sets whether ErrorScanner should ignore failures in JMeter result file.
     * <p/>
     * By default false
     */
    private Boolean jmeterIgnoreFailure = false;

    /**
     * Sets whether ErrorScanner should ignore errors in JMeter result file.
     * <p/>
     * By default false
     */
    private Boolean jmeterIgnoreError = false;

    /**
     * Postfix to add to report file.
     * <p/>
     * By default "-report.html"
     */
    private String reportPostfix = "-report.html";

     /**
     * Custom Xslt which is used to create the report.
     */
    private File reportXslt;

    private List<String> jmeterUserProperties;

    private List<String> jmeterPluginJars = pluginList();

    private File jmeterPropertyFile;

    /**
     * The maximum heap size for the forked findbugs process (ex: '1g').
     */
    private String maxHeapSize = "512M";

    /**
     * Path to a Jmeter XML file which will be edited. This is for jmeterEditor task
     * Relative to srcDir.
     */
    private File jmeterEditFile;

    /**
     * Paths to user properties files
     */
    private List<File> jmeterUserPropertiesFiles;

    public static final String JMETER_DEFAULT_PROPERTY_NAME = "jmeter.properties";

    public JmeterPluginConvention(Project project) {
        reportDir = new File(project.getBuildDir(), "jmeter-report");
        srcDir = new File(project.getProjectDir(), "src/test/jmeter");
        jmeterPropertyFile = new File(srcDir.getAbsolutePath() + File.separator + JMETER_DEFAULT_PROPERTY_NAME);
    }

    public List<File> getJmeterTestFiles() {
        return jmeterTestFiles;
    }

    public void setJmeterTestFile(List<File> jmeterTestFiles) {
        this.jmeterTestFiles = jmeterTestFiles;
    }

    public File getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public File getReportDir() {
        return reportDir;
    }

    public void setReportDir(File reportDir) {
        this.reportDir = reportDir;
    }

    public Boolean getEnableReports() {
        return enableReports;
    }

    public void setEnableReports(boolean enableReports) {
        this.enableReports = enableReports;
    }

    public boolean getEnableExtendedReports() {
        return enableExtendedReports;
    }

    public void setEnableExtendedReports(Boolean enableExtenededReports) {
        this.enableExtendedReports = enableExtenededReports;
    }

    public Boolean getRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public Boolean getJmeterIgnoreFailure() {
        return jmeterIgnoreFailure;
    }

    public void setJmeterIgnoreFailure(boolean jmeterIgnoreFailure) {
        this.jmeterIgnoreFailure = jmeterIgnoreFailure;
    }

    public Boolean getJmeterIgnoreError() {
        return jmeterIgnoreError;
    }

    public void setJmeterIgnoreError(boolean jmeterIgnoreError) {
        this.jmeterIgnoreError = jmeterIgnoreError;
    }

    public String getReportPostfix() {
        return reportPostfix;
    }

    public void setReportPostfix(String reportPostfix) {
        this.reportPostfix = reportPostfix;
    }

    public File getReportXslt() {
        return reportXslt;
    }

    public void setReportXslt(File reportXslt) {
        this.reportXslt = reportXslt;
    }

    public List<String> getJmeterUserProperties() {
        return jmeterUserProperties;
    }

    public void setJmeterUserProperties(List<String> jmeterUserProperties) {
        this.jmeterUserProperties = jmeterUserProperties;
    }

    public List<String> getJmeterPluginJars() {
        return jmeterPluginJars;
    }

    public void setJmeterPluginJars(List<String> jmeterPluginJars) {

        this.jmeterPluginJars = pluginList();
        this.jmeterPluginJars.addAll(jmeterPluginJars);
    }

    public File getJmeterPropertyFile() {
        return this.jmeterPropertyFile;
    }

    public void setJmeterPropertyFile(File jmeterPropertyFile) {
        this.jmeterPropertyFile = jmeterPropertyFile;
    }

    public String getMaxHeapSize() {
        return maxHeapSize;
    }

    public void setMaxHeapSize(String maxHeapSize) {
        this.maxHeapSize = maxHeapSize;
    }

    public File getJmeterEditFile() {
        return jmeterEditFile;
    }

    public void setJmeterEditFile(File jmeterEditFile) {
        this.jmeterEditFile = jmeterEditFile;
    }

    public List<File> getJmeterUserPropertiesFiles() {
        return jmeterUserPropertiesFiles;
    }

    public void setJmeterUserPropertiesFiles(List<File> jmeterUserPropertiesFiles) {
        this.jmeterUserPropertiesFiles = jmeterUserPropertiesFiles;
    }

    private List<String> pluginList() {
        // These come from the build.gradle
        return new ArrayList<String>
                (Arrays.asList(
                "jmeter-plugins-1.2.1.jar",
                "jmeter-plugins-standard-1.2.1.jar",
                "jmeter-plugins-common-1.2.1.jar",
                "jmeter-plugins-extras-1.2.1.jar"));

    }
}

