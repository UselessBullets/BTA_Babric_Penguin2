package useless.penguinmod.model;

import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.model.ModelBase;
import useless.penguinmod.entity.EntityPenguin;

public class PenguinRenderer extends LivingRenderer<EntityPenguin> {
    public PenguinRenderer(ModelBase modelbase, float f) {
        super(modelbase, f);
    }

    public void renderCow(EntityPenguin entity, double d, double d1, double d2, float f, float f1) {
        super.doRenderLiving(entity, d, d1, d2, f, f1);
    }

    public void doRenderLiving(EntityPenguin entity, double x, double y, double z, float yaw, float renderPartialTicks) {
        this.renderCow(entity, x, y, z, yaw, renderPartialTicks);
    }

    public void doRender(EntityPenguin entity, double x, double y, double z, float yaw, float renderPartialTicks) {
        this.renderCow(entity, x, y, z, yaw, renderPartialTicks);
    }
}
