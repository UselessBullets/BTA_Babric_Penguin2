package useless.penguinmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.entity.CreeperRenderer;
import net.minecraft.core.entity.monster.EntityCreeper;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.EntityHelper;


public class PenguinMod implements ModInitializer {
    public static final String MOD_ID = "penguinmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("PenguinMod initialization started.");
        //HalpLibe.LOGGER.info("test");
        EntityHelper.createEntity(EntityCreeper.class, new CreeperRenderer(), 200, "Penguin");
        LOGGER.info("PenguinMod initialized.");
    }
}
