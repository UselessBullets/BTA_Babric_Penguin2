package useless.penguinmod.entity;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.animal.EntityAnimal;
import net.minecraft.core.entity.animal.EntitySheep;
import net.minecraft.core.entity.animal.EntityWolf;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.projectile.EntityArrow;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pathfinder.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityPet extends EntityAnimal {
    private boolean looksWithInterest = false;
    private float field_25048_b;
    private float field_25054_c;
    protected int tameItemID;

    public EntityPet(World world) {
        super(world);
    }

    protected void init() {
        super.init();
        this.entityData.define(16, (byte) 0); // Ai State
        this.entityData.define(17, ""); // Owner String
        this.entityData.define(18, this.health); // Health
    }
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Angry", this.isWolfAngry());
        tag.putBoolean("Sitting", this.isWolfSitting());

        if (this.getWolfOwner() == null) {
            tag.putString("Owner", "");
        } else {
            tag.putString("Owner", this.getWolfOwner());
        }

    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setWolfAngry(tag.getBoolean("Angry"));
        this.setWolfSitting(tag.getBoolean("Sitting"));
        String s = tag.getString("Owner");
        if (s.length() > 0) {
            this.setWolfOwner(s);
            this.setWolfTamed(true);
        }

    }

    protected boolean canDespawn() {
        return !this.isWolfTamed();
    }

    protected int getDropItemId() {
        return -1;
    }

    protected void updatePlayerActionState() {
        super.updatePlayerActionState();

        if (this.isInWater()) {
            this.setWolfSitting(false);
        }

        if (!this.world.isClientSide) {
            this.entityData.set(18, this.health);
        }

    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.looksWithInterest = false;
        if (this.hasCurrentTarget() && !this.hasPath() && !this.isWolfAngry()) {
            Entity entity = this.getCurrentTarget();
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entity;
                ItemStack itemstack = entityplayer.inventory.getCurrentItem();
                if (itemstack != null) {
                    if (!this.isWolfTamed() && itemstack.itemID == tameItemID) {
                        this.looksWithInterest = true;
                    }
                }
            }
        }

    }


    public void tick() {
        super.tick();
        this.field_25054_c = this.field_25048_b;
        if (this.looksWithInterest) {
            this.field_25048_b += (1.0F - this.field_25048_b) * 0.4F;
        } else {
            this.field_25048_b += (0.0F - this.field_25048_b) * 0.4F;
        }

        if (this.looksWithInterest) {
            this.numTicksToChaseTarget = 10;
        }
    }
    public float getInterestedAngle(float f) {
        return (this.field_25054_c + (this.field_25048_b - this.field_25054_c) * f) * 0.15F * 3.141593F;
    }

    public float getHeadHeight() {
        return this.bbHeight * 0.8F;
    }

    protected int func_25026_x() {
        return this.isWolfSitting() ? 20 : super.func_25026_x();
    }

    private void getPathOrWalkableBlock(Entity entity, float f) {
        Path pathentity = this.world.getPathToEntity(this, entity, 16.0F);
        if (pathentity == null && f > 12.0F) {
            int i = MathHelper.floor_double(entity.x) - 2;
            int j = MathHelper.floor_double(entity.z) - 2;
            int k = MathHelper.floor_double(entity.bb.minY);

            for (int l = 0; l <= 4; ++l) {
                for (int i1 = 0; i1 <= 4; ++i1) {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.world.isBlockNormalCube(i + l, k - 1, j + i1) && !this.world.isBlockNormalCube(i + l, k, j + i1) && !this.world.isBlockNormalCube(i + l, k + 1, j + i1)) {
                        this.moveTo((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.yRot, this.xRot);
                        return;
                    }
                }
            }
        } else {
            this.setPathToEntity(pathentity);
        }

    }

    protected boolean isMovementCeased() {
        return this.isWolfSitting();
    }

    public boolean hurt(Entity entity, int i, DamageType type) {
        this.setWolfSitting(false);
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
            i = (i + 1) / 2;
        }

        if (!super.hurt((Entity) entity, i, type)) {
            return false;
        } else {
            if (!this.isWolfTamed() && !this.isWolfAngry()) {
                if (entity instanceof EntityPlayer) {
                    this.setWolfAngry(true);
                    this.entityToAttack = (Entity) entity;
                }

                if (entity instanceof EntityArrow && ((EntityArrow) entity).owner != null) {
                    entity = ((EntityArrow) entity).owner;
                }

                if (entity instanceof EntityLiving) {
                    List list = this.world.getEntitiesWithinAABB(EntityPet.class, AABB.getBoundingBoxFromPool(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0).expand(16.0, 4.0, 16.0));
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity1 = (Entity) iterator.next();
                        EntityPet entitywolf = (EntityPet) entity1;
                        if (!entitywolf.isWolfTamed() && entitywolf.entityToAttack == null) {
                            entitywolf.entityToAttack = (Entity) entity;
                            if (entity instanceof EntityPlayer) {
                                entitywolf.setWolfAngry(true);
                            }
                        }
                    }
                }
            } else if (entity != this && entity != null) {
                if (this.isWolfTamed() && entity instanceof EntityPlayer && ((EntityPlayer) entity).username.equalsIgnoreCase(this.getWolfOwner())) {
                    return true;
                }

                this.entityToAttack = (Entity) entity;
            }

            return true;
        }
    }
    protected Entity findPlayerToAttack() {
        return this.isWolfAngry() ? this.world.getClosestPlayerToEntity(this, 16.0) : null;
    }

    protected void attackEntity(Entity entity, float distance) {
        if (!(entity instanceof EntityItem)) {
            if (distance > 2.0F && distance < 6.0F && this.random.nextInt(10) == 0) {
                if (this.onGround) {
                    double d = entity.x - this.x;
                    double d1 = entity.z - this.z;
                    float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                    this.xd = d / (double) f1 * 0.5 * 0.800000011920929 + this.xd * 0.20000000298023224;
                    this.zd = d1 / (double) f1 * 0.5 * 0.800000011920929 + this.zd * 0.20000000298023224;
                    this.yd = 0.4000000059604645;
                }
            } else if ((double) distance < 1.5 && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
                this.attackTime = 20;
                byte byte0 = 2;
                if (this.isWolfTamed()) {
                    byte0 = 4;
                }

                entity.hurt(this, byte0, DamageType.COMBAT);
            }

        }
    }

    public boolean isHealingItem(Item item) {
        if (item instanceof ItemFood){
            if (((ItemFood)item).getIsWolfsFavoriteMeat()){
                return true;
            }
        }
        return false;
    }

    public int healAmount(Item item){
        return ((ItemFood) Item.foodPorkchopRaw).getHealAmount();
    }
    public boolean interact(EntityPlayer entityplayer) {
        if (super.interact(entityplayer)) {
            return true;
        } else {
            ItemStack itemstack = entityplayer.inventory.getCurrentItem();
            if (!this.isWolfTamed()) {
                if (itemstack != null && itemstack.itemID == tameItemID && !this.isWolfAngry()) {
                    itemstack.consumeItem(entityplayer);
                    if (itemstack.stackSize <= 0) {
                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack) null);
                    }

                    if (!this.world.isClientSide) {
                        if (this.random.nextInt(3) == 0) {
                            this.setWolfTamed(true);
                            this.setPathToEntity((Path) null);
                            this.setWolfSitting(true);
                            this.health = 20;
                            this.setWolfOwner(entityplayer.username);
                            this.showHeartsOrSmokeFX(true);
                            this.world.sendTrackedEntityStatusUpdatePacket(this, (byte) 7);
                        } else {
                            this.showHeartsOrSmokeFX(false);
                            this.world.sendTrackedEntityStatusUpdatePacket(this, (byte) 6);
                        }
                    }

                    return true;
                }
            } else {
                if (itemstack != null) {
                    Item healItem = (Item) Item.itemsList[itemstack.itemID];
                    if (isHealingItem(healItem) && this.entityData.getInt(18) < 20 /*health less that max*/) {
                        if (entityplayer.getGamemode().consumeBlocks) {
                            --itemstack.stackSize;
                            if (itemstack.stackSize <= 0) {
                                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack) null);
                            }
                        }

                        this.heal(this.healAmount(healItem));
                        return true;
                    }
                }

                if (entityplayer.username.equalsIgnoreCase(this.getWolfOwner())) {
                    if (!this.world.isClientSide) {
                            this.setWolfSitting(!this.isWolfSitting());
                            this.isJumping = false;
                            this.setPathToEntity((Path) null);

                    }
                    return true;
                }
            }
            return false;
        }
    }

    void showHeartsOrSmokeFX(boolean flag) {
        String s = "heart";
        if (!flag) {
            s = "smoke";
        }

        for (int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double d1 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            this.world.spawnParticle(s, this.x + (double) (this.random.nextFloat() * this.bbWidth * 2.0F) - (double) this.bbWidth, this.y + 0.5 + (double) (this.random.nextFloat() * this.bbHeight), this.z + (double) (this.random.nextFloat() * this.bbWidth * 2.0F) - (double) this.bbWidth, d, d1, d2);
        }

    }

    public void handleEntityEvent(byte byte0, float attackedAtYaw) {
        if (byte0 == 7) {
            this.showHeartsOrSmokeFX(true);
        } else if (byte0 == 6) {
            this.showHeartsOrSmokeFX(false);
        } else {
            super.handleEntityEvent(byte0, attackedAtYaw);
        }

    }

    public float setTailRotation() {
        if (this.isWolfAngry()) {
            return 1.53938F;
        } else {
            return this.isWolfTamed() ? (0.55F - (float) (20 - this.entityData.getInt(18)) * 0.02F) * 3.141593F : 0.6283185F;
        }
    }

    public int getMaxSpawnedInChunk() {
        return 8;
    }

    public String getWolfOwner() {
        return this.entityData.getString(17);
    }

    public void setWolfOwner(String s) {
        this.entityData.set(17, s);
    }

    public boolean isWolfSitting() {
        return (this.entityData.getByte(16) & 1) != 0;
    }

    public void setWolfSitting(boolean flag) {
        byte byte0 = this.entityData.getByte(16);
        if (flag) {
            this.entityData.set(16, (byte) (byte0 | 1));
        } else {
            this.entityData.set(16, (byte) (byte0 & -2));
        }

    }

    public boolean isWolfAngry() {
        return (this.entityData.getByte(16) & 2) != 0;
    }

    public void setWolfAngry(boolean flag) {
        byte byte0 = this.entityData.getByte(16);
        if (flag) {
            this.entityData.set(16, (byte) (byte0 | 2));
        } else {
            this.entityData.set(16, (byte) (byte0 & -3));
        }

    }

    public boolean isWolfTamed() {
        return (this.entityData.getByte(16) & 4) != 0;
    }

    public void setWolfTamed(boolean flag) {
        byte byte0 = this.entityData.getByte(16);
        if (flag) {
            this.entityData.set(16, (byte) (byte0 | 4));
        } else {
            this.entityData.set(16, (byte) (byte0 & -5));
        }

    }
}