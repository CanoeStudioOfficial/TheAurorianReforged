package com.shiroroku.theaurorian;

import com.shiroroku.theaurorian.Compat.Conarm.ConstructsArmoryCompat;
import com.shiroroku.theaurorian.Compat.DynamicTrees.DynamicTreesCompat;
import com.shiroroku.theaurorian.Compat.TinkersConstruct.TinkersConstructCompat;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AurorianCompatibility {

	@SideOnly(Side.CLIENT)
	public static void clientPreInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("tconstruct") && AurorianConfig.Config_EnableTinkersConstructCompatibility) {
			TinkersConstructCompat.preInitSetMaterialRender();
		}
		if (Loader.isModLoaded("dynamictrees") && AurorianConfig.Config_EnableDynamicTreesCompatibility) {
			preInitDynamicTreesClientCompat();
		}
	}

	public static void preInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("tconstruct") && AurorianConfig.Config_EnableTinkersConstructCompatibility) {
			TinkersConstructCompat.preinitMetalsAndMaterials();
			if (Loader.isModLoaded("conarm") && AurorianConfig.Config_EnableConstructsArmoryCompatibility) {
				ConstructsArmoryCompat.preinitArmorTraits();
			}
		}
		if (Loader.isModLoaded("dynamictrees") && AurorianConfig.Config_EnableDynamicTreesCompatibility) {
			preInitDynamicTreesCompat();
		}
	}

	public static void init() {
		if (Loader.isModLoaded("tconstruct") && AurorianConfig.Config_EnableTinkersConstructCompatibility) {
			TinkersConstructCompat.initMaterials();
			if (Loader.isModLoaded("conarm") && AurorianConfig.Config_EnableConstructsArmoryCompatibility) {
				ConstructsArmoryCompat.initArmorMaterials();
			}
		}
		if (Loader.isModLoaded("dynamictrees") && AurorianConfig.Config_EnableDynamicTreesCompatibility) {
			initDynamicTreesCompat();
		}
	}

	public static void postInit(FMLPostInitializationEvent event) {
	}

	@Optional.Method(modid = "dynamictrees")
	public static void preInitDynamicTreesCompat() {
		DynamicTreesCompat.preInit();
	}

	@Optional.Method(modid = "dynamictrees")
	public static void initDynamicTreesCompat() {
		DynamicTreesCompat.init();
	}

	@Optional.Method(modid = "dynamictrees")
	@SideOnly(Side.CLIENT)
	public static void preInitDynamicTreesClientCompat() {
		DynamicTreesCompat.clientPreInit();
	}

	@Optional.Method(modid = "dynamictrees")
	@SideOnly(Side.CLIENT)
	public static void initDynamicTreesClientCompat() {
		DynamicTreesCompat.clientInit();
	}

}
