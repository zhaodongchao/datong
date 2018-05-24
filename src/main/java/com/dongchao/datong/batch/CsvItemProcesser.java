package com.dongchao.datong.batch;

import com.dongchao.datong.batch.bean.Person;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

public class CsvItemProcesser extends ValidatingItemProcessor<Person> {
    @Override
    public Person process(Person item) throws ValidationException {
        super.process(item); //需要执行父类方法才会调用自定义的校验器

        /*
          自定义对数据的校验以及处理
         */
        if(item.getNation().equals("汉族")){
            item.setNation("01");
        }else {
            item.setNation("02");
        }
        return item;
    }
}
