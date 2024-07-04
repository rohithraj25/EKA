package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.dao.DocumentsDao;
import com.example.entity.Documents;

@Service
@Transactional
public class DocumentsService {

    @Autowired
    private DocumentsDao documentsRepository;

    public Documents saveOrUpdateDocuments(MultipartFile degreememoFile,
                                            MultipartFile pancardFile,
                                            MultipartFile sscmemoFile,
                                            MultipartFile voteridFile,
                                            Long empnumber) throws IOException {
        Documents documents = documentsRepository.findByEmpnumber(empnumber)
                .orElse(new Documents());

        if (degreememoFile != null && !degreememoFile.isEmpty()) {
            String degreememoPath = saveFileAndGetPath(degreememoFile);
            documents.setDegreememoPath(degreememoPath);
        }

        if (pancardFile != null && !pancardFile.isEmpty()) {
            String pancardPath = saveFileAndGetPath(pancardFile);
            documents.setPancardPath(pancardPath);
        }

        if (sscmemoFile != null && !sscmemoFile.isEmpty()) {
            String sscmemoPath = saveFileAndGetPath(sscmemoFile);
            documents.setSscmemoPath(sscmemoPath);
        }

        if (voteridFile != null && !voteridFile.isEmpty()) {
            String voteridPath = saveFileAndGetPath(voteridFile);
            documents.setVoteridPath(voteridPath);
        }

        documents.setEmpnumber(empnumber);
        return documentsRepository.save(documents);
    }

    public Documents saveOrUpdateImage(MultipartFile imageFile, Long empnumber) throws IOException {
        Documents documents = documentsRepository.findByEmpnumber(empnumber)
                .orElse(new Documents());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveFileAndGetPath(imageFile);
            documents.setImagePath(imagePath);
        }

        documents.setEmpnumber(empnumber);
        return documentsRepository.save(documents);
    }

    public Documents updateDocuments(Long empnumber,
                                     MultipartFile degreememoFile,
                                     MultipartFile pancardFile,
                                     MultipartFile sscmemoFile,
                                     MultipartFile voteridFile) throws IOException {
        Documents documents = documentsRepository.findByEmpnumber(empnumber)
                .orElseThrow(() -> new RuntimeException("Documents not found for empnumber: " + empnumber));

        if (degreememoFile != null && !degreememoFile.isEmpty()) {
            String degreememoPath = saveFileAndGetPath(degreememoFile);
            documents.setDegreememoPath(degreememoPath);
        }

        if (pancardFile != null && !pancardFile.isEmpty()) {
            String pancardPath = saveFileAndGetPath(pancardFile);
            documents.setPancardPath(pancardPath);
        }

        if (sscmemoFile != null && !sscmemoFile.isEmpty()) {
            String sscmemoPath = saveFileAndGetPath(sscmemoFile);
            documents.setSscmemoPath(sscmemoPath);
        }

        if (voteridFile != null && !voteridFile.isEmpty()) {
            String voteridPath = saveFileAndGetPath(voteridFile);
            documents.setVoteridPath(voteridPath);
        }

        return documentsRepository.save(documents);
    }

    public Documents getDocumentsByEmpnumber(Long empnumber) {
        return documentsRepository.findByEmpnumber(empnumber)
                .orElseThrow(() -> new RuntimeException("Documents not found for empnumber: " + empnumber));
    }

    private String saveFileAndGetPath(MultipartFile file) throws IOException {
        String uploadDir = "./uploads"; // Example path, adjust as needed
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String filePath = uploadDir + "/" + originalFilename;
        Path destinationPath = Paths.get(filePath);
        Files.write(destinationPath, file.getBytes());

        return filePath;
    }

    public byte[] downloadDocument(Long empnumber, String documentType) throws IOException {
        Documents documents = documentsRepository.findByEmpnumber(empnumber)
                .orElseThrow(() -> new RuntimeException("Documents not found for empnumber: " + empnumber));

        String filePath = null;
        switch (documentType) {
            case "degreememo":
                filePath = documents.getDegreememoPath();
                break;
            case "pancard":
                filePath = documents.getPancardPath();
                break;
            case "sscmemo":
                filePath = documents.getSscmemoPath();
                break;
            case "voterid":
                filePath = documents.getVoteridPath();
                break;
            // Add more cases for other document types if needed
            default:
                throw new IllegalArgumentException("Unsupported document type: " + documentType);
        }

        if (filePath == null) {
            throw new RuntimeException("Document path not found for type: " + documentType);
        }

        // Load the file bytes from the specified path
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}

