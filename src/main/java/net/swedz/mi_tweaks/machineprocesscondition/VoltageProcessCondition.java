package net.swedz.mi_tweaks.machineprocesscondition;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.swedz.mi_tweaks.MITweaksText;
import net.swedz.mi_tweaks.api.CableTierHolder;

import java.util.List;

public record VoltageProcessCondition(CableTier tier) implements MachineProcessCondition
{
	public static final MapCodec<VoltageProcessCondition>                             CODEC        = RecordCodecBuilder.mapCodec(
			(g) -> g.group(
					StringRepresentable.fromValues(() -> CableTier.allTiers().stream().map(WrappedCableTier::new).toList().toArray(new WrappedCableTier[0]))
							.fieldOf("voltage")
							.forGetter((c) -> new WrappedCableTier(c.tier()))
			).apply(g, (wrappedTier) -> new VoltageProcessCondition(wrappedTier.tier()))
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, VoltageProcessCondition> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8.map(CableTier::getTier, (tier) -> tier.name),
			VoltageProcessCondition::tier,
			VoltageProcessCondition::new
	);
	
	@Override
	public boolean canProcessRecipe(Context context, MachineRecipe recipe)
	{
		if(context.getBlockEntity() instanceof CableTierHolder machine)
		{
			return machine.getCableTier().eu >= tier.eu;
		}
		return false;
	}
	
	@Override
	public void appendDescription(List<Component> list)
	{
		list.add(MITweaksText.RECIPE_REQUIRES_VOLTAGE.text(Component.translatable(tier.shortEnglishKey())));
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
	
	private record WrappedCableTier(CableTier tier) implements StringRepresentable
	{
		@Override
		public String getSerializedName()
		{
			return tier.name;
		}
	}
}