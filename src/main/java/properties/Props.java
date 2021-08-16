package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public interface Props {
    static int getPort() {
        Properties property = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(property.getProperty("port"));
    }

    static String getHost() {
        Properties property = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property.getProperty("host");
    }
}
