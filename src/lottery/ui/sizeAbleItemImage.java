package lottery.ui;

import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import mindustry.core.UI;
import mindustry.type.ItemStack;
import mindustry.type.PayloadStack;
import mindustry.ui.Styles;

public class sizeAbleItemImage extends Stack {

    public sizeAbleItemImage(TextureRegion region, int amount, float size){

        add(new Table(o -> {
            o.left();
            o.add(new Image(region)).size(size).scaling(Scaling.fit);
        }));

        if(amount != 0){
            add(new Table(t -> {
                t.left().bottom();
                t.add(amount >= 1000 ? UI.formatAmount(amount) : amount + "").style(Styles.outlineLabel).fontScale(size/32);
                t.pack();
            }));
        }
    }

    public sizeAbleItemImage(ItemStack stack, float size){
        this(stack.item.uiIcon, stack.amount, size);
    }

    public sizeAbleItemImage(ItemStack stack){
        this(stack, 32);
    }

    public sizeAbleItemImage(PayloadStack stack){
        this(stack.item.uiIcon, stack.amount, 32);
    }
}
