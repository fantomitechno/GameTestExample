package dev.renoux.example;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.level.block.Blocks;
import org.quiltmc.qsl.testing.api.game.QuiltGameTest;
import org.quiltmc.qsl.testing.api.game.QuiltTestContext;

public class GameTests implements QuiltGameTest {

    // We use an empty structure created by QSL
    @GameTest(template = QuiltGameTest.EMPTY_STRUCTURE)
    public void checkForDiamond(QuiltTestContext context) {
        // Place a block at 0 2 0 (relative)
        context.setBlock(0, 2, 0, Blocks.DIAMOND_BLOCK);
        context.succeedWhen(() ->
                context.assertBlock( // Simple assert
                        new BlockPos(0, 2, 0),
                        (block) -> block == Blocks.DIAMOND_BLOCK,
                        "Expected block to be diamond"
                )
        );
    }

    // Here, we set no template so the GameTest will search for it in our resource folder: data/<mod id>/structures/<name of this class>/<name of the method>.nbt
    @GameTest
    public void checkForPistonWorking(QuiltTestContext context) {
        // Press a button
        context.pressButton(new BlockPos(0, 2, 1));
        context.succeedWhen(() ->
                context.assertBlock(
                        new BlockPos(3, 1, 1),
                        (block) -> block == Blocks.COPPER_BLOCK,
                        "Expected block to be moved"
                )
        );
    }
}
