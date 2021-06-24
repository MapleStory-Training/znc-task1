package utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author zhengnachuan
 * @date 2021-04-22
 * @description
 */
public class EditUtil {

    public static String[] editStr(String str) {
        // 根据空格分割输入命令
        Pattern pattern = compile("([\u4e00-\u9fa5_a-zA-Z0-9>.\\\\/]*) *");
        Matcher m = pattern.matcher(str);
        ArrayList<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group(1));
        }
        return list.toArray(new String[0]);
    }
}
