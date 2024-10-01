package dsd.project.productscrud.model.entity;


import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "products")
public record Products(UUID id, String name) {
    public Products(String name) {
        this(UUID.randomUUID(), name);
    }
}
