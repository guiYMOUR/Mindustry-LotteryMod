package lottery.net;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.input.InputHandler;
import mindustry.io.TypeIO;
import mindustry.net.NetConnection;
import mindustry.net.Packet;
import mindustry.type.Item;

public class removeStackPacket extends Packet {
    private byte[] DATA;
    public Building build;
    public Item item;
    public int amount;

    public removeStackPacket() {
        this.DATA = NODATA;
    }

    public void write(Writes WRITE) {
        TypeIO.writeBuilding(WRITE, build);
        TypeIO.writeItem(WRITE, this.item);
        WRITE.i(amount);
    }

    public void read(Reads READ, int LENGTH) {
        this.DATA = READ.b(LENGTH);
    }

    public void handled() {
        BAIS.setBytes(this.DATA);

        this.build = TypeIO.readBuilding(READ);
        this.item = TypeIO.readItem(READ);
        this.amount = READ.i();
    }

    public void handleServer(NetConnection con) {
        if (con.player != null && !con.kicked) {
            build.removeStack(item, amount);
            LCall.removeStack(con, build, item, amount);
        }
    }

    public void handleClient() {
        build.removeStack(item, amount);
    }
}
