package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.service.FileService;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class FileControllerTest {

    private FileService fileService;
    private FileController fileController;

    private MultipartFile testFile;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    @Test
    public void whenGetByIdThenResponseEntityOk() throws IOException {
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        when(fileService.getFileById(anyInt())).thenReturn(Optional.of(fileDto));
        var actual = fileController.getById(1);

        assertThat(actual).isEqualTo(ResponseEntity.ok(fileDto.getContent()));
    }

    @Test
    public void whenGetByIdThenResponseEntityNotFound() {
        when(fileService.getFileById(anyInt())).thenReturn(Optional.empty());
        var actual = fileController.getById(1);

        assertThat(actual).isEqualTo(ResponseEntity.notFound().build());
    }
}