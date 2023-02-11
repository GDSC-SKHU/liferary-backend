package gdsc.skhu.liferary.domain.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import gdsc.skhu.liferary.domain.MainPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MainPostDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MainPostDTO.Request")
    public static class Request {
        @Schema(description = "Title", defaultValue = "Test Title")
        private String title;
        @Schema(description = "Username(email)", defaultValue = "testuser@gmail.com")
        private String author;
        @Schema(description = "Category", defaultValue = "programming")
        private String category;
        @Schema(description = "Context", defaultValue = "Test Context")
        private String context;
        @Schema(description = "Video URL", defaultValue = "https://www.youtube.com")
        private String video;
    }

    @Getter
    @Schema(name = "MainPostDTO.Response")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "Title")
        private String title;
        @Schema(description = "Nickname")
        private String nickname;
        @Schema(description = "Category")
        private String category;
        @Schema(description = "Context")
        private String context;
        @Schema(description = "Video URL")
        private String video;
        @Schema(description = "Modified Date")
        private LocalDateTime modifiedDate;

        public Response(MainPost mainPost) {
            this.id = mainPost.getId();
            this.title = mainPost.getTitle();
            this.nickname = mainPost.getAuthor().getNickname();
            this.category = mainPost.getCategory();
            this.context = mainPost.getContext();
            this.video = mainPost.getVideo();
            this.modifiedDate = mainPost.getModifiedDate();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MainPostDTO.Update")
    public static class Update {
        @Schema(description = "Title", defaultValue = "Modified Title")
        private String title;
        @Schema(description = "Category", defaultValue = "exercise")
        private String category;
        @Schema(description = "Context", defaultValue = "Modified Context")
        private String context;
        @Schema(description = "Video URL", defaultValue = "https://www.youtube.com")
        private String video;
    }
}
