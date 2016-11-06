package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.CTPCore;
import de.canitzp.ctpcore.inventory.CTPGuiHandler;
import de.canitzp.ctpcore.registry.IRegistryEntry;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author canitzp
 */
public class BlockContainerBase extends BlockBase implements ITileEntityProvider {

    private final Object mod;
    protected Class<? extends TileEntityBase> tileClass;
    protected int guiId = -1;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockContainerBase(Object mod, Material material, ResourceLocation resource, Class<? extends TileEntityBase> tileClass) {
        super(material, resource);
        this.mod = mod;
        this.isBlockContainer = true;
        this.tileClass = tileClass;
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public BlockContainerBase addGuiContainer(Class<? extends GuiContainer> gui, Class<? extends Container> con){
        this.guiId = CTPGuiHandler.addGuiContainer(gui, con);
        return this;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(guiId >= 0){
            if(!worldIn.isRemote){
                if(CTPGuiHandler.REGISTERED_MODS.contains(this.mod)){
                    playerIn.openGui(this.mod, guiId, worldIn, pos.getX(), pos.getY(), pos.getZ());
                } else {
                    CTPCore.logger.error("The mod '" + this.mod + "' isn't registered in the CTPGuiHandler! Please call CTPGuiHandler.registerMod(ModInstance) first!");
                }
            }
            return true;
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param){
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }
    @Override
    public TileEntityBase createNewTileEntity(World worldIn, int meta) {
        try {
            return tileClass.newInstance();
        } catch (Exception e) {
            CTPCore.logger.fatal("Creating a TileEntity threw an exception!", e);
            return null;
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public IRegistryEntry[] getRegisterElements(){
        return ArrayUtils.addAll(super.getRegisterElements(), createNewTileEntity(null, 0));
    }
}
