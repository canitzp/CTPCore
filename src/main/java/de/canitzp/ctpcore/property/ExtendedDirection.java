package de.canitzp.ctpcore.property;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
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
        UP_NORTH(EnumFacing.UP, EnumFacing.NORTH),
        UP_SOUTH(EnumFacing.UP, EnumFacing.SOUTH),
        UP_WEST(EnumFacing.UP, EnumFacing.WEST),
        UP_EAST(EnumFacing.UP, EnumFacing.EAST),
        DOWN_NORTH(EnumFacing.DOWN, EnumFacing.NORTH),
        DOWN_SOUTH(EnumFacing.DOWN, EnumFacing.SOUTH),
        DOWN_WEST(EnumFacing.DOWN, EnumFacing.WEST),
        DOWN_EAST(EnumFacing.DOWN, EnumFacing.EAST);

        private EnumFacing vanillaFacing, secondAttribute;

        ExtendedFacing(EnumFacing vanillaFacing, EnumFacing secondAttribute){
            this.vanillaFacing = vanillaFacing;
            this.secondAttribute = secondAttribute;
        }

        ExtendedFacing(EnumFacing vanillaFacing){
            this(vanillaFacing, null);
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

        public EnumFacing getAlternativeFacing(){
            return secondAttribute;
        }
    }

}
