

package security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/hello")
    public String show(){
        return "this is my controller";
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public  String userEndpoint(){
        return "this is user end point";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public  String adminEndpoint(){
        return "this is admin end point";
    }
}
