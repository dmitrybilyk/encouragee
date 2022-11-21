package com.encouragee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Validated
@ConfigurationProperties("data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataAccessProperties {

	@NotNull
	@Valid
	private DatabaseProperties database = new DatabaseProperties();

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DatabaseProperties {
		@NotNull
		private Database type = Database.POSTGRESQL;
		@NotEmpty
		private String driver = "org.postgresql.Driver";
		@NotEmpty
		private String address = "jdbc:postgresql://localhost:5432/callrec";
		private String username = "postgres";
		private String password = "postgres";
	}

}
