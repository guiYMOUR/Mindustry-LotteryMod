package lottery.ui;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Time;
import lottery.contents.LFx;
import lottery.worlds.blocks.LotteryBlock;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import universecore.ui.elements.BloomGroup;
import universecore.ui.elements.SceneEffect;

import static lottery.contents.LUI.lotteryRes;
import static mindustry.gen.Tex.whiteui;

public class BeforeLottery extends BaseDialog {
    private final Color tc = new Color();
    private float pull = 0;
    private float timer = 0;
    public BeforeLottery() {
        super("Lucky");
    }

    public void show(Seq<UnlockableContent> us, Seq<Color> uc, IntSeq tier, LotteryBlock.mainBuild owner){
        int i = getMaxIndex(tier);
        Color c = uc.get(i);

        cont.clear();
        pull = 0;
        timer = 0;

        cont.add("往右拉动条来打开(或者按esc跳过)").pad(10).center().row();
        cont.add("pull right to open(Or press ESC to skip)").pad(10).center().row();
        cont.add("该页面尚未完成").pad(10).center().row();
        cont.add("this page is not yet completed...").pad(10).center().row();
        BloomGroup bg = new BloomGroup();
        bg.setClip(true);
        bg.touchable = Touchable.disabled;

        Slider slider = new Slider(0, 100, 1, false);
        slider.setValue(pull);
        slider.changed(() -> {
            pull = slider.getValue();
        });
        slider.change();
        var tb = cont.table(Styles.grayPanel, table -> {
            table.table(sld -> {
                sld.update(() -> {
                    if(pull >= 10){
                        //TODO fx
                        //if((timer += Time.delta) > 15){
                        //timer = 0;
                        SceneEffect effectAp = SceneEffect.showOnGroup(bg, LFx.lcr, sld.getX(Align.center), sld.getY(Align.center), 0, c, 20f);
                        effectAp.scaleX = 8;
                        effectAp.scaleY = 8;
                        //}
                    }
                    if(pull >= 95){
                        Core.app.post(() -> {
                            hide();
                            lotteryRes.show(us, uc, tier, owner);
                        });
                    }
                    sld.background(((TextureRegionDrawable) whiteui).tint(tc.set(c).a(pull/20f)));
                    sld.setWidth(Math.min(Core.graphics.getWidth() / 1.2f, 460f) * pull/135f);
                });
            }).height(slider.getHeight() + 4f).padTop(4f).left();
            table.add(slider).width(Math.min(Core.graphics.getWidth() / 1.2f, 460f)).left().padTop(4f);
            table.fill(upper -> upper.add(bg).grow());
        }).width(Core.graphics.getWidth()/2f).height(Core.graphics.getHeight()/3f).get();

        cont.add(bg).update(e -> e.setBounds(tb.x, tb.y, tb.getWidth(), tb.getHeight()));

        cont.row();

        cont.add("the color of pane is the highest tier which you get").pad(10).center().row();
        cont.add("yes, i want to make it like arknights but....").pad(10).center().row();
        cont.add("i also have my own idea, just after ExtraUtilitiesMod update...").pad(10).center().row();
        cont.add("maybe?").pad(10).center().row();

        buttons.clear();
        buttons.defaults().size(210, 50f);
        buttons.button("skip", Icon.right, () -> {
            hide();
            lotteryRes.show(us, uc, tier, owner);
        }).size(210, 50f);

        keyDown((key) -> {
            if (key == KeyCode.escape || key == KeyCode.back) {
                Core.app.post(() -> {
                    hide();
                    lotteryRes.show(us, uc, tier, owner);
                });
            }

        });

        show();
    }

    public int getMaxIndex(IntSeq seq){
        int max = seq.get(0);
        for(int i = 1; i < seq.size; i++){
            if(seq.get(i) > max) max = seq.get(i);
        }
        return seq.indexOf(max);
    }
}
