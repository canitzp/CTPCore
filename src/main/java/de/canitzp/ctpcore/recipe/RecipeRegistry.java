package de.canitzp.ctpcore.recipe;

import de.canitzp.ctpcore.util.StackUtil;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class RecipeRegistry{

    private static HashMap<String, HashMap<ItemStack, ICustomRecipe>> recipes = new HashMap<>();

    public static void addRecipe(ICustomRecipe recipe){
        if(!recipes.containsKey(recipe.getCategory())){
            recipes.put(recipe.getCategory(), new HashMap<>());
        }
        HashMap<ItemStack, ICustomRecipe> categorizedRecipes = recipes.get(recipe.getCategory());
        if(!containsStack(categorizedRecipes, recipe.getKey())){
            categorizedRecipes.put(recipe.getKey(), recipe);
        }
    }

    public static void removeRecipe(String cat, ItemStack stack){
        if(recipes.containsKey(cat)){
            if(containsStack(recipes.get(cat), stack)){
                recipes.get(cat).remove(getKeyStack(recipes.get(cat), stack).getKey());
            }
        }
    }

    public static void removeRecipe(String cat, ICustomRecipe recipe){
        removeRecipe(cat, recipe.getKey());
    }

    public static boolean hasRecipeFor(String cat, ItemStack input){
        return recipes.containsKey(cat) && containsStack(recipes.get(cat), input);
    }

    public static ICustomRecipe getRecipeFor(String cat, ItemStack input){
        if(hasRecipeFor(cat, input)){
            return getKeyStack(recipes.get(cat), input).getValue();
        }
        return null;
    }

    private static Pair<ItemStack, ICustomRecipe> getKeyStack(HashMap<ItemStack, ICustomRecipe> map, ItemStack key){
        for (Map.Entry<ItemStack, ICustomRecipe> entry : map.entrySet()){
            if (StackUtil.compareItemStacks(key, entry.getKey())){
                return Pair.of(entry.getKey(), entry.getValue());
            }
        }
        return Pair.of(ItemStack.EMPTY, null);
    }

    private static boolean containsStack(HashMap<ItemStack, ICustomRecipe> map, ItemStack key){
        return !getKeyStack(map, key).getKey().isEmpty();
    }

}
