package com.supercoding.commerce03.repository.user.entity;

import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.web.dto.user.SignUp;
import com.supercoding.commerce03.web.dto.user.UpdateProfile;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name="Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@SQLDelete(sql = "UPDATE User as u SET u.is_deleted = true WHERE id = ?")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "image_url")
	private String imageUrl;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	public static User toEntity(SignUp signUp){
		return User.builder()
			.userName(signUp.getUserName())
			.createdAt(LocalDateTime.now())
			.isDeleted(false)
			.build();
	}
	public static void update(User user,UpdateProfile updateProfile){
		user.setUserName(updateProfile.getUserName());
		user.setImageUrl(updateProfile.getImageUrl());
		user.setUpdatedAt(LocalDateTime.now());
	}
}
