package com.vocab.service;

import com.vocab.dto.VocabDto;
import com.vocab.entity.Folder;
import com.vocab.entity.User;
import com.vocab.entity.Vocabulary;
import com.vocab.repository.FolderRepository;
import com.vocab.repository.VocabularyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocabService {

    private final VocabularyRepository vocabRepository;
    private final FolderRepository folderRepository;

    public VocabService(VocabularyRepository vocabRepository, FolderRepository folderRepository) {
        this.vocabRepository = vocabRepository;
        this.folderRepository = folderRepository;
    }

    public List<VocabDto> getAll(User user) {
        return vocabRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(VocabDto::from)
                .collect(Collectors.toList());
    }

    public VocabDto add(User user, VocabDto dto) {
        Vocabulary vocab = new Vocabulary(user, dto.getWord(), dto.getTranslation(), dto.getDate());
        if (dto.getFolderId() != null) {
            folderRepository.findByIdAndUser(dto.getFolderId(), user)
                    .ifPresent(vocab::setFolder);
        }
        return VocabDto.from(vocabRepository.save(vocab));
    }

    @Transactional
    public void delete(User user, Long id) {
        Vocabulary vocab = vocabRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다."));
        vocabRepository.delete(vocab);
    }

    @Transactional
    public void deleteAll(User user) {
        vocabRepository.deleteByUser(user);
    }
}
