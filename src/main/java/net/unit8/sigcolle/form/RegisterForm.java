package net.unit8.sigcolle.form;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import enkan.component.doma2.DomaProvider;
import lombok.Data;
import net.unit8.sigcolle.validator.Password;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * @author takahashi
 */
@Data
public class RegisterForm extends FormBase {
    @Inject
    private DomaProvider domaProvider;

    @DecimalMin("1")
    @DecimalMax("9999")
    private String userId;

    @NotNull
    @Length(min = 1, max = 20)
    private String firstName;

    @NotNull
    @Length(min = 1, max = 20)
    private String lastName;

    @NotNull
    @Length(min = 1, max = 50)
    @Email
    private String email;

    @NotNull
    @Length(min = 4, max = 20)
    @Password
    private String pass;

    @Override
    public boolean hasErrors() {
        return super.hasErrors();
    }

    @Override
    public boolean hasErrors(String name) {
        return super.hasErrors(name);
    }

    @Override
    public List<String> getErrors(String name) {
        return super.getErrors(name);
    }
}
