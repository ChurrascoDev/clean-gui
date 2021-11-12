package com.github.imthenico.cleangui.util;

import org.bukkit.Bukkit;

public enum Version {

    v1_8(1.8),
    v1_9(1.9),
    v1_10(1.10),
    v1_11(1.11),
    v1_12(1.12),
    v1_13(1.13),
    v1_14(1.14),
    v1_15(1.15),
    v1_16(1.16),
    v1_17(1.17);

    private final double numberReference;

    public static final Version RUNNING_VERSION;
    public static final String revision;

    static {
        revision = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        StringBuilder builder = new StringBuilder();

        String[] splited = revision.split("_");

        builder.append(splited[0]).append("_").append(splited[1]);

        RUNNING_VERSION = valueOf(builder.toString());
    }

    Version(double numberReference) {
        this.numberReference = numberReference;
    }

    public double getNumberReference() {
        return numberReference;
    }

    public boolean isMajorThan(Version version) {
        return numberReference > version.numberReference;
    }

    public boolean equals(Version version) {
        return numberReference == version.numberReference;
    }

}