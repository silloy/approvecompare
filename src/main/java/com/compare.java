package com;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2017/10/17
 * Time: 10:54
 * CopyRight: Zhouji
 */
public class compare {
    public static void main(String[] args) {
        System.out.println("arguement order: " + "zrobot1014.csv zhouji1014.csv result.csv");
        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);
        if (args.length <= 2) {
            System.out.println("missing argument");
            return;
        }
        compare(args);
    }

    public static void compare(String[] args) {
        try {
            String zrdir = args[0];
            String zhdir = args[1];
            String targerdir = args[2];
//            List<String> zrobot = getSingleLine("C:\\Users\\SuShaohua\\Desktop\\luotuo1014.csv");
//            List<String> zhouji = getSingleLine("C:\\Users\\SuShaohua\\Desktop\\zhouji1014.csv");

            List<String> zrobot = getSingleLine(zrdir);
            List<String> zhouji = getSingleLine(zhdir);

            System.out.println(JSON.toJSONString(zrobot));
            System.out.println(JSON.toJSONString(zrobot.size()));
            System.out.println(JSON.toJSONString(zhouji));
            System.out.println(JSON.toJSONString(zhouji.size()));


            List<String> uncom = new ArrayList<String>();
            for (String uid : zrobot) {
                String cid = DESCoder.decryptStr(uid);
                if (!zhouji.contains(cid)) {
                    uncom.add(cid);
                }
            }
            System.out.println(JSON.toJSONString(uncom));
            System.out.println(JSON.toJSONString(uncom.size()));

            writeTiFile(JSON.toJSONString(uncom).replaceAll("[\\[\\]]", "")
                    .replaceAll(",", "\n"), targerdir);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeTiFile(String s, String dir) {
        File writename = new File(dir); // 相对路径，如果没有则要建立一个新的output。txt文件

        try {
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(String.join(",", s) + "\n"); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getSingleLine(String dir) {
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(new File(dir))); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader);
            List<String> zrobotlist = new ArrayList<String>();
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                if (null != line) {
                    line = line.replace("\"", "");
                    zrobotlist.add(line);
                }
            }
            return zrobotlist;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }
}
