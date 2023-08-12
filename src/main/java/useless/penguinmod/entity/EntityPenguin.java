package useless.penguinmod.entity;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.animal.EntityChicken;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucketEmpty;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pathfinder.Path;

public class EntityPenguin extends EntityChicken {
    public EntityPenguin(World world) {
        super(world);
        this.skinName = "penguin";
        this.highestSkinVariant = -1;
        this.setSize(8f/16f, 14f/16f);
    }
    protected void init() {
        super.init();
        this.entityData.define(PenguinState.ID_AI_STATE, (byte)0);
        this.entityData.define(PenguinState.ID_AI_OWNER, "");
        this.entityData.define(18, this.health);

    }
    public boolean interact(EntityPlayer entityplayer) {
        if (super.interact(entityplayer)) {
            return true;
        } else {
            ItemStack itemstack = entityplayer.inventory.getCurrentItem();
            if (!this.isPenguinTamed()) {
                if (itemstack != null && itemstack.itemID == Item.bone.id && !this.isPenguinAngry()) {
                    itemstack.consumeItem(entityplayer);
                    if (itemstack.stackSize <= 0) {
                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
                    }

                    if (!this.world.isClientSide) {
                        if (this.random.nextInt(3) == 0) {
                            this.setPenguinTamed(true);
                            this.setPathToEntity((Path)null);
                            this.setPenguinSitting(true);
                            this.health = 20;
                            this.setPenguinOwner(entityplayer.username);
                            this.showHeartsOrSmokeFX(true);
                            this.world.sendTrackedEntityStatusUpdatePacket(this, (byte)7);
                        } else {
                            this.showHeartsOrSmokeFX(false);
                            this.world.sendTrackedEntityStatusUpdatePacket(this, (byte)6);
                        }
                    }

                    return true;
                }
            } else {
                if (itemstack != null && Item.itemsList[itemstack.itemID] instanceof ItemFood) {
                    ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];
                    if (itemfood.getIsWolfsFavoriteMeat() && this.entityData.getInt(18) < 20) {
                        if (entityplayer.getGamemode().consumeBlocks) {
                            --itemstack.stackSize;
                            if (itemstack.stackSize <= 0) {
                                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
                            }
                        }

                        this.heal(((ItemFood)Item.foodPorkchopRaw).getHealAmount());
                        return true;
                    }
                }

                if (entityplayer.username.equalsIgnoreCase(this.getPenguinOwner())) {
                    if (!this.world.isClientSide) {
                        this.setPenguinSitting(!this.isPenguinSitting());
                        this.isJumping = false;
                        this.setPathToEntity((Path)null);
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public String getEntityTexture() {return "/assets/penguinmod/entity/penguin/penguin2.png";}
    public String getDefaultEntityTexture() {
        return "/assets/penguinmod/entity/penguin/penguin3.png";
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Angry", this.isPenguinAngry());
        tag.putBoolean("Sitting", this.isPenguinSitting());

        if (this.getPenguinOwner() == null) {
            tag.putString("Owner", "");
        } else {
            tag.putString("Owner", this.getPenguinOwner());
        }

    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setPenguinAngry(tag.getBoolean("Angry"));
        this.setPenguinSitting(tag.getBoolean("Sitting"));
        String s = tag.getString("Owner");
        if (s.length() > 0) {
            this.setPenguinOwner(s);
            this.setPenguinTamed(true);
        }

    }

    public void setPenguinAngry(boolean flag) {
        byte byte0 = this.entityData.getByte(PenguinState.ID_AI_STATE);
        if (flag) {
            this.entityData.set(PenguinState.ID_AI_STATE, (byte)(byte0 | 2));
        } else {
            this.entityData.set(PenguinState.ID_AI_STATE, (byte)(byte0 & -3));
        }

    }
    public void setPenguinSitting(boolean flag) {
        byte byte0 = this.entityData.getByte(PenguinState.ID_AI_STATE);
        if (flag) {
            this.entityData.set(PenguinState.ID_AI_STATE, (byte)(byte0 | 1));
        } else {
            this.entityData.set(PenguinState.ID_AI_STATE, (byte)(byte0 & -2));
        }

    }


    public String getPenguinOwner() {
        return this.entityData.getString(PenguinState.ID_AI_OWNER);
    }

    public void setPenguinOwner(String s) {
        this.entityData.set(PenguinState.ID_AI_OWNER, s);
    }

    public void setPenguinTamed(boolean flag) {
        byte byte0 = this.entityData.getByte(PenguinState.ID_AI_STATE);
        if (flag) {
            this.entityData.set(PenguinState.ID_AI_STATE, (byte)(byte0 | 4));
        } else {
            this.entityData.set(PenguinState.ID_AI_STATE, (byte)(byte0 & -5));
        }

    }
    public boolean isPenguinAngry() {
        return (this.entityData.getByte(PenguinState.ID_AI_STATE) & 2) != 0;
    }
    public boolean isPenguinTamed() {
        return (this.entityData.getByte(PenguinState.ID_AI_STATE) & 4) != 0;
    }

    public boolean isPenguinSitting() {
        return (this.entityData.getByte(PenguinState.ID_AI_STATE) & 1) != 0;
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

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected int getDropItemId() {
        return Item.featherChicken.id;
    }
    
    void showHeartsOrSmokeFX(boolean flag) {
        String s = "heart";
        if (!flag) {
            s = "smoke";
        }

        for(int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double d1 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            this.world.spawnParticle(s, this.x + (double)(this.random.nextFloat() * this.bbWidth * 2.0F) - (double)this.bbWidth, this.y + 0.5 + (double)(this.random.nextFloat() * this.bbHeight), this.z + (double)(this.random.nextFloat() * this.bbWidth * 2.0F) - (double)this.bbWidth, d, d1, d2);
        }

    }
}

