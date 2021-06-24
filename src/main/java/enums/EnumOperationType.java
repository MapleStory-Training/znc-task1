package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengnachuan
 * @date 2021-04-20
 * @description
 */
@Getter
@AllArgsConstructor
public enum EnumOperationType {

    PWD("pwd", "查看路径"),
    EXIT("exit", "退出"),
    FORMAT("format", "格式化"),
    MKDIR("mkdir", "创建目录"),
    TOUCH("touch", "创建文件"),
    LS("ls", "显示当前目录下的所有文件和子目录"),
    CD("cd", "切换目录"),
    CAT("cat", "输出文件内容"),
    WRITE("echo", "写入数据"),
    APPEND("append", "追加写"),
    HELP("help", "帮助"),
    ;

    private final String code;
    private final String name;

    public static EnumOperationType instanceOf(String value) {
        return ALL.get(value);
    }

    private static final Map<String, EnumOperationType> ALL = new HashMap<>();

    static {
        for (EnumOperationType item : EnumOperationType.values()) {
            ALL.put(item.code, item);
        }
    }

}
