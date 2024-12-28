package net.swedz.mi_tweaks.attribute;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import net.swedz.mi_tweaks.MITweaksText;

public final class HeatProtectionAttribute extends Attribute
{
	public HeatProtectionAttribute()
	{
		super(MITweaksText.ATTRIBUTE_HEAT_PROTECTION.getTranslationKey(), 0);
	}
	
	@Override
	public MutableComponent toValueComponent(AttributeModifier.Operation op, double value, TooltipFlag flag)
	{
		return MITweaksText.ATTRIBUTE_VALUE_GIVES.text();
	}
	
	@Override
	public MutableComponent toComponent(AttributeModifier modifier, TooltipFlag flag)
	{
		double value = modifier.amount();
		ChatFormatting color = this.getStyle(value > 0);
		return Component.translatable("neoforge.modifier.bool", this.toValueComponent(modifier.operation(), value, flag), Component.translatable(this.getDescriptionId())).withStyle(color);
	}
}
