package lottery.contents;

import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.Units;
import mindustry.gen.Building;
import mindustry.gen.UnitWaterMove;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.Block;

public class popAll {
    public static final int len = 6;
    public static ObjectMap<Integer, Seq<UnlockableContent>> popMap = new ObjectMap<>();

    public static void init(){
        for(int i = 0; i < len; i++){
            popMap.put(i, new Seq<>());
        }
        
        for(int i = 0; i < Vars.content.blocks().size; i++){
            Block b = Vars.content.block(i);
            var item = b.requirements;

            float buildCost = 0;
            if(item.length > 0){
                buildCost = 0f;
                for(ItemStack stack : item){
                    buildCost += stack.amount * stack.item.cost;
                }
            }

            if(buildCost == 0) continue;
            if(buildCost <= 90){
                popMap.get(0).add(b);
            } else if(buildCost <= 180){
                popMap.get(1).add(b);
            } else if(buildCost <= 300){
                popMap.get(2).add(b);
            } else if(buildCost <= 600){
                popMap.get(3).add(b);
            } else if(buildCost <= 1800){
                popMap.get(4).add(b);
            } else {
                popMap.get(5).add(b);
            }
        }


        for(int i = 0; i < Vars.content.units().size; i++) {
            UnitType u = Vars.content.unit(i);
            if(u != null && u.getFirstRequirements() != null && u.getFirstRequirements().length > 0){
                if(u.getFirstRequirements().length == 1 && u.getFirstRequirements()[0].item == Items.graphite && u.getFirstRequirements()[0].amount == 1) continue;
                if(u.armor <= 3 && u.health <= 400){
                    popMap.get(0).add(u);
                } else if(u.armor <= 7 && u.health <= 2000){
                    popMap.get(1).add(u);
                } else if(u.armor <= 11 && u.health <= 5000){
                    popMap.get(2).add(u);
                } else if(u.armor <= 20 && u.health <= 12000){
                    popMap.get(3).add(u);
                } else if(u.armor <= 30 && u.health <= 30000){
                    popMap.get(4).add(u);
                } else {
                    popMap.get(5).add(u);
                }
            }
        }
    }

    public static UnlockableContent popRandom(Seq<UnlockableContent> seq){
        if(seq.size > 0){
            int r = Mathf.random(0, seq.size - 1);
            return seq.get(r);
        }
        return null;
    }

    public static boolean canRelease(UnitType ut, Building owner){
        return owner != null &&
                (!(Vars.state.rules.planet == Planets.sun || ut.shownPlanets.contains(Vars.state.rules.planet)) ||
                        owner.team.data().countType(ut) >= Units.getCap(owner.team()) ||
                        (ut.constructor.get() instanceof UnitWaterMove && owner.floor() != null && !owner.floor().isLiquid));
    }
}
