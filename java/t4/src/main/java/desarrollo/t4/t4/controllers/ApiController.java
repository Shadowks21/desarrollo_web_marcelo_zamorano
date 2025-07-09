package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.services.ApiService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    private final ApiService apiService;
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }
}
