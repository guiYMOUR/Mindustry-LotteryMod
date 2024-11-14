package lottery.util;

import arc.func.*;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.util.Strings;

public class uiUtil {
    public static void number(Table main, String text, Floatc cons, Floatp prov){
        number(main, text, false, cons, prov, () -> true, 0, Float.MAX_VALUE);
    }

    public static void number(Table main, String text, Floatc cons, Floatp prov, float min, float max){
        number(main, text, false, cons, prov, () -> true, min, max);
    }

    public static void number(Table main, String text, boolean integer, Floatc cons, Floatp prov, Boolp condition){
        number(main, text, integer, cons, prov, condition, 0, Float.MAX_VALUE);
    }

    public static void number(Table main, String text, Floatc cons, Floatp prov, Boolp condition){
        number(main, text, false, cons, prov, condition, 0, Float.MAX_VALUE);
    }

    public static void numberi(Table main, String text, Intc cons, Intp prov, int min, int max){
        numberi(main, text, cons, prov, () -> true, min, max);
    }

    public static void number(Table main, String text, boolean integer, Floatc cons, Floatp prov, Boolp condition, float min, float max){
        main.table(t -> {
            t.left();
            t.add(text).left().padRight(5)
                    .update(a -> a.setColor(condition.get() ? Color.white : Color.gray));
            t.field((integer ? (int)prov.get() : prov.get()) + "", s -> cons.get(Strings.parseFloat(s)))
                    .padRight(100f)
                    .update(a -> a.setDisabled(!condition.get()))
                    .valid(f -> Strings.canParsePositiveFloat(f) && Strings.parseFloat(f) >= min && Strings.parseFloat(f) <= max).width(120f).left();
        }).padTop(0);
        main.row();
    }

    public static void numberi(Table main, String text, Intc cons, Intp prov, Boolp condition, int min, int max){
        main.table(t -> {
            t.left();
            t.add(text).left().padRight(5)
                    .update(a -> a.setColor(condition.get() ? Color.white : Color.gray));
            t.field((prov.get()) + "", s -> cons.get(Strings.parseInt(s)))
                    .update(a -> a.setDisabled(!condition.get()))
                    .padRight(100f)
                    .valid(f -> Strings.parseInt(f) >= min && Strings.parseInt(f) <= max).width(120f).left();
        }).padTop(0).row();
    }
}
