package com.example.package_campaign_adapter;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ListController {

    private final ListProviderService service;

    public ListController(ListProviderService service) {
        this.service = service;
    }

    @GetMapping("/v1/packages")
    public List<PackageDto> getPackages() {
        return service.getPackages();
    }
}
