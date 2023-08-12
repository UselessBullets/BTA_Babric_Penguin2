package useless.penguinmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.entity.ChickenRenderer;
import net.minecraft.client.render.model.ModelChicken;
import net.minecraft.client.render.model.ModelCow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.EntityHelper;
import useless.penguinmod.entity.EntityPenguin;
import useless.penguinmod.model.ModelPenguin;
import useless.penguinmod.model.PenguinRenderer;


public class PenguinMod implements ModInitializer {
    static {
        //this is here to possibly fix some class loading issues, do not delete
        try {
            Class.forName("net.minecraft.core.block.Block");
            Class.forName("net.minecraft.core.item.Item");
        } catch (ClassNotFoundException ignored) {}
    }
    public static final String MOD_ID = "penguinmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    @Override
    public void onInitialize() {
        LOGGER.info("PenguinMod initialization started.");
        EntityHelper.createEntity(EntityPenguin.class, new PenguinRenderer(new ModelPenguin(), .25F), 200, "Penguin");
        LOGGER.info("PenguinMod initialized.");
    }
}
