package com.techguy.repository;

import com.techguy.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends  PagingAndSortingRepository <Product,Long>{


//    @Query(value = "SELECT \n" +
//            "p.id,\n" +
//            "p.buy_amount,\n" +
//            "p.buy_status,\n" +
//            "p.free,\n" +
//            "p.name,\n" +
//            "p.total_unit_price\n" +
//            " FROM product p join product_record pd on pd.product_id where pd.",nativeQuery = true)
//    Page<Product> findAll(Pageable page,Long memberId);


    List<Product> findByNameContains(String name);
    @Query(value = "SELECT * from product  where :currentTime>= start_time and :currentTime<=end_time or free=1",nativeQuery = true)
    Page<Product> findProductBetweenStartTimeAndEndTime(@Param("currentTime") String currentTime, Pageable page);
}
