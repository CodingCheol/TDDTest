package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_address",length = 100)
    private String userAddress;

    @Column(name = "user_email",length = 100)
    private String userEmail;

}
