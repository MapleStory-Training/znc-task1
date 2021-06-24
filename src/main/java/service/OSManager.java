package service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import constants.Constant;
import enums.EnumDataType;
import model.DataBlockDTO;
import model.FatDTO;
import org.apache.commons.collections4.MapUtils;
import service.storage.implement.FileLineStorage;
import utils.KeyUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author zhengnachuan
 * @date 2021-04-20
 * @description
 */
public class OSManager {

    private FatDTO fatDTO;

    private FileLineStorage fileLineStorage = new FileLineStorage();

    /**
     * 启动系统
     */
    public void initOS() {

        fatDTO = fileLineStorage.readFAT();
        if (fatDTO == null) {
            System.out.println("执行系统重置");
            this.format();
        }
    }

    /**
     * 格式化
     * 1、初始化fat表
     * 2、初始化存储空间
     */
    public void format() {
        this.formatMemory();
        this.formatFile(fatDTO);
        System.out.println("系统重置完成");
    }

    /**
     * 初始化内存
     */
    private void formatMemory() {

        int[] fat = new int[Constant.FAT_SIZE];
        // 纪录磁盘剩余块数
        fat[0] = Constant.FAT_SIZE - 2;
        // 将第一位设为根目录的空间
        fat[1] = Constant.FULL;
        // 将FAT表初始化为0
        for (int i = 2; i < fat.length; i++) {
            fat[i] = 0;
        }
        DataBlockDTO root = new DataBlockDTO(Constant.ROOT, EnumDataType.DIRECTORY.getCode(), Constant.ROOT_OFFSET, Constant.ROOT_OFFSET);

        Map<Integer, DataBlockDTO> totalFileMap = new HashMap<>();
        totalFileMap.put(root.getOffset(), root);

        fatDTO = FatDTO.builder()
                .fat(fat)
                .nowCatalog(root)
                .root(root)
                .totalDataBlockMap(totalFileMap)
                .build();
    }

    /**
     * 初始化文件
     *
     * @param fatDTO
     */
    private void formatFile(FatDTO fatDTO) {
        fileLineStorage.initFile(fatDTO);
    }

    /**
     * 系统退出
     */
    public boolean exit() {
        return fileLineStorage.writeData(0, JSONObject.toJSONString(fatDTO, SerializerFeature.DisableCircularReferenceDetect));
    }

    /**
     * 创建目录
     *
     * @param name
     */
    public void mkdir(String name) {

        if (fatDTO.getFat()[0] == 0) {
            System.out.println("创建目录失败，磁盘空间不足！");
            return;
        }
        //判断该目录下是否存在同名目录
        DataBlockDTO value = this.getFileModelByKey(KeyUtil.generateDirKey(name));
        if (value != null) {
            System.out.println("创建目录失败，该目录已存在！");
            return;
        }
        int offset = this.setFat(1);
        int fatherOffset = fatDTO.getNowCatalog().getOffset();
        DataBlockDTO catalog = new DataBlockDTO(name, EnumDataType.DIRECTORY.getCode(), offset, fatherOffset);
        fatDTO.getNowCatalog().subMap.put(KeyUtil.generateDirKey(name), offset);
        fatDTO.getFat()[0]--;
        fatDTO.getTotalDataBlockMap().put(catalog.getOffset(), catalog);
        System.out.println("创建目录成功！");
    }

    /**
     * 创建文件
     *
     * @param name
     * @param size 文件大小（暂时不处理文件大小）
     */
    public void touch(String name, int size) {

        if (fatDTO.getFat()[0] == 0) {
            System.out.println("创建文件失败，磁盘空间不足！");
            return;
        }
        //判断该目录下是否存在同名文件
        DataBlockDTO value = this.getFileModelByKey(KeyUtil.generateFileKey(name));
        if (value != null) {
            System.out.println("创建文件失败，该文件已存在！");
            return;
        }
        int offset = this.setFat(1);
        int fatherOffset = fatDTO.getNowCatalog().getOffset();
        DataBlockDTO catalog = new DataBlockDTO(name, EnumDataType.FILE.getCode(), offset, fatherOffset);
        fatDTO.getNowCatalog().subMap.put(KeyUtil.generateFileKey(name), offset);
        fatDTO.getFat()[0]--;
        fatDTO.getTotalDataBlockMap().put(catalog.getOffset(), catalog);
        System.out.println("创建文件成功！");
    }

