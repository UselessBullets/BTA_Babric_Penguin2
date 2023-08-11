package useless.penguinmod.entity;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.animal.EntityAnimal;
import net.minecraft.core.entity.animal.EntityChicken;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucketEmpty;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class EntityPenguin extends EntityAnimal {
    public EntityPenguin(World world) {
        super(world);
        this.skinName = "cow";
        this.highestSkinVariant = 3;
        this.setSize(0.9F, 1.3F);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }
    protected String getLivingSound() {
        return "mob.cow";
    }

    protected String getHurtSound() {
        return "mob.cowhurt";
    }

    protected String getDeathSound() {
        return "mob.cowhurt";
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected int getDropItemId() {
        return Item.diamond.id;
    }

    protected void dropFewItems() {
        int i = this.getDropItemId();
        if (i > 0) {
            int j = this.random.nextInt(5) + 1;

            for(int k = 0; k < j; ++k) {
                this.spawnAtLocation(i, 1);
            }
        }

    }

    public boolean interact(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (itemstack != null && itemstack.itemID == Item.bucket.id) {
            ItemBucketEmpty.UseBucket(entityplayer, new ItemStack(Item.bucketMilk));
            return true;
        } else {
            return super.interact(entityplayer);
        }
    }
}
