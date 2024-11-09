package com.jcondotta;

import io.micronaut.context.env.Environment;
import io.micronaut.runtime.Micronaut;

public class BankAccountApplication {

	public static void main(String[] args) {
		Micronaut.build(args)
				.mainClass(BankAccountApplication.class)
				.defaultEnvironments(Environment.DEVELOPMENT)
				.start();
	}
}
