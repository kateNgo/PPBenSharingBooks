/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author phuong ngo
 *
 * This is a validator for publish year
 */
@FacesValidator("au.edu.uts.aip.sharingBook.validator.MyValidator")
public class MyValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            if ((value == null) || (value.toString().isEmpty()) || (value.toString().equalsIgnoreCase("0"))) {
                value = "";
                return;
            }
            int number = Integer.parseInt(value.toString());
            if ((number != 0) && ((number < 1880) || (number > 2016))) {
                throwError("publish year must be a year between 1800 to 2016");
            }
        } catch (NumberFormatException ee) {
            throwError("Invalid publish year, it must be a year between 1800 to 2016");
        }
    }

    private void throwError(String message) {
        FacesMessage msg = new FacesMessage(message);
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(msg);
    }

}
