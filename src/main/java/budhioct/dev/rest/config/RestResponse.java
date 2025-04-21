package budhioct.dev.rest.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class RestResponse {

    @Getter
    @Setter
    @Builder
    public static class list<T>{
        private Integer status_code;
        private String message;
        private String errors;
        private restPagingResponse paging;
        private Integer total_data;
        private T list;
    }

    @Getter
    @Setter
    @Builder
    public static class object<T> {
        private Integer status_code;
        private String message;
        private String errors;
        private T data;
    }

    @Getter
    @Setter
    @Builder
    public static class restError<T> {
        private Integer status_code;
        private String message;
        private String errors;
    }

    @Getter
    @Setter
    @Builder
    public static class restPagingResponse {
        private Integer currentPage;
        private Integer totalPage;
        private Integer sizePage;
    }

}
