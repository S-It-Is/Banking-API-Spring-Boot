package com.project.bank.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bank.entity.Account;
import com.project.bank.repository.AccountRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExcelService {
	
	@Autowired
	AccountRepository accountRepository;
	
	public void generateExcel(HttpServletResponse response)throws IOException {
		
	List<Account> accounts=accountRepository.findAll();
	
	HSSFWorkbook workbook=new HSSFWorkbook();
	HSSFSheet sheet= workbook.createSheet("Accounts Details");
	HSSFRow row=sheet.createRow(0);
	row.createCell(0).setCellValue("Id");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Balance");
	row.createCell(3).setCellValue("Email");
	row.createCell(4).setCellValue("Password");
	
	int dataRow=1;
	for(Account account:accounts) {
		row=sheet.createRow(dataRow);
		row.createCell(0).setCellValue(account.getId());
		row.createCell(1).setCellValue(account.getAccountHolderName());
		row.createCell(2).setCellValue(account.getBalance());
		row.createCell(3).setCellValue(account.getEmail());
		row.createCell(4).setCellValue(account.getPassword());
		dataRow++;
		
	}
	ServletOutputStream ops=response.getOutputStream();
	workbook.write(ops);
	ops.close();
	workbook.close();

		
	}

}
