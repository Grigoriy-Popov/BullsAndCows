package com.example.bullsAndCows.record;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/main/records")
public class RecordController {
    private final RecordService recordService;

    @GetMapping
    public String getRecords(Model model) {
        List<Record> records = recordService.getRecords();
        if (records.isEmpty()) {
            return "emptyRecords";
        }
        model.addAttribute("records", records);
        return "records";
    }
}
