package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * @author zhengnachuan
 * @date 2021-04-20
 * @description
 */
@Data
@NoArgsConstructor
public class DataBlockDTO {

    /**
     * 文件名或目录名
     */
    private String name;
    /**
     * 用来识别是文件还是目录
     */
    private Integer dataType;
    /**
     * 在FAT表中起始位置
     */
    private Integer offset;
    /**
     * 该文件或目录的上级目录
     */
    private Integer fatherOffset;
    /**
     * 子目录或子文件
     */
    public Map<String, Integer> subMap = new HashedMap<>();

    public DataBlockDTO(String name, int dataType, int offset, int fatherOffset) {
        this.name = name;
        this.dataType = dataType;
        this.offset = offset;
        this.fatherOffset = fatherOffset;
    }

}
