package com.icyeasy.easybreeding;

import net.fabricmc.api.ModInitializer;

/**
 * Main mod class. Initialization is not needed for the core functionality,
 * since behavior is injected via mixin.
 */
public class EasyBreedingMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // No init logic needed; mixin handles animal feeding behavior.
    }
}
