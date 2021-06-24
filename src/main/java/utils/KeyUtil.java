package utils;

import enums.EnumDataType;

/**
 * @author zhengnachuan
 * @date 2021-04-23
 * @description
 */
public class KeyUtil {

    public static String generateFileKey(String name) {
        return EnumDataType.FILE.getCode() + "_" + name;
    }

    public static String generateDirKey(String name) {
        return EnumDataType.DIRECTORY.getCode() + "_" + name;
    }
}
