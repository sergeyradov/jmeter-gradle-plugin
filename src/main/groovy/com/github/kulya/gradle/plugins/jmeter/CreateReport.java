package com.github.kulya.gradle.plugins.jmeter;

import kg.apc.cmdtools.ReporterTool;
import org.apache.commons.io.FilenameUtils;
import org.apache.jmeter.util.JMeterUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 * A Simple wrapper around the reporter tool that will generate the graphs for all the plugin types.
 * It will configure the reporter tool so that it generates a csv & png.
 */
public class CreateReport extends ReporterTool {
    public static final List<String> pluginTypes = Arrays.asList(
            "ResponseTimesOverTime",
            "HitsPerSecond",
            "BytesThroughputOverTime",
            "LatenciesOverTime",
            "ResponseCodesPerSecond",
            "TransactionsPerSecond",
            "ResponseTimesDistribution",
            "ResponseTimesPercentiles",
            "ThreadsStateOverTime",
            "TimesVsThreads",
            "ThroughputVsThreads"
    );

    private final ListIterator mArg;

    public CreateReport(List<String> args) {
        mArg = (new LinkedList(args)).listIterator();
    }

    public void run() {
        this.processParams(mArg);
    }

    public static void main(String args[]) {
        // Initialize the JMeter settings..
        JMeterUtils.setJMeterHome(System.getProperty("jmeter.home"));
        JMeterUtils.loadJMeterProperties(System.getProperty("jmeter.properties"));
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();

        String resultFile = args[0];
        String name = FilenameUtils.removeExtension(resultFile);

        for (String plugin : pluginTypes) {
            List<String> reportArgs = Arrays.asList(
                    "--generate-png",
                    name + "-" + plugin + ".png",
                    "--generate-csv",
                    name + "-" + plugin + ".csv",
                    "--plugin-type",
                    plugin,
                    "--input-jtl",
                    resultFile);
            new CreateReport(reportArgs).run();
        }
    }
}