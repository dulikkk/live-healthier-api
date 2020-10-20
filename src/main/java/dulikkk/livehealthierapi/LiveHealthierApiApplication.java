package dulikkk.livehealthierapi;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class LiveHealthierApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveHealthierApiApplication.class, args);
    }

}
