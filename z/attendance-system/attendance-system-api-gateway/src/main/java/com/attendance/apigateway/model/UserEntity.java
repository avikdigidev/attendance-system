package com.attendance.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tbl_user")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true)
    private Long userId;
    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "password", nullable = false, length = 1000)
    private String password;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column
    private String emailId;
}
