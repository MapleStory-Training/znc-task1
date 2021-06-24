package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengnachuan
 * @date 2021-04-23
 * @description
 */
@Getter
@AllArgsConstructor
public enum EnumFatType {

    EMPTY(0, "空数据块"),
    FULL(255, "文件结束"),
    ;

    private final Integer code;
    private final String name;

    public static EnumFatType instanceOf(Integer value) {
        return ALL.get(value);
    }

    private static final Map<Integer, EnumFatType> ALL = new HashMap<>();

    static {
        for (EnumFatType item : EnumFatType.values()) {
            ALL.put(item.code, item);
        }
    }
}
