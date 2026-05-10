package com.shiroroku.theaurorian.Compat.DynamicTrees;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.shiroroku.theaurorian.AurorianMod;
import com.shiroroku.theaurorian.Compat.DynamicTrees.trees.TreeSilentwood;
import com.shiroroku.theaurorian.Compat.DynamicTrees.trees.TreeWeepingWillow;
import com.shiroroku.theaurorian.Registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class DynamicTreesCompat {

	public static ILeavesProperties silentwoodLeavesProperties, weepingwillowLeavesProperties;
	public static TreeFamily silentwoodTree, weepingwillowTree;

	@Optional.Method(modid = "dynamictrees")
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	public static void preInit() {
		IForgeRegistry<Block> blockRegistry = GameRegistry.findRegistry(Block.class);
		IForgeRegistry<Item> itemRegistry = GameRegistry.findRegistry(Item.class);

		silentwoodLeavesProperties = new LeavesProperties(
				BlockRegistry.Registry.SILENTWOODLEAVES.getBlock().getDefaultState(),
				new ItemStack(Item.getItemFromBlock(BlockRegistry.Registry.SILENTWOODLEAVES.getBlock())));
		weepingwillowLeavesProperties = new LeavesProperties(
				BlockRegistry.Registry.WEEPINGWILLOWLEAVES.getBlock().getDefaultState(),
				new ItemStack(Item.getItemFromBlock(BlockRegistry.Registry.WEEPINGWILLOWLEAVES.getBlock())));

		LeavesPaging.getLeavesBlockForSequence(AurorianMod.MODID, 0, silentwoodLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(AurorianMod.MODID, 1, weepingwillowLeavesProperties);

		silentwoodTree = new TreeSilentwood();
		weepingwillowTree = new TreeWeepingWillow();

		silentwoodTree.registerSpecies(Species.REGISTRY);
		weepingwillowTree.registerSpecies(Species.REGISTRY);

		ArrayList<Block> treeBlocks = new ArrayList<>();
		silentwoodTree.getRegisterableBlocks(treeBlocks);
		weepingwillowTree.getRegisterableBlocks(treeBlocks);
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(AurorianMod.MODID).values());
		blockRegistry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));

		ArrayList<Item> treeItems = new ArrayList<>();
		silentwoodTree.getRegisterableItems(treeItems);
		weepingwillowTree.getRegisterableItems(treeItems);
		itemRegistry.registerAll(treeItems.toArray(new Item[treeItems.size()]));

		if (ModConfigs.replaceVanillaSapling) {
			MinecraftForge.EVENT_BUS.register(new SaplingReplacer());
		}
	}

	public static void init() {
	}

	@SideOnly(Side.CLIENT)
	public static void clientPreInit() {
		ModelHelper.regModel(silentwoodTree.getDynamicBranch());
		ModelHelper.regModel(weepingwillowTree.getDynamicBranch());
		ModelHelper.regModel(silentwoodTree.getCommonSpecies().getSeed());
		ModelHelper.regModel(weepingwillowTree.getCommonSpecies().getSeed());
		ModelHelper.regModel(silentwoodTree);
		ModelHelper.regModel(weepingwillowTree);
		LeavesPaging.getLeavesMapForModId(AurorianMod.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
	}

	@SideOnly(Side.CLIENT)
	public static void clientInit() {
		final int magenta = 0x00FF00FF;

		for (BlockDynamicLeaves leaves : LeavesPaging.getLeavesMapForModId(AurorianMod.MODID).values()) {
			ModelHelper.regColorHandler(leaves, new IBlockColor() {
				@Override
				public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
					Block block = state.getBlock();

					if (TreeHelper.isLeaves(block)) {
						return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
					}
					return magenta;
				}
			});
		}
	}

	public static Item getSilentwoodSeed() {
		return silentwoodTree.getCommonSpecies().getSeed();
	}

	public static Item getWeepingWillowSeed() {
		return weepingwillowTree.getCommonSpecies().getSeed();
	}

	public static boolean replaceWorldGen() {
		return WorldGenRegistry.isWorldGenEnabled();
	}

}
