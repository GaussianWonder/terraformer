package com.gaussianwonder.terraformer.setup.blocks;

import com.gaussianwonder.terraformer.setup.blocks.containers.MatterRecyclerContainer;
import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTitle;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class MatterRecyclerBlock extends BaseBlock {
    static {
        name = "matter_recycler";
    }

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public MatterRecyclerBlock() {
        super(AbstractBlock.Properties
                .create(Material.IRON)
                .hardnessAndResistance(5.0f, 10.0f)
                .sound(SoundType.ANVIL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .setRequiresTool());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof MatterRecyclerTitle) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("screen.terraformer.matter_recycler");
                    }

                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new MatterRecyclerContainer(i, world, pos, playerInventory, playerEntity);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    // Tile Related Methods Override
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MatterRecyclerTitle();
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

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tileEntity = builder.get(LootParameters.BLOCK_ENTITY);
        if(tileEntity instanceof MatterRecyclerTitle) {
            final List<ItemStack> drops = new ArrayList<>();
            ItemStack stack = new ItemStack(this);

            CompoundNBT tag = new CompoundNBT();
            ((MatterRecyclerTitle)tileEntity).write(tag);

            stack.setTagInfo("BlockEntityTag", tag);
            drops.add(stack);

            return drops;
        }
        else {
            return super.getDrops(state, builder);
        }
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
    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(6, 0, 6, 10, 1, 10),
            Block.makeCuboidShape(6, 2, 6, 10, 3, 10),
            Block.makeCuboidShape(5, 5, 5, 11, 6, 11),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(4, 3, 4, 12, 5, 12),
            Block.makeCuboidShape(3, 6, 3, 13, 8, 13),
            Block.makeCuboidShape(6, 31, 6, 10, 32, 10),
            Block.makeCuboidShape(6, 29, 6, 10, 30, 10),
            Block.makeCuboidShape(5, 26, 5, 11, 27, 11),
            Block.makeCuboidShape(5, 30, 5, 11, 31, 11),
            Block.makeCuboidShape(4, 27, 4, 12, 29, 12),
            Block.makeCuboidShape(3, 24, 3, 13, 26, 13),
            Block.makeCuboidShape(1, 8, 1, 15, 24, 15),
            Block.makeCuboidShape(13, 7, 2, 14, 8, 3),
            Block.makeCuboidShape(13, 4, 1, 15, 7, 3),
            Block.makeCuboidShape(13, 0, 0, 16, 4, 3),
            Block.makeCuboidShape(13, 7, 13, 14, 8, 14),
            Block.makeCuboidShape(13, 4, 13, 15, 7, 15),
            Block.makeCuboidShape(13, 0, 13, 16, 4, 16),
            Block.makeCuboidShape(2, 7, 13, 3, 8, 14),
            Block.makeCuboidShape(1, 4, 13, 3, 7, 15),
            Block.makeCuboidShape(0, 0, 13, 3, 4, 16),
            Block.makeCuboidShape(2, 7, 2, 3, 8, 3),
            Block.makeCuboidShape(1, 4, 1, 3, 7, 3),
            Block.makeCuboidShape(0, 0, 0, 3, 4, 3),
            Block.makeCuboidShape(13, 0, 3, 14, 2, 4),
            Block.makeCuboidShape(13, 0, 12, 14, 2, 13),
            Block.makeCuboidShape(2, 0, 12, 3, 2, 13),
            Block.makeCuboidShape(2, 0, 3, 3, 2, 4),
            Block.makeCuboidShape(12, 0, 2, 13, 2, 4),
            Block.makeCuboidShape(12, 0, 12, 13, 2, 14),
            Block.makeCuboidShape(3, 0, 12, 4, 2, 14),
            Block.makeCuboidShape(3, 0, 2, 4, 2, 4),
            Block.makeCuboidShape(12, 5, 1, 13, 8, 3),
            Block.makeCuboidShape(11, 7, 1, 12, 8, 3),
            Block.makeCuboidShape(3, 7, 1, 4, 8, 3)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.makeCuboidShape(6, 0, 6, 10, 1, 10),
            Block.makeCuboidShape(6, 2, 6, 10, 3, 10),
            Block.makeCuboidShape(5, 5, 5, 11, 6, 11),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(4, 3, 4, 12, 5, 12),
            Block.makeCuboidShape(3, 6, 3, 13, 8, 13),
            Block.makeCuboidShape(6, 31, 6, 10, 32, 10),
            Block.makeCuboidShape(6, 29, 6, 10, 30, 10),
            Block.makeCuboidShape(5, 26, 5, 11, 27, 11),
            Block.makeCuboidShape(5, 30, 5, 11, 31, 11),
            Block.makeCuboidShape(4, 27, 4, 12, 29, 12),
            Block.makeCuboidShape(3, 24, 3, 13, 26, 13),
            Block.makeCuboidShape(1, 8, 1, 15, 24, 15),
            Block.makeCuboidShape(13, 7, 13, 14, 8, 14),
            Block.makeCuboidShape(13, 4, 13, 15, 7, 15),
            Block.makeCuboidShape(13, 0, 13, 16, 4, 16),
            Block.makeCuboidShape(2, 7, 13, 3, 8, 14),
            Block.makeCuboidShape(1, 4, 13, 3, 7, 15),
            Block.makeCuboidShape(0, 0, 13, 3, 4, 16),
            Block.makeCuboidShape(2, 7, 2, 3, 8, 3),
            Block.makeCuboidShape(1, 4, 1, 3, 7, 3),
            Block.makeCuboidShape(0, 0, 0, 3, 4, 3),
            Block.makeCuboidShape(13, 7, 2, 14, 8, 3),
            Block.makeCuboidShape(13, 4, 1, 15, 7, 3),
            Block.makeCuboidShape(13, 0, 0, 16, 4, 3),
            Block.makeCuboidShape(12, 0, 13, 13, 2, 14),
            Block.makeCuboidShape(3, 0, 13, 4, 2, 14),
            Block.makeCuboidShape(3, 0, 2, 4, 2, 3),
            Block.makeCuboidShape(12, 0, 2, 13, 2, 3),
            Block.makeCuboidShape(12, 0, 12, 14, 2, 13),
            Block.makeCuboidShape(2, 0, 12, 4, 2, 13),
            Block.makeCuboidShape(2, 0, 3, 4, 2, 4),
            Block.makeCuboidShape(12, 0, 3, 14, 2, 4),
            Block.makeCuboidShape(13, 5, 12, 15, 8, 13),
            Block.makeCuboidShape(13, 7, 11, 15, 8, 12),
            Block.makeCuboidShape(13, 7, 3, 15, 8, 4)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.makeCuboidShape(6, 0, 6, 10, 1, 10),
            Block.makeCuboidShape(6, 2, 6, 10, 3, 10),
            Block.makeCuboidShape(5, 5, 5, 11, 6, 11),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(4, 3, 4, 12, 5, 12),
            Block.makeCuboidShape(3, 6, 3, 13, 8, 13),
            Block.makeCuboidShape(6, 31, 6, 10, 32, 10),
            Block.makeCuboidShape(6, 29, 6, 10, 30, 10),
            Block.makeCuboidShape(5, 26, 5, 11, 27, 11),
            Block.makeCuboidShape(5, 30, 5, 11, 31, 11),
            Block.makeCuboidShape(4, 27, 4, 12, 29, 12),
            Block.makeCuboidShape(3, 24, 3, 13, 26, 13),
            Block.makeCuboidShape(1, 8, 1, 15, 24, 15),
            Block.makeCuboidShape(2, 7, 13, 3, 8, 14),
            Block.makeCuboidShape(1, 4, 13, 3, 7, 15),
            Block.makeCuboidShape(0, 0, 13, 3, 4, 16),
            Block.makeCuboidShape(2, 7, 2, 3, 8, 3),
            Block.makeCuboidShape(1, 4, 1, 3, 7, 3),
            Block.makeCuboidShape(0, 0, 0, 3, 4, 3),
            Block.makeCuboidShape(13, 7, 2, 14, 8, 3),
            Block.makeCuboidShape(13, 4, 1, 15, 7, 3),
            Block.makeCuboidShape(13, 0, 0, 16, 4, 3),
            Block.makeCuboidShape(13, 7, 13, 14, 8, 14),
            Block.makeCuboidShape(13, 4, 13, 15, 7, 15),
            Block.makeCuboidShape(13, 0, 13, 16, 4, 16),
            Block.makeCuboidShape(2, 0, 12, 3, 2, 13),
            Block.makeCuboidShape(2, 0, 3, 3, 2, 4),
            Block.makeCuboidShape(13, 0, 3, 14, 2, 4),
            Block.makeCuboidShape(13, 0, 12, 14, 2, 13),
            Block.makeCuboidShape(3, 0, 12, 4, 2, 14),
            Block.makeCuboidShape(3, 0, 2, 4, 2, 4),
            Block.makeCuboidShape(12, 0, 2, 13, 2, 4),
            Block.makeCuboidShape(12, 0, 12, 13, 2, 14),
            Block.makeCuboidShape(3, 5, 13, 4, 8, 15),
            Block.makeCuboidShape(4, 7, 13, 5, 8, 15),
            Block.makeCuboidShape(12, 7, 13, 13, 8, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.makeCuboidShape(6, 0, 6, 10, 1, 10),
            Block.makeCuboidShape(6, 2, 6, 10, 3, 10),
            Block.makeCuboidShape(5, 5, 5, 11, 6, 11),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(4, 3, 4, 12, 5, 12),
            Block.makeCuboidShape(3, 6, 3, 13, 8, 13),
            Block.makeCuboidShape(6, 31, 6, 10, 32, 10),
            Block.makeCuboidShape(6, 29, 6, 10, 30, 10),
            Block.makeCuboidShape(5, 26, 5, 11, 27, 11),
            Block.makeCuboidShape(5, 30, 5, 11, 31, 11),
            Block.makeCuboidShape(4, 27, 4, 12, 29, 12),
            Block.makeCuboidShape(3, 24, 3, 13, 26, 13),
            Block.makeCuboidShape(1, 8, 1, 15, 24, 15),
            Block.makeCuboidShape(2, 7, 2, 3, 8, 3),
            Block.makeCuboidShape(1, 4, 1, 3, 7, 3),
            Block.makeCuboidShape(0, 0, 0, 3, 4, 3),
            Block.makeCuboidShape(13, 7, 2, 14, 8, 3),
            Block.makeCuboidShape(13, 4, 1, 15, 7, 3),
            Block.makeCuboidShape(13, 0, 0, 16, 4, 3),
            Block.makeCuboidShape(13, 7, 13, 14, 8, 14),
            Block.makeCuboidShape(13, 4, 13, 15, 7, 15),
            Block.makeCuboidShape(13, 0, 13, 16, 4, 16),
            Block.makeCuboidShape(2, 7, 13, 3, 8, 14),
            Block.makeCuboidShape(1, 4, 13, 3, 7, 15),
            Block.makeCuboidShape(0, 0, 13, 3, 4, 16),
            Block.makeCuboidShape(3, 0, 2, 4, 2, 3),
            Block.makeCuboidShape(12, 0, 2, 13, 2, 3),
            Block.makeCuboidShape(12, 0, 13, 13, 2, 14),
            Block.makeCuboidShape(3, 0, 13, 4, 2, 14),
            Block.makeCuboidShape(2, 0, 3, 4, 2, 4),
            Block.makeCuboidShape(12, 0, 3, 14, 2, 4),
            Block.makeCuboidShape(12, 0, 12, 14, 2, 13),
            Block.makeCuboidShape(2, 0, 12, 4, 2, 13),
            Block.makeCuboidShape(1, 5, 3, 3, 8, 4),
            Block.makeCuboidShape(1, 7, 4, 3, 8, 5),
            Block.makeCuboidShape(1, 7, 12, 3, 8, 13)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
}
