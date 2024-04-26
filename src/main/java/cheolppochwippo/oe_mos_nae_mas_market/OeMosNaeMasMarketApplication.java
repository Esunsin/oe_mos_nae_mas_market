package cheolppochwippo.oe_mos_nae_mas_market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class OeMosNaeMasMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(OeMosNaeMasMarketApplication.class, args);
	}

}
