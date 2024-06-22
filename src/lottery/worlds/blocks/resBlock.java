package lottery.worlds.blocks;

import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.ItemDisplay;
import mindustry.ui.ItemImage;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class resBlock extends Block {
    public Item res;
    public int amount;

    public resBlock(Item item, int amount) {
        super(item.name + "-lot-" + amount);
        buildVisibility = BuildVisibility.hidden;
        res = item;
        this.amount = amount;
        localizedName = amount + item.localizedName;
        requirements(Category.effect, ItemStack.with(item, amount));
        uiIcon = item.uiIcon;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean canBeBuilt() {
        return false;
    }
}
