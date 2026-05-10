package com.shiroroku.theaurorian.Compat.DynamicTrees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.EnumChance;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import com.shiroroku.theaurorian.AurorianMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

	private static Species silentwood, weepingwillow;

	private static void createStaticAliases() {
		silentwood = TreeRegistry.findSpecies(new ResourceLocation(AurorianMod.MODID, "silentwood"));
		weepingwillow = TreeRegistry.findSpecies(new ResourceLocation(AurorianMod.MODID, "weepingwillow"));
	}

	@Override
	public void populate(BiomeDataBase dbase) {
		if (silentwood == null) {
			createStaticAliases();
		}

		RandomSpeciesSelector silentwoodSelector = new RandomSpeciesSelector().add(silentwood, 1);
		RandomSpeciesSelector weepingwillowSelector = new RandomSpeciesSelector().add(weepingwillow, 1);
		RandomSpeciesSelector bothSelector = new RandomSpeciesSelector().add(silentwood, 1).add(weepingwillow, 1);

		Biome.REGISTRY.forEach(biome -> {
			boolean hasMagical = BiomeDictionary.hasType(biome, Type.MAGICAL);
			boolean hasForest = BiomeDictionary.hasType(biome, Type.FOREST);
			boolean hasPlains = BiomeDictionary.hasType(biome, Type.PLAINS);
			boolean hasHills = BiomeDictionary.hasType(biome, Type.HILLS);
			boolean hasWater = BiomeDictionary.hasType(biome, Type.WATER);

			boolean silentwoodSplice = hasMagical && (hasForest || hasPlains || hasHills);
			boolean weepingwillowSplice = hasMagical && (hasForest || hasWater);

			if (silentwoodSplice || weepingwillowSplice) {
				RandomSpeciesSelector selector = (silentwoodSplice && weepingwillowSplice) ? bothSelector : (weepingwillowSplice ? weepingwillowSelector : silentwoodSelector);
				dbase.setSpeciesSelector(biome, selector, Operation.REPLACE);
				dbase.setCancelVanillaTreeGen(biome, true);
				dbase.setDensitySelector(biome, (rnd, nd) -> nd * 0.5, Operation.REPLACE);
				dbase.setChanceSelector(biome, (rnd, spc, rad) -> EnumChance.OK, Operation.REPLACE);
				dbase.setForestness(biome, 0.5f);
			}
		});
	}

}
