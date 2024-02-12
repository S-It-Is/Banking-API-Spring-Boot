package com.project.bank.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bank.DTO.AccountDTO;
import com.project.bank.DTO.UserDTO;
import com.project.bank.controller.auth.AuthenticationRequest;
import com.project.bank.controller.auth.AuthenticationResponse;
import com.project.bank.entity.Account;
import com.project.bank.service.AccountService;
import com.project.bank.service.ExcelService;
import com.project.bank.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/bank")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ExcelService excel;
	
	@Autowired
	MailService mailService;
	

	
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDTO userDTO) {
      return ResponseEntity.ok(accountService.register(userDTO));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ) {
      return ResponseEntity.ok(accountService.authenticate(request));
    }
	
	@PostMapping("/add")
	public ResponseEntity<String> addAccount(@RequestBody Account account) throws MessagingException{
		try {
		accountService.addAccount(account);
		ByteArrayOutputStream file=accountService.createFile();
		mailService.sendMail(account.getEmail(), "Account Creation Update","Welcome To Our Bank",file,"Greetings.pdf");
		return new ResponseEntity<>("Your Account Added Succesfully",HttpStatus.CREATED);
		}
		catch(Exception e) {
			return new ResponseEntity<>("Failed To Add",HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping
	public Map<String,List<Map<String,Object>>> getAccount(){
		Map<String,List<Map<String,Object>>> getAcc=new HashMap();
		getAcc.put("Account Details", accountService.getAccount());
		return getAcc;
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteAcc(@RequestBody AccountDTO deleteRequest){
		try {
		accountService.deleteAcc(deleteRequest.getId());
		return new ResponseEntity<>("Account Deleted Succesfully",HttpStatus.ACCEPTED);
		}
		catch(Exception e) {
			return new ResponseEntity<>("Failed To delete",HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/find")
	public ResponseEntity<List<Map<String,Object>>> findAccount(@RequestBody AccountDTO findID){
		List<Map<String,Object>> data=new ArrayList();
	try {
		data.addAll(accountService.findAccount(findID.getId()));
		return new ResponseEntity<List<Map<String,Object>>>(data,HttpStatus.ACCEPTED);
	}
	catch(Exception e) {
		return new ResponseEntity<List<Map<String,Object>>>(data,HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
	}
	
	}
	
	@GetMapping("/Excel")
	public ResponseEntity<String> generateExcel(HttpServletResponse response) throws IOException{
		try{response.setContentType("application/octet-stream");
		String headerKey="Content-Disposition";
		String headerValue="attachement;filename=Accounts.xls";
		response.setHeader(headerKey, headerValue);
		excel.generateExcel(response);
		return new ResponseEntity<String>("Downloaded Succesfully",HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<String>("Download Fail",HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	

	
}
