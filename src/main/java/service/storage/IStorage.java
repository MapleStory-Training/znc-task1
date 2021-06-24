package service.storage;

import model.FatDTO;

/**
 * 存储功能
 *
 * @author zhengnachuan
 * @date 2021-04-26
 * @description
 */
public interface IStorage {

    /**
     * 初始化文件
     *
     * @param fatDTO
     */
    void initFile(FatDTO fatDTO);

    /**
     * 读FAT
     *
     * @return
     */
    FatDTO readFAT();

    /**
     * 根据偏移量写入数据
     *
     * @param offset
     * @param data
     */
    boolean writeData(int offset, String data);

    /**
     * 指定偏移量读数据
     *
     * @param offset
     * @return
     */
    String readData(int offset);
}
