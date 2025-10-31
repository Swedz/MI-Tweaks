package net.swedz.mi_tweaks.machine.processcondition;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.AABB;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.helper.CodecHelper;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum SurroundingArea
{
	ALL(
			() -> MITweaks.text().recipeRequiresSurroundingAreaAll(),
			(origin, range) -> origin.offset(-range, -range, -range),
			(origin, range) -> origin.offset(range, range, range)
	),
	AT_AND_BELOW(
			() -> MITweaks.text().recipeRequiresSurroundingAreaAtAndBelow(),
			(origin, range) -> origin.offset(-range, -range, -range),
			(origin, range) -> origin.offset(range, 0, range)
	),
	BELOW(
			() -> MITweaks.text().recipeRequiresSurroundingAreaBelow(),
			(origin, range) -> origin.offset(-range, -range, -range),
			(origin, range) -> origin.offset(range, -1, range)
	),
	AT_AND_ABOVE(
			() -> MITweaks.text().recipeRequiresSurroundingAreaAtAndAbove(),
			(origin, range) -> origin.offset(-range, 0, -range),
			(origin, range) -> origin.offset(range, range, range)
	),
	ABOVE(
			() -> MITweaks.text().recipeRequiresSurroundingAreaAbove(),
			(origin, range) -> origin.offset(-range, 1, -range),
			(origin, range) -> origin.offset(range, range, range)
	);
	
	public static final Codec<SurroundingArea> CODEC = CodecHelper.forLowercaseEnum(SurroundingArea.class);
	
	public static final StreamCodec<ByteBuf, SurroundingArea> STREAM_CODEC = CodecHelper.forLowercaseEnumStream(SurroundingArea.class);
	
	private final Supplier<MutableComponent>              text;
	private final BiFunction<BlockPos, Integer, BlockPos> firstPos, secondPos;
	
	SurroundingArea(Supplier<MutableComponent> text,
					BiFunction<BlockPos, Integer, BlockPos> firstPos,
					BiFunction<BlockPos, Integer, BlockPos> secondPos)
	{
		this.text = text;
		this.firstPos = firstPos;
		this.secondPos = secondPos;
	}
	
	public MutableComponent text()
	{
		return text.get();
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
