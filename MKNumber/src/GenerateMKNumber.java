import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * MarkDowm[top]不能智能根据标题生成有序的目录
 * 该程序是自动给标题生成响应的序号.
 * 规则如下:
 *  1.为已#开头或者#前面有空格/制表符 的行前面生成序号.
 *  2.如果之前自己手动添加了序号,将覆盖之前的序号,从新生成.
 */
public class GenerateMKNumber {
    /**
     * 上一个标题序号
     */
    private static String serialNumber;

    public static void main(String[] args) throws Exception {
        //args = new String[]{"/home/pdx/下载/存证空间.md"};
        if (args.length == 0) {
            System.out.println("请输入文件路径.Eg: ./aa.md");
            return;
        }
        String filePath = args[0];
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("文件不存在");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        File dest = new File(filePath + ".new");
        BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
        String line = null;
        while ((line = reader.readLine()) != null) {
            //去不BOM字符
            if (line.startsWith(String.valueOf('\uFEFF'))) {
                line = line.replace((char) 65279, ' ');
            }
            //已#开头或者#前面有空格/制表符
            String regex = "^\\s*#";
            Pattern r = compile(regex);
            Matcher m = r.matcher(line);
            if (m.find()) {
                int num = getNums(line, '#');
                //生成新的标题
                generateSerialNumber(num);
                String result = m.group().trim();
                writer.write(line.replaceFirst("^\\s*#+[\\s\\d\\.]*", result + serialNumber + " "));
                writer.newLine();
                writer.flush();
            } else {
                writer.write(line);
                writer.newLine();
                writer.flush();
            }
        }
        System.out.println("编写成功");
    }

    private static int getNums(String str, char c) {
        int num = 0;
        for (int i = 0; i < str.toCharArray().length; i++) {
            if (str.charAt(i) == c) {
                num++;
            }
        }
        return num;
    }

    /**
     * 得到下次的标题序号
     *
     * @param num #的数量
     */
    private static void generateSerialNumber(int num) {
        if (serialNumber == null) {
            serialNumber = "1.";
        } else {
            //现在是几级标题
            int nums = getNums(serialNumber, '.');
            if (nums < num) {
                for (int i = 0; i < num; i++) {
                    //下级标题
                    for (int j = 0; j < serialNumber.toCharArray().length; j++) {
                        if ('.' == serialNumber.charAt(j)) {
                            num--;
                        }
                    }
                    serialNumber += "1.";
                }
            } else if (num == nums) {
                serialNumber = PlusOne(serialNumber);
            } else {
                String regex = "^(\\d*\\.){" + num + "}";
                Pattern r = compile(regex);
                Matcher m = r.matcher(serialNumber);
                if (m.find()) {
                    serialNumber = PlusOne(m.group());
                }
            }
        }
    }


    private static String PlusOne(String serialNumber) {
        String regex = "\\d*\\.$";
        Pattern r = compile(regex);
        Matcher m = r.matcher(serialNumber);
        if (m.find()) {
            String str = m.group();
            String str1 = str.replaceFirst("\\.", "");
            int i = Integer.parseInt(str1) + 1;
            String s = serialNumber.replaceFirst(regex, i + ".");
            return s;
        }
        return null;
    }
}
