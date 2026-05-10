package com.shiroroku.theaurorian.Compat.DynamicTrees.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.shiroroku.theaurorian.AurorianMod;
import com.shiroroku.theaurorian.Blocks.WeepingWillowLeaves;
import com.shiroroku.theaurorian.Compat.DynamicTrees.DropCreatorFruit;
import com.shiroroku.theaurorian.Compat.DynamicTrees.DynamicTreesCompat;
import com.shiroroku.theaurorian.Registry.BlockRegistry;
import com.shiroroku.theaurorian.Registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.Objects;

public class TreeWeepingWillow extends TreeFamily {

	public static Block logBlock = BlockRegistry.Registry.WEEPINGWILLOWLOG.getBlock();

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

		setPrimitiveLog(logBlock.getDefaultState());

		DynamicTreesCompat.weepingwillowLeavesProperties.setTree(this);

		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof WeepingWillowLeaves;
		});
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesWeepingWillow(this));
	}

}
