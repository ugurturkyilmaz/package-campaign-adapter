package com.example.package_campaign_adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching

public class PackageCampaignAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(PackageCampaignAdapterApplication.class, args);
	}

}
