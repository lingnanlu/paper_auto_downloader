package Model;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rabbit on 6/10/2016.
 */
public class Paper {


    public String order;                    //该论文在所有论文中的序号
    public int orderOfMinChen;             //陈老师在作者当中的排名
    public String year;                       //年份
    public String pressName;               //出版社名称
    public String pressNameAbbr;           //出版社名称简写
    public String title;

    static Pattern pressReg = Pattern.compile("\", *([^,]+),");
    static Pattern titleReg = Pattern.compile("\"([^\"]+)\"");
    static Pattern yearReg = Pattern.compile("\\d{4}");
    public String generateFormatName(){
        return year + "-" + order + "-" + pressNameAbbr + "-" + orderOfMinChen;
    }

    //从纯文本生成Paper对象
    public static Paper newPaper(String str, Map<String, String> fullToAbbr) {
        Paper paper = new Paper();


        //序号
        int indexOfSpace = str.indexOf(" ");
        paper.order = str.substring(0, indexOfSpace);

        //第几作者
        int indexOfQuote = str.indexOf("\"");
        String[] names = str.substring(indexOfSpace + 1, indexOfQuote).split(",");
        for (int i = 0; i < names.length; i++) {
            if (names[i].matches(" *Min Chen[ \\*]*")) {
                paper.orderOfMinChen = i + 1;
                break;
            }
        }

        //年份
        int lastIndexOfCommoa = str.lastIndexOf(",");

        Matcher yearMatcher = yearReg.matcher(str.substring(lastIndexOfCommoa));
        yearMatcher.find();
        paper.year = yearMatcher.group();

        //出版社名称
        Matcher pressNameMatcher = pressReg.matcher(str);
        pressNameMatcher.find();
        paper.pressName = pressNameMatcher.group(1);

        if (fullToAbbr != null) {
            //出版社Abbr
            paper.pressNameAbbr = fullToAbbr.get(paper.pressName);
        }


        //paper title
        Matcher titleMatcher = titleReg.matcher(str);
        titleMatcher.find();
        paper.title = titleMatcher.group(1);

        return paper;

    }

    @Override
    public String toString() {
        return "[" +
                "order='" + order + '\'' +
                ", orderOfMinChen=" + orderOfMinChen +
                ", year='" + year + '\'' +
                ", pressName='" + pressName + '\'' +
                ", pressNameAbbr='" + pressNameAbbr + '\'' +
                ", title='" + title + '\'' +
                ']';
    }
}
