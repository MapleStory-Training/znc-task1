package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author zhengnachuan
 * @date 2021-04-20
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FatDTO {

    /**
     * 所有数据块映射关系
     */
    private Map<Integer, DataBlockDTO> totalDataBlockMap;
    /**
     * 定义FAT表
     */
    private int[] fat;
    /**
     * 创建根目录 使用fat表的第一项
     */
    private DataBlockDTO root;
    /**
     * 当前目录项
     */
    private DataBlockDTO nowCatalog;
}
