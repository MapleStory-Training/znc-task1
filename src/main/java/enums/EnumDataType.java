package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengnachuan
 * @date 2021-04-22
 * @description
 */
@Getter
@AllArgsConstructor
public enum EnumDataType {

    DIRECTORY(1, "目录"),
    FILE(2, "文件"),
    ;

    private final Integer code;
    private final String name;

    public static EnumDataType instanceOf(Integer value) {
        return ALL.get(value);
    }

    private static final Map<Integer, EnumDataType> ALL = new HashMap<>();

    static {
        for (EnumDataType item : EnumDataType.values()) {
            ALL.put(item.code, item);
        }
    }
}
