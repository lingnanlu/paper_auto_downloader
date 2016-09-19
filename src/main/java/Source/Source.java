package Source;

import Model.Paper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by rabbit on 6/10/2016.
 */
public abstract class Source {

    String name;

    public abstract boolean download(String url_str, Paper paper, String year);


    @Override
    public String toString() {
        return name;
    }
}
