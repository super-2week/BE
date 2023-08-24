package com.supercoding.commerce03.repository.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.web.dto.product.ProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 연관 검색
     *
     * @param searchWord
     * @return
     */
//Full Text Index
//    @Query(value =
//            "SELECT DISTINCT p.product_name FROM products p" +
//                    " WHERE MATCH(p.product_name) AGAINST(?1 IN BOOLEAN MODE)", nativeQuery = true)
    //B-Tree Index
    @Query(value =
            "SELECT DISTINCT product_name FROM products p" +
                    " WHERE p.product_name LIKE ?1", nativeQuery = true)
    List<Object[]> fullTextSearch(String searchWord);


    /**
     * 상품 리스트 페이지 전체검색
     * @param
     * @param pageable
     * @return
     */
//    @Query("SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
//            "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
//            "p.modelNum, p.originLabel, p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt, p.store) " +
//            "FROM Product p " +
//            "WHERE p.productName LIKE :searchWord OR p.description LIKE :searchWord " +
//            "ORDER BY p.productName LIKE :searchWord DESC")
    @Query(value="SELECT p.product_id, p.image_url, p.animal_category, p.product_category, p.product_name, " +
            "p.model_num, p.origin_label, p.price, p.description, p.stock, p.wish_count, p.purchase_count, p.created_at, s.store_name " +
            "FROM products p LEFT JOIN stores s on p.store_id = s.store_id " +
            "WHERE ((p.product_name LIKE :searchTokenOne OR p.description LIKE :searchTokenOne) " +
            "AND (:searchTokenTwo IS NULL OR p.product_name LIKE :searchTokenTwo OR p.description LIKE :searchTokenTwo))" +
            "ORDER BY p.product_name LIKE :searchTokenOne DESC",
            nativeQuery = true)
    List<Object[]> getProductList(@Param("searchTokenOne") String searchTokenOne,
                                  @Param("searchTokenTwo") String searchTokenTwo,
                                  Pageable pageable);

    /**
     * 상품 리스트 페이지 상세검색
     *
     * @param animalCategory
     * @param productCategory
     * @param searchWord
     * @param sortBy
     * @return
     */
    @Query("SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
            "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName," +
            "p.modelNum, p.originLabel, p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt, p.store) " +
            "FROM Product p " +
            "WHERE p.animalCategory = :animalCategory " +
            "AND p.productCategory = :productCategory " +
            "AND ((:searchTokenOne IS NULL OR p.productName LIKE :searchTokenOne OR p.description LIKE :searchTokenOne) " +
            "AND (:searchTokenTwo IS NULL OR p.productName LIKE :searchTokenTwo OR p.description LIKE :searchTokenTwo))" +
            "ORDER BY CASE " +
            "   WHEN :sortBy = 'wishCount' THEN p.wishCount " +
            "   WHEN :sortBy = 'createdAt' THEN p.createdAt " +
            "   ELSE p.price END")
    List<ProductDto> getProductsWithFilters(
            @Param("animalCategory") Integer animalCategory,
            @Param("productCategory")Integer productCategory,
            @Param("searchTokenOne")String searchTokenOne,
            @Param("searchTokenTwo")String searchTokenTwo,
            @Param("sortBy")String sortBy,
            @Param("pageable")Pageable pageable);

    /**
     * 인기상품 TOP10 //JPQL이 LIMIT 쿼리를 지원하지 않아, JPA 메서드로 작성
     *
     * @param animalCategory
     * @param productCategory
     * @return
     */
    List<Product> findTop10ByAnimalCategoryAndProductCategoryOrderByWishCountDesc(
            Integer animalCategory, Integer productCategory);


    /**
     * 추천상품 3종
     *
     * @param animalCategory
     * @param productCategory
     * @return
     */
    List<Product> findTop3ByAnimalCategoryAndProductCategoryOrderByStockDesc(
            Integer animalCategory, Integer productCategory);


    /**
     * 배너상품4종
     *
     * @param animalCategory
     * @param productCategory
     * @return
     */
    List<Product> findTop4ByAnimalCategoryAndProductCategoryOrderByPurchaseCountDesc(
            Integer animalCategory, Integer productCategory);

    /**
     * 상품 상세 페이지
     *
     * @param productId
     * @return
     */
    Optional<Product> findProductById(Long productId);


    @Query(
            value= "SELECT COUNT(*) FROM products p " +
                    "WHERE ((p.product_name LIKE :searchTokenOne OR p.description LIKE :searchTokenOne) " +
                    "AND (:searchTokenTwo IS NULL OR p.product_name LIKE :searchTokenTwo OR p.description LIKE :searchTokenTwo))",
            nativeQuery = true
    )
    Integer getCount(
            @Param("searchTokenOne") String searchTokenOne,
            @Param("searchTokenTwo") String searchTokenTwo
    );

    @Query(
            value= "SELECT COUNT(*) FROM products p " +
                    "WHERE p.animal_category = :animalCategory " +
                    "AND p.product_category = :productCategory " +
                    "AND ((:searchTokenOne IS NULL OR (p.product_name LIKE :searchTokenOne OR p.description LIKE :searchTokenOne))" +
                    "AND :searchTokenTwo IS NULL OR (p.product_name LIKE :searchTokenTwo OR p.description LIKE :searchTokenTwo))",
            nativeQuery = true
    )
    Integer getCountWithFilter(
            @Param("animalCategory") Integer animalCategory,
            @Param("productCategory") Integer productCategory,
            @Param("searchTokenOne") String searchTokenOne,
            @Param("searchTokenTwo") String searchTokenTwo
    );

}
