package com.shiroroku.theaurorian.Compat.DynamicTrees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
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

		int silentwoodWeight = 200;
		int weepingwillowWeight = 200;

		RandomSpeciesSelector silentwoodSelector = new RandomSpeciesSelector().add(1000 - silentwoodWeight).add(silentwood, silentwoodWeight);
		RandomSpeciesSelector weepingwillowSelector = new RandomSpeciesSelector().add(1000 - weepingwillowWeight).add(weepingwillow, weepingwillowWeight);
		RandomSpeciesSelector bothSelector = new RandomSpeciesSelector().add(1000 - (silentwoodWeight + weepingwillowWeight)).add(silentwood, silentwoodWeight).add(weepingwillow, weepingwillowWeight);

		Biome.REGISTRY.forEach(biome -> {
			boolean hasMagical = BiomeDictionary.hasType(biome, Type.MAGICAL);
			boolean hasForest = BiomeDictionary.hasType(biome, Type.FOREST);
			boolean hasPlains = BiomeDictionary.hasType(biome, Type.PLAINS);
			boolean hasHills = BiomeDictionary.hasType(biome, Type.HILLS);
			boolean hasWater = BiomeDictionary.hasType(biome, Type.WATER);

			boolean silentwoodSplice = hasMagical && (hasForest || hasPlains || hasHills);
			boolean weepingwillowSplice = hasMagical && (hasForest || hasWater);

			if (silentwoodSplice || weepingwillowSplice) {
				dbase.setSpeciesSelector(biome, (silentwoodSplice && weepingwillowSplice) ? bothSelector : (weepingwillowSplice ? weepingwillowSelector : silentwoodSelector), Operation.SPLICE_BEFORE);
			}
		});
	}

}
