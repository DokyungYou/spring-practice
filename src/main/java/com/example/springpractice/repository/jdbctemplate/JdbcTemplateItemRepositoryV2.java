package com.example.springpractice.repository.jdbctemplate;

import com.example.springpractice.domain.Item;
import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.ItemSearchCondition;
import com.example.springpractice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NamedParameterJdbcTemplate 사용
 *
 * SqlParameterSource
 * BeanPropertySqlParameterSource
 * MapSqlParameterSource
 * Map
 * BeanPropertyRowMapper
 */
@Slf4j
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

   // private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateItemRepositoryV2(DataSource dataSource){
        //jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values(:itemName,:price,:quantity)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);

        // jdbcTemplate 을 쓸 때 db 에서 생성해준 아이디 값을 가져오려면 KeyHolder 를 사용해야함
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, param, keyHolder);

        // 데이터베이스에 Insert 가 완료돼야 생성된 PK ID 값 확인 가능
        long key = keyHolder.getKey().longValue();
        item.setId(key);

        return item;
    }


    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);

        jdbcTemplate.update(sql, param);
    }

    @Override
    public Optional<Item> findById(Long id) {

        String sql = "select id, item_name, price, quantity from item where id =:id";
        
        try {
            Map<String, Long> param = Map.of("id,", id);

            // 데이터가 없으면 예외터짐
            Item item = jdbcTemplate.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * JDBC의 단점
     * 동적쿼리를 생성하기가 까다롭다.
     */
    @Override
    public List<Item> findAll(ItemSearchCondition condition) {
        Integer maxPrice = condition.getMaxPrice();
        String itemName = condition.getItemName();
        SqlParameterSource param = new BeanPropertySqlParameterSource(condition);
        String sql = "select id, item_name, price, quantity from item";

        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }        log.info("sql={}", sql);
        return jdbcTemplate.query(sql, param, itemRowMapper());
    }


    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class); //camel 변환 지원
    }
}
