package com.google.code.jtraci.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A TraCI data input stream lets an application read primitive TraCI data types
 * from an underlying input stream in a machine-independent way.
 * @see java.io.DataInputStream
 * 
 * @author DL
 */
public class TraciDataInputStream extends InputStream {
    /**
     * Creates a TraciDataInputStream that uses the specified underlying InputStream.
     * @param in   the specified input stream.
     */
    public TraciDataInputStream(InputStream in) {
        dis = new DataInputStream(in);
    }

    @Override
    public int read() throws IOException {
        return dis.read();
    }

    @Override
    public int available() throws IOException {
        return dis.available();
    }

    @Override
    public void close() throws IOException {
        dis.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        dis.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return dis.markSupported();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return dis.read(b, off, len);
    }

    @Override
    public synchronized void reset() throws IOException {
        dis.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return dis.skip(n);
    }

    public byte readByte() throws IOException {
        return dis.readByte();
    }

    public int readInt() throws IOException {
        return dis.readInt();
    }

    public float readFloat() throws IOException {
        return dis.readFloat();
    }

    public double readDouble() throws IOException {
        return dis.readDouble();
    }

    public String readString() throws IOException {
        int strLen = readInt();
        byte[] rawStr = new byte[strLen];
        dis.readFully(rawStr);
        return new String(rawStr);
    }

    public List<String> readStringList() throws IOException {
        final int numStrings = readInt();
        final List<String> stringList = new ArrayList<String>(numStrings);
        
        for (int i = 0; i < numStrings; ++i) {
            stringList.add(readString());
        }
        return stringList;
    }

    private final DataInputStream dis;
}
