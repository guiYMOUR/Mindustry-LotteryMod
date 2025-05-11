package lottery.ui;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.actions.Actions;
import arc.scene.style.BaseDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Time;
import lottery.contents.LFx;
import lottery.contents.popAll;
import lottery.worlds.blocks.LotteryBlock;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import universecore.ui.elements.BloomGroup;
import universecore.ui.elements.SceneEffect;

public class LotteryRes extends BaseDialog{
    private float floatTime;

    public LotteryRes() {
        super("Lucky");
    }

    public void show(Seq<UnlockableContent> us, Seq<Color> uc, IntSeq tier, LotteryBlock.mainBuild owner){
        floatTime = 55;
        cont.clear();

        float h = Core.graphics.getHeight()/1.1f;
        if(us.size == 0 || us.size != uc.size || us.size != tier.size) return;
        BloomGroup bg = new BloomGroup();//预先创建泛光组以放置特效
        bg.setClip(true);
        var p = cont.pane(tb -> {
            tb.update(() -> floatTime = Math.max(0f, floatTime - Time.delta));
            for (int i = 0; i < us.size; i++) {
                var u = us.get(i);
                int finalI = i;
                Color c = uc.get(i);
                float cb1 = c.toFloatBits();
                float cb2 = Color.clear.toFloatBits();
                Color ca = c.cpy().a(0.4f);
                //((TextureRegionDrawable) Tex.whiteui).tint(uc.get(i)),
                tb.table(r -> {
                    r.setBackground(new BaseDrawable(){
                        @Override
                        public void draw(float x, float y, float width, float height) {
                            Fill.quad(x, y, cb2,
                                    x + width, y, cb2,
                                    x + width, y + height/4, cb1,
                                    x , y + height/4,cb1
                            );
                            Fill.quad(x, y + height/4, cb1,
                                    x + width, y + height/4, cb1,
                                    x + width, y + height/4 * 3, cb1,
                                    x , y + height/4 * 3, cb1
                            );
                            Fill.quad(x, y + height/4 * 3, cb1,
                                    x + width, y + height/4 * 3, cb1,
                                    x + width, y + height, cb2,
                                    x , y + height, cb2
                            );
                        }
                    });

                    r.actions(
                            Actions.moveBy(0, h * (finalI % 2 == 0 ? 1 : -1)),
                            Actions.delay(finalI * 0.05f),
                            Actions.moveBy(0, h * (finalI % 2 == 0 ? -1 : 1) * 0.98f, 0.5f, Interp.pow2Out),
                            Actions.run(() -> {
                                SceneEffect effect = SceneEffect.showOnGroup(bg, LFx.lpr, r.getX(Align.center), r.getY(Align.center), 0, c, h * 1.01f + tier.get(finalI) * h/8f); //对齐到中心位置，注意，bg的原点与此处的父级对齐的原点是一致的
                                effect.scaleX = 8;
                                effect.scaleY = 8;
                            })

                    );
                    r.update(() -> {
                        if (floatTime <= 0.001f){
                            if(Mathf.chance(0.04f * tier.get(finalI) + 0.04f)){
                                SceneEffect effectAp = SceneEffect.showOnGroup(bg, LFx.lcr, r.getX(Align.center), r.getY(Align.center), 0, ca, tier.get(finalI) * h/8f);
                                effectAp.scaleX = 8;
                                effectAp.scaleY = 8;
                                SceneEffect effect = SceneEffect.showOnGroup(bg, LFx.lcr, r.getX(Align.center), r.getY(Align.center), 0, c, tier.get(finalI) * h/8f);
                                effect.scaleX = 8;
                                effect.scaleY = 8;
                            }
                        }
                    });

                    //r.add(new Image(u.uiIcon)).size(45);
                    r.stack(
                            new Table(table -> {
                                var gu = new Image(u.uiIcon);
                                table.add(gu).size(45);

                                boolean can = false;
                                float itemSize = 0;
                                if(u instanceof Block b){
                                    var item = b.requirements;
                                    if(item.length > 0) {
                                        can = true;
                                        int m = item.length/2 + item.length % 2;
                                        itemSize = 10f * m;
                                    }
                                } else if(u instanceof UnitType ut){
                                    if(popAll.canRelease(ut, owner)) {
                                        var item = ut.getFirstRequirements();
                                        if(item.length > 0) {
                                            can = true;
                                            int m = item.length/2 + item.length % 2;
                                            itemSize = 10f * m;
                                        }
                                    }
                                }

                                if(can) {
                                    table.actions(
                                            Actions.delay(1.2f),
                                            //Actions.alpha(0, 0.7f)
                                            Actions.moveBy(7.5f, itemSize + 30, 0.7f)

                                    );
                                    gu.actions(
                                            Actions.delay(1.2f),
                                            Actions.sizeBy(-15, -15, 0.7f)
                                    );
                                }
                            }),
                            new Table(table -> {
                                boolean can = false;
                                if(u instanceof Block b){
                                    var item = b.requirements;
                                    if(item.length > 0) {
                                        showOnTable(item, table);
                                        can = true;
                                    }
                                } else if(u instanceof UnitType ut){
                                    if(popAll.canRelease(ut, owner)) {
                                        var item = ut.getFirstRequirements();
                                        if(item.length > 0) {
                                            showOnTable(item, table);
                                            can = true;
                                        }
                                    }
                                }

                                table.addAction(
                                        Actions.alpha(0)
                                );
                                if(can) table.actions(
                                        Actions.delay(1.2f),
                                        Actions.alpha(1, 1)
                                );
                            })
                    );
                }).height(h).width(Math.min(Core.graphics.getWidth()/18f, 60f)).pad(6);
            }
            tb.fill(upper -> upper.add(bg).grow());
        }).width(Core.graphics.getWidth()).height(Core.graphics.getHeight()).get();
        cont.add(bg).update(e -> e.setBounds(p.x, p.y, p.getWidth(), p.getHeight()));

        buttons.clear();
        buttons.button("@ok", Icon.ok, () -> {
            if(owner != null && us.size > 0) {
                owner.configure(us.items);
            }
            hide();
        }).width(210);

        closeOnBack(() -> {
            if (owner != null && us.size > 0) {
                owner.configure(us.items);
            }
        });

        show();
    }

    public void addToMap(ObjectMap<Item, Integer> map, ItemStack[] itemStacks){
        for(var i : itemStacks){
            if(!map.containsKey(i.item)) {
                map.put(i.item, i.amount);
            } else {
                if(map.get(i.item) == null) map.put(i.item, i.amount);
                else map.put(i.item, map.get(i.item) + i.amount);
            }
        }
    }

    public void showOnTable(ItemStack[] item, Table massage){
        massage.table(t -> t.add(new Image(Icon.down)).size(15)).pad(5);
        massage.row();
        massage.table(t -> {
            for(int i = 0; i < item.length; i++){
                var it = item[i];
                t.add(new sizeAbleItemImage(it, 24));
                if((i + 1) % 2 == 0) t.row();
            }
        }).pad(5);
    }
}
