package com.shiroroku.theaurorian.Compat.DynamicTrees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.shiroroku.theaurorian.AurorianMod;
import com.shiroroku.theaurorian.Blocks.SilentwoodSapling;
import com.shiroroku.theaurorian.Blocks.WeepingWillowSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SaplingReplacer {

	@SubscribeEvent
	public void onPlaceSaplingEvent(PlaceEvent event) {
		IBlockState state = event.getPlacedBlock();

		Species species = null;

		if (state.getBlock() instanceof SilentwoodSapling) {
			species = TreeRegistry.findSpecies(new ResourceLocation(AurorianMod.MODID, "silentwood"));
		} else if (state.getBlock() instanceof WeepingWillowSapling) {
			species = TreeRegistry.findSpecies(new ResourceLocation(AurorianMod.MODID, "weepingwillow"));
		}

		if (species != null) {
			event.getWorld().setBlockToAir(event.getPos());
			if (!species.plantSapling(event.getWorld(), event.getPos())) {
				double x = event.getPos().getX() + 0.5;
				double y = event.getPos().getY() + 0.5;
				double z = event.getPos().getZ() + 0.5;
				EntityItem itemEntity = new EntityItem(event.getWorld(), x, y, z, species.getSeedStack(1));
				event.getWorld().spawnEntity(itemEntity);
			}
		}
	}

}