    public void pwd() {

        Stack<String> stack = new Stack<>();
        DataBlockDTO temp = fatDTO.getNowCatalog();
        while (temp.getOffset() != 1) {
            stack.add(temp.getName());
            temp = fatDTO.getTotalDataBlockMap().get(temp.getFatherOffset());
        }
        stack.add(temp.getName());
        StringBuilder pwd = new StringBuilder();
        while (!stack.isEmpty()) {
            pwd.append("/").append(stack.pop());
        }
        System.out.println(pwd);
        System.out.println();
    }

    /**
     * 显示该目录下的所有文件信息
     */
    public void ls() {

        if (MapUtils.isNotEmpty(fatDTO.getNowCatalog().subMap)) {
            for (String name : fatDTO.getNowCatalog().subMap.keySet()) {
                DataBlockDTO value = this.getFileModelByKey(name);
                if (value == null) {
                    continue;
                }
                if (EnumDataType.DIRECTORY.getCode().equals(value.getDataType())) {
                    System.out.println("目录: " + value.getName());
                } else if (EnumDataType.FILE.getCode().equals(value.getDataType())) {
                    System.out.println("文件: " + value.getName());
                }
            }
        }
//        for (int i = 0; i < 2; i++) {
//            System.out.println();
//        }
        System.out.println("磁盘剩余空间 ：" + fatDTO.getFat()[0] + "            " + "退出系统请输入exit");
//        System.out.println();
    }

    /**
     * 打开目录
     */
    public void cdDirectory(String name) {

        DataBlockDTO value = this.getFileModelByKey(KeyUtil.generateDirKey(name));
        if (value == null) {
            System.out.println("打开失败，目录不存在！");
            return;
        }
        fatDTO.setNowCatalog(value);
        System.out.println("***************** < " + value.getName() + " > *****************");
    }

    /**
     * 覆盖写文件
     */
    public void write(String name, String data) {

        DataBlockDTO value = this.getFileModelByKey(KeyUtil.generateFileKey(name));
        if ((value == null) && fatDTO.getFat()[0] == 0) {
            System.out.println("写入文件失败，磁盘空间不足！");
            return;
        }
        if (value == null) {
            System.out.println("文件不存在，正在尝试创建文件！");
            this.touch(name, 1);
            value = this.getFileModelByKey(KeyUtil.generateFileKey(name));
        }
        fileLineStorage.writeData(value.getOffset(), data);
        System.out.println("写入成功");
    }

    /**
     * 返回上一层目录
     */
    public void backFile() {
        fatDTO.setNowCatalog(fatDTO.getTotalDataBlockMap().get(fatDTO.getNowCatalog().getFatherOffset()));
        System.out.println("***************** < " + fatDTO.getNowCatalog().getName() + " > *****************");
    }

    /**
     * 查看文件内容
     *
     * @param name
     */
    public void catFile(String name) {

        DataBlockDTO dataBlockDTO = this.getFileModelByKey(KeyUtil.generateFileKey(name));
        if (dataBlockDTO == null) {
            System.out.println("该文件不存在！");
        } else {
            System.out.println(fileLineStorage.readData(dataBlockDTO.getOffset()));
        }
    }

    /**
     * @param size 文件大小（当前默认为1）
     * @return
     */
    private int setFat(int size) {
        //纪录fat循环定位
        int i = 2;
        for (; i < Constant.FAT_SIZE; i++) {
            if (fatDTO.getFat()[i] == 0) {
                fatDTO.getFat()[i] = Constant.FULL;
                break;
            }
        }
        return i;
    }

    /**
     * 输出重定向
     *
     * @param fromName
     * @param toName
     */
    public void redirect(String fromName, String toName) {

        DataBlockDTO dataBlockDTO = this.getFileModelByKey(KeyUtil.generateFileKey(fromName));
        if (dataBlockDTO == null) {
            System.out.println("原文件不存在！");
            return;
        }
        String data = fileLineStorage.readData(dataBlockDTO.getOffset());
        this.write(toName, data);
    }

    private DataBlockDTO getFileModelByKey(String name) {
        Integer offSet = fatDTO.getNowCatalog().getSubMap().get(name);
        if (offSet == null) {
            return null;
        }
        return fatDTO.getTotalDataBlockMap().get(offSet);
    }

}