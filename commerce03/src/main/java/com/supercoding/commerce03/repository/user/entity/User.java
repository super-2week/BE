package com.supercoding.commerce03.repository.user.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


@Entity
@Table(name ="Users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name ="user_name")
    private String userName;

    @Column(name ="image_url")
    private String imageUrl;

    @CreatedDate
    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name ="updated_at")
    private LocalDateTime updatedAt;

    @Column (name ="is_deleted")
    private boolean isDeleted;
}
