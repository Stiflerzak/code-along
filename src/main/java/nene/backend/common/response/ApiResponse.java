package nene.backend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// TODO: Concept - GENERIC CLASS (ApiResponse<T>)
//  The <T> means this class can wrap ANY type of data.
//  Example: ApiResponse<TaskDto.Response> wraps a task response.
//  This gives us a CONSISTENT response structure for all our endpoints.

// TODO: Concept - API RESPONSE PATTERN
//  Instead of returning raw data, we wrap it in a standard envelope:
//  { success: true/false, message: "...", data: {...}, timestamp: "..." }
//  This makes it easier for frontend developers to handle responses.

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // TODO: Concept - STATIC FACTORY METHOD
    //  These are helper methods to create success/error responses easily.
    //  Usage: ApiResponse.success("Task created", taskData)
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
