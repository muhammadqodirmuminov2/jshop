package com.muhammad.jshop.service.product;

import com.muhammad.jshop.dto.ProductDto;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.mapper.ProductMapper;
import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.model.Image;
import com.muhammad.jshop.model.Product;
import com.muhammad.jshop.repository.CategoryRepository;
import com.muhammad.jshop.repository.ImageRepository;
import com.muhammad.jshop.repository.ProductRepository;
import com.muhammad.jshop.request.ProductFilter;
import com.muhammad.jshop.request.ProductRequest;
import com.muhammad.jshop.request.ProductUpdateRequest;
import com.muhammad.jshop.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService implements  IProductService {
    private  final ProductRepository productRepository;
    private  final CategoryRepository categoryRepository;
    private  final ImageRepository imageRepository;
    private  final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto createProduct(ProductRequest productRequest) {
        long categoryId = productRequest.getCategoryId();
        List<Integer> imageIds = productRequest.getImages();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        Product newProduct = new Product();
        newProduct.setCategory(category);
        newProduct.setPrice(productRequest.getPrice());
        newProduct.setName(productRequest.getName());
        newProduct.setDescription(productRequest.getDescription());

        if (!imageIds.isEmpty()) {
            List<Image> images = imageIds.stream().map(imageId -> {
                return imageRepository.findById((long) imageId)
                        .orElseThrow(() -> new ResourceNotFoundException("Image not found with id " + imageId));
            }).toList();
            newProduct.setImages(images);
        }

        return productMapper.toDto(productRepository.save(newProduct));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(ProductFilter filter, Pageable pageable) {
        return productRepository
                .findAll(ProductSpecification.filter(filter), pageable)
                .map(productMapper::toDto);
    }

    @Override
    public ProductDto getOneProduct(long productId) {
        return productMapper.toDto(productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long productId, ProductUpdateRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        if (productRequest.getName() != null && !productRequest.getName().isBlank()) {
            product.setName(productRequest.getName());
        }
        if (productRequest.getDescription() != null) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }

        Long categoryId = productRequest.getCategoryId();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
            product.setCategory(category);
        }

        List<Long> imageIds = productRequest.getImages();
        if (imageIds != null) {
            List<Image> images = imageRepository.findAllById(imageIds);
            if (images.size() != new HashSet<>(imageIds).size()) {
                throw new ResourceNotFoundException("One or more images not found: " + imageIds);
            }
            product.getImages().clear();
            product.getImages().addAll(images);
        }

        return productMapper.toDto(product);
    }

    @Transactional
    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        productRepository.delete(product);
    }
}
