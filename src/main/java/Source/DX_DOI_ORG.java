package Source;

import Model.Paper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.io.File;
/**
 * Created by rabbit on 6/10/2016.
 */
public class DX_DOI_ORG extends Source {

    public DX_DOI_ORG() {
       name = "dx.doi.org";
    }

    public boolean download(String url_str, Paper paper, String year) {

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(18, TimeUnit.SECONDS);
        driver.get(url_str);

//        WebElement frame = (new WebDriverWait(driver, 10))
//                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("iframe")));

        try {
            driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
            WebElement element = driver.findElement(By.id("pdfLink"));
            element.click();
            for (String winHandle : driver.getWindowHandles()) {
                driver.switchTo().window(winHandle);
            }

            driver.getCurrentUrl();
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
            }
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            driver.quit();
        }


    }


}
