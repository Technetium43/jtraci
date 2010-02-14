package com.google.code.jtraci.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * A TraCI data output stream lets an application write primitive TraCI data
 * types to an output stream in a portable way. An application can then use a
 * TraCI data input stream to read the data back in.
 * @see java.io.DataOutputStream
 * 
 * @author DL
 */
public class TraciDataOutputStream extends OutputStream {
    /**
     * Creates a new TraCI data output stream to write data to the specified
     * underlying output stream.
     * @param out   the underlying output stream, to be saved for later use.
     */
    public TraciDataOutputStream(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    @Override
    public void write(int b) throws IOException {
        dos.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        dos.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        dos.close();
    }

    @Override
    public void flush() throws IOException {
        dos.flush();
    }

    public void writeByte(int v) throws IOException {
        dos.writeByte(v);
    }

    public void writeInt(int v) throws IOException {
        dos.writeInt(v);
    }

    public void writeFloat(float v) throws IOException {
        dos.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        dos.writeDouble(v);
    }

    public void writeString(String v) throws IOException {
        writeInt(v.length());
        dos.writeBytes(v);
    }

    public void writeStringList(List<String> v) throws IOException {
        writeInt(v.size());
        for (String s : v) {
            writeString(s);
        }
    }

    private final DataOutputStream dos;
}