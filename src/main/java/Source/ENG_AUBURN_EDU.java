package Source;

import Model.Paper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by rabbit on 6/10/2016.
 */
public class ENG_AUBURN_EDU extends Source {

    public ENG_AUBURN_EDU() {
        name = "eng.auburn.edu";
    }

    public boolean download(String url_str, Paper paper, String year) {
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(url_str);

        File destination = new File(year + "/" + paper.generateFormatName() + ".pdf");
        try {
            URL url = new URL(driver.getCurrentUrl());
            FileUtils.copyURLToFile(url, destination);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            driver.quit();
        }
    }


}
