package com.gaussianwonder.terraformer.setup.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class MatterFuserBlock extends BaseBlock {
    static {
        name = "matter_fuser";
    }

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public MatterFuserBlock() {
        super(AbstractBlock.Properties
                .create(Material.IRON)
                .hardnessAndResistance(8.0f, 10.0f)
                .sound(SoundType.ANVIL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .setRequiresTool());
    }

    // Model Related Methods Override
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch(state.get(FACING)) {
            case SOUTH:
                return SHAPE_S;
            case EAST:
                return SHAPE_E;
            case WEST:
                return SHAPE_W;
            default:
                return SHAPE_N;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.65f;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        switch(state.get(FACING)) {
            case SOUTH:
                return SHAPE_S;
            case EAST:
                return SHAPE_E;
            case WEST:
                return SHAPE_W;
            default:
                return SHAPE_N;
        }
    }

    // VOXEL SHAPES
    //TODO update voxel shapes
    private static VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(2, 0, 2, 4, 2, 4),
            Block.makeCuboidShape(14, 15, 1, 16, 16, 3),
            Block.makeCuboidShape(14, 15, 13, 16, 16, 16),
            Block.makeCuboidShape(0, 15, 13, 2, 16, 16),
            Block.makeCuboidShape(0, 14, 15, 2, 15, 16),
            Block.makeCuboidShape(14, 14, 15, 16, 15, 16),
            Block.makeCuboidShape(14, 0, 1, 16, 15, 2),
            Block.makeCuboidShape(0, 0, 1, 2, 15, 2),
            Block.makeCuboidShape(6, 14, 6, 10, 15, 10),
            Block.makeCuboidShape(5, 15, 5, 11, 16, 11),
            Block.makeCuboidShape(5, 12, 5, 11, 13, 11),
            Block.makeCuboidShape(4, 13, 4, 12, 14, 12),
            Block.makeCuboidShape(3, 11, 3, 13, 12, 13),
            Block.makeCuboidShape(0, 15, 1, 2, 16, 3),
            Block.makeCuboidShape(12, 0, 2, 14, 2, 4),
            Block.makeCuboidShape(14, 0, 12, 15, 2, 14),
            Block.makeCuboidShape(1, 0, 12, 2, 2, 14),
            Block.makeCuboidShape(3, 2, 3, 13, 11, 13),
            Block.makeCuboidShape(13, 2, 10, 14, 3, 15),
            Block.makeCuboidShape(2, 2, 10, 3, 3, 15),
            Block.makeCuboidShape(3, 2, 13, 4, 3, 15),
            Block.makeCuboidShape(12, 2, 13, 13, 3, 15),
            Block.makeCuboidShape(2, 0, 12, 14, 2, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static VoxelShape SHAPE_S = Stream.of(
            Block.makeCuboidShape(2, 0, 12, 4, 2, 14),
            Block.makeCuboidShape(14, 15, 13, 16, 16, 15),
            Block.makeCuboidShape(14, 15, 0, 16, 16, 3),
            Block.makeCuboidShape(0, 15, 0, 2, 16, 3),
            Block.makeCuboidShape(0, 14, 0, 2, 15, 1),
            Block.makeCuboidShape(14, 14, 0, 16, 15, 1),
            Block.makeCuboidShape(14, 0, 14, 16, 15, 15),
            Block.makeCuboidShape(0, 0, 14, 2, 15, 15),
            Block.makeCuboidShape(6, 14, 6, 10, 15, 10),
            Block.makeCuboidShape(5, 15, 5, 11, 16, 11),
            Block.makeCuboidShape(5, 12, 5, 11, 13, 11),
            Block.makeCuboidShape(4, 13, 4, 12, 14, 12),
            Block.makeCuboidShape(3, 11, 3, 13, 12, 13),
            Block.makeCuboidShape(0, 15, 13, 2, 16, 15),
            Block.makeCuboidShape(12, 0, 12, 14, 2, 14),
            Block.makeCuboidShape(14, 0, 2, 15, 2, 4),
            Block.makeCuboidShape(1, 0, 2, 2, 2, 4),
            Block.makeCuboidShape(3, 2, 3, 13, 11, 13),
            Block.makeCuboidShape(13, 2, 1, 14, 3, 6),
            Block.makeCuboidShape(2, 2, 1, 3, 3, 6),
            Block.makeCuboidShape(3, 2, 1, 4, 3, 3),
            Block.makeCuboidShape(12, 2, 1, 13, 3, 3),
            Block.makeCuboidShape(2, 0, 1, 14, 2, 4)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static VoxelShape SHAPE_E = Stream.of(
            Block.makeCuboidShape(12, 0, 2, 14, 2, 4),
            Block.makeCuboidShape(13, 15, 14, 15, 16, 16),
            Block.makeCuboidShape(0, 15, 14, 3, 16, 16),
            Block.makeCuboidShape(0, 15, 0, 3, 16, 2),
            Block.makeCuboidShape(0, 14, 0, 1, 15, 2),
            Block.makeCuboidShape(0, 14, 14, 1, 15, 16),
            Block.makeCuboidShape(14, 0, 14, 15, 15, 16),
            Block.makeCuboidShape(14, 0, 0, 15, 15, 2),
            Block.makeCuboidShape(6, 14, 6, 10, 15, 10),
            Block.makeCuboidShape(5, 15, 5, 11, 16, 11),
            Block.makeCuboidShape(5, 12, 5, 11, 13, 11),
            Block.makeCuboidShape(4, 13, 4, 12, 14, 12),
            Block.makeCuboidShape(3, 11, 3, 13, 12, 13),
            Block.makeCuboidShape(13, 15, 0, 15, 16, 2),
            Block.makeCuboidShape(12, 0, 12, 14, 2, 14),
            Block.makeCuboidShape(2, 0, 14, 4, 2, 15),
            Block.makeCuboidShape(2, 0, 1, 4, 2, 2),
            Block.makeCuboidShape(3, 2, 3, 13, 11, 13),
            Block.makeCuboidShape(1, 2, 13, 6, 3, 14),
            Block.makeCuboidShape(1, 2, 2, 6, 3, 3),
            Block.makeCuboidShape(1, 2, 3, 3, 3, 4),
            Block.makeCuboidShape(1, 2, 12, 3, 3, 13),
            Block.makeCuboidShape(1, 0, 2, 4, 2, 14)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static VoxelShape SHAPE_W = Stream.of(
            Block.makeCuboidShape(2, 0, 2, 4, 2, 4),
            Block.makeCuboidShape(1, 15, 14, 3, 16, 16),
            Block.makeCuboidShape(13, 15, 14, 16, 16, 16),
            Block.makeCuboidShape(13, 15, 0, 16, 16, 2),
            Block.makeCuboidShape(15, 14, 0, 16, 15, 2),
            Block.makeCuboidShape(15, 14, 14, 16, 15, 16),
            Block.makeCuboidShape(1, 0, 14, 2, 15, 16),
            Block.makeCuboidShape(1, 0, 0, 2, 15, 2),
            Block.makeCuboidShape(6, 14, 6, 10, 15, 10),
            Block.makeCuboidShape(5, 15, 5, 11, 16, 11),
            Block.makeCuboidShape(5, 12, 5, 11, 13, 11),
            Block.makeCuboidShape(4, 13, 4, 12, 14, 12),
            Block.makeCuboidShape(3, 11, 3, 13, 12, 13),
            Block.makeCuboidShape(1, 15, 0, 3, 16, 2),
            Block.makeCuboidShape(2, 0, 12, 4, 2, 14),
            Block.makeCuboidShape(12, 0, 14, 14, 2, 15),
            Block.makeCuboidShape(12, 0, 1, 14, 2, 2),
            Block.makeCuboidShape(3, 2, 3, 13, 11, 13),
            Block.makeCuboidShape(10, 2, 13, 15, 3, 14),
            Block.makeCuboidShape(10, 2, 2, 15, 3, 3),
            Block.makeCuboidShape(13, 2, 3, 15, 3, 4),
            Block.makeCuboidShape(13, 2, 12, 15, 3, 13),
            Block.makeCuboidShape(12, 0, 2, 15, 2, 14)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
}
