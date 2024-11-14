package lottery.contents;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;

public class LFx {
    //自己差点忘了，加个注释
    //展开线条
    public static Effect lpr = new Effect(24, e->{
        if(!(e.data instanceof Float f)) return;
        Draw.color(e.color);
        Lines.stroke(4f * e.foutpow());
        float w = 45f/Vars.tilesize * e.finpow();
        float h = f/Vars.tilesize * e.finpow();
        Scl.scl();
        Lines.rect(e.x - w/2f, e.y - h/2f, w, h);
    });
    //粒子效果
    public static Effect lcr = new Effect(45, e -> {
        if(!(e.data instanceof Float f)) return;
        float h = Core.graphics.getHeight()/4.3f;
        float rdx = Mathf.randomSeed(e.id, -2.8f, 2.8f);
        Draw.color(e.color);
        for(int i : Mathf.signs){
            Angles.randLenVectors(e.id, 1, (e.finpow() * f)/Vars.tilesize, 90 * i, 0,
                    (x, y) -> Drawf.tri(e.x + x + rdx, e.y + y + h/Vars.tilesize * i, 2.5f * e.foutpow(), 1.7f * 1.25f * e.foutpow(), 90 - e.time * rdx * 4f + rdx));
        }
    });
}
