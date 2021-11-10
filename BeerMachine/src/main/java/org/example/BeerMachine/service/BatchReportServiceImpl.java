package org.example.BeerMachine.service;

import org.example.BeerMachine.data.models.Batch;
import org.example.BeerMachine.data.models.BatchReport;
import org.example.BeerMachine.data.payloads.request.BatchReportRequest;
import org.example.BeerMachine.data.payloads.request.BatchRequest;
import org.example.BeerMachine.data.payloads.response.MessageResponse;
import org.example.BeerMachine.data.repository.BatchReportRepository;
import org.example.BeerMachine.data.repository.BatchRepository;
import org.example.BeerMachine.data.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class BatchReportServiceImpl implements BatchReportService {
    @Autowired
    BatchReportRepository batchReportRepository;

    @Autowired
    BatchRepository batchRepository;

    @Override
    public MessageResponse createBatchReport(BatchReportRequest batchReportRequest) {
        BatchReport newBatchReport = new BatchReport();
        newBatchReport.setBatch(batchReportRequest.getBatch(batchRepository));
        newBatchReport.setSpeed(batchReportRequest.getSpeed());
        newBatchReport.setTotalCount(batchReportRequest.getTotalCount());
        batchReportRepository.save(newBatchReport);
        return new MessageResponse("New Batch report created successfully");
    }

    @Override
    public Optional<BatchReport> updateBatchReport(Integer batchReportId, BatchReportRequest batchReportRequest) throws ParseException {
        Optional<BatchReport> batchReport = batchReportRepository.findById(batchReportId);
        if(batchReport.isEmpty()){
            return null;
        }
        else {
            batchReport.get().setSpeed(batchReportRequest.getSpeed());
            batchReport.get().setTotalCount(batchReportRequest.getTotalCount());
            batchReport.get().setGoodCount(batchReportRequest.getGoodCount());
            batchReport.get().setRejectedCount(batchReportRequest.getRejectedCount());
            batchReport.get().setStartTime(batchReportRequest.getStartTimeFormat(batchReportRequest.getStartTime()));
            batchReport.get().setEndTime(batchReportRequest.getEndTimeFormat(batchReportRequest.getEndTime()));
            batchReport.get().setOEE(batchReportRequest.getOEE());
            batchReportRepository.save(batchReport.get());
        }
        return batchReport;
    }

    @Override
    public void deleteBatchReport(Integer batchReportId) {
        if (batchReportRepository.getById(batchReportId).getId().equals(batchReportId)){
            batchReportRepository.deleteById(batchReportId);
        }
    }

    @Override
    public BatchReport getBatchReport(Integer batchReportId) {
        try {
            return batchReportRepository.findById(batchReportId).get();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<BatchReport> getAllBatchReports() {
        return batchReportRepository.findAll();
    }
}