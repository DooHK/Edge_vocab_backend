package com.vocab.service;

import com.vocab.dto.FolderDto;
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
public class FolderService {

    private final FolderRepository folderRepository;
    private final VocabularyRepository vocabRepository;

    public FolderService(FolderRepository folderRepository, VocabularyRepository vocabRepository) {
        this.folderRepository = folderRepository;
        this.vocabRepository = vocabRepository;
    }

    public List<FolderDto> getAll(User user) {
        return folderRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(f -> FolderDto.from(f, vocabRepository.countByUserAndFolder(user, f)))
                .collect(Collectors.toList());
    }

    public FolderDto create(User user, String name) {
        if (folderRepository.findByUserAndName(user, name).isPresent()) {
            throw new IllegalArgumentException("이미 같은 이름의 폴더가 있습니다.");
        }
        Folder folder = folderRepository.save(new Folder(user, name));
        return FolderDto.from(folder, 0);
    }

    @Transactional
    public void delete(User user, Long folderId) {
        Folder folder = folderRepository.findByIdAndUser(folderId, user)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        List<Vocabulary> vocabs = vocabRepository.findByUserAndFolderOrderByCreatedAtDesc(user, folder);
        vocabs.forEach(v -> v.setFolder(null));
        vocabRepository.saveAll(vocabs);

        folderRepository.delete(folder);
    }

    @Transactional
    public void moveVocabToFolder(User user, Long vocabId, Long folderId) {
        Vocabulary vocab = vocabRepository.findByIdAndUser(vocabId, user)
                .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다."));

        if (folderId == null) {
            vocab.setFolder(null);
        } else {
            Folder folder = folderRepository.findByIdAndUser(folderId, user)
                    .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));
            vocab.setFolder(folder);
        }
        vocabRepository.save(vocab);
    }

    public Folder getOrCreateDateFolder(User user, String dateName) {
        return folderRepository.findByUserAndName(user, dateName)
                .orElseGet(() -> folderRepository.save(new Folder(user, dateName)));
    }
}
