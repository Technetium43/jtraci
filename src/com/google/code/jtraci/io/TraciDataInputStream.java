package com.google.code.jtraci.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Extends java.io.DataInputStream with TraCI-specific deserializers.
 * @author DL
 */
public class TraciDataInputStream extends DataInputStream {

    /**
     * Creates a TraciDataInputStream that uses the specified underlying InputStream.
     */
    public TraciDataInputStream(InputStream in) {
        super(in);
    }

    /**
     * Reads a TraCI-serialized String.
     * @return the String value read.
     * @throws IOException if an I/O error occurs.
     */
    public String readTraciString() throws IOException {
        int strLen = readInt();
        byte[] rawStr = new byte[strLen];
        readFully(rawStr);
        return new String(rawStr);
    }

    /**
     * Reads a TraCI-serialized StringList.
     * @return the StringList value read.
     * @throws IOException if an I/O error occurs.
     */
    public List<String> readTraciStringList() throws IOException {
        List<String> stringList = new ArrayList<String>();
        int numStrings = readInt();
        for (int i = 0; i < numStrings; ++i) {
            stringList.add(readTraciString());
        }
        return stringList;
    }
}
