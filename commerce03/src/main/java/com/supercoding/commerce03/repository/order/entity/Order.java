package com.supercoding.commerce03.repository.order.entity;


import com.supercoding.commerce03.repository.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="Orders")
@EqualsAndHashCode(of="order_id")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="status", nullable = false)
    private Integer status;

    @Column(name="ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(name = "address", nullable = false, length= 100)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name="is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

}
