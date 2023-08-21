package com.supercoding.commerce03.repository.user.entity;

import com.supercoding.commerce03.web.dto.user.SignUp;
import com.supercoding.commerce03.web.dto.user.UpdateProfile;
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

@Getter
@Table(name = "User_Details")
@Entity
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_detail_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "password")
	private String password;

	@Column(name = "address")
	private String address;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "detail_address")
	private String detailAddress;

	public static UserDetail toEntity(User user, SignUp signUp, String passwordEncode) {
		return UserDetail.builder()
			.email(signUp.getEmail())
			.password(passwordEncode)
			.address(signUp.getAddress())
			.detailAddress(signUp.getDetailAddress())
			.phoneNumber(signUp.getPhoneNumber())
			.user(user)
			.build();
	}
	public static void update(UserDetail userDetail,UpdateProfile updateProfile,String passwordEncode) {
		userDetail.setPassword(passwordEncode);
		userDetail.setAddress(updateProfile.getAddress());
		userDetail.setDetailAddress(updateProfile.getDetailAddress());
	}
}

