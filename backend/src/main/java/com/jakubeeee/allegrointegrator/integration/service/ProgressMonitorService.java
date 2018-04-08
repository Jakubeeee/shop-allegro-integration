package com.jakubeeee.allegrointegrator.integration.service;

import com.jakubeeee.allegrointegrator.integration.model.ProgressMonitor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class ProgressMonitorService {

    @Autowired
    ProgressMonitor progressMonitor;

    public boolean isUpdateInProgress() {
        return progressMonitor.getIsUpdating() == 1;
    }

    public void startProgress() {
        progressMonitor.setIsUpdating(1);
    }

    public void setMaxProgress(int maxProgress) {
        progressMonitor.setMaxProgress(maxProgress);
    }

    public void advanceProgress() {
        progressMonitor.setCurrentProgress(progressMonitor.getCurrentProgress() + 1);
    }

    public void resetProgress() {
        progressMonitor.setIsUpdating(0);
        progressMonitor.setCurrentProgress(0);
        progressMonitor.setMaxProgress(0);
    }

}
