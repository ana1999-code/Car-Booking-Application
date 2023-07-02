package com.example.carbooking.brand;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${application.path}/brands")
@AllArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public List<Brand> getAllBrands(){
        return brandService.getAllBrands();
    }

    @GetMapping("/{id}")
    public Brand getBrandById(@PathVariable("id") Long id){
        return brandService.getBrandById(id);
    }

    @PostMapping("/register")
    public void registerNewBrand(@RequestBody BrandRegistrationRequest request){
        brandService.registerNewBrand(request);
    }

    @DeleteMapping("/{id}")
    public void deleteBrandById(@PathVariable("id") Long id){
        brandService.deleteBrandById(id);
    }
}
