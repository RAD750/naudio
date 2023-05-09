/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.Configuration
 *  net.minecraftforge.common.Property
 */
package naudio.config;

import net.minecraftforge.common.Configuration;

import java.io.File;

public class ConfigHandler {
    public static void init(File file) {
        Configuration config = new Configuration(file);
        
        config.load();

        config.save();
    }
}
