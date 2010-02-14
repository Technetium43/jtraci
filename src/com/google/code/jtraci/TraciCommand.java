package com.google.code.jtraci;

/**
 * TODO: docuemnt
 *
 * @author DL
 */
public interface TraciCommand {
    public byte getId();
    public byte[] serializeContent();

    // @see http://sourceforge.net/apps/mediawiki/sumo/index.php?title=TraCI/Protocol#Atomar_Types
    public static final byte TraciUbyte = 0x07;
    public static final byte TraciByte = 0x08;
    public static final byte TraciInteger = 0x09;
    public static final byte TraciFloat = 0x0A;
    public static final byte TraciDouble = 0x0B;
    public static final byte TraciString = 0x0C;
    public static final byte TraciStringList = 0x0E;
    public static final byte TraciCompound = 0x0F;
}
