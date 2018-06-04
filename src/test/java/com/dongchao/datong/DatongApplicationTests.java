package com.dongchao.datong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

/**
 * Created by dongchao.zhao on 2018/6/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DatongApplicationTests {
    @Autowired
    DataSource dataSource ;
    @Test
    public void contextLoads() {
        System.out.println(dataSource.getClass());
    }

}
