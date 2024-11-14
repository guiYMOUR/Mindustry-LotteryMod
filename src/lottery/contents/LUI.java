package lottery.contents;

import lottery.ui.BeforeLottery;
import lottery.ui.LotteryRes;
import lottery.ui.MdfUI;

public class LUI {
    public static MdfUI mdfUI;
    public static BeforeLottery beforeLottery;
    public static LotteryRes lotteryRes;
    public static void init(){
        mdfUI = new MdfUI();
        beforeLottery = new BeforeLottery();
        lotteryRes = new LotteryRes();
    }
}
