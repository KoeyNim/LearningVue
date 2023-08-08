package com.project.vue.common.auth.jwt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "jwt_refresh_token")
@IdClass(value = JwtEntity.Key.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtEntity {

	@Id
	@Column(columnDefinition = "varchar(255)")
	private String refreshTokenUid;

	@Id
	@Column(columnDefinition = "varchar(32)")
	private String memberUid;

	@Data
	public class Key implements Serializable {

		private static final long serialVersionUID = -1388280383618849060L;

		private String refreshTokenUid;
		private String memberUid;

	}
}
