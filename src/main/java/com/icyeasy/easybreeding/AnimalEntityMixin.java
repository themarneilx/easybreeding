package com.icyeasy.easybreeding.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public class AnimalEntityMixin {
    @Inject(method = "mobTick", at = @At("TAIL"))
    private void onMobTick(CallbackInfo ci) {
        AnimalEntity animal = (AnimalEntity)(Object)this;
        World world = animal.getWorld();
        if (world.isClient) return;
        if (animal.isBaby() || animal.getLoveTicks() > 0) return;

        double radius = 4.0;
        for (ItemEntity itemEntity : world.getEntitiesByClass(
                ItemEntity.class,
                animal.getBoundingBox().expand(radius, 2.0, radius),
                e -> true)) {

            Item item = itemEntity.getStack().getItem();
            boolean canBreed = false;

            if ((animal instanceof CowEntity || animal instanceof SheepEntity || animal instanceof MooshroomEntity)
                    && item == Items.WHEAT) {
                canBreed = true;
            }

            if (animal instanceof PigEntity &&
                    (item == Items.CARROT || item == Items.BEETROOT || item == Items.POTATO)) {
                canBreed = true;
            }

            if (animal instanceof ChickenEntity &&
                    (item == Items.WHEAT_SEEDS || item == Items.BEETROOT_SEEDS ||
                     item == Items.PUMPKIN_SEEDS || item == Items.MELON_SEEDS ||
                     item == Items.TORCHFLOWER_SEEDS)) {
                canBreed = true;
            }

            if (animal instanceof RabbitEntity &&
                    (item == Items.CARROT || item == Items.DANDELION)) {
                canBreed = true;
            }

            if ((animal instanceof HorseEntity || animal instanceof DonkeyEntity) &&
                    (item == Items.GOLDEN_CARROT || item == Items.GOLDEN_APPLE)) {
                canBreed = true;
            }

            if (animal instanceof LlamaEntity && item == Items.HAY_BLOCK) {
                canBreed = true;
            }

            if (animal instanceof FoxEntity && item == Items.SWEET_BERRIES) {
                canBreed = true;
            }

            if (animal instanceof BeeEntity && itemEntity.getStack().isIn(ItemTags.FLOWERS)) {
                canBreed = true;
            }


            if (animal instanceof FrogEntity && item == Items.SLIME_BALL) {
                canBreed = true;
            }

            if (animal instanceof SnifferEntity && item == Items.TORCHFLOWER_SEEDS) {
                canBreed = true;
            }

            if (animal instanceof PandaEntity && item == Items.BAMBOO) {
                canBreed = true;
            }

            if (animal instanceof TurtleEntity && item == Items.SEAGRASS) {
                canBreed = true;
            }

            if (canBreed) {
                ItemStack stack = itemEntity.getStack();
                stack.decrement(1);
                if (stack.isEmpty()) itemEntity.remove(Entity.RemovalReason.DISCARDED);

                animal.setLoveTicks(600);
                ((ServerWorld)world).sendEntityStatus(animal, (byte)7);
                break;
            }
        }
    }
}
