package com.careers.backend.jobAdvert;

import java.time.LocalDate;

public record JobAdDTO(
    String id,
    String title,
    String description,
    String location,
    LocalDate expiryDate


){}
