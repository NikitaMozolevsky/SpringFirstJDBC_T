package com.nikita.lessons.controllers;

import com.nikita.lessons.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-batch-update")
public class BatchController {

    private final PersonDao personDao;

    @Autowired
    public BatchController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping
    public String index() {
        return "batch/push_person";
    }

    @GetMapping("/without-batch")
    public String withoutBatch() {
        personDao.testMultipleUpdate();
        return "redirect:/people";
    }

    @GetMapping("/with-batch")
    public String withBatch() {
        personDao.testBatchUpdate();
        return "redirect:/people";
    }
}
