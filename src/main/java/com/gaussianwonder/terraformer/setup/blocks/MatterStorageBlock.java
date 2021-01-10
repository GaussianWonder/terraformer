package com.gaussianwonder.terraformer.setup.blocks;

import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class MatterStorageBlock extends BaseBlock {
    static {
        name = "matter_storage";
    }

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public MatterStorageBlock() {
        super(AbstractBlock.Properties
                .create(Material.IRON)
                .hardnessAndResistance(5.0f, 10.0f)
                .sound(SoundType.ANVIL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .setRequiresTool());
    }

//TODO Creative middle button click copy - check NBT tags
//    @Override
//    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
//        return null;
//    }

    // Model Related Methods Override
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(BlockStateProperties.ATTACHED))
            return SHAPE_CONNECTED;
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState()
                .with(FACING, context.getPlacementHorizontalFacing().getOpposite())
                .with(BlockStateProperties.ATTACHED, isConnected(context.getWorld(), context.getPos()));
    }

    private boolean isConnected(World world, BlockPos pos) {
        BlockPos top = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        if(world.isBlockPresent(top))
            return world.getBlockState(top).getBlock() instanceof MatterRecyclerBlock;
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, BlockStateProperties.ATTACHED);
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
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        BlockPos top = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        if(worldIn.isBlockPresent(top)) {
            TileEntity tileEntity = worldIn.getTileEntity(top);
            worldIn.setBlockState(
                    pos,
                    this.getDefaultState()
                            .with(FACING, state.get(FACING))
                            .with(BlockStateProperties.ATTACHED, tileEntity instanceof MatterRecyclerTile ? true : false),
                    1 | 2
            );
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        if (state.get(BlockStateProperties.ATTACHED))
            return SHAPE_CONNECTED;
        return SHAPE;
    }

    // VOXEL SHAPES
    private static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(0, 0, 1, 1, 16, 2),
            Block.makeCuboidShape(0, 0, 14, 1, 16, 15),
            Block.makeCuboidShape(15, 0, 1, 16, 16, 2),
            Block.makeCuboidShape(15, 0, 14, 16, 16, 15),
            Block.makeCuboidShape(14, 0, 0, 16, 16, 1),
            Block.makeCuboidShape(2, 0, 0, 14, 2, 1),
            Block.makeCuboidShape(2, 14, 0, 14, 16, 1),
            Block.makeCuboidShape(15, 0, 2, 16, 2, 14),
            Block.makeCuboidShape(15, 14, 2, 16, 16, 14),
            Block.makeCuboidShape(2, 0, 15, 14, 2, 16),
            Block.makeCuboidShape(2, 14, 15, 14, 16, 16),
            Block.makeCuboidShape(0, 0, 2, 1, 2, 14),
            Block.makeCuboidShape(0, 14, 2, 1, 16, 14),
            Block.makeCuboidShape(14, 0, 15, 16, 16, 16),
            Block.makeCuboidShape(0, 0, 0, 2, 16, 1),
            Block.makeCuboidShape(0, 0, 15, 2, 16, 16),
            Block.makeCuboidShape(4, 3, 4, 12, 13, 12)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_CONNECTED = Stream.of(
            Block.makeCuboidShape(0, 0, 1, 1, 16, 2),
            Block.makeCuboidShape(0, 0, 14, 1, 16, 15),
            Block.makeCuboidShape(15, 0, 1, 16, 16, 2),
            Block.makeCuboidShape(15, 0, 14, 16, 16, 15),
            Block.makeCuboidShape(14, 0, 0, 16, 16, 1),
            Block.makeCuboidShape(2, 0, 0, 14, 2, 1),
            Block.makeCuboidShape(2, 14, 0, 14, 16, 1),
            Block.makeCuboidShape(15, 0, 2, 16, 2, 14),
            Block.makeCuboidShape(15, 14, 2, 16, 16, 14),
            Block.makeCuboidShape(2, 0, 15, 14, 2, 16),
            Block.makeCuboidShape(2, 14, 15, 14, 16, 16),
            Block.makeCuboidShape(0, 0, 2, 1, 2, 14),
            Block.makeCuboidShape(0, 14, 2, 1, 16, 14),
            Block.makeCuboidShape(14, 0, 15, 16, 16, 16),
            Block.makeCuboidShape(0, 0, 0, 2, 16, 1),
            Block.makeCuboidShape(0, 0, 15, 2, 16, 16),
            Block.makeCuboidShape(4, 3, 4, 12, 13, 12),
            Block.makeCuboidShape(6, 13, 6, 10, 16, 10)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
}
