package com.supercoding.commerce03.repository.order.entity;


import com.supercoding.commerce03.repository.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

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
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="status", nullable = false)
    private String status;

    @CreatedDate
    @Column(name="ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name="post_comment")
    private String postComment;

    @ColumnDefault("false")
    @Column(name="is_deleted", nullable = false)
    private Boolean isDeleted;

    //orderedAt 이랑 중복되는 느낌 고려해보자.
    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "total_amount")
    private Integer totalAmount;

    public void setStatusAndTimeNow(String status) {
        this.setStatus(status);
        this.setModifiedAt(LocalDateTime.now());
    }

}
