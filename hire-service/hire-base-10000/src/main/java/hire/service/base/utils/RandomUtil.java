package hire.service.base.utils;

import java.text.DecimalFormat;
import java.util.Random;

public class RandomUtil {

    private static final Random random = new Random();

    private static final DecimalFormat sixdf = new DecimalFormat("000000");

    /**
     * 随机生成六位验证码
     * @return
     */
    public static String getSixBitRandom() {
        return sixdf.format(random.nextInt(1000000));
    }
}
