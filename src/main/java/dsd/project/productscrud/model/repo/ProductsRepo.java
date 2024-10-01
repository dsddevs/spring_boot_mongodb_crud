package dsd.project.productscrud.model.repo;

import dsd.project.productscrud.model.entity.Products;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductsRepo extends MongoRepository<Products, UUID> {
}
