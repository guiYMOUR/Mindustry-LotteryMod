package lottery.ui;

import arc.scene.ui.Image;
import lottery.util.uiUtil;
import lottery.worlds.blocks.LotteryBlock;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

public class MdfUI extends BaseDialog {
    private int[] pop;
    private int under;
    public MdfUI() {
        super("setting");
    }
    public void show(LotteryBlock m){
        pop = m.pop;
        under = m.under;
        if(m.pop.length == 0 || under <= 0) return;
        cont.clear();
        cont.pane(p -> {
            p.add(new Image(m.uiIcon)).size(96).pad(20);
            p.row();
            p.table(s -> {
                uiUtil.numberi(s, "底数 (level 1 + 2 + 3 + ... = 底数) : ", ip -> under = ip, () -> under, () -> !Vars.net.client(), 0, Integer.MAX_VALUE);
                s.row();
                for(int i = 0; i < pop.length; i++){
                    //s.add("level " + i + ": ");
                    int finalI = i;
                    uiUtil.numberi(s, "level " + i + ": ", ip -> pop[finalI] = ip, () -> pop[finalI], () -> !Vars.net.client(), 0, under);
                }
            }).left();
        });

        buttons.clear();
        buttons.button("@ok", Icon.ok, () -> {
            m.reInit(under, pop);
            hide();
        }).width(210).height(64);
        addCloseButton(210);

        closeOnBack(() -> m.reInit(under, pop));

        show();
    }
}
