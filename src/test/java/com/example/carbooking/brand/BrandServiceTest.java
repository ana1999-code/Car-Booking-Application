package com.example.carbooking.brand;

import com.example.carbooking.exception.DuplicateResourceException;
import com.example.carbooking.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandDao brandDao;

    @InjectMocks
    private BrandService underTest;

    @Captor
    private ArgumentCaptor<Brand> brandArgumentCaptor;

    @Test
    void itShouldGetAllBrands() {
        List<Brand> brands = List.of(
                new Brand(1L, "BMW"),
                new Brand(2L, "Mercedes"),
                new Brand(3L, "Audi")
        );

        given(brandDao.getAllBrands()).willReturn(brands);

        var underTestAllBrands = underTest.getAllBrands();

        assertThat(underTestAllBrands).hasSize(3);
        assertThat(underTestAllBrands.containsAll(brands)).isTrue();
    }

    @Test
    void itShouldGetBrandById() {
        var brandId = 1L;
        var brandById = new Brand(brandId, "BMW");

        given(brandDao.getBrandById(brandId)).willReturn(Optional.of(brandById));

        var underTestBrandById = underTest.getBrandById(brandId);

        assertThat(underTestBrandById).isEqualTo(brandById);
        assertThat(underTestBrandById.getId()).isEqualTo(brandId);
        assertThat(underTestBrandById.getBrandName()).isEqualTo(brandById.getBrandName());
    }

    @Test
    void itShouldThrowWhenBrandIdIsNotValid() {
        var brandId = 1L;

        given(brandDao.getBrandById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getBrandById(brandId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No brand with id = [%s]".formatted(brandId));
    }

    @Test
    void itShouldRegisterNewBrand() {
        var brandId = 1L;
        var brand = new Brand(brandId, "BMW");

        var brandRegistrationRequest = new BrandRegistrationRequest(brand.getBrandName());

        underTest.registerNewBrand(brandRegistrationRequest);

        then(brandDao).should().registerNewBrand(brandArgumentCaptor.capture());
        brandArgumentCaptor.getValue().setId(brandId);
        assertThat(brandArgumentCaptor.getValue().getBrandName()).isEqualTo(brandRegistrationRequest.brandName());
    }

    @Test
    void itShouldNotRegisterBrandWithExistingName() {
        var brandName = "BMW";

        given(brandDao.existBrandWithName(brandName)).willReturn(true);

        assertThatThrownBy(() -> underTest.registerNewBrand(new BrandRegistrationRequest(brandName)))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Brand with name = [%s] already exists".formatted(brandName));
    }

    @Test
    void itShouldDeleteBrandById() {
        var brand = new Brand(1L, "BMW");

        given(brandDao.getBrandById(1L)).willReturn(Optional.of(brand));

        underTest.deleteBrandById(1L);

        assertThat(underTest.getAllBrands()).isEmpty();
    }
}