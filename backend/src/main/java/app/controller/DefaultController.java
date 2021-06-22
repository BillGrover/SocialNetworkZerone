package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@ApiIgnore
public class DefaultController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(
            method = {
                    RequestMethod.OPTIONS,
                    RequestMethod.GET},
            value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex(@PathVariable String path) {
        return "forward:/";
    }

    @GetMapping("/log/")
    public String getLogs(Model model) {
        model.addAttribute("date", LocalDate.now());
        File logs = new File("logs");
        File[] files = logs.listFiles(File::isFile);
        model.addAttribute(
                "logList",
                Arrays.stream(files)
                        .map(File::getName)
                        .collect(Collectors.toList())
        );
        return "logs";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}





















