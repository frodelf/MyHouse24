package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.model.ScoreDTO;
import com.avada.myHouse24.model.ScoreForFilterDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.repo.ScoreRepository;
import com.avada.myHouse24.services.ScoreService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final AccountTransactionServiceImpl accountTransactionService;
    private final FlatServiceImpl flatService;

    @Override
    public Score getById(long id) {
        return scoreRepository.findById(id).get();
    }
    public Double getAllBalance(){return scoreRepository.findAllBalance();}
    @Override
    public List<Score> getAll() {
        return scoreRepository.findAll();
    }
    @Override
    public List<Score> getAllEmpty() {
        return scoreRepository.findAllByFlatIsNull();
    }

    @Override
    public List<Score> getAllByStatus(String status) {
        return scoreRepository.findAllByStatus(status);
    }

    @Override
    public List<String> getOnlyNumber() {
        List<Score> scores = scoreRepository.findAll();
        List<String> numbers = new ArrayList<>();
        for (Score score : scores) {
            numbers.add(score.getNumber());
        }
        return numbers;
    }

    @Override
    public boolean existNumber(String number) {
        return scoreRepository.existsByNumber(number);
    }

    @Override
    public void save(Score score) {
        scoreRepository.save(score);
    }

    @Override
    public void deleteById(Long id) {
        if (getById(id).getFlat() != null) {
            Flat flat = getById(id).getFlat();
            flat.setScore(null);
            flatService.save(flat);
        }
        accountTransactionService.clearAllScore(getById(id));
        scoreRepository.deleteById(id);
    }

    @Override
    public Score getByNumber(String number) {
        return scoreRepository.findByNumber(number).get();
    }


    @Override
    public Page<Score> getPage(int pageNumber, Model model) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(scoreRepository.findAll().size() / size - 1) > 0 ? (int) Math.ceil(scoreRepository.findAll().size() / size - 1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int) size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber + 1);
        return scoreRepository.findAll(pageRequest);
    }

    @Override
    public Page<ScoreDTO> getPage(int pageNumber, Model model, List<ScoreDTO> scoreDTOS) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(scoreDTOS.size() / size - 1) > 0 ? (int) Math.ceil(scoreDTOS.size() / size - 1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, scoreDTOS.size());
        List<ScoreDTO> pageList = scoreDTOS.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<ScoreDTO> scoreDTOPage = new PageImpl<>(pageList, pageable, scoreDTOS.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber + 1);
        return scoreDTOPage;
    }

    public List<Score> forSelect(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Score> scorePage;

        if (search != null && !search.isEmpty()) {
            scorePage = scoreRepository.findByNumberContainingIgnoreCase(search, pageable);
        } else {
            scorePage = scoreRepository.findAll(pageable);
        }

        return scorePage.getContent();
    }

    public List<ScoreDTO> filter(ScoreForFilterDTO scoreForFilterDTO, List<ScoreDTO> scoreDTOS) {
        if (!scoreForFilterDTO.getNumber().isBlank()) {
            scoreDTOS = scoreDTOS.stream()
                    .filter(dto -> dto.getNumber() != null && dto.getNumber().contains(scoreForFilterDTO.getNumber()))
                    .collect(Collectors.toList());
        }
        if (!scoreForFilterDTO.getStatus().isBlank()) {
            scoreDTOS = scoreDTOS.stream()
                    .filter(dto -> dto.getStatus() != null && dto.getStatus().contains(scoreForFilterDTO.getStatus()))
                    .collect(Collectors.toList());
        }
        if (scoreForFilterDTO.getFlatNumber() != null) {
            scoreDTOS = scoreDTOS.stream()
                    .filter(dto -> dto.getFlat() != null && String.valueOf(dto.getFlat().getNumber()).contains(scoreForFilterDTO.getFlatNumber().toString()))
                    .collect(Collectors.toList());
        }
        if (scoreForFilterDTO.getHouse() != null) {
            scoreDTOS = scoreDTOS.stream()
                    .filter(dto -> dto.getHouse() != null && dto.getHouse().getId() == scoreForFilterDTO.getHouse().getId())
                    .collect(Collectors.toList());
        }
        if (scoreForFilterDTO.getSection() != null) {
            scoreDTOS = scoreDTOS.stream()
                    .filter(dto -> dto.getSection() != null && dto.getSection().getId() == scoreForFilterDTO.getSection().getId())
                    .collect(Collectors.toList());
        }
        if (scoreForFilterDTO.getUser() != null) {
            scoreDTOS = scoreDTOS.stream()
                    .filter(dto -> dto.getFlat() != null && dto.getFlat().getUser().getId() == scoreForFilterDTO.getUser().getId())
                    .collect(Collectors.toList());
        }
        if (scoreForFilterDTO.getIsDebt() != null) {
            if (scoreForFilterDTO.getIsDebt()) {
                scoreDTOS = scoreDTOS.stream()
                        .filter(dto -> dto.getBalance() != null && dto.getBalance() >= 0)
                        .collect(Collectors.toList());
            } else {
                scoreDTOS = scoreDTOS.stream()
                        .filter(dto -> dto.getBalance() != null && dto.getBalance() < 0)
                        .collect(Collectors.toList());
            }
        }
        return scoreDTOS;
    }

    public void excel(HttpServletResponse response) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("page1");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"№", "Статус", "Квартира", "Дом", "Секции","Владелец", "Останок (грн)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        List<Score> scores = getAll();
        for (int rowIndex = 0; rowIndex < scores.size(); rowIndex++) {
            Score score = scores.get(rowIndex);
            Row dataRow = sheet.createRow(rowIndex + 1);

            dataRow.createCell(0).setCellValue(score.getNumber());
            dataRow.createCell(1).setCellValue(score.getStatus());
            if(score.getFlat()!=null) {
                dataRow.createCell(2).setCellValue(score.getFlat().getNumber());
                dataRow.createCell(3).setCellValue(score.getFlat().getHouse().getName());
                dataRow.createCell(4).setCellValue(score.getFlat().getSection().getName());
                dataRow.createCell(5).setCellValue(score.getFlat().getUser().getFirstName());
            }
            dataRow.createCell(6).setCellValue(score.getBalance());
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=example.xls");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
    public long count(){
        return scoreRepository.count();
    }
}