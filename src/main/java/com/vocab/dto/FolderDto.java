package com.vocab.dto;

import com.vocab.entity.Folder;

public class FolderDto {

    private Long id;
    private String name;
    private long wordCount;

    public FolderDto() {}

    public static FolderDto from(Folder f, long wordCount) {
        FolderDto dto = new FolderDto();
        dto.id = f.getId();
        dto.name = f.getName();
        dto.wordCount = wordCount;
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public long getWordCount() { return wordCount; }

    public void setName(String name) { this.name = name; }
}
