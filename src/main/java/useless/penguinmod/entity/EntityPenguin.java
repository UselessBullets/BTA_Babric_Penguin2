package useless.penguinmod.entity;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class EntityPenguin extends EntityPet {
    public float flapAngle = 0.0F;
    public float destPos = 0.0F;
    public float oldDestPos;
    public float oldFlapAngle = 0.0F;
    public float flapRate = 1.0F;

    private boolean sliding = false;

    public EntityPenguin(World world) {
        super(world);
        this.tameItemID = Item.foodFishRaw.id;
        this.skinName = "penguin";
        this.highestSkinVariant = -1;
        this.setSize(8f/16f, 14f/16f);
    }
    public String getEntityTexture() {return "/assets/penguinmod/entity/penguin/penguin2.png";}
    public String getDefaultEntityTexture() {
        return "/assets/penguinmod/entity/penguin/penguin2.png";
    }
    protected String getLivingSound() {
        return "mob.chicken";
    }

    protected String getHurtSound() {
        return "mob.chickenhurt";
    }

    protected String getDeathSound() {
        return "mob.chickenhurt";
    }

    @Override
    protected int getDropItemId() {
        return Item.featherChicken.id;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.oldFlapAngle = this.flapAngle;
        this.oldDestPos = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3);
        if (this.destPos < 0.0F) {
            this.destPos = 0.0F;
        }

        if (this.destPos > 1.0F) {
            this.destPos = 1.0F;
        }

        if (!this.onGround && this.flapRate < 1.0F) {
            this.flapRate = 1.0F;
        }

        this.flapRate = (float) ((double) this.flapRate * 0.9);

        // Slow falling
        if (!this.onGround && this.yd < 0.0) {
            this.yd *= 0.6;
        }

        this.flapAngle += this.flapRate * 2.0F;
    }

    public boolean isSliding(){
        return sliding;
    }

    public void setSliding(boolean flag){
        sliding = flag;
        if (isSliding()){
            setSitting(true);
        }

    }

    public void setSitting(boolean flag) {
        byte byte0 = this.entityData.getByte(16);
        if (flag) {
            this.entityData.set(16, (byte) (byte0 | 1));
        } else {
            this.entityData.set(16, (byte) (byte0 & -2));
        }

    }

    @Override
    public boolean interact(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (this.isTamed()) {
            if (itemstack != null && itemstack.itemID == Item.stick.id && !this.isAngry()) {
                if (!this.world.isClientSide) {
                    setSliding(!isSliding());
                    //this.world.sendTrackedEntityStatusUpdatePacket(this, (byte) 7);
                }

                return true;
            }
        }
        return super.interact(entityplayer);


    }
}

