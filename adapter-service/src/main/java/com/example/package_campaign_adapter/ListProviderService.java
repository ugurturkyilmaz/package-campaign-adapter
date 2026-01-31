package com.example.package_campaign_adapter;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ListProviderService {

    @Cacheable(cacheNames = "package-list")
    public List<PackageDto> getPackages() {

        System.out.println("ALTYAPIYA GİDİLDİ (package)");

        return List.of(
                new PackageDto("PKG-001", "Starter Package", new BigDecimal("99.90")),
                new PackageDto("PKG-002", "Plus Package", new BigDecimal("149.90")),
                new PackageDto("PKG-003", "Max Package", new BigDecimal("199.90"))
        );
    }
}
