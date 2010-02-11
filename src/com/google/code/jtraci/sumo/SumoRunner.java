package com.google.code.jtraci.sumo;

import com.google.code.jtraci.TraciClient;
import com.google.code.jtraci.io.StreamGobbler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Runs the SUMO (Simulation of Urban MObility) application.
 * @author DL
 */
public class SumoRunner {
    
    public SumoRunner(String sumoPath, String configPath, int port) {
        this.sumoPath = sumoPath;
        this.configPath = configPath;
        this.port = port;
    }

    public void start() throws IOException {
        if (sumoProcess != null) {
            throw new IllegalStateException("SUMO already running");
        }
        
        sumoProcess = Runtime.getRuntime().exec(new String[] {
            //"cmd.exe", "/C", "start",
            sumoPath, "--configuration-file", configPath, "--remote-port", Integer.toString(port)
        });
        
        errorGobbler = new StreamGobbler(sumoProcess.getErrorStream());
        outputGobbler = new StreamGobbler(sumoProcess.getInputStream());
        errorGobbler.start();
        outputGobbler.start();
    }

    public TraciClient getClient() throws IOException {
        // SUMO not running?
        if (sumoProcess == null) {
            throw new IllegalStateException("SUMO not running");
        }

        // TraCI client previously created?
        if (client != null) {
            return client;
        }

        // Create TraCI client
        socket = new Socket(InetAddress.getLocalHost(), port);
        return new TraciClient(socket.getInputStream(), socket.getOutputStream());
    }

    public void stop() {
        if (sumoProcess != null) {
            client = null;
            if (socket != null) {
                try {
                    socket.close();
                } catch(IOException e) {}
            }

            sumoProcess.destroy();

            try {
                sumoProcess.waitFor();
                outputGobbler.join();
                errorGobbler.join();
            } catch (InterruptedException e) {}
            
            sumoProcess = null;
        }
    }

    private Process sumoProcess;

    private final String sumoPath;
    private final String configPath;
    private final int port;

    private StreamGobbler outputGobbler;
    private StreamGobbler errorGobbler;

    private Socket socket;
    private TraciClient client;
}
