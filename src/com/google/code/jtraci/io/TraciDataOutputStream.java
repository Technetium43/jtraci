package com.google.code.jtraci.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Extends java.io.DataOutputStream with TraCI-specific serializers.
 * @author DL
 */
public class TraciDataOutputStream extends DataOutputStream {
    /**
     * Creates a TraciDataOutputStream that uses the specified underlying OutputStream.
     */
    public TraciDataOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * Writes a TraCI-serialized String.
     * @param v   The String to write.
     * @throws IOException if an I/O error occurs.
     */
    public void writeTraciString(String v) throws IOException {
        writeInt(v.length());
        writeBytes(v);
    }
}