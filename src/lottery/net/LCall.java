package lottery.net;

import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.net.Net;
import mindustry.net.NetConnection;
import mindustry.type.Item;

public class LCall {
    public static void removeStack(Building build, Item item, int amount){
        buildRemoveStack(build, item, amount);
        if (Vars.net.server() || Vars.net.client()) {
            removeStackPacket packet = new removeStackPacket();

            packet.build = build;
            packet.item = item;
            packet.amount = amount;
            Vars.net.send(packet, true);
        }
    }
    public static void removeStack(NetConnection con, Building build, Item item, int amount){
        if (Vars.net.server() || Vars.net.client()) {
            removeStackPacket packet = new removeStackPacket();

            packet.build = build;
            packet.item = item;
            packet.amount = amount;
            Vars.net.sendExcept(con, packet, true);
        }
    }

    public static void buildRemoveStack(Building build, Item item, int amount){
        if(build != null && item != null &&  amount > 0){
            build.removeStack(item, amount);
        }
    }

    public static void registerPackets(){
        Net.registerPacket(removeStackPacket::new);
    }
}
