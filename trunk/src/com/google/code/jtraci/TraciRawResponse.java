package com.google.code.jtraci;

/**
 * A serialized TraCI response.
 * @author DL
 */
public class TraciRawResponse {
    public TraciRawResponse(byte id, byte[] content) {
        this.id = id;
        this.content = content;
    }

    public byte getId() {
        return id;
    }

    public byte[] getContent() {
        return content;
    }

    private final byte id;
    private final byte[] content;
}
