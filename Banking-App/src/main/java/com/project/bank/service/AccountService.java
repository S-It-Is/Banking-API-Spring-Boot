package com.project.bank.service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bank.DTO.UserDTO;
import com.project.bank.config.JwtService;
import com.project.bank.controller.auth.AuthenticationRequest;
import com.project.bank.controller.auth.AuthenticationResponse;
import com.project.bank.entity.Account;
import com.project.bank.entity.Role;
import com.project.bank.repository.AccountRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
    @Autowired
    AuthenticationManager authenticationManager;
	
	   public AuthenticationResponse register(UserDTO userDTO) {
	        var user = Account.builder()
	            .accountHolderName(userDTO.getUserName())
	            .email(userDTO.getEmail())
	            .balance(userDTO.getBalance())
	            .pass(passwordEncoder.encode(userDTO.getPassword()))
	            .role(Role.USER)
	            .build();
	        accountRepository.save(user);
	       var jwtToken=jwtService.generateToken(user);
	       return AuthenticationResponse.builder()
	    		   .token(jwtToken)
	    		   .build();
	    }

	    public AuthenticationResponse authenticate(AuthenticationRequest request) {
	    	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
	    			request.getEmail(),request.getPassword()));
	    	
	    	var user=accountRepository.findByEmail(request.getEmail()).orElseThrow();
	    	var jwtToken=jwtService.generateToken(user);
	        return AuthenticationResponse.builder()
	     		   .token(jwtToken)
	     		   .build();
	    }
	
	public void addAccount(Account account) {
	accountRepository.save(account);
	}
	public List<Map<String,Object>> getAccount() {
	List<Map<String,Object>> giveAcc=new ArrayList();
	List<Account> data=accountRepository.findAll();
	for(Account ac:data) {
		Map<String,Object> datas=new HashMap();
		datas.put("Account Id",ac.getId());
		datas.put("Account Holder Name", ac.getAccountHolderName());
		datas.put("Account Balance", ac.getBalance());
		datas.put("Email", ac.getEmail());
		giveAcc.add(datas);
		
	}
	return giveAcc;
		
	}
	
	@Transactional
	public void deleteAcc(int no) {
	accountRepository.deleteById(no);
	}
	
	public List<Map<String,Object>> findAccount(int no){
	
	Optional<Account> optionalAccount=accountRepository.findById(no);
	Map<String,Object> datas=new HashMap();
	List<Map<String,Object>> data=new ArrayList();
	if(optionalAccount.isPresent()) {
		Account account =optionalAccount.get();
		datas.put("Account Holder Name",account.getAccountHolderName());
		datas.put("Balance",account.getBalance());
		datas.put("Email", account.getEmail());
		data.add(datas);
		
	}
	return data;
	}
	
	public ByteArrayOutputStream createFile() throws IOException {
		
	ByteArrayOutputStream outputStream=new java.io.ByteArrayOutputStream();
	
	PDDocument document=new PDDocument();
	PDPage page=new PDPage();
	
	document.addPage(page);
	
	PDPageContentStream contentStream=new PDPageContentStream(document,page);
	contentStream.setFont(PDType1Font.HELVETICA_BOLD,12);
	contentStream.beginText();
	contentStream.newLineAtOffset(100,700);
	contentStream.showText("welcome To Our Bank");
	contentStream.showText("Thank You");
	contentStream.endText();
	contentStream.close();
	
	document.save(outputStream);
	document.close();
	return outputStream;
	}
	
}
