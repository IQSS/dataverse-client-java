package com.researchspace.dataverse.entities.facade;

import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class License {
    private @NonNull String name;
    private @NonNull URI uri;
}
