package net.swedz.mi_tweaks.machine.processcondition;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.CableTierHolder;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.compat.mi.serialization.MICodecs;
import net.swedz.tesseract.neoforge.compat.mi.serialization.MIStreamCodecs;

import java.util.List;

public record VoltageProcessCondition(CableTier tier) implements MachineProcessCondition
{
	public static final MapCodec<VoltageProcessCondition> CODEC = RecordCodecBuilder.mapCodec(
			(g) -> g.group(
					MICodecs.CABLE_TIER.fieldOf("voltage").forGetter(VoltageProcessCondition::tier)
			).apply(g, VoltageProcessCondition::new)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, VoltageProcessCondition> STREAM_CODEC = StreamCodec.composite(
			MIStreamCodecs.CABLE_TIER,
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
	public void appendDescription(List<Component> lines)
	{
		lines.add(MITweaks.text().recipeRequiresVoltage(tier));
	}
	
	@Override
	public ItemStack icon()
	{
		ResourceLocation id = tier.itemKey == null ? MI.id("basic_machine_hull") : tier.itemKey;
		return BuiltInRegistries.BLOCK.get(id).asItem().getDefaultInstance();
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
}