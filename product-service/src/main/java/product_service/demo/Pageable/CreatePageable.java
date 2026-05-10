package product_service.demo.Pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CreatePageable {
    public static Pageable createPageableWithSort(Pageable pageable, String sort) {
        if (sort == null || sort.isEmpty()) {
            return pageable;
        }

        String[] sortParams = sort.split(",");
        String field = sortParams[0];
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);

        Sort sortOrder = Sort.by(direction, field);

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortOrder
        );
    }
}
