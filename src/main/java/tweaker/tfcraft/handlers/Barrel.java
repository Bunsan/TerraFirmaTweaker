package tweaker.tfcraft.handlers;

import static tweaker.tfcraft.InputHelper.toFluid;
import static tweaker.tfcraft.InputHelper.toIItemStack;
import static tweaker.tfcraft.InputHelper.toILiquidStack;
import static tweaker.tfcraft.InputHelper.toStack;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tweaker.tfcraft.BaseListAddition;
import tweaker.tfcraft.BaseListRemoval;
import tweaker.tfcraft.LogHelper;
import tweaker.tfcraft.StackHelper;
import tweaker.tfcraft.TFCHelper;

import com.bioxx.tfc.api.Crafting.BarrelLiquidToLiquidRecipe;
import com.bioxx.tfc.api.Crafting.BarrelManager;
import com.bioxx.tfc.api.Crafting.BarrelRecipe;
@ZenClass("mods.tfcraft.Barrel")
public class Barrel {
    
    public static final String name = "TFC Barrel";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @param outputFluid If you want this recipe consume fluid, <code>outputFluid=inputFluid</code>
	 * @param time How long the recipe finished. Unit is TFC hour.
	 * @param techLv Set to 0 if you want vessel to be able to use your new recipe, otherwise you will set to 1
	 * @param removeFluid Set false if there is special need
	 * @param allowAny By default it is <code>true</code>
	 */
	@ZenMethod
	public static void addSealed(IItemStack inputItem, ILiquidStack inputFluid, IItemStack outputItem, ILiquidStack outputFluid, int time, int techLv, boolean removeFluid, boolean allowAny){
		MineTweakerAPI.apply(new AddRecipe(new BarrelRecipe(toStack(inputItem), toFluid(inputFluid), toStack(outputItem), toFluid(outputFluid), time).setMinTechLevel(techLv).setRemovesLiquid(removeFluid).setAllowAnyStack(allowAny)));
	}
	
	
	@ZenMethod
	public static void addUnsealed(IItemStack inputItem, ILiquidStack inputFluid, IItemStack outputItem, ILiquidStack outputFluid, int techLv, boolean removeFluid, boolean allowAny){
		MineTweakerAPI.apply(new AddRecipe(new BarrelRecipe(toStack(inputItem), toFluid(inputFluid), toStack(outputItem), toFluid(outputFluid)).setSealedRecipe(false).setMinTechLevel(techLv).setRemovesLiquid(removeFluid).setAllowAnyStack(allowAny)));
	}
	
	/**
	 * 
	 * @param inputInBarrel The input fluid which is in barrel
	 * @param input The input fluid which is in a fluid container, like a water bucket
	 * @param output The output fluid
	 */
	@ZenMethod
	public static void addFluidToFluid(ILiquidStack inputInBarrel, ILiquidStack input, ILiquidStack output){
		MineTweakerAPI.apply(new AddRecipe(new BarrelLiquidToLiquidRecipe(toFluid(inputInBarrel), toFluid(input), toFluid(output))));
	}
	
	private static class AddRecipe extends BaseListAddition<BarrelRecipe> {
		public AddRecipe(BarrelRecipe recipe){
			super(Barrel.name, TFCHelper.barrelRecipes);
			recipes.add(recipe);
		}
		
		@Override
		protected String getRecipeInfo(BarrelRecipe recipe) {
		    if(recipe.getRecipeOutIS() != null)
		        return LogHelper.getStackDescription(recipe.getRecipeOutIS());
		    else if(recipe.getRecipeOutFluid() != null)
		        return LogHelper.getStackDescription(recipe.getRecipeOutFluid());
		    else
		        return "Unknown output";
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    @ZenMethod
    public static void remove(IIngredient output){
        List<BarrelRecipe> recipes = new LinkedList<BarrelRecipe>();
        
        // Test for item outputs
        for (BarrelRecipe recipe : BarrelManager.getInstance().getRecipes()){
            if (recipe.getRecipeOutIS() != null && StackHelper.matches(output, toIItemStack(recipe.getRecipeOutIS()))) {
                recipes.add(recipe);
            }
        }
        
        // Test for liquid outputs
        for (BarrelRecipe recipe : BarrelManager.getInstance().getRecipes()){
            if (recipe.getRecipeOutFluid() != null && StackHelper.matches(output, toILiquidStack(recipe.getRecipeOutFluid()))) {
                recipes.add(recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveRecipe(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Barrel.name, output.toString()));
        }
    }
	
	private static class RemoveRecipe extends BaseListRemoval<BarrelRecipe> {
		public RemoveRecipe(List<BarrelRecipe> recipes){
			super(Barrel.name, TFCHelper.barrelRecipes, recipes);
		}
		
        @Override
        protected String getRecipeInfo(BarrelRecipe recipe) {
            if(recipe.getRecipeOutIS() != null)
                return LogHelper.getStackDescription(recipe.getRecipeOutIS());
            else if(recipe.getRecipeOutFluid() != null)
                return LogHelper.getStackDescription(recipe.getRecipeOutFluid());
            else
                return "Unknown output";
        }
	}
}
