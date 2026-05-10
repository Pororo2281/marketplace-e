package order_service.order.Controller;

import order_service.order.Service.DownloadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/downloads")
public class DownloadController {
    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }


    @GetMapping("/{orderItemId}")
    public ResponseEntity<String> getDownloadLink(
            @PathVariable Long orderItemId,
            @RequestHeader("X-USER-ID") Long userId
    ){
        return ResponseEntity.ok(downloadService.getDownloadLink(orderItemId,userId));
    }

}
