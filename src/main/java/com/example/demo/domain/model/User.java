package com.example.demo.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author eduardooliveira
 */

// @Data faz parte da lib Lombok para utilizar Getter e Setter
@Data
@Entity(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "usuario", unique = true)
    private String username;

    @Column(name = "nome")
    private String name;

    @Column(name = "senha")
    private String password;

    @CreationTimestamp
    private LocalDateTime created_at;

}
