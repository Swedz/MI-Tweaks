package net.swedz.mi_tweaks.machine.processcondition;

import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricBlastFurnaceBlockEntity;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.mixin.accessor.AbstractCraftingMultiblockBlockEntityAccessor;

import java.util.List;

public record EBFCoilProcessCondition(ElectricBlastFurnaceBlockEntity.Tier coilTier) implements MachineProcessCondition
{
	public static final MapCodec<EBFCoilProcessCondition>                             CODEC        = RecordCodecBuilder.mapCodec(
			(g) -> g.group(
					StringRepresentable.fromValues(() -> ElectricBlastFurnaceBlockEntity.tiers.stream().map(WrappedEBFCoilTier::new).toList().toArray(new WrappedEBFCoilTier[0]))
							.fieldOf("coil")
							.forGetter((c) -> new WrappedEBFCoilTier(c.coilTier()))
			).apply(g, (wrappedTier) -> new EBFCoilProcessCondition(wrappedTier.coilTier()))
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, EBFCoilProcessCondition> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8.map(
					(coilBlockId) -> ElectricBlastFurnaceBlockEntity.tiersByCoil.get(ResourceLocation.parse(coilBlockId)),
					(coilTier) -> coilTier.coilBlockId().toString()
			),
			EBFCoilProcessCondition::coilTier,
			EBFCoilProcessCondition::new
	);
	
	@Override
	public boolean canProcessRecipe(Context context, MachineRecipe recipe)
	{
		if(context.getBlockEntity() instanceof ElectricBlastFurnaceBlockEntity ebf)
		{
			AbstractCraftingMultiblockBlockEntityAccessor multiblock = (AbstractCraftingMultiblockBlockEntityAccessor) ebf;
			int activeShapeIndex = multiblock.getActiveShape().getActiveShapeIndex();
			ElectricBlastFurnaceBlockEntity.Tier ebfTier = ElectricBlastFurnaceBlockEntity.tiers.get(activeShapeIndex);
			return coilTier.maxBaseEu() <= ebfTier.maxBaseEu();
		}
		return false;
	}
	
	@Override
	public void appendDescription(List<Component> lines)
	{
		lines.add(MITweaks.text().recipeRequiresCoil(coilTier));
	}
	
	@Override
	public ItemStack icon()
	{
		return BuiltInRegistries.BLOCK.get(coilTier.coilBlockId()).asItem().getDefaultInstance();
	}
	
	@Override
	public MapCodec<? extends MachineProcessCondition> codec()
	{
		return CODEC;
	}
	
	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, ? extends MachineProcessCondition> streamCodec()
	{
		return STREAM_CODEC;
	}
	
	private record WrappedEBFCoilTier(ElectricBlastFurnaceBlockEntity.Tier coilTier) implements StringRepresentable
	{
		@Override
		public String getSerializedName()
		{
			return coilTier.coilBlockId().toString();
		}
	}
}