import Model.Paper;
import Source.Source;
import Utils.Comparator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Source.*;
import Utils.*;
/**
 * Created by rabbit on 6/10/2016.
 */
public class Main {

    public static List<String> readPaperList(String file) {
        List<String> papers = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.equals("")) {
                    papers.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return papers;
    }

    public static Map<String, String> readAbbrMap(String file) {

        Map<String, String> fullNameToAbbr = new HashMap<String, String>();
        try {
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(!line.equals("")) {
                    //Log.toConsole(line);
                    int indexOfComma = line.indexOf(",");
                    fullNameToAbbr.put(line.substring(0, indexOfComma), line.substring(indexOfComma + 1));
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fullNameToAbbr;
    }

    public static void generatePressNameList() throws IOException {

        String[] years = {
                "2014", "2015", "2016"
        };


        Set<String> pressNameSet = new HashSet<String>();
        FileWriter pressNameListFile = new FileWriter("abbr.txt");

        for (String year : years) {
            List<Paper> papers = new ArrayList<Paper>();
            List<String> paperlist = readPaperList(year + "/paperlist.txt");

            for (String paper : paperlist) {
               // Log.toConsole(year + " " + paper);
                papers.add(Paper.newPaper(paper, null));
            }

            for (Paper paper : papers) {
                pressNameSet.add(paper.pressName);

                Log.toConsole(year + " " + paper.order + " " + paper.pressName);
            }

        }

        List<String> pressNameList = new ArrayList<String>(pressNameSet);
        Collections.sort(pressNameList);

        for (String pressName : pressNameList) {
            pressNameListFile.write(pressName + "\n");
        }

        pressNameListFile.close();



    }

    public static void searchEveryPaperInNewWindow(String title) {

    }
    public static void main(String[] args) throws IOException {

      //  generatePressNameList();

        Map<String, String> fullToAbbr = readAbbrMap("abbr.txt");

        String[] years = {
                "2016"
        };

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        String baseUrl = "http://xueshu.baidu.com/";


        for (String year : years) {

            List<String> paperlist = readPaperList(year + "/paperlist.txt");

            List<Paper> papers = new ArrayList<Paper>();



            for (String paper : paperlist) {
                papers.add(Paper.newPaper(paper, fullToAbbr));
            }
//
//            FileWriter paperTitleListFile = new FileWriter("paperTitleListFile.txt");
//
//            for (Paper paper : papers) {
//                Log.toFile(paper.year + "\t" + paper.order + "\t" + paper.pressNameAbbr + "\t" + paper.orderOfMinChen
//                + "\t" + paper.title, paperTitleListFile);
//            }
//
//            paperTitleListFile.close();

            for (Paper paper : papers) {

                Log.toConsole("正在处理 " + paper.toString());
                driver.get(baseUrl);
                driver.findElement(By.id("kw")).clear();
                driver.findElement(By.id("kw")).sendKeys(paper.title);
                driver.findElement(By.id("su")).click();

                WebElement body = driver.findElement(By.tagName("body"));
                body.sendKeys(Keys.CONTROL + "t");

            }

        }




    }


    public static void run(List<Paper> papers, String year) throws IOException {

        FileWriter logFile = new FileWriter(year + "/download_fail_file.txt");

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        String baseUrl = "http://xueshu.baidu.com/";


        //下载源， 顺序就是优先级
        Source[] sources = {
                new ENG_AUBURN_EDU(),
                new SRO_SUSSEX_AC_UK(),
                new DX_DOI_ORG()
        };

        for (Paper paper : papers) {

            Log.toConsole("正在处理 " + paper);

            String title = paper.title;

            driver.get(baseUrl);
            driver.findElement(By.id("kw")).clear();
            driver.findElement(By.id("kw")).sendKeys(title);
            driver.findElement(By.id("su")).click();

            List<WebElement> dlSourceElements = driver.findElements(By.className("dl_source"));

            //如果搜索出来的是一个列表
            if (dlSourceElements.size() == 0) {

                WebElement firstItem = driver.findElement(By.xpath("//div[@id='1']/div/h3/a"));

                //判断第一条是否是要找的论文
                if (!Comparator.similarity(firstItem.getText(), title)) {
                    Log.toFile(paper, logFile);
                    continue;
                }

                //切换到新窗口
                firstItem.click();
                for (String winHandle : driver.getWindowHandles()) {
                    driver.switchTo().window(winHandle);
                }
                dlSourceElements = driver.findElements(By.className("dl_source"));

            }

            boolean hasDownloaded = false;
            for (Source source : sources) {
                boolean contain = false;
                WebElement targetElement = null;
                for (WebElement element : dlSourceElements) {
                    if (element.getText().equals(source.toString())) {
                        contain = true;
                        targetElement = element;
                        break;
                    }
                }

                if (contain == true) {
                    WebElement parent = targetElement.findElement(By.xpath(".."));
                    String downloadURL = parent.getAttribute("href");
                    Boolean success = source.download(downloadURL, paper, year);
                    if (success) {
                        hasDownloaded = true;
                        break;
                    }
                }

            }

            if (!hasDownloaded) {
                Log.toFile(year + " " + paper.title, logFile);
            }
        }

        logFile.close();
    }
}
