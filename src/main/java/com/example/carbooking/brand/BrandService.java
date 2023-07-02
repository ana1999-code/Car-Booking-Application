package com.example.carbooking.brand;

import com.example.carbooking.exception.DuplicateResourceException;
import com.example.carbooking.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandService {
    private final BrandDao brandDao;

    public List<Brand> getAllBrands() {
        return brandDao.getAllBrands();
    }

    public Brand getBrandById(Long id) {
        return brandDao
                .getBrandById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No brand with id = [%s]".formatted(id))
                );
    }

    public void registerNewBrand(BrandRegistrationRequest request) {

        if (brandDao.existBrandWithName(request.brandName())){
            throw new DuplicateResourceException("Brand with name = [%s] already exists".formatted(request.brandName()));
        }

        var brand = new Brand(request.brandName());
        brandDao.registerNewBrand(brand);
    }

    public void deleteBrandById(Long id) {
        var brand = getBrandById(id);

        brandDao.deleteBrandById(id);
    }
}
