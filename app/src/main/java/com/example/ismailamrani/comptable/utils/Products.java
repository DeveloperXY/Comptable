package com.example.ismailamrani.comptable.utils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.models.Product;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 04/05/2016.
 */
public class Products {
    /**
     * @param products to be filtered
     * @param query    based upon the products will be filtered
     * @return the filtered list of products
     */
    public static List<Product> filter(List<Product> products, String query) {
        return Stream.of(products)
                .filter(product -> product.getLabel()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
