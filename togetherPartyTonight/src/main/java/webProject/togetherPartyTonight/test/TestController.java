package webProject.togetherPartyTonight.test;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<HttpStatus> test(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
