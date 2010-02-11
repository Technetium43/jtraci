package com.google.code.jtraci.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * A StreamGobbler is a worker thread that constantly consumes input from an
 * InputStream.
 *
 * @see http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
 *
 * @author DL
 */
public class StreamGobbler extends Thread {
    public StreamGobbler(InputStream is) {
        this.is = is;
    }
    
    public void run() {
        byte[] buffer = new byte[32];
        try {
            while (is.read(buffer) != -1) {}
        } catch (IOException e) {}
    }
    
    private InputStream is;
}
