package com.dongchao.datong.common;

import com.dongchao.datong.system.Authors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VueHandler {
    @Autowired
    private Authors author;

    @RequestMapping("/testVue")
    public String foo(Model model) {
        model.addAttribute("name",author.getName());
        return "testVue";
    }
}
