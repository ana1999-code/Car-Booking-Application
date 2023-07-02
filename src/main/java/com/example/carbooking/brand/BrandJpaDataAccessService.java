package com.example.carbooking.brand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BrandJpaDataAccessService implements BrandDao{
    private final BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Boolean existBrandWithName(String name) {
        return brandRepository.findByBrandName(name).isPresent();
    }

    @Override
    public void registerNewBrand(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public void deleteBrandById(Long id) {
        brandRepository.deleteById(id);
    }

}
