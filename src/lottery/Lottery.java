package lottery;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.Image;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Strings;
import lottery.contents.LBlocks;
import lottery.contents.popAll;
import lottery.net.LCall;
import lottery.ui.LotteryRes;
import lottery.worlds.blocks.main;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.mod.*;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.SettingsMenuDialog;

import static arc.Core.settings;
import static mindustry.Vars.ui;

public class Lottery extends Mod{
    public static final String ModName = "lottery";
    public static boolean onlyPlugIn = Core.settings.getBool("lottery-plug-in-mode");

    private static final Seq<UnlockableContent> us = new Seq<>();
    private static final Seq<Color> uc = new Seq<>();
    private static final IntSeq tier = new IntSeq();

    private static final int[] pop = {50, 24, 15, 8, 2, 1};

    private static final Color[] colors = {
            Color.valueOf("DDDDDD"),
            Color.valueOf("AFE2FF"),
            Color.valueOf("DEB0FF"),
            Color.valueOf("FBFFB0"),
            Color.valueOf("FFE5B0"),
            Color.valueOf("FFB0B0")
    };

    public Lottery() {
    }

    @Override
    public void init() {
        settings.defaults("lottery-plug-in-mode", false);
        Vars.mods.locateMod(ModName).meta.hidden = onlyPlugIn;

        if(onlyPlugIn){
            Mods.LoadedMod mod = Vars.mods.locateMod(ModName);
            mod.meta.displayName = mod.meta.displayName + "-Plug-In";
            mod.meta.version = Vars.mods.locateMod(ModName).meta.version + "-plug-in";
        }

        if(!onlyPlugIn) {
            LCall.registerPackets();
            LBlocks.initItemRes();
        }
        popAll.init();

        if(ui != null) {
            ui.settings.addCategory(Core.bundle.get("lottery-settings"), "lottery-bag", settingsTable -> {
                settingsTable.checkPref("lottery-plug-in-mode", false);

                settingsTable.row();
                settingsTable.add("[accent]-----------------------[]" + Core.bundle.get("lottery-settings") + "[accent]-----------------------[]");
                settingsTable.row();
                settingsTable.button(Core.bundle.get("stat.lottery.single-draws"), Styles.defaultt, Lottery::oneLottery).margin(14).width(200f).pad(6);
                settingsTable.row();
                settingsTable.button(Core.bundle.get("stat.lottery.ten-draws"), Styles.defaultt, Lottery::tenLottery).margin(14).width(200f).pad(6);
                settingsTable.row();
                settingsTable.button(b -> b.add(new Image(Icon.file)),
                        Styles.cleari, Lottery::showPop).margin(6).pad(6).tooltip(Core.bundle.get("stat.lottery.p-p"));
                settingsTable.row();
            });
        }
    }

    public static void showPop(){
        BaseDialog dialog = new BaseDialog("All! All! All!"){{
            cont.pane(table -> {
                table.row();
                table.table(img -> img.add(Core.bundle.get("stat.lottery.p-p"))).pad(20);
                table.row();
                for(int i = 0; i < pop.length; i++){
                    table.row();
                    float p = pop[i];
                    var m = popAll.popMap.get(i);
                    int finalI = i;
                    table.table(t -> t.add("[accent]概率: []" + Strings.autoFixed((p / 100) * 100, 3) + " %" + "(" + "level " + (finalI + 1) + ")").left()).left().pad(8);
                    table.row();
                    table.table(all -> {
                        for(int k = 0; k < m.size; k++){
                            var u = m.get(k);
                            all.table(s -> {
                                s.add(new Image(u.uiIcon)).size(20).left().pad(2);
                                s.row();
                                s.add(u.localizedName).left().pad(2);
                            }).left().pad(3);

                            if((k + 1) % 6 == 0) all.row();
                        }
                    }).padBottom(10).left();
                }
            });
            addCloseButton(210);
        }};
        dialog.show();
    }

    @Override
    public void loadContent(){
        if(!onlyPlugIn) LBlocks.load();
    }

    private static void lottery(){
        int r = Mathf.random(1, 100);
        int max = 100;
        for(int p = pop.length - 1; p >= 0; p--){
            max -= pop[p];
            if(r > max) {
                us.add(popAll.popRandom(popAll.popMap.get(p)));
                uc.add(colors[p]);
                tier.add(p);
                break;
            }
        }
    }

    public static void oneLottery(){
        us.clear();
        uc.clear();
        tier.clear();

        lottery();

        new LotteryRes().show(us, uc, tier, null);
    }

    public static void tenLottery(){
        us.clear();
        uc.clear();
        tier.clear();

        for(int i = 0; i < 10; i++) lottery();

        new LotteryRes().show(us, uc, tier, null);
    }
}
