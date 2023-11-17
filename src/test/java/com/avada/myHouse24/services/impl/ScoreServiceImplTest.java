package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.model.ScoreDTO;
import com.avada.myHouse24.model.ScoreForFilterDTO;
import com.avada.myHouse24.repo.ScoreRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScoreServiceImplTest {
    @InjectMocks
    private ScoreServiceImpl scoreService;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private AccountTransactionServiceImpl accountTransactionService;

    @Mock
    private FlatServiceImpl flatService;
    @Test
    void getById() {
        Score mockScore = new Score();
        when(scoreRepository.findById(anyLong())).thenReturn(Optional.of(mockScore));

        Score result = scoreService.getById(1L);

        assertEquals(mockScore, result);
        verify(scoreRepository, times(1)).findById(1L);
    }

    @Test
    void getAll() {
        List<Score> mockScoreList = new ArrayList<>();
        when(scoreRepository.findAll()).thenReturn(mockScoreList);

        List<Score> result = scoreService.getAll();
        assertEquals(mockScoreList, result);
        verify(scoreRepository, times(1)).findAll();
    }

    @Test
    void getAllByStatus() {
        List<Score> mockScoreList = new ArrayList<>();
        when(scoreRepository.findAllByStatus(anyString())).thenReturn(mockScoreList);
        List<Score> result = scoreService.getAllByStatus("active");
        assertEquals(mockScoreList, result);
        verify(scoreRepository, times(1)).findAllByStatus("active");
    }

    @Test
    void getOnlyNumber() {
        List<Score> mockScoreList = new ArrayList<>();
        Score score = new Score();
        score.setNumber("134");
        mockScoreList.add(score);
        when(scoreRepository.findAll()).thenReturn(mockScoreList);
        List<String> result = scoreService.getOnlyNumber();
        assertEquals("134", result.get(0));
        verify(scoreRepository, times(1)).findAll();
    }

    @Test
    void existNumber() {
        when(scoreRepository.existsByNumber(anyString())).thenReturn(true);
        boolean result = scoreService.existNumber("123456");
        assertTrue(result);
        verify(scoreRepository, times(1)).existsByNumber("123456");
    }

    @Test
    void save() {
        Score scoreToSave = new Score();
        scoreService.save(scoreToSave);
        verify(scoreRepository, times(1)).save(scoreToSave);
    }

    @Test
    void deleteById() {
        Score mockScore = new Score();
        Flat flat = new Flat();
        mockScore.setFlat(flat);
        when(scoreRepository.findById(anyLong())).thenReturn(Optional.of(mockScore));
        scoreService.deleteById(1L);
        verify(scoreRepository, times(3)).findById(1L);
        verify(accountTransactionService, times(1)).clearAllScore(any(Score.class));
        verify(scoreRepository, times(1)).deleteById(1L);
    }

    @Test
    void getByNumber() {
        Score mockScore = new Score();
        when(scoreRepository.findByNumber(anyString())).thenReturn(Optional.of(mockScore));
        Score result = scoreService.getByNumber("123456");
        assertEquals(mockScore, result);
        verify(scoreRepository, times(1)).findByNumber("123456");
    }

    @Test
    void getPage() {
        List<Score> scores = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            scores.add(new Score());
        }
        when(scoreRepository.findAll()).thenReturn(scores);

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, scores.size());

        List<Score> currentPageItems = scores.subList(startIndex, endIndex);

        Page<Score> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), scores.size());

        Model mockModel = new ExtendedModelMap();
        when(scoreRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);


        Page<Score> result = scoreService.getPage(0, mockModel);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
        verify(scoreRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetPage() {
        List<ScoreDTO> scoreDTOs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            scoreDTOs.add(new ScoreDTO());
        }

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, scoreDTOs.size());

        List<ScoreDTO> currentPageItems = scoreDTOs.subList(startIndex, endIndex);

        Page<ScoreDTO> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), scoreDTOs.size());

        Model mockModel = new ExtendedModelMap();

        Page<ScoreDTO> result = scoreService.getPage(0, mockModel, scoreDTOs);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
    }

    @Test
    void forSelect() {
        List<Score> mockScores = new ArrayList<>();
        Page<Score> mockScorePage = new PageImpl<>(mockScores);
        when(scoreRepository.findAll(any(Pageable.class))).thenReturn(mockScorePage);

        List<Score> result = scoreService.forSelect(1, 10, null);

        assertEquals(mockScores, result);
        verify(scoreRepository, times(1)).findAll(any(Pageable.class));

        when(scoreRepository.findByNumberContainingIgnoreCase(anyString(), any())).thenReturn(mockScorePage);
        List<Score> result1 = scoreService.forSelect(1, 10, "qwerty");

        assertEquals(mockScores, result1);
    }

    @Test
    void filter() {
        ScoreForFilterDTO score = new ScoreForFilterDTO();
        score.setNumber("1");
        score.setStatus("1");
        score.setFlatNumber(1L);
        score.setHouse(new House());
        score.setSection(new Section());
        User user = new User();
        user.setId(1L);
        score.setUser(user);
        score.setIsDebt(true);

        List<ScoreDTO> scoreDTOS = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setNumber(String.valueOf(i));
            scoreDTO.setStatus("1");
            Flat flat = new Flat();
            flat.setNumber(1);
            User user1 = new User();
            user1.setId(1L);
            flat.setUser(user1);
            scoreDTO.setFlat(flat);
            scoreDTO.setHouse(new House());
            scoreDTO.setSection(new Section());
            scoreDTO.setBalance(1.0);
            scoreDTOS.add(scoreDTO);
        }

        List<ScoreDTO> result1 = scoreService.filter(score, scoreDTOS);
        assertEquals(1, result1.size());
        assertEquals("1", result1.get(0).getNumber());

        scoreDTOS.get(1).setBalance(-1.0);
        score.setIsDebt(false);
        List<ScoreDTO> result2 = scoreService.filter(score, scoreDTOS);
        assertEquals(1, result1.size());
        assertEquals("1", result1.get(0).getNumber());
    }

    @Test
    void excel() throws IOException {
        Score score = new Score();
        score.setNumber("score number");
        score.setStatus("score status");
        score.setBalance(10.0);
        Flat flat = new Flat();
        flat.setNumber(121);
        Section section = new Section();
        section.setName("section name");
        User user = new User();
        user.setFirstName("user name");
        House house = new House();
        house.setName("house name");
        flat.setSection(section);
        flat.setUser(user);
        flat.setHouse(house);
        score.setFlat(flat);
        when(scoreRepository.findAll()).thenReturn(Arrays.asList(score));
        MockHttpServletResponse response = new MockHttpServletResponse();
        scoreService.excel(response);
        byte[] content = response.getContentAsByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);
        Iterator<Cell> cellIterator = row.cellIterator();

        String[] expectResult = new String[]{"score number", "score status", "121.0", "house name", "section name", "user name", "10.0"};
        int index = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = String.valueOf(cell);
            assertEquals(cellValue, expectResult[index++]);
        }
        assertEquals(7, index);
        assertEquals("application/vnd.ms-excel", response.getContentType());
        assertEquals("attachment; filename=example.xls", response.getHeader("Content-Disposition"));
    }
    @Test
    void getAllBalance(){
        when(scoreRepository.findAllBalance()).thenReturn(100.0);
        Double result = scoreService.getAllBalance();
        verify(scoreRepository, times(1)).findAllBalance();
        assertEquals(100.0, result);
    }
    @Test
    void getAllEmpty(){
        List<Score> expectedEmptyScores = new ArrayList<>();
        when(scoreRepository.findAllByFlatIsNull()).thenReturn(expectedEmptyScores);
        List<Score> result = scoreService.getAllEmpty();
        verify(scoreRepository, times(1)).findAllByFlatIsNull();
        assertEquals(expectedEmptyScores, result);
    }
}