package constants;

/**
 * @author zhengnachuan
 * @date 2021-04-23
 * @description
 */
public class Constant {

    public static final String FILE_NAME = "OS";
    /**
     * 255表示磁盘块已占用
     */
    public static final int FULL = 255;
    /**
     * FAT文件块大小
     */
    public static final int FAT_SIZE = 256;

    public static final String ROOT = "root";

    /**
     * 根目录默认偏移量
     */
    public static int ROOT_OFFSET = 1;
}
