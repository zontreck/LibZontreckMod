package dev.zontreck.libzontreck.lore;

public enum LoreType {
    UNKNOWN((byte) 0x00),
    STATS((byte) 0x01),
    ORIGINAL_CRAFTER((byte)0x02);

    private final byte type;

    private LoreType(byte Option) {
        type = Option;
    }

    public static LoreType valueOf(byte b) {
        LoreType _T = LoreType.UNKNOWN;
        _T = values()[b];

        return _T;
    }

    public byte get()
    {
        return type;
    }
}
