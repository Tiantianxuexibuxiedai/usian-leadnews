package com.example.admin.pojo;

import lombok.Data;

import java.util.List;
@Data
public class RequestParam {
    private List<String> scenes;
    private List<Task> tasks;
}
