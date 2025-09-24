package com.ilayangudi_news_posting.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ilayangudi_news_posting.servicerepo.SuperAdminServiceRepository;

@RestController
@RequestMapping("/super/v1")
public class SuperAdminController {

	@Autowired
	private SuperAdminServiceRepository superAdminServiceRepo;

	@GetMapping("/auth/user-data")
	public ResponseEntity<?> getAllDatasForSuperAdmin(Principal principal) {

		return ResponseEntity.ok(superAdminServiceRepo.getAllDatasForSuperAdmin(principal.getName()));

	}

	@GetMapping("/auth/user-reports")
	public ResponseEntity<?> getAllReportDatasForSuperAdmin(Principal principal) {

		return ResponseEntity.ok(superAdminServiceRepo.getAllReportsDataForSuperAdmin(principal.getName()));

	}

}
