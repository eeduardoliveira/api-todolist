package com.example.demo.domain.model;

import java.time.LocalDateTime;

public record TaskUpd (
        String description,

        String title,

        LocalDateTime startAt,

        LocalDateTime endAt,

        String priority
){

}
