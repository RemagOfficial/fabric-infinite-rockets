package com.remag.fir.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
public abstract class FireworkRocketMixin {
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	public void preventDepletion(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		if (user.isFallFlying()) {
			ItemStack itemStack = user.getStackInHand(hand);
			if (!world.isClient) {
				FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, itemStack, user);
				world.spawnEntity(fireworkRocketEntity);
				user.incrementStat(Stats.USED.getOrCreateStat((FireworkRocketItem) (Object) this));
			}

			cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand), world.isClient()));
		}
	}
}