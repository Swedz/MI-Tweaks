package net.swedz.mi_tweaks.machine.processcondition;

import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.swedz.mi_tweaks.MITweaksText;
import net.swedz.tesseract.neoforge.compat.mi.helper.MachineTierHelper;

import java.util.List;
import java.util.Locale;

public record MachineTierProcessCondition(MachineTierReference tier) implements MachineProcessCondition
{
	public static final MapCodec<MachineTierProcessCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
			.group(
					StringRepresentable.fromEnum(MachineTierReference::values).fieldOf("tier").forGetter(MachineTierProcessCondition::tier)
			)
			.apply(instance, MachineTierProcessCondition::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, MachineTierProcessCondition> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8.map((string) -> MachineTierReference.valueOf(string.toUpperCase(Locale.ROOT)), MachineTierReference::toString),
			MachineTierProcessCondition::tier,
			MachineTierProcessCondition::new
	);
	
	@Override
	public boolean canProcessRecipe(Context context, MachineRecipe recipe)
	{
		return tier.tier() == MachineTierHelper.getMachineTier(context.getBlockEntity());
	}
	
	private MutableComponent tierComponent()
	{
		return (switch (tier)
		{
			case BRONZE -> MITweaksText.MACHINE_TIER_BRONZE;
			case STEEL -> MITweaksText.MACHINE_TIER_STEEL;
			case SINGLEBLOCK_ELECTRIC -> MITweaksText.MACHINE_TIER_SINGLEBLOCK_ELECTRIC;
			case MULTIBLOCK_ELECTRIC -> MITweaksText.MACHINE_TIER_MULTIBLOCK_ELECTRIC;
		}).text();
	}
	
	@Override
	public void appendDescription(List<Component> list)
	{
		list.add(MITweaksText.RECIPE_REQUIRES_MACHINE_TIER.text(this.tierComponent()));
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
	
	public enum MachineTierReference implements StringRepresentable
	{
		BRONZE(MachineTier.BRONZE),
		STEEL(MachineTier.STEEL),
		SINGLEBLOCK_ELECTRIC(MachineTier.LV),
		MULTIBLOCK_ELECTRIC(MachineTier.MULTIBLOCK);
		
		private final MachineTier tier;
		
		MachineTierReference(MachineTier tier)
		{
			this.tier = tier;
		}
		
		public MachineTier tier()
		{
			return tier;
		}
		
		@Override
		public String getSerializedName()
		{
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
}
