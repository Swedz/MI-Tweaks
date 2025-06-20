package net.swedz.mi_tweaks.machine.processcondition;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.AABB;
import net.swedz.mi_tweaks.MITweaksText;
import net.swedz.tesseract.neoforge.helper.CodecHelper;
import net.swedz.tesseract.neoforge.tooltip.TranslatableTextEnum;

import java.util.function.BiFunction;

public enum SurroundingArea
{
	ALL(
			MITweaksText.RECIPE_REQUIRES_SURROUNDING_AREA_ALL,
			(origin, range) -> origin.offset(-range, -range, -range),
			(origin, range) -> origin.offset(range, range, range)
	),
	AT_AND_BELOW(
			MITweaksText.RECIPE_REQUIRES_SURROUNDING_AREA_AT_AND_BELOW,
			(origin, range) -> origin.offset(-range, -range, -range),
			(origin, range) -> origin.offset(range, 0, range)
	),
	BELOW(
			MITweaksText.RECIPE_REQUIRES_SURROUNDING_AREA_BELOW,
			(origin, range) -> origin.offset(-range, -range, -range),
			(origin, range) -> origin.offset(range, -1, range)
	),
	AT_AND_ABOVE(
			MITweaksText.RECIPE_REQUIRES_SURROUNDING_AREA_AT_AND_ABOVE,
			(origin, range) -> origin.offset(-range, 0, -range),
			(origin, range) -> origin.offset(range, range, range)
	),
	ABOVE(
			MITweaksText.RECIPE_REQUIRES_SURROUNDING_AREA_ABOVE,
			(origin, range) -> origin.offset(-range, 1, -range),
			(origin, range) -> origin.offset(range, range, range)
	);
	
	public static final Codec<SurroundingArea> CODEC = CodecHelper.forLowercaseEnum(SurroundingArea.class);
	
	public static final StreamCodec<ByteBuf, SurroundingArea> STREAM_CODEC = CodecHelper.forLowercaseEnumStream(SurroundingArea.class);
	
	private final TranslatableTextEnum                    text;
	private final BiFunction<BlockPos, Integer, BlockPos> firstPos, secondPos;
	
	SurroundingArea(TranslatableTextEnum text,
					BiFunction<BlockPos, Integer, BlockPos> firstPos,
					BiFunction<BlockPos, Integer, BlockPos> secondPos)
	{
		this.text = text;
		this.firstPos = firstPos;
		this.secondPos = secondPos;
	}
	
	public MutableComponent text()
	{
		return text.text();
	}
	
	public Iterable<BlockPos> blocks(BlockPos origin, int range)
	{
		return BlockPos.betweenClosed(firstPos.apply(origin, range), secondPos.apply(origin, range));
	}
	
	public AABB bounds(BlockPos origin, int range)
	{
		return AABB.encapsulatingFullBlocks(firstPos.apply(origin, range), secondPos.apply(origin, range));
	}
}
