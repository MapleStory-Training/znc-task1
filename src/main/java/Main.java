import enums.EnumOperationType;
import org.apache.commons.lang.StringUtils;
import service.OSManager;
import utils.EditUtil;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            OSManager manager = new OSManager();
            menu(manager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void menu(OSManager manager) {

        Scanner s = new Scanner(System.in);
        String str;
        System.out.println("***********" + "欢迎使用文件模拟操作系统" + "***********");
        System.out.println();
        manager.initOS();

        System.out.println("请输入命令（输入help查看命令表）：");
        System.out.print("~: ");
        while ((str = s.nextLine()) != null) {
            if (EnumOperationType.EXIT.getCode().equals(str)) {
                if (manager.exit()) {
                    System.out.println("感谢您的使用！");
                } else {
                    System.out.println("系统崩溃");
                }
                break;
            }

            String[] strs = EditUtil.editStr(str);
            EnumOperationType enumOperationType = EnumOperationType.instanceOf(strs[0]);
            if (enumOperationType == null) {
                for (String st : strs) {
                    System.out.println(st);
                }
                System.out.println("您所输入的命令有误，请检查！");
                continue;
            }
            switch (enumOperationType) {
                case FORMAT:
                    manager.format();
                    break;
                case MKDIR:
                    if (strs.length < 2 || StringUtils.isEmpty(strs[1])) {
                        System.out.println("usage: mkdir [directory]");
                        System.out.println();
                    } else {
                        manager.mkdir(strs[1]);
                    }
                    break;
                case TOUCH:
                    if (strs.length < 3) {
                        System.out.println("usage: touch [file]");
                        System.out.println();
                    } else {
                        manager.touch(strs[1], 1);
                    }
                    break;
                case PWD:
                    manager.pwd();
                    break;
                case LS:
                    manager.ls();
                    break;
                case CD:
                    if (strs.length < 3) {
                        System.out.println("usage: cd [directory]");
                        System.out.println("usage: cd ..");
                        System.out.println();
                    } else if ("..".equals(strs[1])) {
                        manager.backFile();
                    } else {
                        manager.cdDirectory(strs[1]);
                    }
                    break;
                case WRITE:
                    if (strs.length < 3) {
                        System.out.println("usage: echo [file] [data]");
                        System.out.println();
                    } else {
                        manager.write(strs[1], strs[2]);
                    }
                    break;
                case CAT:
                    if (strs.length < 2) {
                        System.out.println("usage: cat [file]");
                        System.out.println("usage: cat [file] > [file]");
                        System.out.println();
                    }
                    if (strs.length > 2 && ">".equals(strs[2])) {
                        manager.redirect(strs[1], strs[3]);
                    } else {
                        manager.catFile(strs[1]);
                    }
                    break;
                case HELP: {
                    System.out.println("命令如下（空格不能省略）：");
                    System.out.println();
                    System.out.println("format                        [格式化]");
                    System.out.println();
                    System.out.println("mkdir [directory]             [创建目录]");
                    System.out.println();
                    System.out.println("touch [file]                  [创建文件]");
                    System.out.println();
                    System.out.println("pwd                           [查看当前路径]");
                    System.out.println();
                    System.out.println("ls                            [查看当前文件夹]");
                    System.out.println();
                    System.out.println("cd [directory]                [打开目录]");
                    System.out.println();
                    System.out.println("cd..                          [返回上级目录]");
                    System.out.println();
                    System.out.println("echo [file] [data]           [写入数据]");
                    System.out.println();
                    System.out.println("cat [file]                    [显示文件内容]");
                    System.out.println();
                    System.out.println("cat [file] > [file]           [输出重定向]");
                    System.out.println();
                    System.out.println("********************暂未完成*********************");
                    System.out.println("append [file] [data]          [追加写入](暂未实现)");
                    System.out.println();
                    break;
                }
                default:
                    for (String st : strs) {
                        System.out.println(st);
                    }
                    System.out.println("您所输入的命令有误，请检查！");
            }
            System.out.print("~: ");
        }
    }

}
