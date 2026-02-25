package com.vocab.controller;

import com.vocab.dto.VocabDto;
import com.vocab.entity.User;
import com.vocab.service.FolderService;
import com.vocab.service.VocabService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vocab")
public class VocabController {

    private final VocabService vocabService;
    private final FolderService folderService;

    public VocabController(VocabService vocabService, FolderService folderService) {
        this.vocabService = vocabService;
        this.folderService = folderService;
    }

    @GetMapping
    public ResponseEntity<List<VocabDto>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(vocabService.getAll(user));
    }

    @PostMapping
    public ResponseEntity<VocabDto> add(@AuthenticationPrincipal User user,
                                         @Valid @RequestBody VocabDto dto) {
        return ResponseEntity.ok(vocabService.add(user, dto));
    }

    @PatchMapping("/{id}/folder")
    public ResponseEntity<?> moveToFolder(@AuthenticationPrincipal User user,
                                          @PathVariable Long id,
                                          @RequestBody Map<String, Long> body) {
        try {
            folderService.moveVocabToFolder(user, id, body.get("folderId"));
            return ResponseEntity.ok(Map.of("message", "폴더 이동 완료"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@AuthenticationPrincipal User user,
                                                       @PathVariable Long id) {
        try {
            vocabService.delete(user, id);
            return ResponseEntity.ok(Map.of("message", "삭제 완료"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<Map<String, String>> deleteAll(@AuthenticationPrincipal User user) {
        vocabService.deleteAll(user);
        return ResponseEntity.ok(Map.of("message", "전체 삭제 완료"));
    }
}
