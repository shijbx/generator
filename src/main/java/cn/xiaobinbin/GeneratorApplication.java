package cn.xiaobinbin;

import cn.xiaobinbin.generator.CodeGeneration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeneratorApplication {


    public static void main(String[] args) {
        CodeGeneration.run();
    }

}
