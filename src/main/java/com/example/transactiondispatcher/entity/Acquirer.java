package com.example.transactiondispatcher.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acquirers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Acquirer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "percent_share", nullable = false)
    private Integer percentShare;
}
