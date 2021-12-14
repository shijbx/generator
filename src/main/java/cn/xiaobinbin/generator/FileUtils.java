package cn.xiaobinbin.generator;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtils {

    public static void main(String[] args) {
        System.out.println(readProperty("spring.application.datasource.url"));
    }

    public static String readProperty(String code) {
        Properties properties = new Properties();
        InputStream inputStream = Object.class.getResourceAsStream("/application.yml");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.get(code) + "";
    }

    public static String readYml(String code) {

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties properties = yaml.getObject();
        return properties.get(code) + "";
    }

}
