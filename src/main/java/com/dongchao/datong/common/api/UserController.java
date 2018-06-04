package com.dongchao.datong.common.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Scope("singleton")
@RestController
public class UserController {
    @Autowired
    private UserDao userDao ;
    @GetMapping(value = "/user/{id}")
    public String findUser(@PathVariable(name = "id",required = false) String id){
        if (id == null){
            id = new Random(10000).nextInt() +"" ;
        }
        User user1 = new User(id,"赵东朝"+id,"东东"+id,25,new BigDecimal(5000+Integer.parseInt(id)));
        userDao.save(user1);
        return userDao.getStr(id);
    }
    @PostMapping(value = "employee/dzsg/contract/download")
    @ResponseBody
    public Map<String,Object> testKb(@RequestParam(name = "kbUserId") String kbUserId){
        Map<String,Object> result = new ConcurrentHashMap<>();
        result.put("status",200);
        result.put("message","访问下载图pain接口成功");
        Map<String,Object> body = new Hashtable<>();
        body.put("fileUrl","https://oss-test-hd2-03.oss-cn-shanghai-finance-1-pub.aliyuncs.com/kbao/img/20180530/1527651010525-1017231260.jpg");
        body.put("fileName",kbUserId+"测试用户的合同文件");

        result.put("body",body);

        return result ;
    }
}
