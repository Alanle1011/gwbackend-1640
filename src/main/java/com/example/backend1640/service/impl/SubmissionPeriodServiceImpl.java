package com.example.backend1640.service.impl;

import com.example.backend1640.dto.CreateSubmissionPeriodDTO;
import com.example.backend1640.dto.ReadSubmissionPeriodDTO;
import com.example.backend1640.dto.SubmissionPeriodDTO;
import com.example.backend1640.dto.UpdateSubmissionPeriodDTO;
import com.example.backend1640.entity.SubmissionPeriod;
import com.example.backend1640.exception.SubmissionPeriodNotExistsException;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.repository.SubmissionPeriodRepository;
import com.example.backend1640.service.SubmissionPeriodService;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubmissionPeriodServiceImpl implements SubmissionPeriodService {
    private final SubmissionPeriodRepository submissionPeriodRepository;

    public SubmissionPeriodServiceImpl(SubmissionPeriodRepository submissionPeriodRepository) {
        this.submissionPeriodRepository = submissionPeriodRepository;
    }

    @Override
    public SubmissionPeriodDTO createSubmissionPeriod(CreateSubmissionPeriodDTO submissionPeriodDTO) throws ParseException {
        validateSubmissionPeriodExists(submissionPeriodDTO.getName());
        SubmissionPeriod submissionPeriod = new SubmissionPeriod();

        submissionPeriod.setName(submissionPeriodDTO.getName());
        submissionPeriod.setStartDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(submissionPeriodDTO.getStartDate()));
        submissionPeriod.setClosureDate(new SimpleDateFormat("dd/MM/yyy HH:mm").parse(submissionPeriodDTO.getClosureDate()));
        submissionPeriod.setFinalClosureDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(submissionPeriodDTO.getFinalClosureDate()));
        submissionPeriod.setCreatedAt(new Date());
        submissionPeriod.setUpdatedAt(new Date());

        //Save Submission Period
        SubmissionPeriod savedSubmissionPeriod = submissionPeriodRepository.save(submissionPeriod);
        SubmissionPeriodDTO responseSubmissionPeriodDTO = new SubmissionPeriodDTO();
        BeanUtils.copyProperties(savedSubmissionPeriod, responseSubmissionPeriodDTO);

        return responseSubmissionPeriodDTO;
    }

    @Override
    public List<ReadSubmissionPeriodDTO> findAll() {
        List<SubmissionPeriod> submissionPeriods = submissionPeriodRepository.findAll();
        List<ReadSubmissionPeriodDTO> readSubmissionPeriodDTOs = new ArrayList<>();
        for (SubmissionPeriod submissionPeriod : submissionPeriods) {
            ReadSubmissionPeriodDTO readSubmissionPeriodDTO = new ReadSubmissionPeriodDTO();
            readSubmissionPeriodDTO.setId(submissionPeriod.getId());
            readSubmissionPeriodDTO.setName(submissionPeriod.getName());
            String startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(submissionPeriod.getStartDate());
            readSubmissionPeriodDTO.setStartDate(startDate);
            String closureDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(submissionPeriod.getClosureDate());
            readSubmissionPeriodDTO.setClosureDate(closureDate);
            String finalClosureDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(submissionPeriod.getFinalClosureDate());
            readSubmissionPeriodDTO.setFinalClosureDate(finalClosureDate);

            readSubmissionPeriodDTOs.add(readSubmissionPeriodDTO);
        }

        return readSubmissionPeriodDTOs;
    }

    @Override
    public void deleteSubmissionPeriod(Long id) {
        SubmissionPeriod submissionPeriod = validateSubmissionPeriodExists(id);
        submissionPeriodRepository.delete(submissionPeriod);
    }

    @Override
    public SubmissionPeriodDTO updateSubmissionPeriod(UpdateSubmissionPeriodDTO submissionPeriodDTO) throws ParseException {
        SubmissionPeriod submissionPeriod = validateSubmissionPeriodExists(submissionPeriodDTO.getId());

        if (submissionPeriodDTO.getName() != null)
            submissionPeriod.setName(submissionPeriodDTO.getName());
        if (submissionPeriodDTO.getStartDate() != null) {
            submissionPeriod.setStartDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(submissionPeriodDTO.getStartDate()));
        }
        if (submissionPeriodDTO.getClosureDate() != null) {
            submissionPeriod.setClosureDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(submissionPeriodDTO.getClosureDate()));
        }
        if (submissionPeriodDTO.getFinalClosureDate() != null) {
            submissionPeriod.setFinalClosureDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(submissionPeriodDTO.getFinalClosureDate()));
        }
        submissionPeriod.setUpdatedAt(new Date());

        SubmissionPeriod savedSubmissionPeriod = submissionPeriodRepository.save(submissionPeriod);
        SubmissionPeriodDTO responseSubmissionPeriodDTO = new SubmissionPeriodDTO();
        BeanUtils.copyProperties(savedSubmissionPeriod, responseSubmissionPeriodDTO);

        return responseSubmissionPeriodDTO;
    }

//    @Scheduled(cron = "0 0 0 1 * ?")
    @Override
    public void createNewSubmissionPeriod() {
//        LocalDateTime now = LocalDateTime.now();
//        String currentMonthYear = now.format(DateTimeFormatter.ofPattern("MM/yyyy"));
//        String startDate = now.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
//        String closureDate = now.withDayOfMonth(23).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
//        String finalClosureDate = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
//
//        CreateSubmissionPeriodDTO submissionPeriodDTO = new CreateSubmissionPeriodDTO();
//        submissionPeriodDTO.setName(currentMonthYear);
//        submissionPeriodDTO.setStartDate(startDate);
//        submissionPeriodDTO.setClosureDate(closureDate);
//        submissionPeriodDTO.setFinalClosureDate(finalClosureDate);
//
//        try {
//            createSubmissionPeriod(submissionPeriodDTO);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    private SubmissionPeriod validateSubmissionPeriodExists(Long id) {
        Optional<SubmissionPeriod> optionalSubmissionPeriod = submissionPeriodRepository.findById(id);

        if (optionalSubmissionPeriod.isEmpty()) {
            throw new SubmissionPeriodNotExistsException("Submission Period Not Found");
        }

        return optionalSubmissionPeriod.get();
    }

    private void validateSubmissionPeriodExists(String name) {
        Optional<SubmissionPeriod> optionalSubmissionPeriod = submissionPeriodRepository.findByName(name);

        if (optionalSubmissionPeriod.isPresent()) {
            throw new UserAlreadyExistsException("SubmissionPeriodAlreadyExists");
        }
    }
}
