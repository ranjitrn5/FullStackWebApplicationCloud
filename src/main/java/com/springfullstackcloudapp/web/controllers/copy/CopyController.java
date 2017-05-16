package com.springfullstackcloudapp.web.controllers.copy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ranji on 5/15/2017.
 */
@Controller
public class CopyController {

    @RequestMapping("/about")
    public String about(){
        // This is view name
        return "copy/about";
    }
}
