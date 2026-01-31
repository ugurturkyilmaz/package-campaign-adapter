package com.example.package_campaign_adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "adapter-service",
        url = "${adapter.base-url}"
)
public interface PackageFeignClient {

    @GetMapping("/v1/packages")
    List<PackageDto> getPackages();
}
