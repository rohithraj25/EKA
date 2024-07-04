package com.example.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.dao.PayslipRepository;
import com.example.entity.Payslip;
import com.example.entity.User;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class PayrollService {

    

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void generateAndSendPayslips() {
        List<Payslip> employees = payslipRepository.findAll();
        for (Payslip employee : employees) {
            generatePayslipForEmployee(employee);
        }
    }
    public boolean generateAndSendPayslipById(Long employeeId) {
    	Payslip employee = payslipRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return false;
        }
        generatePayslipForEmployee(employee);
        return true;
    }

    public Payslip getPayslipById(Long payslipId) {
        return payslipRepository.findById(payslipId).orElse(null);
    }

    private void generatePayslipForEmployee(Payslip employee) {
        User payslip = new User();
        payslip.setDate(LocalDate.now());
        payslip.setAmount(employee.getBasicSalary());
        payslip.setEmployee(employee);
        payslipRepository.save(payslip);

        try {
            byte[] pdfBytes = generatePdf(employee, payslip);
            sendPayslipEmail(employee, pdfBytes);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Double> getEarnings(Payslip employee) {
        Map<String, Double> earnings = new HashMap<>();
        earnings.put("Basic Salary", employee.getBasicSalary());
        earnings.put("House Rent Allowances", employee.getHra());
        earnings.put("Conveyance Allowances", employee.getConveyanceAllowance());
        earnings.put("Medical Allowances", employee.getMedicalAllowance());
        earnings.put("Other Allowances", employee.getOtherAllowance());
        earnings.put("Employer PF", calculateEmployerPF(employee.getBasicSalary()));
        earnings.put("Gross Salary", calculateGrossSalary(earnings));
        return earnings;
    }

    private Map<String, Double> getDeductions(Payslip employee) {
        Map<String, Double> deductions = new HashMap<>();
        deductions.put("EPF", calculateEmployeePF(employee.getBasicSalary()));
        deductions.put("Health Insurance/ESI", employee.getHealthInsurance());
        deductions.put("Professional Tax", employee.getProfessionalTax());
        deductions.put("Employer ESI", calculateEmployerESI(employee.getBasicSalary()));
        deductions.put("Total Deductions", calculateTotalDeductions(deductions));
        return deductions;
    }

    private double calculateEmployerPF(double basicSalary) {
        return basicSalary * 0.12;
    }

    private double calculateEmployeePF(double basicSalary) {
        return basicSalary * 0.12;
    }

    private double calculateEmployerESI(double basicSalary) {
        return basicSalary * 0.036;
    }

    private double calculateGrossSalary(Map<String, Double> earnings) {
        return earnings.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    private double calculateTotalDeductions(Map<String, Double> deductions) {
        return deductions.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    private double calculateNetSalary(double grossSalary, double totalDeductions) {
        return grossSalary - totalDeductions;
    }


    public byte[] generatePdf(Payslip employee, User payslip) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        // Fetch earnings and deductions
        Map<String, Double> earnings = getEarnings(employee);
        Map<String, Double> deductions = getDeductions(employee);

        double grossSalary = earnings.get("Gross Salary");
        double totalDeductions = deductions.get("Total Deductions");
        double netSalary = calculateNetSalary(grossSalary, totalDeductions);

        // Add Payslip Header
        Paragraph header = new Paragraph("RJAY TECHNOLOGIES PRIVATE LIMITED")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        document.add(header);

        document.add(new Paragraph("7-1-396/2/A/3/31 5, BK Guda, SR Nagar, Sanjeev Reddy Nagar, Ameerpet, Hyderabad-500038, Telangana.")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)).setFontSize(10).setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));

        document.add(new Paragraph("\n"));

        // Add Employee Details
        Table empTable = new Table(new float[]{1, 2, 1, 2});
        empTable.setWidth(UnitValue.createPercentValue(100));
        empTable.addCell(createCell("Employee ID", employee.getId().toString()));
        empTable.addCell(createCell("UAN", employee.getUan()));
        empTable.addCell(createCell("Employee Name", employee.getName()));
        empTable.addCell(createCell("PAN No", employee.getPan()));
        empTable.addCell(createCell("Designation", employee.getDesignation()));
        empTable.addCell(createCell("Bank Name", employee.getBankName()));
        empTable.addCell(createCell("Department", employee.getDepartment()));
        empTable.addCell(createCell("Bank A/C No", employee.getBankAccountNumber()));
        empTable.addCell(createCell("Date Of Joining", employee.getJoiningDate().toString()));
        empTable.addCell(createCell("Date Of Payment", payslip.getDate().toString()));
        empTable.addCell(createCell("Month", payslip.getDate().getMonth().toString()));
        empTable.addCell(createCell("Total Working Days", "30")); // Update as needed
        empTable.addCell(createCell("Paid Days", "30")); // Update as needed
        empTable.addCell(createCell("LOP Days", "0")); // Update as needed
        empTable.addCell(createCell("Leaves Taken", "0")); // Update as needed

        document.add(empTable);

        // Add Earnings and Deductions in a single table row format
        document.add(new Paragraph("\n"));
        Table combinedTable = new Table(new float[]{3, 2, 3, 2});
        combinedTable.setWidth(UnitValue.createPercentValue(100));
        combinedTable.addHeaderCell(createHeaderCell("Earnings"));
        combinedTable.addHeaderCell(createHeaderCell("Amount"));
        combinedTable.addHeaderCell(createHeaderCell("Deductions"));
        combinedTable.addHeaderCell(createHeaderCell("Amount"));

        // Add earnings and deductions in rows
        String[] earningsKeys = {"Basic Salary", "House Rent Allowances", "Conveyance Allowances", "Medical Allowances", "Other Allowances", "Employer PF"};
        String[] deductionsKeys = {"EPF", "Health Insurance/ESI", "Professional Tax", "Employer ESI"};

        for (int i = 0; i < Math.max(earningsKeys.length, deductionsKeys.length); i++) {
            if (i < earningsKeys.length) {
                combinedTable.addCell(createCell(earningsKeys[i], String.format("%.2f", earnings.getOrDefault(earningsKeys[i], 0.0))));
            } else {
                combinedTable.addCell(createCell("", ""));
                combinedTable.addCell(createCell("", ""));
            }

            if (i < deductionsKeys.length) {
                combinedTable.addCell(createCell(deductionsKeys[i], String.format("%.2f", deductions.getOrDefault(deductionsKeys[i], 0.0))));
            } else {
                combinedTable.addCell(createCell("", ""));
                combinedTable.addCell(createCell("", ""));
            }
        }

        combinedTable.addCell(createCell("Gross Salary", String.format("%.2f", grossSalary)));
        combinedTable.addCell(createCell("", ""));
        combinedTable.addCell(createCell("Total Deductions", String.format("%.2f", totalDeductions)));
        combinedTable.addCell(createCell("", ""));

        document.add(combinedTable);

        // Add Net Salary
        document.add(new Paragraph("\n"));
        Table netSalaryTable = new Table(new float[]{1, 1});
        netSalaryTable.setWidth(UnitValue.createPercentValue(100));
        netSalaryTable.addHeaderCell(createHeaderCell("Net Salary"));
        netSalaryTable.addHeaderCell(createHeaderCell("Amount"));
        netSalaryTable.addCell(createCell("Net Salary", String.format("%.2f", netSalary)));
        netSalaryTable.addCell(createCell("In Words", convertToWords(netSalary)));

        document.add(netSalaryTable);

        // Add Signatures
        document.add(new Paragraph("\n"));
        Table signaturesTable = new Table(new float[]{1, 1});
        signaturesTable.setWidth(UnitValue.createPercentValue(100));
        signaturesTable.addCell(createCell("Employer Signature", ""));
        signaturesTable.addCell(createCell("Employee Signature", ""));

        document.add(signaturesTable);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private Cell createCell(String header, String value) throws IOException {
        Cell cell = new Cell();
        cell.add(new Paragraph(header + ": " + value).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)).setFontSize(10));
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    private Cell createHeaderCell(String content) throws IOException {
        Cell cell = new Cell();
        cell.add(new Paragraph(content).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(10));
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    private String convertToWords(double amount) {
        return "Rupees " + NumberFormat.getInstance().format(amount) + " Only";
    }
    private void sendPayslipEmail(Payslip employee, byte[] pdfBytes) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(employee.getEmail());
        helper.setSubject("Your Payslip for " + LocalDate.now().getMonth());

        String emailContent = "<p>Dear " + employee.getName() + ",</p>" +
                "<p>Please find attached your payslip for " + LocalDate.now().getMonth() + ".</p>" +
                "<p>Best regards,<br>Payroll Department</p>";
        helper.setText(emailContent, true);

        helper.addAttachment("Payslip.pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));

        mailSender.send(message);
    }
}
