package org.example;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.Container.ExecResult;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class ExampleTest {

	@Container
	GenericContainer<?> sftServer = new GenericContainer<>(DockerImageName.parse("atmoz/sftp:alpine"))
			.withExposedPorts(22)
			.withEnv("SFTP_USERS", "tester:s3cr3t:::tmp");

	@Test
	void test() throws Exception {
		ExecResult r = sftServer.execInContainer("sshd", "-V");

		assertThat(r.getExitCode()).isZero();

		assertThat(r.getStderr()).contains("OpenSSH_");
	}

}
