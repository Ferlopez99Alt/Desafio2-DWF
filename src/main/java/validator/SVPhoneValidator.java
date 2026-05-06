package validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Validador personalizado para numeros telefonicos de El Salvador
 * El formato que acepta es: 2356-7 seguido de un guion y 4 digitos
 * Ejemplos validos: 2250-5555, 3678-1234, 7123-4567
 * 
 * El estandar salvadoreno: 
 * - Empieza con 2, 3, 6 o 7
 * - Luego 3 digitos mas
 * - Un guion "-"
 * - Y finalmente 4 digitos
 */
@FacesValidator("SVPhoneValidator")
public class SVPhoneValidator implements Validator {

    // La expresion regular del formato telefonico de El Salvador
    // ####-#### donde el primer digito debe ser 2, 3, 6 o 7
    private static final String SV_PHONE_REGEX = "^[2367]\\d{3}-\\d{4}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(SV_PHONE_REGEX);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {

        // Si el valor esta vacio lo dejamos pasar (el required se encarga de eso)
        if (value == null || value.toString().trim().isEmpty()) {
            return;
        }

        String phone = value.toString().trim();
        Matcher matcher = PHONE_PATTERN.matcher(phone);

        if (!matcher.matches()) {
            // El numero no cumple con el formato salvadoreno
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Telefono invalido",
                "El numero debe tener el formato salvadoreno: ####-#### " +
                "y comenzar con 2, 3, 6 o 7 (ejemplo: 2250-5555)"
            );
            throw new ValidatorException(message);
        }
    }
}
