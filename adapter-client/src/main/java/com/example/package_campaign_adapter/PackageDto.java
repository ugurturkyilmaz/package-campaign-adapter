package com.example.package_campaign_adapter;

import java.math.BigDecimal;

public record PackageDto(
        String uniqueId,
        String title,
        BigDecimal price,
        String description
) {
}
