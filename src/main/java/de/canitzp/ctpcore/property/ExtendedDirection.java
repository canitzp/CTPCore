package de.canitzp.ctpcore.property;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

/**
 * @author canitzp
 */
public class ExtendedDirection extends PropertyEnum<ExtendedDirection.ExtendedFacing>{

    public ExtendedDirection(String name, Collection<ExtendedFacing> allowedValues){
        super(name, ExtendedFacing.class, allowedValues);
    }

    public static ExtendedDirection create(String name){
        return create(name, Predicates.alwaysTrue());
    }

    public static ExtendedDirection create(String name, Predicate<ExtendedFacing> filter){
        return create(name, Collections2.filter(Lists.newArrayList(ExtendedFacing.values()), filter));
    }

    public static ExtendedDirection create(String name, Collection<ExtendedFacing> values){
        return new ExtendedDirection(name, values);
    }

    public enum ExtendedFacing implements IStringSerializable{
        NORTH(EnumFacing.NORTH),
        SOUTH(EnumFacing.SOUTH),
        WEST(EnumFacing.WEST),
        EAST(EnumFacing.EAST),
        UP_NORTH(EnumFacing.UP),
        UP_SOUTH(EnumFacing.UP),
        UP_WEST(EnumFacing.UP),
        UP_EAST(EnumFacing.UP),
        DOWN_NORTH(EnumFacing.DOWN),
        DOWN_SOUTH(EnumFacing.DOWN),
        DOWN_WEST(EnumFacing.DOWN),
        DOWN_EAST(EnumFacing.DOWN);

        private EnumFacing vanillaFacing;

        ExtendedFacing(EnumFacing vanillaFacing){
            this.vanillaFacing = vanillaFacing;
        }

        @Override
        public String getName(){
            return this.name().toLowerCase();
        }

        public static ExtendedFacing getDirectionFromLiving(BlockPos pos, EntityLivingBase entity){
            EnumFacing facing = EnumFacing.getDirectionFromEntityLiving(pos, entity);
            if(!(facing.equals(EnumFacing.NORTH) && facing.equals(EnumFacing.SOUTH) && facing.equals(EnumFacing.WEST) && facing.equals(EnumFacing.EAST))){
                EnumFacing horizontal = entity.getHorizontalFacing().getOpposite();
                switch(facing){
                    case UP: {
                        switch(horizontal){
                            case NORTH: return UP_NORTH;
                            case SOUTH: return UP_SOUTH;
                            case WEST: return UP_WEST;
                            case EAST: return UP_EAST;
                        }
                        break;
                    }
                    case DOWN: {
                        switch(horizontal){
                            case NORTH: return DOWN_NORTH;
                            case SOUTH: return DOWN_SOUTH;
                            case WEST: return DOWN_WEST;
                            case EAST: return DOWN_EAST;
                        }
                    }
                }
            }
            return facing.equals(EnumFacing.NORTH) ? NORTH : facing.equals(EnumFacing.SOUTH) ? SOUTH : facing.equals(EnumFacing.WEST) ? WEST : EAST;
        }

        public EnumFacing getVanillaFacing(){
            return this.vanillaFacing;
        }
    }

}
