package com.google.code.jtraci;

/**
 * TODO: docuemnt
 *
 * @author DL
 */
public interface TraciCommand {
    public byte getId();
    public byte[] serializeContent();
}
