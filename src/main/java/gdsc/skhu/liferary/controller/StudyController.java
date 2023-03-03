package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.StudyDTO;
import gdsc.skhu.liferary.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Study", description = "API for study")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study")
public class StudyController {
    private final StudyService studyService;

    // Create
    @Operation(summary = "create study", description = "Create study")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping(name = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudyDTO.Response> save(Principal principal, @ModelAttribute StudyDTO.Request request) {
        return ResponseEntity.ok(studyService.save(principal, request));
    }

    // Read
    @Operation(summary = "get study by main post id", description = "Read study by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{mainPostId}")
    public StudyDTO.Response findByMainPost(@PathVariable("mainPostId") Long mainPostId) {
        return studyService.findByMainPost(mainPostId);
    }

    // Update
    @Operation(summary = "Update study", description = "Update study")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping(name = "/{mainPostId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudyDTO.Response> update(Principal principal,
                                                    @ModelAttribute StudyDTO.Update update,
                                                    @RequestParam("mainPostId") Long mainPostId) {
        return ResponseEntity.ok(studyService.update(principal, update, mainPostId));
    }

    // Delete
    @Operation(summary = "Delete study", description = "Delete study")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{mainPostId}")
    public ResponseEntity<String> delete(@PathVariable("mainPostId") Long mainPostId) {
        return studyService.delete(mainPostId);
    }
}
