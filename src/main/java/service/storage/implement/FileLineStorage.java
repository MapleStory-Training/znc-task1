package service.storage.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import constants.Constant;
import enums.EnumDataType;
import model.DataBlockDTO;
import model.FatDTO;
import service.storage.IStorage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhengnachuan
 * @date 2021-04-20
 * @description
 */
public class FileLineStorage implements IStorage {

    @Override
    public void initFile(FatDTO fatDTO) {

        File file = new File(Constant.FILE_NAME);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bos = new BufferedWriter(new FileWriter(file))) {
            bos.write(JSONObject.toJSONString(fatDTO, SerializerFeature.DisableCircularReferenceDetect));
            int[] fat = fatDTO.getFat();
            for (int i = 0; i < fat.length; i++) {
                bos.newLine();
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FatDTO readFAT() {

        File file = new File(Constant.FILE_NAME);
        if (!file.exists()) {
            return null;
        }
        FatDTO fatDTO = null;
        try (LineNumberReader inputStream = new LineNumberReader(new FileReader(file))) {
            String fatDTOStr = inputStream.readLine();
            fatDTO = JSON.parseObject(fatDTOStr, FatDTO.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fatDTO;
    }

    @Override
    public boolean writeData(int offset, String data) {

        Path path = Paths.get(Constant.FILE_NAME);
        try {
            // 末尾一行读不出来
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.set(offset, data);
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String readData(int offset) {

        String result = null;
        try (LineNumberReader reader = new LineNumberReader(new FileReader(Constant.FILE_NAME))) {
            for (int line = 0; line < offset; line++) {
                reader.readLine();
            }
            result = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    static public void main(String[] args) {

        DataBlockDTO dataBlockDTO = new DataBlockDTO("a", EnumDataType.DIRECTORY.getCode(), 10, 1);
        HashMap<String, DataBlockDTO> totalFiles = new HashMap<>();
        totalFiles.put("k1", dataBlockDTO);
        totalFiles.put("k2", dataBlockDTO);
        FatDTO fatDTO = FatDTO.builder()
                .root(dataBlockDTO)
                .fat(new int[8])
                .nowCatalog(dataBlockDTO)
                .build();
        FileLineStorage fileLineStorage = new FileLineStorage();
        fileLineStorage.initFile(fatDTO);
//        writeFAT(fatDTO);
//        readFAT();
//        writeData(3, dataBlockDTO.toString());
        fileLineStorage.readData(3);
//        test1();
    }

    static void test() {
        String buf = "123123";
        try (
//                FileWriter fileWriter = new FileWriter("OS1");
                BufferedWriter bw = new BufferedWriter(new FileWriter("OS1"));
        ) {
//            System.out.println("test2:    " + bw.readLine());

//            while ((tmp = br.readLine()) != null)
//                // 退出时只需要保存参数块、根目录项、fat前三行
//                if (i > 3) {
//                    buf.append(tmp);;
//                }
//                i++;
//            }
            bw.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void test1() {
        StringBuilder buf = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("OS1"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("OS1", true))
        ) {
            System.out.println(br.readLine());
//            buf.append("a");
//            buf.append("b");
//            buf.append("c");
//            int i = 1;
//            String tmp;
//            while ((tmp = br.readLine()) != null) {
//                // 退出时只需要保存参数块、根目录项、fat前三行
////                if (i > 3) {
//                buf.append(tmp);
////                }
//                i++;
//            }
//            bw.write(buf.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
