package com.avada.myHouse24.validator;

import com.avada.myHouse24.model.AdditionalServiceDTO;
import com.avada.myHouse24.model.AdditionalServiceListDTO;
import com.avada.myHouse24.model.UnitOfMeasurementDTO;

public class AdditionalServiceValidator {
    public boolean valid(AdditionalServiceListDTO additionalServiceListDTO){
        boolean valid = false;
        if(additionalServiceListDTO.getServices() != null)for (AdditionalServiceDTO service : additionalServiceListDTO.getServices()) {
            if(service.getName() == null || service.getName().isBlank()){
                service.setError("Ім'я повино бути вказано");
                valid = true;
            }
            if(service.getUnitOfMeasurementName() == null || service.getUnitOfMeasurementName().equals("Вибрати") || service.getUnitOfMeasurementName().isBlank()){
                service.setError("Одиниці вимірювання повинні бути вказані");
                valid = true;
            }
        }
        if(additionalServiceListDTO.getUnitOfMeasurementNames() != null)for (UnitOfMeasurementDTO unitOfMeasurementName : additionalServiceListDTO.getUnitOfMeasurementNames()) {
            if(unitOfMeasurementName.getName() == null  ||  unitOfMeasurementName.getName().isBlank()){
                unitOfMeasurementName.setError("Назва повина бути вказано");
                valid = true;
            }
        }
        return valid;
    }
}
