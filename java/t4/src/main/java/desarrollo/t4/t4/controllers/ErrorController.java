package desarrollo.t4.t4.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/forbidden")
    public String forbidden() {
        return "forbidden";
    }
}
