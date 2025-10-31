package net.swedz.mi_tweaks.machine.processcondition;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.compat.mi.helper.MachineTierHelper;
import net.swedz.tesseract.neoforge.helper.CodecHelper;

import java.util.List;

public record MachineTierProcessCondition(MachineTierReference tier) implements MachineProcessCondition
{
	public static final MapCodec<MachineTierProcessCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
			.group(
					MachineTierReference.CODEC.fieldOf("tier").forGetter(MachineTierProcessCondition::tier)
			)
			.apply(instance, MachineTierProcessCondition::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, MachineTierProcessCondition> STREAM_CODEC = StreamCodec.composite(
			MachineTierReference.STREAM_CODEC,
			MachineTierProcessCondition::tier,
			MachineTierProcessCondition::new
	);
	
	@Override
	public boolean canProcessRecipe(Context context, MachineRecipe recipe)
	{
		return tier.tier() == MachineTierHelper.getMachineTier(context.getBlockEntity());
	}
	
	@Override
	public void appendDescription(List<Component> lines)
	{
		lines.add(MITweaks.text().recipeRequiresMachineTier(tier.tier()));
	}
	
	@Override
	public ItemStack icon()
	{
		return tier.icon();
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
	
	public enum MachineTierReference
	{
		BRONZE(MachineTier.BRONZE, MI.id("bronze_machine_casing")),
		STEEL(MachineTier.STEEL, MI.id("steel_machine_casing")),
		SINGLEBLOCK_ELECTRIC(MachineTier.LV, MI.id("basic_machine_hull")),
		MULTIBLOCK_ELECTRIC(MachineTier.MULTIBLOCK, MI.id("lv_energy_input_hatch"));
		
		public static final Codec<MachineTierReference> CODEC = CodecHelper.forLowercaseEnum(MachineTierReference.class);
		
		public static final StreamCodec<ByteBuf, MachineTierReference> STREAM_CODEC = CodecHelper.forLowercaseEnumStream(MachineTierReference.class);
		
		private final MachineTier      tier;
		private final ResourceLocation iconId;
		
		MachineTierReference(MachineTier tier, ResourceLocation iconId)
		{
			this.tier = tier;
			this.iconId = iconId;
		}
		
		public MachineTier tier()
		{
			return tier;
		}
		
		public ItemStack icon()
		{
			return BuiltInRegistries.BLOCK.get(iconId).asItem().getDefaultInstance();
		}
	}
}
