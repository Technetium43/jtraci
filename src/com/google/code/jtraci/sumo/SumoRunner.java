package com.google.code.jtraci.sumo;

import java.io.IOException;

/**
 * Runs the SUMO (Simulation of Urban MObility) application.
 * @author DL
 */
public class SumoRunner {

    public SumoRunner(String sumoPath, String networkPath, int port) {
        this.sumoPath = sumoPath;
        this.networkPath = networkPath;
        this.port = port;
    }

    public void start() throws IOException {
        if (sumoProcess != null) {
            throw new IllegalStateException("SUMO already running");
        }
        
        sumoProcess = Runtime.getRuntime().exec(new String[] {
            //"cmd.exe", "/C", "start",
            sumoPath, "--net-file", networkPath, "--remote-port", Integer.toString(port)
        });
    }

    public void stop() {
        if (sumoProcess != null) {
            sumoProcess.destroy();
            try {
                sumoProcess.waitFor();
            } catch (InterruptedException e) {}
            sumoProcess = null;
        }
    }

    private Process sumoProcess;

    private final String sumoPath;
    private final String networkPath;
    private final int port;
}
