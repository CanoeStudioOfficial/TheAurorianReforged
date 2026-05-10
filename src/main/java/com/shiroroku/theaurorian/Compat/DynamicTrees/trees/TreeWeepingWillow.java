package com.shiroroku.theaurorian.Compat.DynamicTrees.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.shiroroku.theaurorian.AurorianMod;
import com.shiroroku.theaurorian.Blocks.WeepingWillowLeaves;
import com.shiroroku.theaurorian.Compat.DynamicTrees.DropCreatorFruit;
import com.shiroroku.theaurorian.Compat.DynamicTrees.DynamicTreesCompat;
import com.shiroroku.theaurorian.Registry.BlockRegistry;
import com.shiroroku.theaurorian.Registry.ItemRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeWeepingWillow extends TreeFamily {

	public class SpeciesWeepingWillow extends Species {

		SpeciesWeepingWillow(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, DynamicTreesCompat.weepingwillowLeavesProperties);

			setBasicGrowingParameters(0.3f, 14.0f, 5, 3, 1.0f);

			envFactor(Type.COLD, 1.05f);
			envFactor(Type.HOT, 0.5f);
			envFactor(Type.DRY, 0.5f);
			envFactor(Type.FOREST, 1.05f);
			envFactor(Type.WATER, 1.1f);

			generateSeed();

			setupStandardSeedDropping();
			addDropCreator(new DropCreatorFruit(ItemRegistry.Registry.WEEPINGWILLOWSAP.getItem(), 24));
		}

		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.FOREST) && BiomeDictionary.hasType(biome, Type.MAGICAL);
		}

	}

	public TreeWeepingWillow() {
		super(new ResourceLocation(AurorianMod.MODID, "weepingwillow"));

		IBlockState primLog = BlockRegistry.Registry.WEEPINGWILLOWLOG.getBlock().getDefaultState();
		setPrimitiveLog(primLog, new ItemStack(BlockRegistry.Registry.WEEPINGWILLOWLOG.getBlock()));

		DynamicTreesCompat.weepingwillowLeavesProperties.setTree(this);

		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof WeepingWillowLeaves;
		});
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesWeepingWillow(this));
	}

}
