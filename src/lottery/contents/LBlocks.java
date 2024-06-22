package lottery.contents;

import arc.graphics.Color;
import lottery.worlds.blocks.main;
import lottery.worlds.blocks.resBlock;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.Block;

import static mindustry.type.ItemStack.with;

public class LBlocks {
    public static Block
        resBag, unitBag, bag, finalLot;
    public static void load(){
        resBag = new main("res-bag"){{
            size = 2;
            health = 1000;
            requirements(Category.effect, with(Items.graphite, 200));
            pop = new int[]{70, 20, 9, 1};
            popLen = 4;

            cutAmount = 1;
            cut = 30;

            lotteryItem = Items.graphite;
            perLottery = 65;

            alwaysUnlocked = true;
        }};

        unitBag = new main("unit-bag"){{
            size = 3;
            health = 1500;
            requirements(Category.effect, with(Items.silicon, 300));

            lotteryItem = Items.silicon;
            perLottery = 50;

            pop = new int[]{80, 15, 4, 1};
            popLen = 4;

            cutAmount = 2;
            cut = 60;

            alwaysUnlocked = true;
        }

            @Override
            public void init() {
                super.init();
                for(int i = 0; i < Vars.content.units().size; i++) {
                    UnitType u = Vars.content.unit(i);
                    if(u != null && u.getFirstRequirements() != null && u.getFirstRequirements().length > 0){
                        if(u.getFirstRequirements().length == 1 && u.getFirstRequirements()[0].item == Items.graphite && u.getFirstRequirements()[0].amount == 1) continue;
                        if(u.armor <= 3 && u.health <= 180){
                            pops.get(0).add(u);
                        } else if(u.armor <= 7 && u.health <= 400){
                            pops.get(1).add(u);
                        } else if(u.armor <= 11 && u.health <= 1200){
                            pops.get(2).add(u);
                        } else if(u.armor <= 20 && u.health <= 10000){
                            pops.get(3).add(u);
                        }
                    }
                }
            }
        };

        bag = new main("bag"){{
            size = 3;
            requirements(Category.effect, with(Items.silicon, 250, Items.graphite, 250, Items.thorium, 180));
            pop = new int[]{70, 28, 2};

            colors = new Color[]{
                    Color.valueOf("DDDDDD"),
                    Color.valueOf("DEB0FF"),
                    Color.valueOf("FBFFB0")
            };
            lotteryItem = Items.silicon;
            perLottery = 60;
            cutAmount = 1;
            health = 1000;

            alwaysUnlocked = true;
        }};
        finalLot = new main("final-lot"){{
            size = 3;
            requirements(Category.effect, with(Items.surgeAlloy, 300, Items.phaseFabric, 300, Items.graphite, 600, Items.silicon, 600));
            health = 2000;

            alwaysUnlocked = true;
        }};
    }
    public static void initItemRes(){
        if(Vars.content.items().size > 0) for(var i : Vars.content.items()){
            if(i.buildable) {
                new resBlock(i, 30);
                new resBlock(i, 200);
                new resBlock(i, 2000);
            }
        }


        for(int i = 0; i < Vars.content.blocks().size; i++) {
            Block b = Vars.content.block(i);
            var item = b.requirements;

            if(item.length > 2) continue;
            float buildCost = 0;
            if (item.length > 0) {
                buildCost = 0f;
                for (ItemStack stack : item) {
                    buildCost += stack.amount * stack.item.cost;
                }
            }

            if (buildCost == 0) continue;
            if (buildCost <= 30) {
                ((main)resBag).pops.get(0).add(b);
            } else if (buildCost <= 60) {
                ((main)resBag).pops.get(1).add(b);
            } else if (buildCost <= 180) {
                ((main)resBag).pops.get(2).add(b);
            } else if(buildCost <= 500){
                ((main)resBag).pops.get(3).add(b);
            }
        }
    }
}
