package com.httpclient.mapper;

import com.httpclient.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrderMapper {

    @Insert("insert into order_number values(null,#{orderName},0,#{orderToken},#{orderId})")
    int insertOrder(OrderEntity orderEntity);

    @Update("update order_number set order_status=#{orderStatus} where order_token=#{orderToken}")
    int updateOrderStatus(String orderToken, Integer orderStatus);

    @Select("select id as id,order_name as ordername,order_status as orderstatus," +
            "order_token as ordertoken, order_id as orderid from order_number" +" "+
            "where order_token = #{orderToken}")
    OrderEntity getOrderNumber(String orderToken);


}
