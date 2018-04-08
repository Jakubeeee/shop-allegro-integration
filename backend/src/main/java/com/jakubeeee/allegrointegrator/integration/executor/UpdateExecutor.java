package com.jakubeeee.allegrointegrator.integration.executor;

import com.jakubeeee.allegrointegrator.integration.exception.UnsuccessfulUpdateException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateExecutor {

    public static void execute(Runnable updateTask) throws UnsuccessfulUpdateException {
        try {
            updateTask.run();
        } catch (Exception e) {
            throw new UnsuccessfulUpdateException("Update unsuccessful");
        }
    }

}
