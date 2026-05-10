package com.shiroroku.theaurorian.Compat.DynamicTrees.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.shiroroku.theaurorian.AurorianMod;
import com.shiroroku.theaurorian.Blocks.SilentwoodLeaves;
import com.shiroroku.theaurorian.Compat.DynamicTrees.DynamicTreesCompat;
import com.shiroroku.theaurorian.Registry.BlockRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeSilentwood extends TreeFamily {

	public class SpeciesSilentwood extends Species {

		SpeciesSilentwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, DynamicTreesCompat.silentwoodLeavesProperties);

			setBasicGrowingParameters(0.3f, 12.0f, 4, 4, 1.0f);

			envFactor(Type.COLD, 1.05f);
			envFactor(Type.HOT, 0.5f);
			envFactor(Type.DRY, 0.75f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();

			setupStandardSeedDropping();
		}

		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.FOREST) && BiomeDictionary.hasType(biome, Type.MAGICAL);
		}

	}

	public TreeSilentwood() {
		super(new ResourceLocation(AurorianMod.MODID, "silentwood"));

		IBlockState primLog = BlockRegistry.Registry.SILENTWOODLOG.getBlock().getDefaultState();
		setPrimitiveLog(primLog, new ItemStack(BlockRegistry.Registry.SILENTWOODLOG.getBlock()));

		DynamicTreesCompat.silentwoodLeavesProperties.setTree(this);

		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof SilentwoodLeaves;
		});
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesSilentwood(this));
	}

}
