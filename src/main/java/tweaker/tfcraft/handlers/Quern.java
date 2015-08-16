package tweaker.tfcraft.handlers;

import static tweaker.tfcraft.InputHelper.toIItemStack;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tweaker.tfcraft.BaseListAddition;
import tweaker.tfcraft.BaseListRemoval;
import tweaker.tfcraft.InputHelper;
import tweaker.tfcraft.LogHelper;
import tweaker.tfcraft.StackHelper;
import tweaker.tfcraft.TFCHelper;

import com.bioxx.tfc.api.Crafting.QuernManager;
import com.bioxx.tfc.api.Crafting.QuernRecipe;

@ZenClass("mods.tfcraft.Quern")
public class Quern {

	public static final String name = "TFC Quern";

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void add(IItemStack input, IItemStack output) {
		MineTweakerAPI.apply(new AddRecipe(new QuernRecipe(InputHelper.toStack(input), InputHelper.toStack(output))));
	}

	private static class AddRecipe extends BaseListAddition<QuernRecipe> {
		public AddRecipe(QuernRecipe recipe) {
			super(Quern.name, TFCHelper.quernRecipes);
			recipes.add(recipe);
		}

		@Override
		public void apply() {
			super.apply();

			for (QuernRecipe recipe : successful) {
				TFCHelper.quernVaildItems.add(recipe.getInItem());
			}
		}

		@Override
		public void undo() {
			for (QuernRecipe recipe : successful) {
				TFCHelper.quernVaildItems.remove(recipe.getInItem());
			}

			super.undo();
		}

		@Override
		protected String getRecipeInfo(QuernRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getResult());
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void remove(IIngredient output) {
		List<QuernRecipe> recipes = new LinkedList<QuernRecipe>();

		for (QuernRecipe recipe : QuernManager.getInstance().getRecipes()) {
			if (recipe != null && recipe.getResult() != null && StackHelper.matches(output, toIItemStack(recipe.getResult()))) {
				recipes.add(recipe);
			}
		}

		MineTweakerAPI.apply(new RemoveRecipe(recipes));
	}

	private static class RemoveRecipe extends BaseListRemoval<QuernRecipe> {
		public RemoveRecipe(List<QuernRecipe> recipes) {
			super(Quern.name, TFCHelper.quernRecipes, recipes);
		}

		@Override
		public void apply() {
			for (QuernRecipe recipe : recipes) {
				TFCHelper.quernVaildItems.remove(recipe.getInItem());
			}

			super.apply();
		}

		@Override
		public void undo() {
			super.undo();

			for (QuernRecipe recipe : successful) {
				TFCHelper.quernVaildItems.add(recipe.getInItem());
			}
		}

		@Override
		protected String getRecipeInfo(QuernRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getResult());
		}
	}

}
