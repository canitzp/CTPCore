package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.CTPCore;
import de.canitzp.ctpcore.inventory.CTPGuiHandler;
import de.canitzp.ctpcore.property.ExtendedDirection;
import de.canitzp.ctpcore.registry.IRegistryEntry;
import de.canitzp.ctpcore.registry.TileEntityProvider;
import de.canitzp.ctpcore.util.NBTSaveType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public class BlockContainerBase extends BlockBase implements ITileEntityProvider {

    private Object mod;
    protected Class<? extends TileEntityBase> tileClass;
    protected int guiId = -1;

    public BlockContainerBase(Material material, ResourceLocation resource, Class<? extends TileEntityBase> tileClass) {
        super(material, resource);
        this.isBlockContainer = true;
        this.tileClass = tileClass;
        this.setDefaultState(this.blockState.getBaseState().withProperty(getFacing(), ExtendedDirection.ExtendedFacing.NORTH));
    }

    public BlockContainerBase addGuiContainer(Object mod, Class<? extends GuiContainer> gui, Class<? extends Container> con){
        this.guiId = CTPGuiHandler.addGuiContainer(gui, con);
        this.mod = mod;
        return this;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
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
        return state.getValue(getFacing()).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(getFacing(), ExtendedDirection.ExtendedFacing.values()[meta]);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getFacing());
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(getFacing(), ExtendedDirection.ExtendedFacing.getDirectionFromLiving(pos, placer));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        TileEntity tile = world.getTileEntity(pos);
        if(tile != null && tile instanceof TileEntityBase){
            if(stack.hasTagCompound() && stack.getTagCompound().hasKey("DropData")){
                ((TileEntityBase) tile).readFromNBT(stack.getTagCompound().getCompoundTag("DropData"), NBTSaveType.DROP_BLOCK);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
        TileEntity tile = world.getTileEntity(pos);
        System.out.println(tile);
        if(tile != null && tile instanceof TileEntityBase){
            ItemStack stack = new ItemStack(this.getItemDropped(state, ((World) world).rand, fortune));
            if(!stack.isEmpty()){
                if(!stack.hasTagCompound()){
                    stack.setTagCompound(new NBTTagCompound());
                }
                NBTTagCompound nbt = new NBTTagCompound();
                ((TileEntityBase) tile).writeToNBT(nbt, NBTSaveType.DROP_BLOCK);
                stack.getTagCompound().setTag("DropData", nbt);
                return Collections.singletonList(stack);
            }
        }
        return super.getDrops(world, pos, state, fortune);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
        if(!player.capabilities.isCreativeMode){
            this.dropBlockAsItem(world, pos, state, 0);
            //dirty workaround because of Forge calling Item.onBlockStartBreak() twice
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
        if(!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound nbt = stack.getTagCompound().hasKey("DropData") ? stack.getTagCompound().getCompoundTag("DropData") : new NBTTagCompound();
        addTooltip(stack, player, tooltip, advanced, nbt);
    }

    @Override
    public IRegistryEntry[] getRegisterElements(){
        return ArrayUtils.addAll(super.getRegisterElements(), new TileEntityProvider(this.tileClass, super.getRegisterName()));
    }

    public ExtendedDirection getFacing(){
        return ExtendedDirection.create("direction");
    }

    @SideOnly(Side.CLIENT)
    public void addTooltip(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced, NBTTagCompound nbt){
        tooltip.addAll(getDescriptionList(stack, player, advanced));
    }

    @SideOnly(Side.CLIENT)
    public String getDescription(ItemStack stack, EntityPlayer player, boolean advanced){
        return null;
    }

    @SideOnly(Side.CLIENT)
    public List<String> getDescriptionList(ItemStack stack, EntityPlayer player, boolean advanced){
        String s = getDescription(stack, player, advanced);
        return s != null ? Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(s, 150) : Collections.emptyList();
    }
}
