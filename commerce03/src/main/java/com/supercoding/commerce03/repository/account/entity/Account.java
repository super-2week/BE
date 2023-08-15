package com.supercoding.commerce03.repository.account.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Integer id;

    @Column(name = "account_by", nullable = false)
    private Integer accountBy;

    @Column(name = "account_num", nullable = false)
    private Integer accountNum;

    @Column(name = "card_num", nullable = false)
    private Integer cardNum;

    @Column(name = "total_account", nullable = false)
    private Integer totalAccount;

    @Column(name = "created_at")
    @CreatedDate
    private String created_at;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "user_id")
    private Integer userId;

}
