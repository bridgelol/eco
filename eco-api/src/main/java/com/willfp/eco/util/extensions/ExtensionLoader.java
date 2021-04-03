package com.willfp.eco.util.extensions;

import java.util.Set;

/**
 * Interface for extension loader.
 * Some external plugins may modify extension loading for internal server purposes.
 */
public interface ExtensionLoader {

    /**
     * Load all extensions.
     */
    void loadExtensions();

    /**
     * Unload all loaded extensions.
     */
    void unloadExtensions();

    /**
     * Retrieve a set of all loaded extensions.
     *
     * @return An {@link Set<Extension>} of all loaded extensions.
     */
    Set<Extension> getLoadedExtensions();
}